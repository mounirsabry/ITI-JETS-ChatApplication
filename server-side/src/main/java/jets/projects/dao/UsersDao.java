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
        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT COUNT(*) FROM normaluser WHERE user_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return new RequestResult<>(true, null);
            } else {
                return new RequestResult<>(false, "User does not exist.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }
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

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT * FROM normaluser WHERE user_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                NormalUser user = new NormalUser();
                user.setUserID(resultSet.getInt("user_ID"));
                user.setDisplayName(resultSet.getString("display_name"));
                user.setPic(  resultSet.getString("pic"));
                user.setPhoneNumber(resultSet.getString("phone_number"));
                user.setPassword(  resultSet.getString("password"));
                user.setEmail( resultSet.getString("email"));
                user.setGender(Gender.valueOf(resultSet.getString("gender")));
                user.setCountry(resultSet.getString("country"));
                user.setBirthDate(resultSet.getDate("birth_date"));
                user.setStatus(NormalUserStatus.valueOf(resultSet.getString("status")));
                user.setBio(resultSet.getString("bio"));                 
                
                return new RequestResult<>(user, null); 
            } else {
                return new RequestResult<>(null, "User not found.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, "Database error: " + e.getMessage());
        }
      
    }




    public RequestResult<Boolean> saveProfileChanges(int userID) {




    }



    public RequestResult<Boolean> isPasswordValid(int userID, String oldPassword) {

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT password FROM normaluser WHERE user_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getString("password").equals(oldPassword)) {
                return new RequestResult<>(true, null); 
            } else {
                return new RequestResult<>(false, "Invalid password.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }
        
    }



    public RequestResult<Boolean> updatePassword(int userID, String oldPassword, String newPassword) {

        try (Connection connection = ConnectionManager.getConnection()) {
            // Check if the old password is valid
            RequestResult<Boolean> isValidResult = isPasswordValid(userID, oldPassword);
            if (isValidResult.getResponseData() == null || !isValidResult.getResponseData()) {
                return new RequestResult<>(false, "Invalid old password.");
            }

            String query = "UPDATE normaluser SET password = ? WHERE user_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, userID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 1) {
                return new RequestResult<>(true, null); 
            } else {
                return new RequestResult<>(false, "Failed to update password.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }
      
    }


    
}
