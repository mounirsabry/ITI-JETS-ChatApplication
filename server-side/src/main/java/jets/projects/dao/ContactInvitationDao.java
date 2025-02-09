package jets.projects.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
        List<NormalUser> invitationSenders = new ArrayList<>();
            String query = "SELECT * FROM contactinvitation c JOIN normaluser u on c.sender_ID = u.user_ID where receiver_id = ? ORDER BY sent_at DESC;";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)) {
            statement.setInt(1, userID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                NormalUser sender = new NormalUser();
                sender.setUserID(rs.getInt("user_ID"));
                sender.setDisplayName(rs.getString("display_name"));
                sender.setPic(rs.getBlob("pic"));
                sender.setEmail(rs.getString("email"));
                sender.setPhoneNumber(rs.getString("phone_number"));
                sender.setPassword(rs.getString("password"));
                sender.setGender(Gender.valueOf(rs.getString("gender")));
                sender.setBirthDate(rs.getDate("birth_date"));
                sender.setBio(rs.getString("bio"));
                sender.setCountry(rs.getString("country"));
                sender.setCreatedAt(rs.getDate("created_at"));
                sender.setIsAdminCreated(rs.getBoolean("is_admin_created"));
                sender.setIsPasswordValid(rs.getBoolean("is_password_valid"));
                sender.setStatus(NormalUserStatus.valueOf(rs.getString("status")));
                
                invitationSenders.add(sender);
            }
            return new RequestResult<>(invitationSenders, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "Database error: " + e.getMessage());
        }
    }
    
    // If invitation does not exist return null.
    public RequestResult<ContactInvitation> getContactInvitation(int invitationID) {
        
    }
    
    public RequestResult<Boolean> sendContactInvitation(int senderID,
            int receiverID, ContactGroup contactGroup) {
        contactGroup;
        String checkQuery = "SELECT invitation_ID FROM contactinvitation WHERE sender_ID = ? AND receiver_ID = ?";
        String insertContactQuery = "INSERT INTO contact (first_ID, second_ID) VALUES (?, ?)";
        String deleteQuery = "DELETE FROM contactinvitation WHERE sender_ID = ? AND receiver_ID = ? OR sender_ID = ? AND receiver_ID = ?";
        String insertQuery = "INSERT INTO contactinvitation (sender_ID, receiver_ID, sent_at) VALUES (?, ?, ?)";
    
        Connection connection = DBConnection.getConnection();
        try {
            connection.setAutoCommit(false);    
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, invitation.getReceiverID());
                checkStatement.setInt(2, invitation.getSenderID());
                ResultSet resultSet = checkStatement.executeQuery();
    
                if (resultSet.next()) {
                    // if mutual invitation accept request and add users as contacts to each other
                    try (PreparedStatement insertContactStatement = connection.prepareStatement(insertContactQuery);
                         PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                        
                        insertContactStatement.setInt(1, invitation.getSenderID());
                        insertContactStatement.setInt(2, invitation.getReceiverID());
                        insertContactStatement.addBatch();
                    
                        insertContactStatement.setInt(1, invitation.getReceiverID());
                        insertContactStatement.setInt(2, invitation.getSenderID());
                        insertContactStatement.addBatch();
                        insertContactStatement.executeBatch();

                        //delete pending invitation
                        deleteStatement.setInt(1, invitation.getSenderID());
                        deleteStatement.setInt(2, invitation.getReceiverID());
                        deleteStatement.setInt(3, invitation.getReceiverID());
                        deleteStatement.setInt(4, invitation.getSenderID());
                        deleteStatement.addBatch();
                        deleteStatement.executeBatch();
                        connection.commit();
                        return new RequestResult<>(true, null);
                    } catch (SQLException e) {
                        connection.rollback(); // Rollback on error
                        return new RequestResult<>(false, e.getMessage());
                    }
                } else {
                    // Insert a new invitation
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setInt(1, invitation.getSenderID());
                        insertStatement.setInt(2, invitation.getReceiverID());
                        insertStatement.setTimestamp(3, new Timestamp(invitation.getSentAt().getTime()));
    
                        int rowsAffected = insertStatement.executeUpdate();
                        if (rowsAffected == 1) {
                            connection.commit();
                            return new RequestResult<>(true, null);
                        } else {
                            connection.rollback();
                            return new RequestResult<>(false, "Failed to send invitation.");
                        }
                    } catch (SQLException e) {
                        connection.rollback(); // Rollback on error
                        return new RequestResult<>(false, e.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);  // Restore auto-commit mode
            } catch (SQLException ignored) {}
        }
    }
    
    public RequestResult<ContactInfo> acceptContactInvitation(
            ContactInvitation invitation) {
        String insertQuery = "INSERT INTO contact (first_ID, second_ID) VALUES (?, ?), (?, ?)";
        String deleteQuery = "DELETE FROM contactinvitation WHERE invitation_ID = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            connection.setAutoCommit(false);
            insertStatement.setInt(1, invitation.getSenderID());
            insertStatement.setInt(2, invitation.getReceiverID());
            insertStatement.setInt(3, invitation.getReceiverID());
            insertStatement.setInt(4, invitation.getSenderID());
            insertStatement.executeUpdate();            
            
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
