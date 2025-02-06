package jets.projects.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import jets.projects.classes.RequestResult;
import jets.projects.db_connections.ConnectionManager;
import jets.projects.entities.Gender;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;

public class UsersQueryDao {

    public RequestResult<Boolean> isNormalUserExists(int userID) {
        String query = "SELECT * FROM NormalUser WHERE user_ID = ?";
        try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(query)) {
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
    
    public RequestResult<List<NormalUser>> getAllNormalUsers() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<NormalUser> getNormalUserByID(int userID) {
        String query = "SELECT * FROM NormalUser WHERE user_ID = ?";
        try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                NormalUser user = new NormalUser();
                user.setUserID(rs.getInt("user_ID"));
                user.setDisplayName(rs.getString("display_name"));
                user.setPic(rs.getBlob("pic").getBytes(0,
                        (int) rs.getBlob("pic").length()));
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
    
    public RequestResult<List<NormalUser>> getNormalUserByName(String displayName) {
        return null;
    }
}
