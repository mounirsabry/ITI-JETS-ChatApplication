package jets.projects.dao;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import jets.projects.classes.RequestResult;
import jets.projects.db_connections.ConnectionManager;
import jets.projects.entities.Country;
import jets.projects.entities.Gender;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;

public class UsersQueryDao {
    public RequestResult<Boolean> isNormalUserExistsByID(int userID) {
        String query = "SELECT user_ID FROM NormalUser WHERE user_ID = ?";
        
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userID);
            
            boolean isExists;
            try (ResultSet resultSet = stmt.executeQuery()) {
                isExists = resultSet.next();
            }
            
            return new RequestResult<>(isExists, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "DB Error: " + e.getMessage());
        }
    }
    
    // Returns the ID of the normal user if exists.
    // Otherwise, returns null for the integer value.
    public RequestResult<Integer> isNormalUserExistsByPhoneNumber(
            String phoneNumber) {
        String query = "SELECT user_ID FROM NormalUser WHERE phone_number = ?";
        
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareCall(query);) {
            stmt.setString(1, phoneNumber);
            
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (!resultSet.next()) {
                    return new RequestResult<>(null, null);
                }
                int ID = resultSet.getInt(1);
                return new RequestResult<>(ID, null);
            }
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: "
                    + ex.getMessage());
        }
    }

    public RequestResult<String> getNormalUserDisplayNameByID(int userID) {
        String query = "SELECT display_name FROM NormalUser"
                + " WHERE user_ID = ?";
        
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareCall(query);) {
            stmt.setInt(1, userID);
            
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (!resultSet.next()) {
                    return new RequestResult<>(null, null);
                }
                String displayName = resultSet.getString(1);
                return new RequestResult<>(displayName, null);
            }
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: "
                    + ex.getMessage());
        }
    }

    public RequestResult<NormalUser> getNormalUserByPhoneNumber(String phoneNumber) {
        String query = "SELECT * FROM NormalUser WHERE phone_number = ?";
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setString(1, phoneNumber);
            
            ResultSet resultSet = stmt.executeQuery();
            if (!resultSet.next()) {
                return new RequestResult<>(null, null);
            }
            
            NormalUser user = new NormalUser();
            user.setUserID(
                    resultSet.getInt("user_ID"));
            user.setDisplayName(
                    resultSet.getString("display_name"));
            
            user.setPhoneNumber(resultSet.getString("phone_number"));
            user.setEmail(resultSet.getString("email"));
            
            Blob blob = resultSet.getBlob("pic");
            if (blob == null) {
                user.setPic(null);
            } else {
                user.setPic(blob.getBytes(1, (int) blob.length()));
            }
            /*
            Blob picBlob = resultSet.getBlob("pic");
            if (picBlob != null) {
                user.setPic(picBlob.getBytes(1, (int) picBlob.length()));
            }
            */
            
            //user.setPassword(resultSet.getString("password"));
            String genderString = resultSet.getString("gender");
            user.setGender(Gender.valueOf(genderString));
            
            String countryString = resultSet.getString("country");
            user.setCountry(Country.valueOf(countryString));
            
            user.setBirthDate(resultSet.getDate("birth_date"));
            
            Timestamp timestamp = resultSet.getTimestamp("created_at");
            user.setCreatedAt(timestamp.toLocalDateTime());

            String statusString = resultSet.getString("status");
            user.setStatus(NormalUserStatus.valueOf(statusString));
            user.setBio(resultSet.getString("bio"));
            
            user.setIsAdminCreated(
                    resultSet.getBoolean("is_admin_created"));
            user.setIsPasswordValid(
                    resultSet.getBoolean("is_password_valid"));
            return new RequestResult<>(user, null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: "
                    + ex.getMessage());
        }
    }
}
