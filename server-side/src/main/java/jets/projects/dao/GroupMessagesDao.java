package jets.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jets.projects.classes.RequestResult;
import jets.projects.dbconnections.ConnectionManager;
import jets.projects.entities.GroupMessage;


public class GroupMessagesDao {

    public RequestResult<List<GroupMessage>> getGroupMessages(int userID) {

        List<GroupMessage> groupMessages = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT gm.message_ID, gm.group_ID, gm.sender_ID, gm.sent_at, gm.content, gm.is_file, gm.message_file " +
                           "FROM usersgroupmember ugm " +
                           "JOIN usersgroupmessage gm ON ugm.group_ID = gm.group_ID " +
                           "WHERE ugm.member_ID = ? ORDER BY gm.sent_at";
            PreparedStatement selectStatement = connection.prepareStatement(query);
            selectStatement.setInt(1, userID);

            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                GroupMessage message = new GroupMessage();

                message.setMessageID( resultSet.getInt("message_ID"));
                message.setGroupID( resultSet.getInt("group_ID"));
                message.setSenderID(resultSet.getInt("sender_ID"));
                message.setSentAt(java.sql.Timestamp.valueOf(resultSet.getTimestamp("sent_at").toLocalDateTime()));
                message.setContent( resultSet.getString("content"));
                message.setIsFile( resultSet.getBoolean("is_file"));
                message.setFileURL( resultSet.getBlob("message_file"));

                
                groupMessages.add(message);
            }

            return new RequestResult<>(groupMessages, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "Database error: " + e.getMessage());
        }
       
    }



    

    public RequestResult<Boolean> sendGroupMessage(GroupMessage message) {

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "INSERT INTO UsersGroupMessage (group_ID, sender_ID, content, is_file, message_file) " +
                           "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(query);
            insertStatement.setInt(1, message.getGroupID());
            insertStatement.setInt(2, message.getSenderID());
            insertStatement.setString(4, message.getContent());
            insertStatement.setBoolean(5, message.getIsFile());
            insertStatement.setBlob(6, message.getFileURL());

            int rowsAffected = insertStatement.executeUpdate();

            if (rowsAffected > 0) {
                return new RequestResult<>(true, null);
            } else {
                return new RequestResult<>(false, "Message sending failed.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }
        
    }

    public RequestResult<Boolean> sendGroupFileMessage(int senderID , int groupId , String file) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendGroupMessage'");
    }
    
}
