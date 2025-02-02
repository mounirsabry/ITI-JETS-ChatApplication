package jets.projects.dao;

import java.util.List;

import jets.projects.classes.RequestResult;
import jets.projects.entities.GroupMessage;

public class GroupMessagesDao {

    public RequestResult<List<GroupMessage>> getGroupMessages(int userID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGroupMessages'");
    }

    public RequestResult<Boolean> sendGroupMessage(GroupMessage message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendGroupMessage'");
    }

    public RequestResult<Boolean> sendGroupFileMessage(int senderID , int groupId , String file) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendGroupMessage'");
    }
    
}
