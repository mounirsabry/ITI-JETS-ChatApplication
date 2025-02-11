package jets.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jets.projects.classes.RequestResult;
import jets.projects.session.AdminToken;
import jets.projects.session.ClientToken;
import jets.projects.db_connections.ConnectionManager;

public class TokenValidatorDao {
    public RequestResult<Boolean> checkAdminToken(AdminToken token) {
        String query = "SELECT * FROM AdminUser WHERE user_ID = ?";
        
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, token.getUserID());
            
            boolean result;
            try (ResultSet resultSet = stmt.executeQuery()) {
                result = resultSet.next();
            }
            
            return new RequestResult<>(result, null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: "
                    + ex.getMessage());
        }
    }  
    
    public RequestResult<Boolean> checkClientToken(ClientToken token) {
        String query = "SELECT * FROM NormalUser WHERE user_ID = ?"
                + " AND phone_number = ?";
        
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, token.getUserID());
            stmt.setString(2, token.getPhoneNumber());
            
            boolean result;
            try (ResultSet resultSet = stmt.executeQuery()) {
                result = resultSet.next();
            }
            
            return new RequestResult<>(result, null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Erorr: "
                    + ex.getMessage());
        }
    }
}
