package jets.projects.normal_user_controller_helpers;

import java.util.List;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.GroupDao;
import jets.projects.dao.GroupMemberDao;
import jets.projects.dao.GroupMessagesDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.entities.GroupMessage;
import jets.projects.online_listeners.OnlineTracker;
import jets.projects.session.ClientToken;
import jets.projects.online_listeners.GroupMessageCallback;

public class GroupMessagesManager {
    private final TokenValidatorDao tokenValidator;
    private final GroupDao groupDao;
    private final GroupMemberDao groupMemberDao;
    private final GroupMessagesDao groupMessagesDao;

    public GroupMessagesManager() {
        tokenValidator = new TokenValidatorDao();
        groupDao = new GroupDao();
        groupMemberDao = new GroupMemberDao();
        groupMessagesDao = new GroupMessagesDao();
    }

    public RequestResult<List<GroupMessage>> getGroupMessages(ClientToken token,
            int groupID) {
        var validationResult = tokenValidator.checkClientToken(token);
        if (validationResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    validationResult.getErrorMessage());
        }
        boolean isTokenValid = validationResult.getResponseData();
        if (!isTokenValid) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        if (!OnlineTracker.isOnline(token.getUserID())) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_TIMEOUT);
        }
        
        var isGroupExistsResult = groupDao.isGroupExists(groupID);
        if (isGroupExistsResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isGroupExistsResult.getErrorMessage());
        }
        boolean isGroupExists = isGroupExistsResult.getResponseData();
        if (!isGroupExists) {
            return new RequestResult<>(null,
                    ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        
        var isMemberResult = groupMemberDao.isMember(groupID,
                token.getUserID());
        if (isMemberResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isMemberResult.getErrorMessage());
        }
        boolean isMember = isMemberResult.getResponseData();
        if (!isMember) {
            return new RequestResult<>(null,
                    ExceptionMessages.NOT_MEMBER);
        }
        
        return groupMessagesDao.getGroupMessages(groupID);
    }
    
    public RequestResult<byte[]> getGroupMessageFile(ClientToken token,
            int groupID, int messageID) {
        var validationResult = tokenValidator.checkClientToken(token);
        if (validationResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    validationResult.getErrorMessage());
        }
        boolean isTokenValid = validationResult.getResponseData();
        if (!isTokenValid) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        if (!OnlineTracker.isOnline(token.getUserID())) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_TIMEOUT);
        }
        
        var isGroupExistsResult = groupDao.isGroupExists(groupID);
        if (isGroupExistsResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isGroupExistsResult.getErrorMessage());
        }
        boolean isGroupExists = isGroupExistsResult.getResponseData();
        if (!isGroupExists) {
            return new RequestResult<>(null,
                    ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        
        var isMemberResult = groupMemberDao.isMember(groupID,
                token.getUserID());
        if (isMemberResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isMemberResult.getErrorMessage());
        }
        boolean isMember = isMemberResult.getResponseData();
        if (!isMember) {
            return new RequestResult<>(null,
                    ExceptionMessages.NOT_MEMBER);
        }
        
        return groupMessagesDao.getGroupMessageFile(groupID, messageID);
    }

    public RequestResult<Boolean> sendGroupMessage(ClientToken token,
            GroupMessage message) {
        var validationResult = tokenValidator.checkClientToken(token);
        if (validationResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    validationResult.getErrorMessage());
        }
        boolean isTokenValid = validationResult.getResponseData();
        if (!isTokenValid) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        if (!OnlineTracker.isOnline(token.getUserID())) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_TIMEOUT);
        }
        
        var isGroupExistsResult = groupDao.isGroupExists(
                message.getGroupID());
        if (isGroupExistsResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isGroupExistsResult.getErrorMessage());
        }
        boolean isGroupExists = isGroupExistsResult.getResponseData();
        if (!isGroupExists) {
            return new RequestResult<>(null,
                    ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        
        var isMemberResult = groupMemberDao.isMember(
                message.getGroupID(),
                token.getUserID());
        if (isMemberResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isMemberResult.getErrorMessage());
        }
        boolean isMember = isMemberResult.getResponseData();
        if (!isMember) {
            return new RequestResult<>(null,
                    ExceptionMessages.NOT_MEMBER);
        }
        
        var result = groupMessagesDao.sendGroupMessage(message);
        if (result.getErrorMessage() != null) {
            return result;
        }
        
        message.setFile(null);
        GroupMessageCallback.groupMessageReceived(message);
        return result;
    }
}
