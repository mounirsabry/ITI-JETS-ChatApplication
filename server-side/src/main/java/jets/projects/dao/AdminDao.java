package jets.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jets.projects.classes.RequestResult;
import jets.projects.db_connections.ConnectionManager;
import jets.projects.session.AdminSessionData;

public class AdminDao {
        // Admin does not have an online status.
        public RequestResult<AdminSessionData> adminLogin(int userID,
                String password) {
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
                return new RequestResult<>(adminSessionData, null);
            } else {
                return new RequestResult<>(null, null);
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, 
                    e.getMessage());
        }
    }

    public RequestResult<Boolean> adminLogout(int userID) {
        try (Connection connection = ConnectionManager.getConnection()) {

            PreparedStatement updateStatement = connection.prepareStatement(
                    "UPDATE AdminUser SET admin_status = 'OFFLINE' WHERE user_ID = ?");
            updateStatement.setInt(1, userID);
            if(updateStatement.executeUpdate() != 1)
                return new RequestResult<>(false, null);
            return new RequestResult<>(true, null);
            
        } catch (SQLException e) {
            return new RequestResult<>(null, 
                    e.getMessage());
        }
    }
}
