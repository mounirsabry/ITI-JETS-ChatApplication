package jets.projects.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import jets.projects.classes.RequestResult;
import jets.projects.dbconnections.ConnectionManager;
import jets.projects.session.AdminSessionData;
import jets.projects.session.ClientSessionData;
import jets.projects.entities.Contact;
import jets.projects.entities.Gender;
import jets.projects.entities.NormalUserStatus;

public class UsersDao {
    public RequestResult<AdminSessionData> adminLogin(int userID, String password) {
        try (Connection connection = ConnectionManager.getConnection()) {
            
            PreparedStatement selectStatement = connection.prepareStatement(
                    "SELECT * FROM AdminUser WHERE user_ID = ?");
            selectStatement.setInt(1, userID);
            ResultSet resultSet = selectStatement.executeQuery();
        
            if (resultSet.next() && resultSet.getString("password").equals(password)) {
                
                PreparedStatement updateStatement = connection.prepareStatement(
                        "UPDATE AdminUser SET admin_status = 'ONLINE' WHERE user_ID = ?");
                updateStatement.setInt(1, userID);
                if(updateStatement.executeUpdate() != 1)
                    return null;
        
                AdminSessionData adminSessionData = new AdminSessionData(userID, resultSet.getString("display_name"));
                return new RequestResult<>(adminSessionData, null);
            } else {
                return new RequestResult<>(null, null);
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, 
                    e.getMessage());
        }
    }

    public RequestResult<Boolean> adminLogout(int userID) {
        try (Connection connection = ConnectionManager.getConnection()) {

            PreparedStatement updateStatement = connection.prepareStatement(
                    "UPDATE AdminUser SET admin_status = 'OFFLINE' WHERE user_ID = ?");
            updateStatement.setInt(1, userID);
            if(updateStatement.executeUpdate() != 1)
                return new RequestResult<>(false, null);
            return new RequestResult<>(true, null);
            
        } catch (SQLException e) {
            return new RequestResult<>(null, 
                    e.getMessage());
        }
    }
    
    public RequestResult<Boolean> isNormalUserExists(int userID) {
        return null;
    }
    
    public RequestResult<ClientSessionData> clientLogin(String phoneNumber, String password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public RequestResult<Boolean> clientLogout(int userID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RequestResult<Boolean> registerNormalUser(String displayName, String phoneNumber,
            String email, String pic, String password, Gender gender,
            String country, Date birthDate, String bio) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<Boolean> setOnlineStatus(int userID, NormalUserStatus newStatus) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<Contact> getContactProfile(int contactID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
