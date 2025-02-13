package jets.projects.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jets.projects.classes.RequestResult;
import jets.projects.db_connections.ConnectionManager;
import jets.projects.entities.ContactMessage;

public class ContactMessagesDao{
    public RequestResult<List<ContactMessage>> getContactMessages(int userID,
            int contactID) {
        String query = "SELECT * FROM ContactMessage " +
                        "WHERE (sender_ID = ? AND receiver_ID = ?) " +
                        "OR (sender_ID = ? AND receiver_ID = ?) " +
                        "ORDER BY sent_at";

        try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, contactID);
            stmt.setInt(3, contactID);
            stmt.setInt(4, userID);

            ResultSet rs = stmt.executeQuery();
            List<ContactMessage> messages = new ArrayList<>();

            while (rs.next()) {
                int messageID = rs.getInt("message_ID");
                int senderID = rs.getInt("sender_ID");
                int receiverID = rs.getInt("receiver_ID");
                String content = rs.getString("content");
                boolean isRead = rs.getBoolean("is_read");
                boolean containsFile = rs.getBoolean("contains_file");
                Timestamp sentAt = rs.getTimestamp("sent_at");
                LocalDateTime localDateTime = sentAt.toLocalDateTime();
                ContactMessage contactMessage = new ContactMessage(
                       messageID, senderID, receiverID, localDateTime, content, isRead, containsFile, null
                );
                messages.add(contactMessage);
            }
            rs.close();
            return new RequestResult<>(messages, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "DB ERROR: " + e.getMessage());
        }
    }
    
    public RequestResult<byte[]> getContactMessageFile(int messageID) {
        String query = "SELECT message_file FROM ContactMessage WHERE message_ID = ?";
        try(Connection connection = ConnectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);){
            statement.setInt(1, messageID);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                Blob blob = rs.getBlob("message_file");
                byte[] file = blob.getBytes(1, (int) blob.length());
                rs.close();
                return new RequestResult<>(file, null);
            }else{
                rs.close();
            }
            return new RequestResult<>(null, "File not Found");
        }catch(SQLException e){
            return new RequestResult<>(null, "DB ERROR: " + e.getMessage());
        }
    }
    
    public RequestResult<List<ContactMessage>> getUnReadContactMessages(int userID, int contactID) {
        String query = "SELECT * FROM ContactMessage " +
                        "WHERE ((sender_ID = ? AND receiver_ID = ?) " +
                        "OR (sender_ID = ? AND receiver_ID = ?)) AND is_read=0 " +
                        "ORDER BY sent_at";

        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, contactID);
            stmt.setInt(3, contactID);
            stmt.setInt(4, userID);
            ResultSet rs = stmt.executeQuery();

            List<ContactMessage> unreadMessages = new ArrayList<>();
            while (rs.next()) {
                int messageID = rs.getInt("message_ID");
                int senderID = rs.getInt("sender_ID");
                int receiverID = rs.getInt("receiver_ID");
                String content = rs.getString("content");
                boolean isRead = rs.getBoolean("is_read");
                boolean containsFile = rs.getBoolean("contains_file");
                Timestamp sentAt = rs.getTimestamp("sent_at");
                LocalDateTime localDateTime = sentAt.toLocalDateTime();
                ContactMessage contactMessage = new ContactMessage(
                        messageID, senderID, receiverID, localDateTime, content, isRead, containsFile, null
                );
                unreadMessages.add(contactMessage);
            }
            return new RequestResult<>(unreadMessages, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "Database error: " + e.getMessage());
        }
    }

    public RequestResult<Integer> sendContactMessage(ContactMessage message) {
        String query = "INSERT INTO ContactMessage (sender_ID, receiver_ID, content, is_read, contains_file, message_file) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, message.getSenderID());
            stmt.setInt(2, message.getReceiverID());
            stmt.setString(3, message.getContent());
            stmt.setBoolean(4, message.getIsRead());
            stmt.setBoolean(5, message.getContainsFile());

            byte[] fileData = message.getFile();
            Blob blob = null;
            try {
                if (fileData == null || fileData.length == 0) {
                    stmt.setNull(6, java.sql.Types.BLOB);
                } else {
                    blob = connection.createBlob();
                    blob.setBytes(1, fileData);
                    stmt.setBlob(6, blob);
                }

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 1) {
                    // Retrieve the generated message_ID
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int messageID = generatedKeys.getInt(1); // Get the first column from the result
                            return new RequestResult<>(messageID, null); // Return the message ID
                        } else {
                            return new RequestResult<>(null, "No message ID returned.");
                        }
                    }
                } else {
                    return new RequestResult<>(null, "Message insertion failed.");
                }
            } finally {
                // Free the blob if it was created
                if (blob != null) {
                    blob.free();
                }
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, "Database error: " + e.getMessage());
        }
    }


    public RequestResult<Boolean> markContactMessagesAsRead(int userID, int contactID) {
        String query = "UPDATE ContactMessage SET is_read = 1 " +
                        "WHERE receiver_ID = ? AND sender_ID = ?";
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, contactID);
            int rowsAffected = stmt.executeUpdate();
            return new RequestResult<>(true, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "DB ERROR: " + e.getMessage());
        }
    }
    
    // Not an available feature for now.
    /*
    public RequestResult<Boolean> deleteContactMessage(ContactMessage message){
        String query = "DELETE FROM ContactMessage WHERE message_ID = ?";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)) {
            statement.setInt(1, message.getID());
            int rowsAffected = statement.executeUpdate();
            return new RequestResult<>(rowsAffected > 0, null);
        } catch (SQLException e) {
            return new RequestResult<>(false, e.getMessage());
        }
    } 
    */
}
