package jets.projects.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jets.projects.classes.RequestResult;
import jets.projects.entities.ContactMessage;

public class ContactMessagesDao{
    public RequestResult<List<ContactMessage>> getContactMessages(int userID,
            int contactID) {
        String query = "SELECT * FROM ContactMessage " +
                        "WHERE (sender_ID = ? AND receiver_ID = ?) " +
                        "OR (sender_ID = ? AND receiver_ID = ?) " +
                        "ORDER BY sent_at";

        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(query)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, contactID);
            stmt.setInt(3, contactID);
            stmt.setInt(4, userID);

            ResultSet rs = stmt.executeQuery();
            List<ContactMessage> messages = new ArrayList<>();

            while (rs.next()) {
                ContactMessage message = new ContactMessage(
                    rs.getInt("message_ID"),
                    rs.getInt("sender_ID"),
                    rs.getInt("receiver_ID"),
                    rs.getDate("sent_at"),
                    rs.getString("content"),
                    rs.getBlob("message_file"),
                    rs.getBoolean("is_read"),
                    rs.getBoolean("is_file")
                );
                messages.add(message);
            }
            return new RequestResult<>(messages, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "Database error: " + e.getMessage());
        }
    }
    
    public RequestResult<byte[]> getContactMessageFile(int messageID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public RequestResult<List<ContactMessage>> getUnReadContactMessages(int senderID, int receiverID) {
        String query = "SELECT * FROM ContactMessage " +
                        "WHERE ((sender_ID = ? AND receiver_ID = ?) " +
                        "OR (sender_ID = ? AND receiver_ID = ?)) AND is_read=0 " +
                        "ORDER BY sent_at";

        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(query)) {
            stmt.setInt(1, senderID);
            stmt.setInt(2, receiverID);
            stmt.setInt(3, senderID);
            stmt.setInt(4, receiverID);
            ResultSet rs = stmt.executeQuery();

            List<ContactMessage> unreadMessages = new ArrayList<>();
            while (rs.next()) {
                unreadMessages.add(new ContactMessage(
                    rs.getInt("message_ID"),
                    rs.getInt("sender_ID"),
                    rs.getInt("receiver_ID"),
                    rs.getDate("sent_at"),
                    rs.getString("content"),
                    rs.getBlob("message_file"),
                    rs.getBoolean("is_read"),
                    rs.getBoolean("is_file")
                ));
            }
            return new RequestResult<>(unreadMessages, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "Database error: " + e.getMessage());
        }
    }
    
    public RequestResult<Boolean> sendContactMessage(ContactMessage message) {
        String query = "INSERT INTO ContactMessage (sender_ID, receiver_ID, sent_at, content, is_read , is_file ,message_file) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(query)) {
            stmt.setInt(1, message.getSenderID());
            stmt.setInt(2, message.getReceiverID());
            stmt.setTimestamp(3,Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(4, message.getContent());
            stmt.setBoolean(5, message.getIsRead());
            stmt.setBoolean(6, message.getIsFile());
            stmt.setBlob(7, message.getFileURL());

            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 1){
                return new RequestResult<>(true, null);
            }else{
                return new RequestResult<>(false, null);
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }
    }
    
    public RequestResult<Boolean> markContactMessagesAsRead(int contactID) {
        String query = "UPDATE ContactMessage SET is_read = 1 " +
                        "WHERE receiver_ID = ? AND sender_ID = ?";
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(query)) {
            stmt.setInt(1, messages.get(0).getReceiverID());
            stmt.setInt(2, messages.get(0).getSenderID());
            int rowsAffected = stmt.executeUpdate();
            return new RequestResult<>(rowsAffected > 0, null);
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
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
