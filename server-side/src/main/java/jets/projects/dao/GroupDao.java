package jets.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jets.projects.classes.RequestResult;
import jets.projects.dbconnections.ConnectionManager;
import jets.projects.entities.Group;

public class GroupDao {

    public RequestResult<Boolean> isGroupExists(int groupID) {
       
        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT COUNT(*) FROM usersgroup WHERE group_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, groupID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return new RequestResult<>(true, null); 
            } else {
                return new RequestResult<>(false, "Group does not exist.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }


    }





    public RequestResult<Integer> getGroupAdmin(int groupID){

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT group_admin_ID FROM usersgroup WHERE group_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, groupID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int adminID = resultSet.getInt("group_admin_ID");
                return new RequestResult<>(adminID, null); 
            } else {
                return new RequestResult<>(null, "Group not found.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(null, "Database error: " + e.getMessage());
        }
       
    }
<<<<<<< HEAD


=======
    public RequestResult<String> getGroupName(int groupID){
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGroupAdmin'");
    }
>>>>>>> c5eda22a6c0c254302654f99663290d23dbbe6d8
    public RequestResult<Boolean> updateAdmin(int groupID, int newAdminID) {
        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "UPDATE usersgroup SET group_admin_ID = ? WHERE group_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, newAdminID);
            preparedStatement.setInt(2, groupID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 1) {
                return new RequestResult<>(true, null); 
            } else {
                return new RequestResult<>(false, "Failed to update group admin.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }



      
    }





    public RequestResult<List<Group>> getAllGroups(int userID) {
       List<Group> groups = new ArrayList<>();
    
    try (Connection connection = ConnectionManager.getConnection()) {
        String query = "SELECT name, picture FROM `group` g JOIN groupmember gm ON g.ID = gm.group_ID " +
                       "WHERE gm.member_ID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userID); 

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Group group = new Group();
           
            group.setName(resultSet.getString("name"));
            group.setPic(resultSet.getString("picture"));
            
            groups.add(group);
        }

        return new RequestResult<>(groups.isEmpty() ? null : groups, null);
    } catch (SQLException e) {
        return new RequestResult<>(null, null);
    }
    }

    public RequestResult<String> getGroupPic(int groupID) {
        String groupPicture = null;

    try (Connection connection = ConnectionManager.getConnection()) {
        String query = "SELECT picture FROM `group` WHERE ID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, groupID); 

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            groupPicture = resultSet.getString("picture");
        }

        return new RequestResult<>(groupPicture, null);
    } catch (SQLException e) {
        return new RequestResult<>(null, null);
    }

    }



    public RequestResult<Boolean> setGroupPic(int groupID, String pic) {
         try (Connection connection = ConnectionManager.getConnection()) {
        String query = "UPDATE `group` SET picture = ? WHERE ID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, pic); 
        preparedStatement.setInt(2, groupID);

        int rowsAffected = preparedStatement.executeUpdate();

        return new RequestResult<>(rowsAffected > 0, null);
    } catch (SQLException e) {
        return new RequestResult<>(false, null);
    }
    }



    public RequestResult<Boolean> createGroup(Group newGroup) {
         try (Connection connection = ConnectionManager.getConnection()) {
        String query = "INSERT INTO `group` (group_admin_ID,name, picture) VALUES ( ?, ? , ?)"; 

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, newGroup.getGroupAdminID()); 
        preparedStatement.setString(2, newGroup.getName());
        preparedStatement.setString(3, newGroup.getPic());

        int rowsAffected = preparedStatement.executeUpdate();

        return new RequestResult<>(rowsAffected > 0, null);
    } catch (SQLException e) {
        return new RequestResult<>(false, null);
    }
    }



    public RequestResult<Boolean> deleteGroup(int groupID) {

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "DELETE FROM usersgroup WHERE group_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, groupID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 1) {
                return new RequestResult<>(true, null); 
            } else {
                return new RequestResult<>(false, "Failed to delete group.");
            }
        } catch (SQLException e) {
            return new RequestResult<>(false, "Database error: " + e.getMessage());
        }
       
    }
    
}
