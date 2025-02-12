package jets.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import jets.projects.classes.RequestResult;
import jets.projects.db_connections.ConnectionManager;
import jets.projects.entities.Country;
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
                + " SET status = 'AVAILABLE'"
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
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?);";
        
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);) {
            int i = 1;
            stmt.setString(i++, user.getDisplayName());
            stmt.setString(i++, user.getPhoneNumber());
            stmt.setString(i++, user.getEmail());
            stmt.setBytes(i++, user.getPic());
            stmt.setString(i++, user.getPassword());
            stmt.setString(i++, user.getGender().toString());
            stmt.setString(i++, user.getCountry().toString());
            
            if (user.getBirthDate() != null) {
                stmt.setDate(i++, new java.sql.Date(
                        user.getBirthDate().getTime()));
            } else {
                stmt.setDate(i++, null);
            }
            
            stmt.setString(i++, user.getBio());
            int rowsInserted = stmt.executeUpdate();
            return new RequestResult<>(rowsInserted > 0, null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: "
                    + ex.getMessage());
        }
    }
    
    public RequestResult<NormalUser> getNormalUserProfile(int userID) {
        String query = "SELECT * FROM NormalUser WHERE user_ID = ?";
        
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet resultSet = stmt.executeQuery();
            
            if (!resultSet.next()) {
                return new RequestResult<>(null, null);
            }
            NormalUser user = new NormalUser();
            user.setUserID(
                    resultSet.getInt("user_ID"));
            user.setDisplayName(
                    resultSet.getString("display_name"));
            user.setPhoneNumber(
                    resultSet.getString("phone_number"));
            user.setEmail(
                    resultSet.getString("email"));
            user.setPic(
                    resultSet.getBytes("pic"));
            String genderString = resultSet.getString("gender");
            user.setGender(
                    Gender.valueOf(genderString));
            String countryString = resultSet.getString("country");
            user.setCountry(Country.valueOf(countryString));

            java.sql.Date sqlDate = resultSet.getDate("birth_date");
            if (sqlDate != null) {
                user.setBirthDate(new java.util.Date(sqlDate.getTime()));
            } else {
                user.setBirthDate(null); // or handle it appropriately
            }
            
            Timestamp createdAtStamp 
                    = resultSet.getTimestamp("created_at");
            user.setCreatedAt(createdAtStamp.toLocalDateTime());
            String statusString = resultSet.getString("status");
            user.setStatus(NormalUserStatus.valueOf(statusString));
            user.setBio(
                    resultSet.getString("bio"));
            user.setIsAdminCreated(
                    resultSet.getBoolean(
                            "is_admin_created"));
            user.setIsPasswordValid(
                    resultSet.getBoolean(
                            "is_password_valid"));
            return new RequestResult<>(user, null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: " 
                    + ex.getMessage());
        }
    }

    public RequestResult<Boolean> editProfile(int userID, String username,
            Date birthDate, String bio, byte[] pic) {
        String query = "UPDATE NormalUser"
                + " SET"
                + " display_name = ?,"
                + " birth_date = ?,"
                + " bio = ?,"
                + " pic = ?"
                + " WHERE user_ID = ?";
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);) {
            int i = 1;
            stmt.setString(i++, username);

            if (birthDate != null) {
                var sqlDate = new java.sql.Date(birthDate.getTime());
                stmt.setDate(i++, sqlDate);
            } else {
                stmt.setDate(i++, null);
            }

            stmt.setString(i++, bio);
            stmt.setBytes(i++, pic);
            stmt.setInt(i++, userID);
            
            int rowsAffected = stmt.executeUpdate();
            return new RequestResult<>(rowsAffected > 0, null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: " 
                    + ex.getMessage());
        }
    }

    public RequestResult<byte[]> getNormalUserProfilePic(int userID) {
        String query = "SELECT pic FROM NormalUser WHERE user_ID = ?";
        
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setInt(1, userID);
            ResultSet resultSet = stmt.executeQuery();
            
            if (!resultSet.next()) {
                return new RequestResult<>(null, null);
            }
            byte[] pic = resultSet.getBytes("pic");
            return new RequestResult<>(pic, null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: " 
                    + ex.getMessage());
        }
    }

    public RequestResult<Boolean> isPasswordValid(int userID, String password) {
        String query = "SELECT password FROM NormalUser "
                + " WHERE user_ID = ?"
                + " AND password = ?";
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setInt(1, userID);
            stmt.setString(2, password);
            ResultSet resultSet = stmt.executeQuery();
            
            return new RequestResult<>(resultSet.next(),
                    null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: " 
                    + ex.getMessage());
        }
    }

    public RequestResult<Boolean> updatePassword(int userID,
            String oldPassword, String newPassword) {
        String query = "UPDATE NormalUser SET password = ?"
                + " WHERE user_ID = ?";
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, userID);
            int rowsAffected = stmt.executeUpdate();
            
            return new RequestResult<>(rowsAffected > 0, null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: " 
                    + ex.getMessage());
        }
    }
    
    public RequestResult<Boolean> setOnlineStatus(int userID, NormalUserStatus newStatus) {
        String query = "UPDATE NormalUser SET status = ?"
                + " WHERE user_ID = ?";
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setString(1, newStatus.toString());
            stmt.setInt(2, userID);
            int rowsAffected = stmt.executeUpdate();
            
            return new RequestResult<>(rowsAffected > 0, null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: "
                    + ex.getMessage());
        }
    }
}
