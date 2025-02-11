package jets.projects.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jets.projects.classes.RequestResult;
import jets.projects.db_connections.ConnectionManager;
import jets.projects.entities.GroupMember;
import jets.projects.entity_info.GroupMemberInfo;

public class GroupMemberDao {
    public RequestResult<Boolean> isMember(int groupID, int userID) {
        String query = "SELECT * FROM UsersGroupMember WHERE group_ID = ? AND member_ID = ?";
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, groupID);
            preparedStatement.setInt(2, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultSet.close();
                return new RequestResult<>(true, null);
            }else{
                resultSet.close();
                return new RequestResult<>(false, null);
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, "DB ERROR: "+e.getMessage());
        }
    }
    
    public RequestResult<List<GroupMemberInfo>> getAllMembers(int groupID) {
        List<GroupMemberInfo> groupMembers = new ArrayList<>();
        String query = "SELECT user_ID, display_name, pic " +
                "FROM UsersGroupMember gm " +
                "JOIN NormalUser u ON gm.member_ID = u.user_ID " +
                "WHERE gm.group_ID = ?;";
            try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, groupID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int user_ID = resultSet.getInt("user_ID");
                String username = resultSet.getString("display_name");
                Blob blob = resultSet.getBlob("pic");
                byte[] pic = (blob != null) ? blob.getBytes(1, (int) blob.length()) : null;

                GroupMember groupMember = new GroupMember(groupID, user_ID);
                GroupMemberInfo groupMemberInfo = new GroupMemberInfo(groupMember, username, pic);
                groupMembers.add(groupMemberInfo);
            }
            resultSet.close();
            return new RequestResult<>(groupMembers.isEmpty() ? null : groupMembers, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "DB ERROR: "+e.getMessage());
        }
    }
    
    public RequestResult<Integer> getNumberOfGroupMembers(int groupID) {
        String query = "SELECT member_ID FROM UsersGroupMember WHERE group_ID = ?";
        try(Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, groupID);
            ResultSet resultSet = preparedStatement.executeQuery();
            int i = 0;
            while(resultSet.next()){
                i++;
            }
            resultSet.close();
            if(i == 0){
                return new RequestResult<>(null, null);
            }
            return new RequestResult<>(i, null);
        }catch (SQLException e){
            return new RequestResult<>(null, "DB ERROR: "+e.getMessage());
        }
    }
    
    public RequestResult<List<Integer>> getGroupMembersIDs(int groupID) {
        String query = "SELECT member_ID FROM UsersGroupMember WHERE group_ID = ?";
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, groupID);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Integer> members = new ArrayList<>();
            while(resultSet.next()){
                members.add(resultSet.getInt("member_ID"));
            }
            resultSet.close();
            return new RequestResult<>(members, null);
        }catch (SQLException e){
            return new RequestResult<>(null, "DB ERROR: "+e.getMessage());
        }
    }
    
    public RequestResult<GroupMemberInfo> addMemberToGroup(int groupID, int otherID) {
        String insertQuery = "INSERT INTO UsersGroupMember (group_ID, member_ID) VALUES (?, ?)";
        String selectQuery = "SELECT display_name, pic FROM NormalUser WHERE user_ID = ?";
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
        PreparedStatement selectStmt = connection.prepareStatement(selectQuery)){
            insertStmt.setInt(1, groupID);
            insertStmt.setInt(2, otherID);
            int rowsAffected = insertStmt.executeUpdate();
            if (rowsAffected != 1) {
                return new RequestResult<>(null, null);
            }
            ResultSet resultSet = selectStmt.executeQuery();
            if(!resultSet.next()){
                resultSet.close();
                return new RequestResult<>(null, null);
            }
            String name = resultSet.getString("display_name");
            Blob blob = resultSet.getBlob("pic");
            byte[] pic = (blob != null) ? blob.getBytes(1, (int) blob.length()) : null;
            resultSet.close();
            GroupMember groupMember = new GroupMember(groupID,otherID);
            GroupMemberInfo groupMemberInfo = new GroupMemberInfo(groupMember,name, pic);
            return new RequestResult<>(groupMemberInfo, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "DB ERROR: " + e.getMessage());
        }
    }
    
    public RequestResult<Boolean> removeMemberFromGroup(int groupID, int otherID) {
        String query = "DELETE FROM UsersGroupMember WHERE group_ID = ? AND member_ID = ?";
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, groupID);
            preparedStatement.setInt(2, otherID);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 1) {
                return new RequestResult<>(true, null);
            } else {
                return new RequestResult<>(false, "Failed to remove user from the group.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, "DB ERROR: " + e.getMessage());
        }
    }
    
    public RequestResult<Boolean> leaveGroupAsMember(int userID, int groupID) {
        String query = "DELETE FROM UsersGroupMember WHERE group_ID = ? AND member_ID = ?";
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, groupID);
            preparedStatement.setInt(2, userID);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 1) {
                return new RequestResult<>(true, null);
            } else {
                return new RequestResult<>(false, "Failed to leave the group.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, "Database error: " + e.getMessage());
        }
    }
    
    // Make this a transaction to ensure both has happened.
    public RequestResult<Boolean> leaveGroupAsAdmin(int userID , int groupID, int newAdminID) {
        String query = "UPDATE UsersGroup SET group_admin_ID = ? WHERE group_ID = ?";
        String query1 = "DELETE FROM UsersGroupMember WHERE group_ID = ? AND member_ID = ? ";
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        PreparedStatement preparedStatement1 = connection.prepareStatement(query1)){
            connection.setAutoCommit(false);
            preparedStatement.setInt(1, newAdminID);
            preparedStatement.setInt(2, groupID);

            int adminRowsAffected = preparedStatement.executeUpdate();
            if (adminRowsAffected != 1) {
                connection.rollback();
                return new RequestResult<>(false, null);
            }
            preparedStatement1.setInt(1,groupID);
            preparedStatement1.setInt(2, userID);
            int rowAffected = preparedStatement1.executeUpdate();
            if(rowAffected != 1){
                connection.rollback();
                return new RequestResult<>(false, null);
            }
            connection.commit();
            return new RequestResult<>(true, null);

        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }
    }
}
