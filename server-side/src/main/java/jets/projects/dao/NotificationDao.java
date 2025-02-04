package jets.projects.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jets.projects.classes.RequestResult;
import jets.projects.dbconnections.ConnectionManager;
import jets.projects.entities.Notification;
import jets.projects.entities.NotificationType;



public class NotificationDao {



    public RequestResult<List<Notification>> getAllNotifications(int userID) {
        List<Notification> notifications = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT * FROM notification WHERE user_ID = ? ORDER BY sent_at DESC";
            PreparedStatement selectStatement = connection.prepareStatement(query);
            selectStatement.setInt(1, userID);

            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                Notification notification = new Notification(
                    resultSet.getInt("notification_ID"),
                    resultSet.getInt("user_ID"),
                    NotificationType.valueOf(resultSet.getString("type")),
                    resultSet.getString("content"),
                    resultSet.getBoolean("is_read"),
                    resultSet.getTimestamp("sent_at")
                );
                notifications.add(notification);
            }

            return new RequestResult<>(notifications, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "Database error: " + e.getMessage());
        }
    }



    public RequestResult<Boolean> markNotificationsAsRead(int userID) {
        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "UPDATE notification SET is_read = 1 WHERE user_ID = ?";
            PreparedStatement updateStatement = connection.prepareStatement(query);
            updateStatement.setInt(1, userID);

            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                return new RequestResult<>(true, null);
            } else {
                return new RequestResult<>(false, "No notifications found for the user.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }
    }





    public RequestResult<List<Notification>> getUnreadNotifications(int userID) {


        List<Notification> notifications = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT * FROM notification WHERE user_ID = ? AND is_read = 0 ORDER BY sent_at DESC";
            PreparedStatement selectStatement = connection.prepareStatement(query);
            selectStatement.setInt(1, userID);

            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                Notification notification = new Notification(
                    resultSet.getInt("notification_ID"),
                    resultSet.getInt("user_ID"),
                    NotificationType.valueOf(resultSet.getString("type")),
                    resultSet.getString("content"),
                    resultSet.getBoolean("is_read"),
                    resultSet.getTimestamp("sent_at")
                );
                notifications.add(notification);
            }

            return new RequestResult<>(notifications, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "Database error: " + e.getMessage());
        }
       
    }

    public RequestResult<Boolean> deleteNotification(int userID, int notificationID) {
        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "DELETE FROM notification WHERE notification_ID = ? AND user_ID = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(query);
            deleteStatement.setInt(1, notificationID);
            deleteStatement.setInt(2, userID);

            int rowsAffected = deleteStatement.executeUpdate();

            if (rowsAffected == 1) {
                return new RequestResult<>(true, null);
            } else {
                return new RequestResult<>(false, "Notification not found or already deleted.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }

        
    }
    
}
