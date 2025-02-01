package jets.projects.dao;

import java.util.List;

import jets.projects.classes.RequestResult;
import jets.projects.entities.GroupMember;

public class GroupMemberDao {
    public RequestResult<Boolean> isMember(int groupID, int userID){
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isMember'");
    }
    public RequestResult<List<GroupMember>> getAllMembers(int groupID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGroupMembers'");
    }
    
    public RequestResult<Boolean> addMemberToGroup(int userID, int groupID, int otherID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addMemberToGroup'");
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
