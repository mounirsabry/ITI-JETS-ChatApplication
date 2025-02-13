package jets.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jets.projects.classes.RequestResult;
import jets.projects.db_connections.ConnectionManager;
import jets.projects.entities.NormalUser;

public class UsersManipulationDao {

    public RequestResult<Boolean> addNormalUser(NormalUser user) {
        String query = "INSERT INTO NormalUser"
                + "(display_name, phone_number, email, pic"
                + ", password, gender, country, birth_date"
                + ", status, bio)"
                + " VALUES"
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

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

            if(user.getBirthDate() != null){
                java.sql.Date sqlDate = new java.sql.Date(
                        user.getBirthDate().getTime());
                stmt.setDate(i++, sqlDate);

            }else{
                stmt.setDate(i++, null);
            }

            stmt.setString(i++, user.getStatus().toString());
            stmt.setString(i++, user.getBio());
            
            int rowsAffected = stmt.executeUpdate();
            boolean isInserted = false;
            if (rowsAffected == 1) {
                isInserted = true;
            }
            return new RequestResult<>(isInserted, null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: "
                    + ex.getMessage());
        }
    }

    public RequestResult<Boolean> deleteNormalUser(int userID) {
        String query = "DELETE FROM NormalUser WHERE user_ID = ?";
        
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);) {
            
            stmt.setInt(1, userID);
            
            int rowsAffected = stmt.executeUpdate();
            boolean isDeleted = false;
            if (rowsAffected == 1) {
                isDeleted = true;
            }
            return new RequestResult<>(isDeleted, null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: "
                    + ex.getMessage());
        }
    }
}
