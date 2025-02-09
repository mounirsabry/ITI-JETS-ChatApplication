package jets.projects.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jets.projects.classes.RequestResult;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entities.ContactGroup;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;

public class ContactDao {
    public RequestResult<Boolean> isContacts(int userID, int contactID) {
            String query = "SELECT * FROM CONTACT WHERE"
                    + " (first_ID = ? AND second_ID = ?)"
                    + " OR (first_ID = ? AND second_ID = ?)";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            statement.setInt(1, userID);
            statement.setInt(2, contactID);
            statement.setInt(3, contactID);
            statement.setInt(4, userID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new RequestResult<>(true, null); 
            } else {
                return new RequestResult<>(false, null);
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, e.getMessage());
        }
    }
    
    public RequestResult<List<ContactInfo>> getAllContacts(int userID) {
        String query = "SELECT distinct u.display_name, u.pic, c.category " +
                        "FROM CONTACT c " +
                        "JOIN NormalUser u ON c.second_ID = u.user_ID " +
                        "WHERE c.first_ID =?";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();
            List<ContactInfo> contacts = new ArrayList<>();
            while (resultSet.next()) {
                ContactInfo contact = new ContactInfo();
                contact.setName(resultSet.getString("display_name"));
                contact.setPicture(resultSet.getBlob("pic"));
                ContactGroup contactGroup = ContactGroup.valueOf(resultSet.getString("category"));
                contact.setContactGroup(contactGroup);
                contacts.add(contact);
            }
            return new RequestResult<>(contacts, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, e.getMessage());
        }
    }
    
    public RequestResult<List<Integer>> getAllContactsIDs(int userID) {
        
    }
    
    public RequestResult<NormalUser> getContactProfile(int contactID) {
        String query = "SELECT display_name, phone_number, email, pic, `status`, bio FROM NormalUser WHERE user_ID = ?";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            statement.setInt(1, contactID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                NormalUser contact = new NormalUser();
                contact.setDisplayName(resultSet.getString("display_name"));
                contact.setPhoneNumber(resultSet.getString("phone_number"));
                contact.setEmail(resultSet.getString("email"));
                contact.setPic(resultSet.getBlob("pic"));
                contact.setStatus(NormalUserStatus.valueOf(resultSet.getString("status")));
                contact.setBio(resultSet.getString("bio"));
                return new RequestResult<>(contact, null);
            } else {
                return new RequestResult<>(null, "Contact not found.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, e.getMessage());
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
