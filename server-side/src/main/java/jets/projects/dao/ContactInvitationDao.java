package jets.projects.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import jets.projects.classes.RequestResult;
import jets.projects.dbconnections.ConnectionManager;
import jets.projects.entities.ContactInvitation;
import jets.projects.entities.NormalUser;

public class ContactInvitationDao {

    public RequestResult<List<NormalUser>> getAllInvitations(int userID) {

        List<NormalUser> invitations = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT * FROM contactinvitation JOIN normaluser on sender_ID = user_ID where receiver_id = ? ORDER BY sent_at DESC";
            
            PreparedStatement selectStatement = connection.prepareStatement(query);
            selectStatement.setInt(1, userID);
            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                NormalUser invitation = new NormalUser();

                invitation.setSenderID( resultSet.getInt("sender_ID"));
                invitation.setReceiverID(resultSet.getInt("receiver_ID"));
                invitation.setSentAt(java.sql.Timestamp.valueOf(resultSet.getTimestamp("sent_at").toLocalDateTime()));
                
                invitations.add(invitation);
            }

            return new RequestResult<>(invitations, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "Database error: " + e.getMessage());
        }


    }





    public RequestResult<Boolean> sendContactInvitation(ContactInvitation invitation) {


        try (Connection connection = ConnectionManager.getConnection()) {
            connection.setAutoCommit(false);
    
            // Check if an invitation exists in the opposite direction
            String checkQuery = "SELECT invitation_ID FROM contactinvitation WHERE sender_ID = ? AND receiver_ID = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setInt(1, invitation.getReceiverID());
            checkStatement.setInt(2, invitation.getSenderID());
            ResultSet resultSet = checkStatement.executeQuery();
    
            if (resultSet.next()) {
                // If mutual invitation exists, accept the invitation
                // Insert into contacts (both directions)
                String insertContactQuery = "INSERT INTO contact (first_ID, second_ID) VALUES (?, ?), (?, ?)";
                PreparedStatement insertContactStatement = connection.prepareStatement(insertContactQuery);
                insertContactStatement.setInt(1, invitation.getSenderID());
                insertContactStatement.setInt(2, invitation.getReceiverID());
                insertContactStatement.setInt(3, invitation.getReceiverID());
                insertContactStatement.setInt(4, invitation.getSenderID());
                insertContactStatement.executeUpdate();
    
                // Delete both invitations
                String deleteQuery = "DELETE FROM contactinvitation WHERE sender_ID = ? AND receiver_ID = ? OR sender_ID = ? AND receiver_ID = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, invitation.getSenderID());
                deleteStatement.setInt(2, invitation.getReceiverID());
                deleteStatement.setInt(3, invitation.getReceiverID());
                deleteStatement.setInt(4, invitation.getSenderID());
                deleteStatement.executeUpdate();
    
                connection.commit();
                return new RequestResult<>(true, null);
            } else {
                // If no mutual invitation, insert the new invitation
                String insertQuery = "INSERT INTO contactinvitation (sender_ID, receiver_ID, sent_at) VALUES (?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, invitation.getSenderID());
                insertStatement.setInt(2, invitation.getReceiverID());
                insertStatement.setTimestamp(3, java.sql.Timestamp.valueOf(
                invitation.getSentAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
    
                int rowsAffected = insertStatement.executeUpdate();
    
                if (rowsAffected == 1) {
                    connection.commit();
                    return new RequestResult<>(true, null);
                } else {
                    return new RequestResult<>(false, "Failed to send invitation.");
                }
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }

       
    }






    public RequestResult<Boolean> acceptContactInvitation(ContactInvitation invitation) {



            try (Connection connection = ConnectionManager.getConnection()) {
                connection.setAutoCommit(false);
                
                String insertQuery = "INSERT INTO contact (first_ID, second_ID) VALUES (?, ?), (?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, invitation.getSenderID());
                insertStatement.setInt(2, invitation.getReceiverID());
                insertStatement.setInt(3, invitation.getReceiverID());
                insertStatement.setInt(4, invitation.getSenderID());
                insertStatement.executeUpdate();
    
                String deleteQuery = "DELETE FROM contactinvitation WHERE invitation_ID = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, invitation.getInvitationID());
                deleteStatement.executeUpdate();
                
                connection.commit();
                return new RequestResult<>(true, null);
            } catch (SQLException e) {
                return new RequestResult<>(false, "Database error: " + e.getMessage());
            }
        
    }








    public RequestResult<Boolean> rejectContactInvitation(ContactInvitation invitation) {

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
