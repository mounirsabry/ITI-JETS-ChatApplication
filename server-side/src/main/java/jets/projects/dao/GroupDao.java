package jets.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jets.projects.classes.RequestResult;
import jets.projects.entities.Gender;
import jets.projects.entities.Group;
import jets.projects.entities.NormalUser;

public class GroupDao {
    public RequestResult<Boolean> isGroupExists(int groupID) {
        String query = "SELECT * FROM UsersGroup WHERE group_ID = ?";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){;
            preparedStatement.setInt(1, groupID);
            ResultSet resultSet = preparedStatement.executeQuery();
            return new RequestResult<>(resultSet.next(), null);
        } catch (SQLException e) {
            return new RequestResult<>(false, e.getMessage());
        }
    }
    
    // If the group does not exist, then return null.
    public RequestResult<Group> getGroupById(int groupID) {
        String query = "SELECT * FROM UsersGroup WHERE group_ID = ?";
    
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(query)) {    
            stmt.setInt(1, groupID);
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
        String query = "SELECT u.* FROM NormalUser u " +
                       "JOIN UsersGroup g ON u.user_ID = g.group_admin_ID " +
                       "WHERE g.group_ID = ?";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, groupID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                NormalUser admin = new NormalUser();
                admin.setUserID(resultSet.getInt("user_ID"));
                admin.setDisplayName((resultSet.getString("display_name")));
                admin.setEmail(resultSet.getString("email"));
                admin.setPhoneNumber(resultSet.getString("phone_number"));
                admin.setBirthDate(resultSet.getDate("birth_date"));
                admin.setGender(Gender.valueOf(resultSet.getString("gender")));
                admin.setPic(resultSet.getBlob("pic"));
                return new RequestResult<>(admin, null);
            }else{
                System.out.println("Group admin not found.");
                return new RequestResult<>(null, "Group admin not found.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, e.getMessage());
        }
    }   
    
    public RequestResult<String> getGroupName(int groupID) {
        String query = "SELECT group_name FROM UsersGroup WHERE group_ID = ?";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1, groupID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new RequestResult<>(resultSet.getString("group_name"), null);
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, e.getMessage());
        }
        return new RequestResult<>(null, "Error retrieving group name.");
    }

    public RequestResult<Boolean> updateAdmin(int groupID, int newAdminID) {
        String query = "UPDATE UsersGroup SET group_admin_ID = ? WHERE group_ID = ?";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            statement.setInt(1, newAdminID);
            statement.setInt(2, groupID);
            int rowsAffected = statement.executeUpdate();
            return new RequestResult<>(rowsAffected > 0, null);
        } catch (SQLException e) {
            return new RequestResult<>(false, e.getMessage());
        }
    }
    
    public RequestResult<List<Group>> getAllGroups(int userID) {
        List<Group> groups = new ArrayList<>();
        String query = "SELECT g.group_ID, g.group_name, g.pic FROM UsersGroup g " +
                       "JOIN UsersGroupMember gm ON g.group_ID = gm.group_ID WHERE gm.member_ID = ?";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Group group = new Group();
                group.setGroupID(resultSet.getInt("group_ID"));
                group.setName(resultSet.getString("group_name"));
                group.setPic(resultSet.getBlob("pic"));
                groups.add(group);
            }
            return new RequestResult<>(groups.isEmpty() ? null : groups, null);
        } catch (SQLException e) {
            return new RequestResult<>(null, e.getMessage());
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
        throw new UnsupportedOperationException("Not implemented.");
    }

    public RequestResult<Boolean> createGroup(Group newGroup) {
        String query = "INSERT INTO UsersGroup (group_name, group_desc, group_admin_ID, pic) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)){
            preparedStatement.setString(1, newGroup.getName());
            preparedStatement.setString(2, newGroup.getDescription());
            preparedStatement.setInt(3, newGroup.getGroupAdminID());
            preparedStatement.setBlob(4, newGroup.getPic());
            int rowsAffected = preparedStatement.executeUpdate();
            return new RequestResult<>(rowsAffected > 0, null);
        } catch (SQLException e) {
            return new RequestResult<>(false, e.getMessage());
        }
    }
    
    public RequestResult<List<Integer>> deleteGroup(int groupID){
        String deleteMemberQuery = "DELETE FROM UsersGroupMember WHERE group_ID = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteMemberQuery)){
            preparedStatement.setInt(1, groupID);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            return new RequestResult<Boolean>( false, e.getMessage());
        }
        String deleteGroupQuery = "DELETE FROM UsersGroup WHERE group_ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteGroupQuery)) {
            preparedStatement.setInt(1, groupID);
            int rowsAffected = preparedStatement.executeUpdate();
            return new RequestResult<>(rowsAffected > 0, null);
            } catch (SQLException e) {
                return new RequestResult<Boolean>(false, e.getMessage());
            }
    }
}
