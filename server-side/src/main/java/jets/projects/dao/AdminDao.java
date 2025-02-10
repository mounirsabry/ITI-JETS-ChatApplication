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
            String query = "SELECT * FROM AdminUser WHERE user_ID = ? AND password = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(
                     query);) {
            selectStatement.setInt(1, userID);
            selectStatement.setString(2, password);
            ResultSet resultSet = selectStatement.executeQuery();
        
            if (resultSet.next()) {

                AdminSessionData adminSessionData = new AdminSessionData(userID, resultSet.getString("display_name"));

                resultSet.close();
                return new RequestResult<>(adminSessionData, null);
            } else {
                resultSet.close();
                return new RequestResult<>(null, null);
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, "DB Error: " +
                    e.getMessage());
        }
    }

//    public RequestResult<Boolean> adminLogout(int userID) {
//        String query = "SELECT * FROM AdminUser WHERE user_ID = ?";
//
//        try (Connection connection = ConnectionManager.getConnection();
//             PreparedStatement selectStatement = connection.prepareStatement(query);) {
//
//            selectStatement.setInt(1, userID);
//            ResultSet resultSet = selectStatement.executeQuery();
//
//            if (resultSet.next()) {
//                resultSet.close();
//                return new RequestResult<>(true, null);
//            } else {
//                resultSet.close();
//                return new RequestResult<>(false, null);
//            }
//
//        } catch (SQLException e) {
//            return new RequestResult<>(null,
//                    e.getMessage());
//        }
//    }
}
