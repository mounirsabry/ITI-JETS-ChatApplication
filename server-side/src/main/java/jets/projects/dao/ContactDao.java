package jets.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jets.projects.classes.RequestResult;
import jets.projects.dbconnections.ConnectionManager;
import jets.projects.entities.Contact;
import jets.projects.entities.ContactGroup;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;

public class ContactDao {
    public RequestResult<Boolean> isContacts(int userID, int contactID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    public RequestResult<List<Contact>> getAllContacts(int userID) {
        try (Connection connection = ConnectionManager.getConnection()) {

        String query = "SELECT username, picture, category " +   // "SELECT u.username, u.picture, c.category " +"FROM contact c " +"JOIN user u ON c.contact_ID = u.ID " +  "WHERE c.user_ID = ?"
                       "FROM contact c " +
                       "JOIN user u ON c.user_ID = u.ID " +  
                       "WHERE c.user_ID = ?";
        PreparedStatement selectStatement = connection.prepareStatement(query);
        selectStatement.setInt(1, userID);

        ResultSet resultSet = selectStatement.executeQuery();
        List<Contact> contacts = new ArrayList<>();

        while (resultSet.next()) {
            Contact contact = new Contact();
            
        
            contact.setName(resultSet.getString("username"));
            contact.setPicture(resultSet.getString("picture"));
            String category = resultSet.getString("category");
            ContactGroup contactGroup = ContactGroup.valueOf(category);
            contact.setContactGroup(contactGroup);
            contacts.add(contact);
        }

        return new RequestResult<>(contacts, null);
    } catch (SQLException e) {
        return new RequestResult<>(null,null);
    }
    }
    public RequestResult<String> getContactProfilePic(int contactID) {
        try (Connection connection = ConnectionManager.getConnection()) {

            String query = "SELECT picture " +
                           "FROM user " +
                           "WHERE ID = ?";
            
            PreparedStatement selectStatement = connection.prepareStatement(query);
            selectStatement.setInt(1, contactID); 
    
            ResultSet resultSet = selectStatement.executeQuery();
    
            if (resultSet.next()) {
                String picture = resultSet.getString("picture");
                return new RequestResult<>(picture, null); 
            } else {
                return new RequestResult<>(null, null);  
            }
        } catch (SQLException e) {
            e.printStackTrace();  
            return new RequestResult<>(null, null);
        }
    }
    public RequestResult<NormalUser> getContactProfile(int contactID) {
        String query = "SELECT username , phone_number,email,picture,status,bio FROM User WHERE ID= ? ";
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement Statement = connection.prepareStatement(query)){
            Statement.setInt(1, contactID);
            ResultSet resultSet = Statement.executeQuery();
            if(!resultSet.next())
                return new RequestResult<>(null, null);
            NormalUser contact = new NormalUser();
            contact.setDisplayName(resultSet.getString(1));
            contact.setPhoneNumber(resultSet.getString(2));
            contact.setEmail(resultSet.getString(3));
            contact.setPic(resultSet.getString(4));
            contact.setStatus(NormalUserStatus.valueOf(resultSet.getString(5)));
            contact.setBio(resultSet.getString(6));
            resultSet.close();
            return new RequestResult<>(contact, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, 
                    e.getMessage());
        }
    }
}
