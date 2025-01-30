package jets.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jets.projects.classes.RequestResult;
import jets.projects.session.AdminToken;
import jets.projects.session.ClientToken;
import jets.projects.dbconnections.ConnectionManager;

public class TokenValidatorDao {
    public RequestResult<Boolean> checkAdminToken(AdminToken token) {
        String query = "SELECT * FROM AdminUser WHERE user_ID = ?";
        
        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement
                (query);) {
            
            statement.setInt(1, token.getUserID());
            ResultSet resultSet = statement.executeQuery();
            
            boolean result = resultSet.next();
            resultSet.close();
            
            return new RequestResult<>(result, null);
        } catch (SQLException ex) {
            return new RequestResult<>(null,
                    ex.getMessage());
        }
    }  
    
    public RequestResult<Boolean> checkClientToken(ClientToken token) {
        String query = "SELECT * FROM User WHERE ID = ? AND phone_number = ?";
        
        try (Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, token.getUserID());
            statement.setString(2, token.getPhoneNumber());
            
            ResultSet resultSet = statement.executeQuery();
            
            boolean result = resultSet.next();
            resultSet.close();
            
            return new RequestResult<>(result, null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, 
                    ex.getMessage());
        }
    }
}
