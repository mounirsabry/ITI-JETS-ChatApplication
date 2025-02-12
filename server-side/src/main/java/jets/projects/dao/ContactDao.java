package jets.projects.dao;

import java.net.ConnectException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jets.projects.classes.RequestResult;
import jets.projects.db_connections.ConnectionManager;
import jets.projects.entities.Contact;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entities.ContactGroup;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;

public class ContactDao {
    public RequestResult<Boolean> isContacts(int userID, int contactID) {
            String query = "SELECT * FROM CONTACT WHERE"
                    + " (first_ID = ? AND second_ID = ?)"
                    + " OR (first_ID = ? AND second_ID = ?)";
        try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(query)){
            statement.setInt(1, userID);
            statement.setInt(2, contactID);
            statement.setInt(3, contactID);
            statement.setInt(4, userID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                resultSet.close();
                return new RequestResult<>(true, null);
            } else {
                resultSet.close();
                return new RequestResult<>(false, null);
            }
        } catch (SQLException e) {
            return new RequestResult<>(false,
                    "DB ERROR: " + e.getMessage());
        }
    }
    
    public RequestResult<List<ContactInfo>> getAllContacts(int userID) {
        String query = "SELECT distinct u.display_name, u.pic, c.category, c.second_ID " +
                        "FROM CONTACT c " +
                        "JOIN NormalUser u ON c.second_ID = u.user_ID " +
                        "WHERE c.first_ID =?";
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();
            List<ContactInfo> contacts = new ArrayList<>();
            while (resultSet.next()) {

                int contactID = resultSet.getInt("second_ID");
                String name =resultSet.getString("display_name");
                ContactGroup contactGroup = ContactGroup.valueOf(resultSet.getString("category"));
                Blob blob = resultSet.getBlob("pic");
                byte[] pic = blob != null ? blob.getBytes(1, (int) blob.length()) : null;

                Contact contact = new Contact(userID, contactID, contactGroup);
                contacts.add(new ContactInfo(contact, name, pic));
            }
            resultSet.close();
            return new RequestResult<>(contacts, null);
        } catch (SQLException e) {
            return new RequestResult<>(null,
                    "DB ERROR: " + e.getMessage());
        }
    }
    
    // Return the secondID's info from the DB.
    public RequestResult<ContactInfo> getContactInfo(int firstID, int secondID) {
        String query = "SELECT display_name, pic, category FROM "
                + "NormalUser, Contact WHERE user_ID = ? and second_ID = ? and first_ID = ?";
        try(Connection connection = ConnectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);){
            statement.setInt(1, secondID);
            statement.setInt(2, secondID);
            statement.setInt(3, firstID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("display_name");
                Blob blob = resultSet.getBlob("pic");
                byte[] pic = blob != null ? blob.getBytes(1, (int) blob.length()) : null;
                ContactGroup contactGroup = ContactGroup.valueOf(resultSet.getString("category"));
                Contact contact = new Contact(firstID, secondID, contactGroup);
                ContactInfo contactInfo = new ContactInfo(contact, name, pic);
                resultSet.close();
                return new RequestResult<>(contactInfo, null);
            }
            resultSet.close();
            return new RequestResult<>(null, "Contact not found.");
        }catch (SQLException e) {
            return new RequestResult<>(null, "DB ERROR: " + e.getMessage());
        }
    }
    
    public RequestResult<NormalUserStatus> getContactOnlineStatus(int contactID) {
        String query = "SELECT status FROM NormalUser WHERE user_ID = ?";
        try(Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);){
            preparedStatement.setInt(1, contactID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                NormalUserStatus status = NormalUserStatus.valueOf(resultSet.getString("status"));
                resultSet.close();
                return new RequestResult<>(status, null);
            } else {
                resultSet.close();
                return new RequestResult<>(null, "Contact not found id: " + contactID);
            }
        }catch (SQLException e) {
            return new RequestResult<>(null, "DB ERROR: " + e.getMessage());
        }
    }
    
    public RequestResult<List<Integer>> getAllContactsIDs(int userID) {
        String query = "SELECT second_ID FROM CONTACT WHERE first_ID = ?";
        try(Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);){
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Integer> contactIDs = new ArrayList<>();
            while (resultSet.next()) {
                contactIDs.add(resultSet.getInt("second_ID"));
            }
            resultSet.close();
            return new RequestResult<>(contactIDs, null);

        }catch (SQLException e) {
            return new RequestResult<>(null, "DB ERROR: " + e.getMessage());
        }
    }
    
    public RequestResult<NormalUser> getContactProfile(int contactID) {
        String query =
                "SELECT display_name, phone_number, email, pic, `status`, bio FROM NormalUser WHERE user_ID = ?";
        try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(query)){
            statement.setInt(1, contactID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                NormalUser contact = new NormalUser();
                contact.setDisplayName(resultSet.getString("display_name"));
                contact.setPhoneNumber(resultSet.getString("phone_number"));
                contact.setEmail(resultSet.getString("email"));
                Blob pic = resultSet.getBlob("pic");
                byte[] picBytes = pic != null ? pic.getBytes(1, (int) pic.length()) : null;
                contact.setPic(picBytes);
                contact.setStatus(NormalUserStatus.valueOf(resultSet.getString("status")));
                contact.setBio(resultSet.getString("bio"));
                return new RequestResult<>(contact, null);
            } else {
                return new RequestResult<>(null, "Contact not found.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(null,"DB ERROR: " + e.getMessage());
        }
    }
    
    /*
    public RequestResult<byte[]> getContactProfilePic(int contactID) {
        String query = "SELECT pic FROM NormalUser WHERE user_ID = ?";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            statement.setInt(1, contactID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Blob pic = resultSet.getBlob("pic");
                return new RequestResult<>(pic, null);
            } else {
                return new RequestResult<>(null, "Profile picture not found.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, e.getMessage());
        }
    }    
    */
}
