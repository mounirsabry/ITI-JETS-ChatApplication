package jets.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jets.projects.classes.RequestResult;
import jets.projects.dbconnections.ConnectionManager;
import jets.projects.entities.GroupMember;

public class GroupMemberDao {
    public RequestResult<Boolean> isMember(int groupID, int userID){

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT COUNT(*) FROM usersgroupmember WHERE group_ID = ? AND member_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, groupID);
            preparedStatement.setInt(2, userID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return new RequestResult<>(true, null); 
            } else {
                return new RequestResult<>(false, "User is not a member of the group.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }
       

    }






    public RequestResult<List<GroupMember>> getAllMembers(int groupID) {
         List<GroupMember> groupMembers = new ArrayList<>();

    try (Connection connection = ConnectionManager.getConnection()) {
        String query = "SELECT username, picture " +
                       "FROM groupmember gm " +
                       "JOIN user u ON gm.member_ID = u.ID " +
                       "WHERE gm.group_ID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, groupID);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {

            String username = resultSet.getString("username");
            String picture = resultSet.getString("picture");

            GroupMember groupMember = new GroupMember();
            groupMember.setPic(picture);
            groupMember.setUsername(username);
        }

        return new RequestResult<>(groupMembers.isEmpty() ? null : groupMembers, null);
    } catch (SQLException e) {
        return new RequestResult<>(null, e.getMessage());
    }
    }


    
    public RequestResult<Boolean> addMemberToGroup(int userID, int groupID, int otherID) {
        try (Connection connection = ConnectionManager.getConnection()) {
           

            String addMemberQuery = "INSERT INTO groupmember (group_ID, member_ID) VALUES (?, ?)";
            PreparedStatement addMemberStatement = connection.prepareStatement(addMemberQuery);
            addMemberStatement.setInt(1, groupID);
            addMemberStatement.setInt(2, otherID); //member 
    
            int rowsAffected = addMemberStatement.executeUpdate();
    
            if (rowsAffected == 1) {
                return new RequestResult<>(true, null); 
            } else {
                return new RequestResult<>(false, "Failed to add user to the group.");
            }
        } catch (SQLException e) {
    
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }
    }




    public RequestResult<Boolean> removeMemberFromGroup(int userID, int groupID, int otherID) {

        try (Connection connection = ConnectionManager.getConnection()) {
            // Check if the logged-in user is the group admin
            String adminCheckQuery = "SELECT group_admin_ID FROM usersgroup WHERE group_ID = ?";
            PreparedStatement adminCheckStatement = connection.prepareStatement(adminCheckQuery);
            adminCheckStatement.setInt(1, groupID);

            ResultSet adminCheckResult = adminCheckStatement.executeQuery();

            if (!adminCheckResult.next() || adminCheckResult.getInt("group_admin_ID") != userID) {
                return new RequestResult<>(false, "Unauthorized: Only the group admin can remove members.");
            }

            // Remove the user from the group
            String query = "DELETE FROM usersgroupmember WHERE group_ID = ? AND member_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, groupID);
            preparedStatement.setInt(2, otherID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 1) {
                return new RequestResult<>(true, null);
            } else {
                return new RequestResult<>(false, "Failed to remove user from the group.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }
       
    }




    public RequestResult<Boolean> leaveGroupAsMemeber(int userID, int groupID) {

        try (Connection connection = ConnectionManager.getConnection()) {
            // Check if the user is the group admin
            String adminCheckQuery = "SELECT group_admin_ID FROM UsersGroup WHERE group_ID = ?";
            PreparedStatement adminCheckStatement = connection.prepareStatement(adminCheckQuery);
            adminCheckStatement.setInt(1, groupID);

            ResultSet adminCheckResult = adminCheckStatement.executeQuery();

            if (adminCheckResult.next() && adminCheckResult.getInt("group_admin_ID") == userID) {
                return new RequestResult<>(false, "Group admin cannot leave the group. Assign a new admin first.");
            }

            // Remove the user from the group
            String query = "DELETE FROM UsersGroupMember WHERE group_ID = ? AND member_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, groupID);
            preparedStatement.setInt(2, userID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 1) {
                return new RequestResult<>(true, null); // Success
            } else {
                return new RequestResult<>(false, "Failed to leave the group.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }
        
    }



    public RequestResult<Boolean> leaveGroupAsAdmin(int userID, int groupID, int newAdminID) {

        try (Connection connection = ConnectionManager.getConnection()) {
            // Check if the logged-in user is the group admin
            String adminCheckQuery = "SELECT group_admin_ID FROM usersgroup WHERE group_ID = ?";
            PreparedStatement adminCheckStatement = connection.prepareStatement(adminCheckQuery);
            adminCheckStatement.setInt(1, groupID);

            ResultSet adminCheckResult = adminCheckStatement.executeQuery();

            if (!adminCheckResult.next() || adminCheckResult.getInt("group_admin_ID") != userID) {
                return new RequestResult<>(false, "Unauthorized: Only the group admin can perform this action.");
            }

            // Assign the new admin
            String updateAdminQuery = "UPDATE usersgroup SET group_admin_ID = ? WHERE group_ID = ?";
            PreparedStatement updateAdminStatement = connection.prepareStatement(updateAdminQuery);
            updateAdminStatement.setInt(1, newAdminID);
            updateAdminStatement.setInt(2, groupID);

            int updateRowsAffected = updateAdminStatement.executeUpdate();

            if (updateRowsAffected != 1) {
                return new RequestResult<>(false, "Failed to assign new admin.");
            }

            // Remove the old admin from the group
            String leaveQuery = "DELETE FROM usersgroupmember WHERE group_ID = ? AND member_ID = ?";
            PreparedStatement leaveStatement = connection.prepareStatement(leaveQuery);
            leaveStatement.setInt(1, groupID);
            leaveStatement.setInt(2, userID);

            int leaveRowsAffected = leaveStatement.executeUpdate();

            if (leaveRowsAffected == 1) {
                return new RequestResult<>(true, null); 
            } else {
                return new RequestResult<>(false, "Failed to leave the group.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }
        
    }



}
