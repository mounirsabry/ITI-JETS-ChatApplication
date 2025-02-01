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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isMember'");
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeMemberFromGroup'");
    }

    public RequestResult<Boolean> leaveGroupAsMemeber(int userID, int groupID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leaveGroupAsMemeber'");
    }
    public RequestResult<Boolean> leaveGroupAsAdmin(int userID, int groupID, int newAdminID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leaveGroupAsAdmin'");
    }
}
