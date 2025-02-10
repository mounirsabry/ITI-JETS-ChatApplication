package jets.projects.dao;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jets.projects.classes.RequestResult;
import jets.projects.db_connections.ConnectionManager;
import jets.projects.entities.ContactGroup;
import jets.projects.entities.ContactInvitation;
import jets.projects.entities.Gender;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entity_info.ContactInvitationInfo;

public class ContactInvitationDao{

    public RequestResult<List<ContactInvitationInfo>> getAllInvitations(int userID) {

            String query = "SELECT * FROM ContactInvitation c " +
                    "JOIN NormalUser u ON c.sender_ID = u.user_ID " +
                    "WHERE c.receiver_ID = ?" +
                    "ORDER BY c.sent_at DESC;";
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement selectStatement = connection.prepareStatement(query);) {
            selectStatement.setInt(1, userID);
            ResultSet resultSet = selectStatement.executeQuery();
            List<ContactInvitationInfo> invitations = new ArrayList<>();
            while(resultSet.next()){
                int invitationID = resultSet.getInt("invitation_ID");
                int senderID = resultSet.getInt("sender_ID");
                int receiverID = resultSet.getInt("receiver_ID");
                Timestamp timestamp = resultSet.getTimestamp("sent_at");
                LocalDateTime sentAt = timestamp.toLocalDateTime();
                String displayName = resultSet.getString("display_name");
                Blob blob = resultSet.getBlob("pic");
                byte[] pic = blob != null ? blob.getBytes(1, (int) blob.length()) : null;
                ContactInvitation contactInvitation = new ContactInvitation(invitationID, senderID, receiverID, sentAt);
                invitations.add(new ContactInvitationInfo(contactInvitation, displayName, pic));
            }
            resultSet.close();
            return new RequestResult<>(invitations, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "DB ERROR: " + e.getMessage());
        }
    }
    
    // If invitation does not exist return null.
    public RequestResult<ContactInvitation> getContactInvitation(int invitationID) {
        String query = "SELECT * FROM ContactInvitation WHERE invitation_ID = ?";
        try(Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);){
            preparedStatement.setInt(1, invitationID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int senderID = resultSet.getInt("sender_ID");
                int receiverID = resultSet.getInt("receiver_ID");
                Timestamp timestamp = resultSet.getTimestamp("sent_at");
                LocalDateTime sentAt = timestamp.toLocalDateTime();
                ContactInvitation contactInvitation = new ContactInvitation(invitationID, senderID, receiverID, sentAt);
                resultSet.close();
                return new RequestResult<>(contactInvitation, null);
            }else{
                resultSet.close();
                return new RequestResult<>(null, "Invitation not found. ID: " + invitationID);
            }
        }catch (SQLException e) {
            return new RequestResult<>(null, "DB ERROR: " + e.getMessage());
        }
    }

    public RequestResult<Boolean> sendContactInvitation(int senderID
            , int receiverID, ContactGroup contactGroup) {
        String checkQuery = "SELECT COUNT(*) FROM ContactInvitation WHERE sender_ID = ? AND receiver_ID = ?";
        String checkQuery2 = "SELECT COUNT(*) FROM ContactInvitation WHERE sender_ID = ? AND receiver_ID = ?";
        String insertQuery = "INSERT INTO ContactInvitation (sender_ID, receiver_ID, category) VALUES (?, ?, ?)";

        try (Connection connection = ConnectionManager.getConnection()) {
            // Step 1: Check if invitation already exists
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, senderID);
                checkStatement.setInt(2, receiverID);
                ResultSet resultSet = checkStatement.executeQuery();
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return new RequestResult<>(false, "Invitation already sent.");
                }
            }
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery2)) {
                checkStatement.setInt(1, receiverID);
                checkStatement.setInt(2, senderID);
                ResultSet resultSet = checkStatement.executeQuery();
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return new RequestResult<>(false, "You have a friend request from this user");
                }
            }

            // Step 2: Insert new invitation
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setInt(1, senderID);
                insertStatement.setInt(2, receiverID);
                insertStatement.setString(3, contactGroup.name());
                int rowsAffected = insertStatement.executeUpdate();
                return (rowsAffected == 1)
                        ? new RequestResult<>(true, null)
                        : new RequestResult<>(false, "Failed to send invitation.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }
    }


    public RequestResult<ContactInfo> acceptContactInvitation(
            ContactInvitation invitation) {
        String selectQuery = "SELECT category FROM ContactInvitation WHERE invitation_ID = ?";
        String insertQuery = "INSERT INTO contact (first_ID, second_ID, category) VALUES (?, ?, ?), (?, ?, ?)";
        String deleteQuery = "DELETE FROM ContactInvitation WHERE invitation_ID = ?";
        String selectQuery2 = "SELECT display_name, pic FROM NormalUser WHERE user_ID = ?";

        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            PreparedStatement selectStatement2 = connection.prepareStatement(selectQuery2);
            ){
            connection.setAutoCommit(false);
            selectStatement.setInt(1, invitation.getInvitationID());
            ResultSet resultSet = selectStatement.executeQuery();
            if(!resultSet.next()){
                resultSet.close();
                return new RequestResult<>(null, "Invitation not found.");
            }
            String category = resultSet.getString("category");
            insertStatement.setInt(1, invitation.getSenderID());
            insertStatement.setInt(2, invitation.getReceiverID());
            insertStatement.setString(3, category);
            insertStatement.setInt(4, invitation.getReceiverID());
            insertStatement.setInt(5, invitation.getSenderID());
            insertStatement.setString(6, category);
            if(insertStatement.executeUpdate() != 2){
                resultSet.close();
                return new RequestResult<>(null, "Failed to accept invitation.");
            }
            resultSet.close();
            
            deleteStatement.setInt(1, invitation.getInvitationID());
            deleteStatement.executeUpdate();
            connection.commit();
            return new RequestResult<>(true, null);
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }        
    }
    
    public RequestResult<Boolean> rejectContactInvitation(
            ContactInvitation invitation) {

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "DELETE FROM contactinvitation WHERE invitation_ID = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(query);
            deleteStatement.setInt(1, invitation.getInvitationID());
            int rowsAffected = deleteStatement.executeUpdate();
            if (rowsAffected == 1) {
                return new RequestResult<>(true, null);
            } else {
                return new RequestResult<>(false, "Failed to reject invitation.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }
    }
}
