package jets.projects.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jets.projects.classes.RequestResult;
import jets.projects.db_connections.ConnectionManager;
import jets.projects.entities.GroupMessage;


public class GroupMessagesDao {

    public RequestResult<List<GroupMessage>> getGroupMessages(int groupID) {
        String query = "SELECT * FROM UsersGroupMessage WHERE group_ID = ? order by sent_at;";
        List<GroupMessage> groupMessages = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query) ) {
            preparedStatement.setInt(1, groupID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                int message_ID = resultSet.getInt("message_ID");
                int sender_ID = resultSet.getInt("sender_ID");
                Timestamp timestamp = resultSet.getTimestamp("sent_at");
                LocalDateTime time = timestamp.toLocalDateTime();
                String content = resultSet.getString("content");
                Boolean contains_file = resultSet.getBoolean("contains_file");

                GroupMessage message = new GroupMessage(message_ID,sender_ID,groupID,time, content, contains_file, null);
                groupMessages.add(message);
            }
            resultSet.close();
            return new RequestResult<>(groupMessages, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "Database error: " + e.getMessage());
        }
    }
    
    public RequestResult<byte[]> getGroupMessageFile(int groupID, int messageID) {
        String query = "SELECT message_file FROM UsersGroupMessage WHERE message_ID = ?;";
        try(Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, messageID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                resultSet.close();
                return new RequestResult<>(null, null);
            }
            Blob blob = resultSet.getBlob("message_file");
            byte[] file = (blob != null) ? blob.getBytes(1, (int) blob.length()) : null;

            return new RequestResult<>(file, null);
        }catch (SQLException e){
            return new RequestResult<>(null, "DB ERROR: " + e.getMessage());
        }
    }


    public RequestResult<Integer> sendGroupMessage(GroupMessage message) {
        String query = "INSERT INTO UsersGroupMessage (group_ID, sender_ID, content, contains_file, message_file) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, message.getGroupID());
            stmt.setInt(2, message.getSenderID());
            stmt.setString(3, message.getContent() != null ? message.getContent() : ""); // Handle null content
            stmt.setBoolean(4, message.getContainsFile());

            if (message.getContainsFile() && message.getFile() != null) {
                stmt.setBytes(5, message.getFile());
            } else {
                stmt.setNull(5, Types.BLOB); // Ensure consistency
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                return new RequestResult<>(-1, null);
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int messageID = generatedKeys.getInt(1); 
                    return new RequestResult<>(
                            messageID, null);
                } else {
                    return new RequestResult<>(
                            -1, null);
                }
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, "Database error: " 
                    + e.getMessage());
        }
    }

//    public RequestResult<Boolean> sendGroupFileMessage(int senderID , int groupId , String file) {
//        String query = "INSERT INTO UserGroupMessage (sender_ID, group_ID, sent_at, content, is_read , is_file ,message_file) " +
//                        "VALUES (?, ?, NOW(), null, 0, 1, ?)";
//        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(query)) {
//            stmt.setInt(1, senderID);
//            stmt.setInt(2, groupId);
//            stmt.setString(3, file);
//
//            int rowsAffected = stmt.executeUpdate();
//            return new RequestResult<>(rowsAffected == 1, null);
//
//        } catch (SQLException e) {
//            return new RequestResult<>(false, "Database error: " + e.getMessage());
//        }
//    }
    
}
