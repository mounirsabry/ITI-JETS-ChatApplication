package jets.projects.dao;

import java.util.List;
import jets.projects.classes.RequestResult;
import jets.projects.entities.Group;

public class GroupDao {

    public RequestResult<Boolean> isGroupExists(int groupID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isGroupExists'");
    }
    public RequestResult<Integer> getGroupAdmin(int groupID){
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGroupAdmin'");
    }
    public RequestResult<String> getGroupName(int groupID){
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGroupAdmin'");
    }
    public RequestResult<Boolean> updateAdmin(int groupID, int newAdminID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAdmin'");
    }
    public RequestResult<List<Group>> getAllGroups(int userID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllGroups'");
    }

    public RequestResult<String> getGroupPic(int groupID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGroupPic'");
    }
    public RequestResult<Boolean> setGroupPic(int groupID, String pic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setGroupPic'");
    }
    public RequestResult<Boolean> createGroup(Group newGroup) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createGroup'");
    }
    public RequestResult<Boolean> deleteGroup(int groupID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteGroup'");
    }
    
}
