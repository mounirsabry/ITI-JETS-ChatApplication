package jets.projects.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jets.projects.classes.RequestResult;
import jets.projects.dbconnections.DBConnection;
import jets.projects.entity_info.GroupMemberInfo;

public class GroupMemberDao {
    public RequestResult<Boolean> isMember(int groupID, int userID) {
        String query = "SELECT * FROM UsersGroupMember WHERE group_ID = ? AND member_ID = ?";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1, groupID);
            preparedStatement.setInt(2, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new RequestResult<>(true, null);
            }else{
                return new RequestResult<Boolean>(false, null);
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, e.getMessage());
        }
    }
    public RequestResult<List<GroupMemberInfo>> getAllMembers(int groupID) {
        List<GroupMemberInfo> groupMembers = new ArrayList<>();
        String query = "SELECT username, picture " +
                        "FROM UsersGroupMember gm " +
                        "JOIN NormalUser u ON gm.member_ID = u.ID " +
                        "WHERE gm.group_ID = ?";
            try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1, groupID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String picture = resultSet.getString("picture");

                GroupMemberInfo groupMember = new GroupMemberInfo();
                groupMember.setPic(picture);
                groupMember.setUsername(username);
                groupMembers.add(groupMember);
            }
            return new RequestResult<>(groupMembers.isEmpty() ? null : groupMembers, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, e.getMessage());
        }
    }
    public RequestResult<Boolean> addMemberToGroup(int groupID, int otherID) {
        String query = "INSERT INTO UsersGroupMember (group_ID, member_ID) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1, groupID);
            preparedStatement.setInt(2, otherID);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 1) {
                return new RequestResult<>(true, null);
            } else {
                return new RequestResult<>(false, "Failed to add user to the group.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }
    }
    public RequestResult<Boolean> removeMemberFromGroup(int groupID, int otherID) {
        String query = "DELETE FROM UsersGroupMember WHERE group_ID = ? AND member_ID = ?";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){
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
    public RequestResult<Boolean> leaveGroupAsMember(int userID, int groupID) {
        String query = "DELETE FROM UsersGroupMember WHERE group_ID = ? AND member_ID = ?";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1, groupID);
            preparedStatement.setInt(2, userID);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 1) {
                return new RequestResult<>(true, null);
            } else {
                return new RequestResult<>(false, "Failed to leave the group.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }
    }
    public RequestResult<Boolean> leaveGroupAsAdmin(int userID , int groupID, int newAdminID) {
        String query = "UPDATE UsersGroup SET group_admin_ID = ? WHERE group_ID = ?";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1, newAdminID);
            preparedStatement.setInt(2, groupID);

            int adminRowsAffected = preparedStatement.executeUpdate();
            if (adminRowsAffected == 1) {
                String removeAdminQuery = "DELETE FROM UsersGroupMember WHERE group_ID = ? AND member_ID = ?";
                PreparedStatement removeAdminStatement = DBConnection.getConnection().prepareStatement(removeAdminQuery);
                removeAdminStatement.setInt(1, groupID);
                removeAdminStatement.setInt(2, userID);

                int rowsAffected = removeAdminStatement.executeUpdate();
                if (rowsAffected == 1) {
                    return new RequestResult<>(true, null);
                } else {
                    return new RequestResult<>(false, "Failed to leave the group as admin.");
                }
            } else {
                return new RequestResult<>(false, "Failed to transfer admin rights.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }
    }
}
