package jets.projects.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jets.projects.dbconnections.ConnectionManager;
import jets.projects.classes.AdminSessionData;

public class UsersDao {

    public AdminSessionData adminLogin(int userID, String password) {
        try (Connection connection = ConnectionManager.getConnection()) {
            
            PreparedStatement selectStatement = connection.prepareStatement(
                    "SELECT * FROM AdminUser WHERE user_ID = ?");
            selectStatement.setInt(1, userID);
            ResultSet resultSet = selectStatement.executeQuery();
        
            if (resultSet.next() && resultSet.getString("password").equals(password)) {
                
                PreparedStatement updateStatement = connection.prepareStatement(
                        "UPDATE AdminUser SET admin_status = 'ONLINE' WHERE user_ID = ?");
                updateStatement.setInt(1, userID);
                if(updateStatement.executeUpdate() != 1)
                    return null;
        
                AdminSessionData adminSessionData = new AdminSessionData(userID, resultSet.getString("display_name"));
                return adminSessionData;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
            return null;
        }
    }

    public boolean logout(int userID) {
        try (Connection connection = ConnectionManager.getConnection()) {

            PreparedStatement updateStatement = connection.prepareStatement(
                    "UPDATE AdminUser SET admin_status = 'OFFLINE' WHERE user_ID = ?");
            updateStatement.setInt(1, userID);
            if(updateStatement.executeUpdate() != 1)
                return false;
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace(); 
            return false;
        }
    }
    
}
