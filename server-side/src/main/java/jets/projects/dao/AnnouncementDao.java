package jets.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import jets.projects.classes.RequestResult;

import jets.projects.dbconnections.ConnectionManager;
import jets.projects.entities.Announcement;

public class AnnouncementDao {

    public RequestResult<Announcement> getLastAnnouncement() {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM Announcement ORDER BY announcement_ID DESC LIMIT 1"
            );
            ResultSet resultSet = preparedStatement.executeQuery();

            
            if (resultSet.next()) {
                int announcementID = resultSet.getInt("announcement_ID");
                String header = resultSet.getString("header");
                String content = resultSet.getString("content");
                Date sentAt = resultSet.getDate("sent_at");
                Announcement announcement = new Announcement(announcementID, header, content, sentAt);
                return new RequestResult<>(announcement, null);
            } else {
                return new RequestResult<>(null, null);
            }

        } catch (SQLException e) {
            return new RequestResult<>(null, 
                    e.getMessage());
        }
    }

     public RequestResult<List<Announcement>> getAllSubmitedAnnouncements() {
        List<Announcement> announcements = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT * FROM Announcement ORDER BY announcement_ID DESC";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int announcementID = resultSet.getInt("announcement_ID");
                String header = resultSet.getString("header");
                String content = resultSet.getString("content");
                Date sentAt = resultSet.getDate("sent_at");

                Announcement announcement = new Announcement(announcementID, header, content, sentAt);
                announcements.add(announcement);
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, 
                    e.getMessage());
        }
        return new RequestResult<>(announcements, null);
    }

    public RequestResult<Boolean> submitNewAnnouncement(Announcement newAnnouncement) {
        boolean isSuccess = false;

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "INSERT INTO Announcement (header, content) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, newAnnouncement.getHeader());
            preparedStatement.setString(2, newAnnouncement.getContent());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 1) {
                isSuccess = true;
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, 
                    e.getMessage());
        }

        return new RequestResult<>(isSuccess, null);
    }

    public RequestResult<List<Announcement>> getAllAnnouncements(int userID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllAnnouncements'");
    }

    public RequestResult<List<Announcement>> getUnreadAnnouncements(int userID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUnreadAnnouncements'");
    }
    
}
