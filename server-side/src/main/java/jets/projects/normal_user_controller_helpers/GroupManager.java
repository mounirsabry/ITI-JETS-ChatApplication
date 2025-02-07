package jets.projects.normal_user_controller_helpers;

import java.rmi.RemoteException;
import java.sql.Blob;
import java.util.List;
import java.util.Map;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.ContactDao;
import jets.projects.dao.GroupDao;
import jets.projects.dao.GroupMemberDao;
import jets.projects.dao.GroupMessagesDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersDao;
import jets.projects.entities.Group;
import jets.projects.entity_info.GroupMemberInfo;
import jets.projects.entities.NormalUser;
import jets.projects.online_listeners.GroupCallback;
import jets.projects.session.ClientToken;
import jets.projects.shared_ds.OnlineNormalUserInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class GroupManager {

    GroupDao groupDao = new GroupDao();
    ContactDao contactsDao = new ContactDao();
    GroupMemberDao groupMemberDao = new GroupMemberDao();
    GroupMessagesDao groupMessagesDao = new GroupMessagesDao();
    UsersDao usersDao = new UsersDao();
    TokenValidatorDao tokenValidator = new TokenValidatorDao();
    GroupCallback groupCallback;
    Map<Integer, OnlineNormalUserInfo> onlineUsers;

    public GroupManager() {
        this.groupCallback = new GroupCallback(groupDao, groupMemberDao, usersDao);
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
    }

    public RequestResult<List<Group>> getAllGroups(ClientToken token) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = groupDao.getAllGroups(token.getUserID());
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }

    public RequestResult<Blob> getGroupPic(ClientToken token, int groupID) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if (!isGroupExists) {
            return new RequestResult<>(null, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        var result = groupDao.getGroupPic(groupID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }

    public RequestResult<Boolean> setGroupPic(ClientToken token, int groupID, byte[] pic) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if (!isGroupExists) {
            return new RequestResult<>(false, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        if (pic == null) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_INPUT_DATA);
        }
        var result = groupDao.setGroupPic(groupID, pic);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }

    public RequestResult<Boolean> createGroup(ClientToken token, Group newGroup) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = groupDao.createGroup(newGroup);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }

    public RequestResult<List<GroupMemberInfo>> getGroupMembers(ClientToken token, int groupID) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if (!isGroupExists) {
            return new RequestResult<>(null, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        boolean isMember = groupMemberDao.isMember(groupID, token.getUserID()).getResponseData();
        if (!isMember) {
            return new RequestResult<>(null, ExceptionMessages.NOT_MEMBER);
        }
        var result = groupMemberDao.getAllMembers(groupID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }

    public RequestResult<Boolean> addMemberToGroup(ClientToken token, int groupID, int otherID) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if (!isGroupExists) {
            return new RequestResult<>(false, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        boolean isContactExists = usersDao.isNormalUserExists(otherID).getResponseData();
        if (!isContactExists) {
            return new RequestResult<>(false, ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), otherID).getResponseData();
        if (!isContacts) {
            return new RequestResult<>(false, ExceptionMessages.NOT_CONTACTS);
        }
        NormalUser admin = groupDao.getGroupAdmin(groupID).getResponseData();
        if (admin.getUserID() != token.getUserID()) {
            return new RequestResult<>(null, ExceptionMessages.NOT_GROUP_ADMIN);
        }
        var result = groupMemberDao.addMemberToGroup(groupID, otherID);   //update in database
        groupCallback.addedToGroup(otherID, groupID);  //callback for added member
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }

    public RequestResult<Boolean> removeMemberFromGroup(ClientToken token, int groupID, int otherID) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if (!isGroupExists) {
            return new RequestResult<>(false, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        boolean isContactExists = usersDao.isNormalUserExists(otherID).getResponseData();
        if (!isContactExists) {
            return new RequestResult<>(false, ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), otherID).getResponseData();
        if (!isContacts) {
            return new RequestResult<>(false, ExceptionMessages.NOT_CONTACTS);
        }
        int AdminID = groupDao.getGroupAdmin(groupID).getResponseData().getUserID();
        if (AdminID != token.getUserID()) {
            return new RequestResult<>(null, ExceptionMessages.NOT_GROUP_ADMIN);
        }
        var result = groupMemberDao.removeMemberFromGroup(groupID, otherID);   //update in database
        groupCallback.removedFromGroup(otherID, groupID);  //callback for removed member
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }

    public RequestResult<Boolean> leaveGroupAsMember(ClientToken token, int groupID) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if (!isGroupExists) {
            return new RequestResult<>(false, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        var result = groupMemberDao.leaveGroupAsMember(token.getUserID(), groupID);   //update in database
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }

    public RequestResult<Boolean> leaveGroupAsAdmin(ClientToken token, int groupID, int newAdminID) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if (!isGroupExists) {
            return new RequestResult<>(false, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        int AdminID = groupDao.getGroupAdmin(groupID).getResponseData().getUserID();
        if (AdminID != token.getUserID()) {
            return new RequestResult<>(false, ExceptionMessages.NOT_GROUP_ADMIN);
        }
        var result1 = groupMemberDao.leaveGroupAsAdmin(token.getUserID(), groupID, newAdminID);   //update in database
        var result2 = groupDao.updateAdmin(groupID, newAdminID);   //update in database
        if (result1.getErrorMessage() != null) {
            throw new RemoteException(result1.getErrorMessage());
        }
        if (result2.getErrorMessage() != null) {
            throw new RemoteException(result2.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }

    public RequestResult<Boolean> assignGroupLeadership(ClientToken token, int groupID, int newAdminID) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if (!isGroupExists) {
            return new RequestResult<>(false, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        int AdminID = groupDao.getGroupAdmin(groupID).getResponseData().getUserID();
        if (AdminID != token.getUserID()) {
            return new RequestResult<>(false, ExceptionMessages.NOT_GROUP_ADMIN);
        }
        var result = groupDao.updateAdmin(groupID, newAdminID);   //update in database
        groupCallback.leadershipGained(newAdminID, groupID);  //callback for new admin
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }

    public RequestResult<Boolean> deleteGroup(ClientToken token, int groupID) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if (!isGroupExists) {
            return new RequestResult<>(false, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        int AdminID = groupDao.getGroupAdmin(groupID).getResponseData().getUserID();
        if (AdminID != token.getUserID()) {
            return new RequestResult<>(false, ExceptionMessages.NOT_GROUP_ADMIN);
        }
        var result = groupDao.deleteGroup(groupID);         //update in database
        groupCallback.groupDeleted(groupID);              //callback for group members
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }
}
