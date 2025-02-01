package jets.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import jets.projects.classes.RequestResult;
import jets.projects.dbconnections.ConnectionManager;
import jets.projects.entities.ContactMessage;

public class ContactMessagesDao {
    
    public RequestResult<List<ContactMessage>> getContactMessages(int userID , int contactID ) {
        try (Connection connection = ConnectionManager.getConnection()) {
        PreparedStatement selectStatement = connection.prepareStatement(
                "SELECT * FROM contactmessage WHERE (sender_ID = ? AND receiver_ID = ?) OR (sender_ID = ? AND receiver_ID = ?) ORDER BY sent_at");
        selectStatement.setInt(1, userID);
        selectStatement.setInt(2, contactID);
        selectStatement.setInt(3, contactID);
        selectStatement.setInt(4, userID);

        ResultSet resultSet = selectStatement.executeQuery();
        List<ContactMessage> messages = new ArrayList<>();

        while (resultSet.next()) {
            ContactMessage message = new ContactMessage();
            message.setSenderID(resultSet.getInt("sender_ID"));
            message.setReceiverID(resultSet.getInt("receiver_ID"));
            message.setContent(resultSet.getString("content"));
            message.setFileURL(resultSet.getString("file_url"));
            message.setSentAt(resultSet.getTimestamp("sent_at"));           
            messages.add(message);
        }

        return new RequestResult<>(messages, null);
    } catch (SQLException e) {
        return new RequestResult<>(null, "Database error: " + e.getMessage());
    }
    }

    public RequestResult<List<ContactMessage>> getUnReadContactMessages(int senderID , int receiverID ) {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement selectStatement = connection.prepareStatement(
                    "SELECT * FROM contactmessage WHERE sender_ID = ? AND receiver_ID = ? AND is_read = 0");
            //"SELECT * FROM contactmessage " +"WHERE ((sender_ID = ? AND receiver_ID = ?) OR (sender_ID = ? AND receiver_ID = ?)) " +"AND is_read = false " + "ORDER BY sent_at";
            // w 3 w 4 contactID W usesrID
                    selectStatement.setInt(1, senderID);
            selectStatement.setInt(2, receiverID);
            ResultSet resultSet = selectStatement.executeQuery();
    
            List<ContactMessage> unreadMessages = new ArrayList<>();
            while (resultSet.next()) {
                ContactMessage message = new ContactMessage();
                message.setSenderID(resultSet.getInt("sender_ID"));
                message.setReceiverID(resultSet.getInt("receiver_ID"));
                message.setContent(resultSet.getString("content"));
                message.setSentAt(resultSet.getTimestamp("sent_at"));
                unreadMessages.add(message);
            }
    
            return new RequestResult<>(unreadMessages, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "Database error: " + e.getMessage());
        }
    }

    public RequestResult<Boolean> sendContactMessage(ContactMessage message) {
        try (Connection connection = ConnectionManager.getConnection()) {
           
    
            String query = "INSERT INTO contactmessage (sender_ID, receiver_ID, sent_at, content, file_uri, is_read, is_file) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(query);
         
        
            insertStatement.setLong(1, message.getSenderID());  
            insertStatement.setLong(2, message.getReceiverID()); 
            insertStatement.setTimestamp(3, new Timestamp(message.getSentAt().getTime()));
            insertStatement.setString(4, message.getContent());   
            insertStatement.setString(5, message.getFileURL());  
            insertStatement.setBoolean(6, message.getIsRead());      
            insertStatement.setBoolean(7, message.getIsFile());      
    
            
            int rowsAffected = insertStatement.executeUpdate();
    
            if (rowsAffected == 1) {
                return new RequestResult<>(true, null); 
            } else {
                return new RequestResult<>(false, null); 
            }
        } catch (SQLException e) {
            
            return new RequestResult<>(false, null);
        }
    }

    public RequestResult<Boolean> markContactMessagesAsRead(List<ContactMessage> messages) {
        if (messages == null || messages.isEmpty()) {
            return new RequestResult<>(false, null);
        }
    
        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "UPDATE contactmessage SET is_read = ? WHERE receiver_ID = ? AND sender_ID = ?";
            PreparedStatement updateStatement = connection.prepareStatement(query);
    
            ContactMessage firstMessage = messages.get(0);

    
            updateStatement.setBoolean(1, true); 
            updateStatement.setLong(2, firstMessage.getReceiverID()); // Receiver ID (messages received by the user)
            updateStatement.setLong(3, firstMessage.getSenderID()); // Sender ID
    
            int rowsAffected = updateStatement.executeUpdate();
    
            return new RequestResult<>(rowsAffected > 0, null);
        } catch (SQLException e) {
            return new RequestResult<>(false, null);
        }
    }

    public RequestResult<Boolean> sendContactFileMessage(int senderID, String file, int receiverID) {
        try (Connection connection = ConnectionManager.getConnection()) {
    
            String query = "INSERT INTO contactmessage (sender_ID, receiver_ID, content, file_uri, is_read, is_file) " +
                           "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(query);
            
            insertStatement.setLong(1, senderID); 
            insertStatement.setLong(2, receiverID); 
            
            insertStatement.setString(4, null); 
            insertStatement.setString(5, file); 
            insertStatement.setBoolean(6, false); 
            insertStatement.setBoolean(7, true); 
    
            int rowsAffected = insertStatement.executeUpdate();
    
            return new RequestResult<>(rowsAffected == 1, null);
        } catch (SQLException e) {
            return new RequestResult<>(false, null);
        }
    }
}
