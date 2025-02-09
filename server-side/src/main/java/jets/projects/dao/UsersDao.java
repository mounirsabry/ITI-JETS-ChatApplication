package jets.projects.dao;

import java.sql.Blob;
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
    public RequestResult<ClientSessionData> clientLogin(String phoneNumber, String password) {
        String query = "SELECT * FROM NormalUser WHERE phone_number = ? AND password= ?";
        try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setString(2, password);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int userID = rs.getInt("user_ID");
                    String name = rs.getString("display_name");
                    try (PreparedStatement updateStatement = ConnectionManager.getConnection().prepareStatement("UPDATE NormalUser SET status = ? WHERE user_ID = ?")) {
                        updateStatement.setString(1, NormalUserStatus.AVAILABLE.toString());
                        updateStatement.setInt(2, userID);
                        int rowsAffected = updateStatement.executeUpdate();
                        if (rowsAffected == 1) {
                            ClientSessionData sessionData = new ClientSessionData(userID, phoneNumber, name);
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
    
    public RequestResult<ClientSessionData> adminAccountCreatedFirstLogin(String phoneNumber,
            String oldPassword, String newPassword) {
        return null;
    }

    public RequestResult<Boolean> clientLogout(int userID) {
        String query = "UPDATE NormalUser SET status = ? WHERE user_ID = ?";
        try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(query)) {
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

    public RequestResult<Boolean> registerNormalUser(NormalUser user) {
        String query = "INSERT INTO NormalUser (display_name, phone_number, email, pic, password, gender, country, birth_date, bio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertStatement = ConnectionManager.getConnection().prepareStatement(query)) {
            insertStatement.setString(1, user.getDisplayName());
            insertStatement.setString(2, user.getPhoneNumber());
            insertStatement.setString(3, user.getEmail());
            insertStatement.setBlob(4, new Blob(user.getPic()));
            insertStatement.setString(5, user.getPassword());
            insertStatement.setString(6, user.getGender().toString());
            insertStatement.setString(7, user.getCountry());
            insertStatement.setDate(8, new java.sql.Date(user.getBirthDate().getTime()));
            insertStatement.setString(9, user.getBio());
            int rowsInserted = insertStatement.executeUpdate();
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
