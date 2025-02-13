package jets.projects.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jets.projects.classes.RequestResult;
import jets.projects.db_connections.ConnectionManager;
import jets.projects.entities.Group;

public class GroupDao {

    public RequestResult<Boolean> isGroupExists(int groupID) {
        String query = "SELECT * FROM UsersGroup WHERE group_ID = ?";
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){;
            preparedStatement.setInt(1, groupID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                resultSet.close();
                return new RequestResult<>(false, null);
            }
            resultSet.close();
            return new RequestResult<>(true, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "DB ERROR: "+e.getMessage());
        }
    }
    
    // If the group does not exist, then return null.
    public RequestResult<Group> getGroupById(int groupID) {
        String query = "SELECT * FROM UsersGroup WHERE group_ID = ?";
    
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, groupID);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                Group group = new Group();
                group.setGroupID(rs.getInt("group_ID"));
                group.setGroupName(rs.getString("group_name"));
                group.setGroupDesc(rs.getString("group_desc"));

                group.setGroupAdminID(rs.getInt("group_admin_ID"));
                Blob pic = rs.getBlob("pic");
                if (pic != null) {
                    byte[] picBytes = pic.getBytes(1, (int) pic.length());
                    group.setPic(picBytes);
                }else {
                    group.setPic(null);
                }
                Timestamp timestamp = rs.getTimestamp("created_at");
                LocalDateTime localDateTime = timestamp.toLocalDateTime();
                group.setCreatedAt(localDateTime);
                rs.close();
                return new RequestResult<>(group, null);
            }
            rs.close();
            return new RequestResult<>(null, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "DB ERROR: "+e.getMessage());
        }
    }    
    
    /*
    // If the group does not exist, then return null.
    public RequestResult<List<Group>> getGroupByName(String groupName) {
        String query = "SELECT * FROM UsersGroup WHERE group_name = ?";
    
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(query)) {    
            stmt.setString(1, groupName);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                Group group = new Group();
                group.setGroupID(rs.getInt("group_ID"));
                group.setName(rs.getString("group_name"));
                group.setDescription(rs.getString("group_desc"));
                group.setCreatedAt(rs.getDate("created_at"));
                group.setPic(rs.getBlob("pic"));
                group.setGroupAdminID(rs.getInt("group_admin_ID"));
                return group;
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return null; 
    }    
    */
    
    // Returns the id of the admin of the group.
    public RequestResult<Integer> getGroupAdminID(int groupID) {
        String query = "SELECT group_admin_ID FROM UsersGroup WHERE group_ID = ?";
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {;
            preparedStatement.setInt(1, groupID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if(!resultSet.next()) {
               resultSet.close();
               return new RequestResult<>(null, "Group not found.");
            }
            int adminID = resultSet.getInt("group_admin_ID");
            resultSet.close();
            return new RequestResult<>(adminID, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "DB ERROR: "+e.getMessage());
        }
    }   
    
    public RequestResult<String> getGroupName(int groupID) {
        String query = "SELECT group_name FROM UsersGroup WHERE group_ID = ?";
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, groupID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String groupName = resultSet.getString("group_name");
                resultSet.close();
                return new RequestResult<>(groupName, null);
            }
            resultSet.close();
            return new RequestResult<>(null, "group not found");
        } catch (SQLException e) {
            return new RequestResult<>(null, "DB ERROR: "+e.getMessage());
        }
    }

    public RequestResult<Boolean> updateAdmin(int groupID, int newAdminID) {
        String query = "UPDATE UsersGroup SET group_admin_ID = ? WHERE group_ID = ?";
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, newAdminID);
            preparedStatement.setInt(2, groupID);
            if(preparedStatement.executeUpdate() == 1){
                return new RequestResult<>(true, null);
            }
            return new RequestResult<>(false, "group not found");
        } catch (SQLException e) {
            return new RequestResult<>(null, "DB ERROR: "+e.getMessage());
        }
    }
    
    public RequestResult<List<Group>> getAllGroups(int userID) {
        List<Group> groups = new ArrayList<>();
        String query = "SELECT g.* " +
                "FROM UsersGroup g " +
                "JOIN UsersGroupMember gm ON g.group_ID = gm.group_ID " +
                "WHERE gm.member_ID = ?;";
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Group group = new Group();
                group.setGroupID(resultSet.getInt("group_ID"));
                group.setGroupName(resultSet.getString("group_name"));
                group.setGroupDesc(resultSet.getString("group_desc"));
                group.setGroupAdminID(resultSet.getInt("group_admin_ID"));
                Blob pic = resultSet.getBlob("pic");
                if (pic != null) {
                    byte[] picBytes = pic.getBytes(1, (int) pic.length());
                    group.setPic(picBytes);
                }else {
                    group.setPic(null);
                }
                Timestamp timestamp = resultSet.getTimestamp("created_at");
                LocalDateTime localDateTime = timestamp.toLocalDateTime();
                group.setCreatedAt(localDateTime);
                groups.add(group);
            }
            resultSet.close();
            return new RequestResult<>(groups, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, "DB ERROR"+e.getMessage());
        }
    }
    
    /*
    public RequestResult<Blob> getGroupPic(int groupID) {
        String query = "SELECT pic FROM UsersGroup WHERE group_ID = ?";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1, groupID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new RequestResult<>(resultSet.getBlob("pic"), null);
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, e.getMessage());
        }
        return new RequestResult<>(null, "Error retrieving group picture.");
    }
    public RequestResult<Boolean> setGroupPic(int groupID, Blob pic) {
        String query = "UPDATE UsersGroup SET pic = ? WHERE group_ID = ?";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){;
            preparedStatement.setBlob(1, pic);
            preparedStatement.setInt(2, groupID);
            int rowsAffected = preparedStatement.executeUpdate();
            return new RequestResult<>(rowsAffected > 0, null);
        } catch (SQLException e) {
            return new RequestResult<>(false, e.getMessage());
        }
    }
    */
    
    public RequestResult<Boolean> setGroupPic(int groupID, byte[] pic) {
        String query = "UPDATE UsersGroup SET pic = ? WHERE group_ID = ?";
        try(Connection connection = ConnectionManager.getConnection();
        PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setBytes(1, pic);
            stmt.setInt(2, groupID);
            int rowsAffected = stmt.executeUpdate();
            return new RequestResult<>(rowsAffected > 0, null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: " 
                    + ex.getMessage());
        }
    }

    public RequestResult<Integer> createGroup(Group newGroup) {
        String query1 = "INSERT INTO UsersGroup (group_name, group_desc, group_admin_ID, pic) VALUES (?, ?, ?, ?)";
        String query2 = "INSERT INTO UsersGroupMember (group_ID, member_ID) VALUES (?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement stmt1 = connection.prepareStatement(
                query1, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement stmt2 = connection.prepareStatement(query2)) {
            stmt1.setString(1, newGroup.getGroupName());
            stmt1.setString(2, newGroup.getGroupDesc());
            stmt1.setInt(3, newGroup.getGroupAdminID());
            if (newGroup.getPic() != null) {
                Blob blob = connection.createBlob();
                blob.setBytes(1, newGroup.getPic());
                stmt1.setBlob(4, blob);
            } else {
                stmt1.setNull(4, Types.BLOB);
            }
            if(stmt1.executeUpdate() != 1){
                return new RequestResult<>(-1, null);
            }
            
            int groupID;
            try (ResultSet keysSet = stmt1.getGeneratedKeys()) {
                if (!keysSet.next()) {
                    return new RequestResult<>(-1, null);
                }
                groupID = keysSet.getInt(1);
            }

            stmt2.setInt(1, groupID);
            stmt2.setInt(2, newGroup.getGroupAdminID());
            if(stmt2.executeUpdate() == 0){
                return new RequestResult<>(-1, null);
            }
            return new RequestResult<>(groupID, null);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: "
                    + ex.getMessage());
        }
    }

    public RequestResult<List<Integer>> deleteGroup(int groupID) {
        String selectMembersQuery = "SELECT member_ID FROM UsersGroupMember WHERE group_ID = ?";
        String deleteMembersQuery = "DELETE FROM UsersGroupMember WHERE group_ID = ?";
        String deleteGroupQuery = "DELETE FROM UsersGroup WHERE group_ID = ?";

        List<Integer> memberIDs = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(selectMembersQuery);
             PreparedStatement deleteMembersStmt = connection.prepareStatement(deleteMembersQuery);
             PreparedStatement deleteGroupStmt = connection.prepareStatement(deleteGroupQuery)) {

            connection.setAutoCommit(false); // Start transaction

            // **Step 1: Retrieve Member IDs**
            selectStmt.setInt(1, groupID);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    memberIDs.add(resultSet.getInt("member_ID"));
                }
            }

            // **Step 2: Delete Members**
            deleteMembersStmt.setInt(1, groupID);
            deleteMembersStmt.executeUpdate();

            // **Step 3: Delete Group**
            deleteGroupStmt.setInt(1, groupID);
            int groupDeleted = deleteGroupStmt.executeUpdate();

            // **Step 4: Commit & Return Member IDs**
            if (groupDeleted == 1) {
                connection.commit();
                return new RequestResult<>(memberIDs, null);
            } else {
                connection.rollback();
                return new RequestResult<>(null, "Group not found or could not be deleted.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, "Database error: " + e.getMessage());
        }
    }

}
