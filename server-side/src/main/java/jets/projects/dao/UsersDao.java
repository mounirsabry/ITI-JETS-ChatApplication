package jets.projects.dao;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import jets.projects.classes.RequestResult;
import jets.projects.db_connections.ConnectionManager;
import jets.projects.session.ClientSessionData;
import jets.projects.entities.Gender;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;

public class UsersDao {
    // Should return early if the user was not found.
    public RequestResult<ClientSessionData> clientLogin(
            String phoneNumber, String password) {
        String query = "SELECT * FROM NormalUser"
                + " WHERE phone_number = ? AND password = ?";
        String updateStatusQuery = "UPDATE NormalUser"
                + " SET status = AVAILABLE"
                + " WHERE user_ID = ?";
        
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, phoneNumber);
            stmt.setString(2, password);
            
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (!resultSet.next()) {
                    return new RequestResult<>(
                            null, null);
                }
                int userID = resultSet.getInt("user_ID");
                String name = resultSet.getString("display_name");
                try (PreparedStatement updateStmt 
                        = connection.prepareStatement(updateStatusQuery)) {
                    updateStmt.setInt(1, userID);
                    
                    int rowsAffected = updateStmt.executeUpdate();
                    if (rowsAffected == 1) {
                        ClientSessionData sessionData = new ClientSessionData(
                                userID, phoneNumber, name);
                        return new RequestResult<>(
                                sessionData, null);
                    } else {
                        return new RequestResult<>(
                                null, null);   
                    }
                } 
            }
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: "
                    + ex.getMessage());
        }
    }
    
    public RequestResult<Boolean> clientLogout(int userID) {
        String query = "UPDATE NormalUser SET status = ?"
                + " WHERE user_ID = ?";
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, 
                    NormalUserStatus.OFFLINE.toString());
            stmt.setInt(2, userID);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 1) {
                return new RequestResult<>(true, null);
            } else {
                return new RequestResult<>(false, null);
            }
        } catch (SQLException ex) {
            return new RequestResult<>(false, "DB Error: " 
                    + ex.getMessage());
        }
    }

    public RequestResult<Boolean> registerNormalUser(NormalUser user) {
        String query = "INSERT INTO NormalUser"
                + "(display_name, phone_number, email, pic"
                + ", password, gender, country, birth_date"
                + ", bio)"
                + " VALUES"
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            int i = 1;
            stmt.setString(i++, user.getDisplayName());
            stmt.setString(i++, user.getPhoneNumber());
            stmt.setString(i++, user.getEmail());
            stmt.setBytes(i++, user.getPic());
            stmt.setString(i++, user.getPassword());
            stmt.setString(i++, user.getGender().toString());
            stmt.setString(i++, user.getCountry().toString());
            stmt.setDate(i++, new java.sql.Date(user.getBirthDate().getTime()));
            stmt.setString(i++, user.getBio());
            int rowsInserted = stmt.executeUpdate();
            return new RequestResult<>(rowsInserted > 0, null);
        } catch (SQLException e) {
            return new RequestResult<>(false, "Error registering user: " + e.getMessage());
        }
    }
    
    public RequestResult<NormalUser> getNormalUserProfile(int userID) {
        String query = "SELECT * FROM NormalUser WHERE user_ID = ?";
        try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                NormalUser user = new NormalUser();
                user.setUserID(resultSet.getInt("user_ID"));
                user.setDisplayName((resultSet.getString("display_name")));
                user.setEmail(resultSet.getString("email"));
                user.setPhoneNumber(resultSet.getString("phone_number"));
                user.setBirthDate(resultSet.getDate("birth_date"));
                user.setGender(Gender.valueOf(resultSet.getString("gender")));
                user.setPic(resultSet.getBlob("pic").getBytes(0,
                        (int) resultSet.getBlob("pic").length()));
                return new RequestResult<>(user, null);
            } else {
                return new RequestResult<>(null, "User not found");
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, "Error fetching user profile: " + e.getMessage());
        }
    }

    public RequestResult<Boolean> editProfile(int userID, String username,
            Date birthDate, String bio, byte[] pic) {
        String query = "UPDATE NormalUser SET display_name = ?, bio = ? ,pic = ? WHERE user_ID = ?";
        try (PreparedStatement updateStatement = ConnectionManager.getConnection().prepareStatement(query)) {
            updateStatement.setString(1, username);
            updateStatement.setString(2, bio);
            birthDate;
            updateStatement.setBlob(3, pic);
            updateStatement.setInt(4, userID);
            int rowsAffected = updateStatement.executeUpdate();
            return new RequestResult<>(rowsAffected > 0, null);
        } catch (SQLException e) {
            return new RequestResult<>(false, "Error updating profile: " + e.getMessage());
        }
    }

    public RequestResult<byte[]> getNormalUserProfilePic(int userID) {
        String query = "SELECT pic FROM NormalUser WHERE user_ID = ?";
        try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Blob picBlob = resultSet.getBlob("pic");
                byte[] pic = picBlob.getBytes(0, (int) picBlob.length());
                return new RequestResult<>(pic, null);
            } else {
                return new RequestResult<>(null, null);
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, null);
        }
    }

    public RequestResult<Boolean> isPasswordValid(int userID, String password) {
        String query = "SELECT password FROM NormalUser WHERE user_ID = ?";
        try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && resultSet.getString("password").equals(password)) {
                return new RequestResult<>(true, null);
            } else {
                return new RequestResult<>(false, "Incorrect password.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Error checking password: " + e.getMessage());
        }
    }

    public RequestResult<Boolean> updatePassword(int userID, String oldPassword, String newPassword) {
        String query = "UPDATE NormalUser SET password = ? WHERE user_ID = ?";
        try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, userID);
            int rowsAffected = preparedStatement.executeUpdate();
            return new RequestResult<>(rowsAffected > 0, null);
        } catch (SQLException e) {
            return new RequestResult<>(false, "Error updating password: " + e.getMessage());
        }
    }
    
    public RequestResult<Boolean> setOnlineStatus(int userID, NormalUserStatus newStatus) {
        String query = "UPDATE NormalUser SET status = ? WHERE user_ID = ?";
        try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, newStatus.toString());
            preparedStatement.setInt(2, userID);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 1) {
                return new RequestResult<>(true, null);
            } else {
                return new RequestResult<>(false, null);
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Error updating online status: " + e.getMessage());
        }
    }
}
