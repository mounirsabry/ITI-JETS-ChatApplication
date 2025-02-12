package jets.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jets.projects.classes.RequestResult;
import jets.projects.db_connections.ConnectionManager;
import jets.projects.entities.Notification;
import jets.projects.entities.NotificationType;

public class NotificationDao {
    public RequestResult<List<Notification>> getAllNotifications(
            int userID) {
        String query = "SELECT * FROM notification"
                + " WHERE user_ID = ?"
                + " ORDER BY sent_at DESC";
        
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setInt(1, userID);

            List<Notification> notifications = new ArrayList<>();
            try (ResultSet resultSet = stmt.executeQuery();) {
                while (resultSet.next()) {
                    Notification notification = new Notification(
                        resultSet.getInt("notification_ID"),
                        resultSet.getInt("user_ID"),
                        NotificationType.valueOf(
                                resultSet.getString("type")),
                        resultSet.getString("content"),
                        resultSet.getBoolean("is_read"),
                        resultSet.getTimestamp(
                                "sent_at").toLocalDateTime()
                    );
                    notifications.add(notification);
                }
            }
            return new RequestResult<>(notifications,
                    null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: " 
                    + ex.getMessage());
        }
    }
    
    public RequestResult<Boolean> markNotificationsAsRead(
            int userID) {
        String query = "UPDATE notification"
                + " SET is_read = TRUE"
                + " WHERE user_ID = ?"
                + " AND is_read = FALSE;";
        
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setInt(1, userID);
            stmt.executeUpdate();

            return new RequestResult<>(true, null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: " 
                    + ex.getMessage());
        }
    }
    
    public RequestResult<List<Notification>> getUnreadNotifications(
            int userID) {
        String query = "SELECT * FROM notification"
                + " WHERE user_ID = ?"
                + " AND is_read = FALSE"
                + " ORDER BY sent_at DESC";
        
        List<Notification> notifications = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setInt(1, userID);

            try (ResultSet resultSet = stmt.executeQuery();) {
                while (resultSet.next()) {
                    Notification notification = new Notification(
                        resultSet.getInt("notification_ID"),
                        resultSet.getInt("user_ID"),
                        NotificationType.valueOf(
                                resultSet.getString("type")),
                        resultSet.getString("content"),
                        resultSet.getBoolean("is_read"),
                        resultSet.getTimestamp(
                                "sent_at").toLocalDateTime()
                    );
                    notifications.add(notification);
                }
            }
            return new RequestResult<>(notifications, null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: " 
                    + ex.getMessage());
        }
    }
    
    // Return the ID of the newly created notification.
    public RequestResult<Notification> saveNotification
            (Notification notification) {
        int userID = notification.getUserID();
        String insertQuery = "INSERT INTO Notification"
                + " (user_ID, type, content)"
                + " VALUES(?, ?, ?);";
        String sentAtQuery = "SELECT sent_at FROM Notification"
                + " WHERE notification_ID = ?;";
        
        Connection connection = ConnectionManager.getConnection();
        if (connection == null) {
            return new RequestResult<>(null, "DB Error: "
                    + "Connection was null.");
        }
        try {
            // Disable auto commit.
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: "
                    + ex.getMessage());
        }
        
        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery, 
                    Statement.RETURN_GENERATED_KEYS);) {
            int i = 1;
            insertStmt.setInt(i++, userID);
            insertStmt.setString(i++, notification.getType().toString());
            insertStmt.setString(i++, notification.getContent());
            
            int rowsAffected = insertStmt.executeUpdate();
            if (rowsAffected == 0) {
                connection.rollback();
                return new RequestResult<>(null, null);
            }
            
            int notificationID;
            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    connection.rollback();
                    return new RequestResult<>(null, null);
                }
                notificationID = generatedKeys.getInt(1);
            }
            
            LocalDateTime sentAt;
            try (PreparedStatement queryStmt 
                    = connection.prepareStatement(sentAtQuery);) {
                queryStmt.setInt(1, notificationID);
                try (ResultSet sentAtResult = queryStmt.executeQuery();) {
                    if (!sentAtResult.next()) {
                        connection.rollback();
                        return new RequestResult<>(null, null);
                    }
                    sentAt 
                            = sentAtResult.getTimestamp(
                                    1).toLocalDateTime();
                }
            }
            
            connection.commit();
            
            Notification savedNotification = new Notification();
            savedNotification.setNotificationID(notificationID);
            savedNotification.setSentAt(sentAt);
            return new RequestResult<>(savedNotification,
                    null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: "
                    + ex.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.err.println("Failed to close connection.");
            }
        }
    }

    public RequestResult<Boolean> deleteNotification(int userID,
            int notificationID) {
        String query = "DELETE FROM notification"
                    + " WHERE notification_ID = ?"
                    + " AND user_ID = ?";
        
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setInt(1, notificationID);
            stmt.setInt(2, userID);

            int rowsAffected = stmt.executeUpdate();
            return new RequestResult<>(rowsAffected > 0, null);
        } catch (SQLException ex) {
            return new RequestResult<>(false, "DB Error: " 
                    + ex.getMessage());
        }
    }
}
