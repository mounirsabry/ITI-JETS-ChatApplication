package jets.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jets.projects.classes.AdminToken;
import jets.projects.classes.ClientToken;
import jets.projects.dbconnections.ConnectionManager;

public class TokenValidatorDao {
    public boolean checkAdminToken(AdminToken token) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }  
    
    public boolean checkClientToken(ClientToken token) {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM User WHERE ID = ? AND phone_number = ?");
            statement.setInt(1, token.getUserID());
            statement.setString(2, token.getPhoneNumber());
            
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            return false;
        }
    }
}
