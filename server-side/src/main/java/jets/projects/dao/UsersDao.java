package jets.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import jets.projects.classes.RequestResult;
import jets.projects.dbconnections.ConnectionManager;
import jets.projects.session.ClientSessionData;
import jets.projects.entities.Gender;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;

public class UsersDao {

    public RequestResult<Boolean> isNormalUserExists(int userID) {
        throw new UnsupportedOperationException("Not supported yet.");
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
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement updateStatement = connection.prepareStatement(
                    "UPDATE user SET status = ? WHERE ID = ?");
            updateStatement.setString(1, newStatus.toString()); 
            updateStatement.setInt(2, userID);
            int rowsAffected = updateStatement.executeUpdate();
    
            if (rowsAffected == 1) {
                return new RequestResult<>(true, null);
            } else {
                return new RequestResult<>(false, null);
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Error updating online status: " + e.getMessage());
        }
    }

    public RequestResult<String> getNormalUserProfilePic(int userID) {
         try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement selectStatement = connection.prepareStatement(
                "SELECT picture FROM user WHERE ID = ?");
            selectStatement.setInt(1, userID);
            ResultSet resultSet = selectStatement.executeQuery();
    
            if (resultSet.next()) {
                String picture = resultSet.getString("picture");
                return new RequestResult<>(picture, null);
            } else {
                return new RequestResult<>(null, null);
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, null);
        }
    }

    public RequestResult<NormalUser> getNormalUserProfile(int userID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNormalUserProfile'");
    }

    public RequestResult<Boolean> saveProfileChanges(int userID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveProfileChanges'");
    }

    public RequestResult<Boolean> isPasswordValid(int userID, String oldPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isPasswordValid'");
    }

    public RequestResult<Boolean> updatePassword(int userID, String oldPassword, String newPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePassword'");
    }
    
}
