package jets.projects.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.ArrayList;
import jets.projects.classes.RequestResult;

import jets.projects.db_connections.ConnectionManager;
import jets.projects.entities.Announcement;
import jets.projects.entity_info.AnnouncementInfo;

public class AnnouncementDao {
//    public RequestResult<Announcement> getLastAnnouncement() {
//        try (Connection connection = ConnectionManager.getConnection()) {
//            PreparedStatement preparedStatement = connection.prepareStatement(
//                "SELECT * FROM Announcement ORDER BY announcement_ID DESC LIMIT 1"
//            );
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            
//            if (resultSet.next()) {
//                int announcementID = resultSet.getInt("announcement_ID");
//                String header = resultSet.getString("header");
//                String content = resultSet.getString("content");
//                Date sentAt = resultSet.getDate("sent_at");
//                Announcement announcement = new Announcement(announcementID, header, content, sentAt);
//                return new RequestResult<>(announcement, null);
//            } else {
//                return new RequestResult<>(null, null);
//            }
//
//        } catch (SQLException e) {
//            return new RequestResult<>(null, 
//                    e.getMessage());
//        }
//    }

    public RequestResult<List<Announcement>> getAllAnnouncements() {
        String query 
                = "SELECT * FROM Announcement ORDER BY announcement_ID DESC";
        
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement 
                    = connection.prepareStatement(query);) {
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Announcement> announcements = new ArrayList<>();
            while (resultSet.next()) {
                int announcementID = resultSet.getInt(
                        "announcement_ID");
                String header = resultSet.getString("header");
                String content = resultSet.getString("content");
                Timestamp sentAtTimestamp = resultSet.getTimestamp(
                        "sent_at");
                LocalDateTime sentAt = sentAtTimestamp.toLocalDateTime();

                Announcement announcement = new Announcement(announcementID,
                        header, content, sentAt);
                announcements.add(announcement);
            }
            resultSet.close();
            return new RequestResult<>(announcements, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "DB Error: " +
                    e.getMessage());
        }
    }

    public RequestResult<Announcement> submitNewAnnouncement(Announcement newAnnouncement) {
        String announcementQuery = "INSERT INTO Announcement (header, content) VALUES (?, ?)";
        String fetchAnnouncementQuery = "SELECT * FROM Announcement WHERE announcement_ID = ?";
        String userQuery = "SELECT user_ID FROM NormalUser";
        String seenAnnouncementQuery = "INSERT INTO AnnouncementSeen (announcement_ID, user_ID, is_read) VALUES (?, ?, ?)";

        int newAnnouncementID = -1;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement announcementStmt = connection.prepareStatement(announcementQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement fetchAnnouncementStmt = connection.prepareStatement(fetchAnnouncementQuery);
             PreparedStatement userStmt = connection.prepareStatement(userQuery);
             PreparedStatement seenStmt = connection.prepareStatement(seenAnnouncementQuery)) {

            connection.setAutoCommit(false); // Begin transaction

            // Insert the new announcement
            announcementStmt.setString(1, newAnnouncement.getHeader());
            announcementStmt.setString(2, newAnnouncement.getContent());
            int rowsAffected = announcementStmt.executeUpdate();

            if (rowsAffected == 1) {
                // Get the generated announcement ID
                try (ResultSet generatedKeys = announcementStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newAnnouncementID = generatedKeys.getInt(1);
                    }
                }
            }

            if (newAnnouncementID == -1) {
                connection.rollback(); // Rollback if announcement ID retrieval fails
                return new RequestResult<>(null, "Failed to retrieve announcement ID");
            }

            // Fetch the created announcement from the database
            fetchAnnouncementStmt.setInt(1, newAnnouncementID);
            Announcement createdAnnouncement = null;
            try (ResultSet resultSet = fetchAnnouncementStmt.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("announcement_ID");
                    String header = resultSet.getString("header");
                    String content = resultSet.getString("content");
                    Timestamp sentAtTimestamp = resultSet.getTimestamp("sent_at");
                    ZoneId zoneId = ZoneId.systemDefault();
                    LocalDateTime sentAt = sentAtTimestamp.toInstant().atZone(zoneId).toLocalDateTime();

                    createdAnnouncement = new Announcement(id, header, content, sentAt);
                }
            }
            if (createdAnnouncement == null) {
                connection.rollback();
                return new RequestResult<>(null, "Failed to fetch created announcement");
            }

            // Fetch all user IDs
            List<Integer> userIDs = new ArrayList<>();
            try (ResultSet resultSet = userStmt.executeQuery()) {
                while (resultSet.next()) {
                    userIDs.add(resultSet.getInt("user_ID"));
                }
            }

            // Batch insert into AnnouncementSeen
            for (int userID : userIDs) {
                seenStmt.setInt(1, newAnnouncementID);
                seenStmt.setInt(2, userID);
                seenStmt.setBoolean(3, false);
                seenStmt.addBatch();
            }
            seenStmt.executeBatch(); // Execute batch insert

            connection.commit(); // Commit transaction
            connection.setAutoCommit(true);

            return new RequestResult<>(createdAnnouncement, null);

        } catch (SQLException e) {
            return new RequestResult<>(null, "Database error: " + e.getMessage());
        }
    }




    // All the announcements but relative to the normal user.
    public RequestResult<List<AnnouncementInfo>> getAllAnnouncements(int userID) {
        String query =
                "SELECT Announcement.announcement_ID, header, content, sent_at, is_read" +
                " FROM Announcement, AnnouncementSeen"+
                " WHERE AnnouncementSeen.announcement_ID = Announcement.announcement_ID" +
                " AND AnnouncementSeen.user_ID = ?" +
                " ORDER BY sent_at DESC";
        try(Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);){
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<AnnouncementInfo> announcements = new ArrayList<>();
            while (resultSet.next()) {
                int announcementID = resultSet.getInt("announcement_ID");
                String header = resultSet.getString("header");
                String content = resultSet.getString("content");
                boolean isRead = resultSet.getBoolean("is_read");
                Timestamp sentAtTimestamp = resultSet.getTimestamp("sent_at");
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDateTime sentAt = sentAtTimestamp.toInstant()
                        .atZone(zoneId).toLocalDateTime();

                Announcement announcement = new Announcement(announcementID,
                        header, content, sentAt);
                announcements.add(new AnnouncementInfo(announcement, userID, isRead));
            }
            return new RequestResult<>(announcements, null);

        }catch (SQLException e) {
            return new RequestResult<>(null, "DB ERROR: " + e.getMessage());
        }

    }

    public RequestResult<List<Announcement>> getUnreadAnnouncements(int userID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUnreadAnnouncements'");
    }
}
