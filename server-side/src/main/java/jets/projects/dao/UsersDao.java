package jets.projects.dao;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import jets.projects.classes.RequestResult;
import jets.projects.dbconnections.DBConnection;
import jets.projects.session.ClientSessionData;
import jets.projects.entities.Gender;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;

public class UsersDao {
    public RequestResult<Boolean> isNormalUserExists(int userID) {
        String query = "SELECT * FROM NormalUser WHERE user_ID = ?";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(query)){
                stmt.setInt(1, userID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    boolean exists = rs.getInt(1) > 0;
                    return new RequestResult<>(exists, null);
                }
            } catch (SQLException e) {
                return new RequestResult<>(false, "Database error: " + e.getMessage());
            }
            return new RequestResult<>(false, "Unexpected error occurred.");
    }        
    public NormalUser getUserById(int userID) {
        String query = "SELECT * FROM NormalUser WHERE user_ID = ?";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(query)){
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                NormalUser user = new NormalUser();
                user.setUserID(rs.getInt("user_ID"));
                user.setDisplayName(rs.getString("display_name"));
                user.setPic(rs.getBlob("pic"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setPassword(rs.getString("password"));
                user.setGender(Gender.valueOf(rs.getString("gender")));
                user.setBirthDate(rs.getDate("birth_date"));
                user.setBio(rs.getString("bio"));
                user.setCountry(rs.getString("country"));
                user.setCreatedAt(rs.getDate("created_at"));
                user.setIsAdminCreated(rs.getBoolean("is_admin_created"));
                user.setIsPasswordValid(rs.getBoolean("is_password_valid"));
                user.setStatus(NormalUserStatus.valueOf(rs.getString("status")));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return null; 
    }    
    public RequestResult<ClientSessionData> clientLogin(String phoneNumber, String password) {
        String query = "SELECT * FROM NormalUser WHERE phone_number = ? AND password= ?";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){
            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setString(2, password);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {                        
                    int userID = rs.getInt("user_ID");
                    String name= rs.getString("display_name");
                    try (PreparedStatement updateStatement = DBConnection.getConnection().prepareStatement("UPDATE NormalUser SET status = ? WHERE user_ID = ?")) {
                        updateStatement.setString(1, NormalUserStatus.AVAILABLE.toString());
                        updateStatement.setInt(2, userID);
                        int rowsAffected = updateStatement.executeUpdate();
                        if (rowsAffected == 1) {
                            ClientSessionData sessionData = new ClientSessionData(userID, phoneNumber,name);
                            return new RequestResult<>(sessionData, null);
                        } else {
                            return new RequestResult<>(null, "Failed to update user status.");
                        }
                    }
                } else {
                    return new RequestResult<>(null, "Invalid password.");
                }
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, e.getMessage());
        }
    }    
    public RequestResult<Boolean> clientLogout(int userID) {
        String query = "UPDATE NormalUser SET status = ? WHERE user_ID = ?";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){
            preparedStatement.setString(1, NormalUserStatus.OFFLINE.toString()); 
            preparedStatement.setInt(2, userID);
            int rowsAffected = preparedStatement.executeUpdate();    
            if (rowsAffected == 1) {
                return new RequestResult<>(true, null);
            } else {
                return new RequestResult<>(false, "Failed to log out user.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Error logging out: " + e.getMessage());
        }
    }
    public RequestResult<Boolean> registerNormalUser(String displayName, String phoneNumber,
            String email, Blob pic, String password, Gender gender,
            String country, Date birthDate, String bio) {
        String query = "INSERT INTO NormalUser (display_name, phone_number, email, pic, password, gender, country, birth_date, bio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertStatement = DBConnection.getConnection().prepareStatement(query)){
            insertStatement.setString(1, displayName);
            insertStatement.setString(2, phoneNumber);
            insertStatement.setString(3, email);
            insertStatement.setBlob(4, pic);
            insertStatement.setString(5, password);
            insertStatement.setString(6, gender.toString());
            insertStatement.setString(7, country);
            insertStatement.setDate(8, new java.sql.Date(birthDate.getTime()));
            insertStatement.setString(9, bio);
            int rowsInserted = insertStatement.executeUpdate();
            return new RequestResult<>(rowsInserted > 0, null);
        } catch (SQLException e) {
            return new RequestResult<>(false, "Error registering user: " + e.getMessage());
        }
    }
    public RequestResult<Boolean> setOnlineStatus(int userID, NormalUserStatus newStatus) {
        String query = "UPDATE NormalUser SET status = ? WHERE user_ID = ?";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){
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
    public RequestResult<Blob> getNormalUserProfilePic(int userID) {
        String query = "SELECT pic FROM NormalUser WHERE user_ID = ?";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Blob picture = resultSet.getBlob("pic");
                return new RequestResult<>(picture, null);
            } else {
                return new RequestResult<>(null, null);
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, null);
        }
    }
    public RequestResult<NormalUser> getNormalUserProfile(int userID) {
        String query = "SELECT * FROM NormalUser WHERE user_ID = ?";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){
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
                user.setPic(resultSet.getBlob("pic"));
                return new RequestResult<>(user, null);
            } else {
                return new RequestResult<>(null, "User not found");
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, "Error fetching user profile: " + e.getMessage());
        }
    }
    public RequestResult<Boolean> saveProfileChanges(int userID , String username , String bio , Blob pic) {
        String query = "UPDATE NormalUser SET display_name = ?, bio = ? ,pic = ? WHERE user_ID = ?";
        try (PreparedStatement updateStatement = DBConnection.getConnection().prepareStatement(query)){
            updateStatement.setString(1, username);
            updateStatement.setString(2, bio);
            updateStatement.setBlob(3, pic);
            updateStatement.setInt(4, userID);
            int rowsAffected = updateStatement.executeUpdate();
            return new RequestResult<>(rowsAffected > 0, null);
        } catch (SQLException e) {
            return new RequestResult<>(false, "Error updating profile: " + e.getMessage());
        }
    }
    public RequestResult<Boolean> isPasswordValid(int userID, String oldPassword) {
        String query = "SELECT password FROM NormalUser WHERE user_ID = ?";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && resultSet.getString("password").equals(oldPassword)) {
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
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, userID);
            int rowsAffected = preparedStatement.executeUpdate();    
            return new RequestResult<>(rowsAffected > 0, null);
        } catch (SQLException e) {
            return new RequestResult<>(false, "Error updating password: " + e.getMessage());
        }
    }
}
