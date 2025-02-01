package jets.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import jets.projects.classes.RequestResult;
import jets.projects.dbconnections.ConnectionManager;
import jets.projects.entities.Contact;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;

public class ContactDao {
    public RequestResult<Boolean> isContacts(int userID, int contactID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    public RequestResult<List<Contact>> getAllContacts(int userID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    public RequestResult<String> getContactProfilePic(int userID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getContactProfilePic'");
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
