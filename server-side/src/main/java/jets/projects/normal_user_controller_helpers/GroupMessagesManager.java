package jets.projects.normal_user_controller_helpers;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.GroupDao;
import jets.projects.dao.GroupMemberDao;
import jets.projects.dao.GroupMessagesDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersDao;
import jets.projects.entities.GroupMessage;
import jets.projects.online_listeners.GroupCallback;
import jets.projects.online_listeners.GroupMessageCallback;
import jets.projects.session.ClientToken;
import jets.projects.shared_ds.OnlineNormalUserInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class GroupMessagesManager {

    GroupDao groupDao = new GroupDao();
    GroupMemberDao groupMemberDao = new GroupMemberDao();
    GroupMessagesDao groupMessagesDao = new GroupMessagesDao();
    UsersDao usersDao = new UsersDao();
    TokenValidatorDao tokenValidator = new TokenValidatorDao();
    GroupMessageCallback groupMessageCallback;
    GroupCallback groupCallback;
    Map<Integer, OnlineNormalUserInfo> onlineUsers;

    public GroupMessagesManager() {
        this.groupMessageCallback = new GroupMessageCallback(groupMessagesDao, groupMemberDao);
        this.groupCallback = new GroupCallback(groupDao, groupMemberDao, usersDao);
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
    }

    public RequestResult<List<GroupMessage>> getGroupMessages(ClientToken token, int groupID) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = groupMessagesDao.getGroupMessages(token.getUserID());
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    
    public RequestResult<byte[]> getGroupMessageFile(ClientToken token, int groupID, int messageID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<Boolean> sendGroupMessage(ClientToken token, GroupMessage message) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        if (message.getContent() == null) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(message.getGroupID()).getResponseData();
        if (!isGroupExists) {
            return new RequestResult<>(false, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        boolean isMember = groupMemberDao.isMember(message.getGroupID(), token.getUserID()).getResponseData();
        if (!isMember) {
            return new RequestResult<>(false, ExceptionMessages.NOT_MEMBER);
        }
        var result = groupMessagesDao.sendGroupMessage(message); //save in database
        groupMessageCallback.groupMessageReceived(message);  //callback for group members
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }

    public RequestResult<Boolean> sendGroupFileMessage(ClientToken token, int groupID, String file) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        if (file == null || file.isBlank()) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if (!isGroupExists) {
            return new RequestResult<>(false, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        boolean isMember = groupMemberDao.isMember(groupID, token.getUserID()).getResponseData();
        if (!isMember) {
            return new RequestResult<>(false, ExceptionMessages.NOT_MEMBER);
        }
        var result = groupMessagesDao.sendGroupFileMessage(token.getUserID(), groupID, file);  //save in database
        groupMessageCallback.fileGroupMessageReceived(token.getUserID(), groupID, file); //callback for group members
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }
}
