package jets.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import jets.projects.dbconnections.ConnectionManager;
import jets.projects.entities.Announcement;

public class AnnouncementDao {

    public Announcement getLastAnnouncement() {
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
                return announcement;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

     public List<Announcement> getAllAnnouncements() {
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
            e.printStackTrace();
            return null;
        }

        return announcements;
    }

    public boolean submitNewAnnouncement(Announcement newAnnouncement) {
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
            e.printStackTrace();
            return false;
        }

        return isSuccess;
    }
    
}
