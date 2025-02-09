package jets.projects.normal_user_controller_helpers;

import java.util.List;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.ContactDao;
import jets.projects.dao.GroupDao;
import jets.projects.dao.GroupMemberDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersQueryDao;
import jets.projects.entities.Group;
import jets.projects.entity_info.GroupMemberInfo;
import jets.projects.online_listeners.GroupCallback;
import jets.projects.online_listeners.OnlineTracker;
import jets.projects.session.ClientToken;

public class GroupsManager {
    private final TokenValidatorDao tokenValidator;
    private final UsersQueryDao usersQueryDao;
    private final ContactDao contactsDao;
    private final GroupDao groupDao;
    private final GroupMemberDao groupMemberDao;

    public GroupsManager() {
        tokenValidator = new TokenValidatorDao();
        usersQueryDao = new UsersQueryDao();
        contactsDao = new ContactDao();
        groupDao = new GroupDao();
        groupMemberDao = new GroupMemberDao();
    }

    public RequestResult<List<Group>> getAllGroups(ClientToken token) {
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
        
        return groupDao.getAllGroups(token.getUserID());
    }

    public RequestResult<Boolean> setGroupPic(ClientToken token,
            int groupID, byte[] pic) {
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
        
        var GroupAdminResult = groupDao.getGroupAdminID(groupID);
        if (GroupAdminResult.getErrorMessage() == null) {
            return new RequestResult<>(null,
                    GroupAdminResult.getErrorMessage());
        }
        int groupAdminID = GroupAdminResult.getResponseData();
        if (groupAdminID != token.getUserID()) {
            return new RequestResult<>(null,
                    ExceptionMessages.NOT_GROUP_ADMIN);
        }
        
        var result = groupDao.setGroupPic(groupID, pic);
        if (result.getErrorMessage() != null) {
            return result;
        }
        
        GroupCallback.groupPicChanged(groupID, pic);
        return result;
    }

    public RequestResult<Boolean> createGroup(ClientToken token,
            Group newGroup) {
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
        
        return groupDao.createGroup(newGroup);
    }

    public RequestResult<List<GroupMemberInfo>> getGroupMembers(
            ClientToken token, int groupID) {
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
        
        return groupMemberDao.getAllMembers(groupID);
    }

    public RequestResult<Boolean> addMemberToGroup(ClientToken token,
            int groupID, int otherID) {
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
        
        var groupAdminResult = groupDao.getGroupAdminID(groupID);
        if (groupAdminResult.getErrorMessage() == null) {
            return new RequestResult<>(null,
                    groupAdminResult.getErrorMessage());
        }
        int groupAdminID = groupAdminResult.getResponseData();
        if (groupAdminID != token.getUserID()) {
            return new RequestResult<>(null,
                    ExceptionMessages.NOT_GROUP_ADMIN);
        }
        
        var isUserExistsResult = usersQueryDao.isNormalUserExistsByID(
                otherID);
        if (isUserExistsResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isUserExistsResult.getErrorMessage());
        }
        boolean isUserExists = isUserExistsResult.getResponseData();
        if (!isUserExists) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_DOES_NOT_EXIST);
        }
        
        var isContactsResult = contactsDao.isContacts(token.getUserID(),
                otherID);
        if (isContactsResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isContactsResult.getErrorMessage());
        }
        boolean isContacts = isContactsResult.getResponseData();
        if (!isContacts) {
            return new RequestResult<>(false,
                    ExceptionMessages.NOT_CONTACTS);
        }
        
        var isMemberResult = groupMemberDao.isMember(groupID,
                otherID);
        if (isMemberResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isMemberResult.getErrorMessage());
        }
        boolean isMember = isMemberResult.getResponseData();
        if (isMember) {
            return new RequestResult<>(null,
                    ExceptionMessages.ALREADY_MEMBER);
        }
        
        var result = groupMemberDao.addMemberToGroup(groupID, otherID);
        if (result.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    result.getErrorMessage());
        }
        
        GroupMemberInfo newMemberInfo = result.getResponseData();
        
        GroupCallback.addedToGroup(otherID, groupID);
        GroupCallback.newGroupMemberAdded(
                groupAdminID, newMemberInfo);
        return new RequestResult<>(true, null);
    }

    public RequestResult<Boolean> removeMemberFromGroup(ClientToken token,
            int groupID, int otherID) {
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
        
        var groupAdminResult = groupDao.getGroupAdminID(groupID);
        if (groupAdminResult.getErrorMessage() == null) {
            return new RequestResult<>(null,
                    groupAdminResult.getErrorMessage());
        }
        int groupAdminID = groupAdminResult.getResponseData();
        if (groupAdminID != token.getUserID()) {
            return new RequestResult<>(null,
                    ExceptionMessages.NOT_GROUP_ADMIN);
        }
        
        var isUserExistsResult = usersQueryDao.isNormalUserExistsByID(
                otherID);
        if (isUserExistsResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isUserExistsResult.getErrorMessage());
        }
        boolean isUserExists = isUserExistsResult.getResponseData();
        if (!isUserExists) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_DOES_NOT_EXIST);
        }
        
        var isMemberResult = groupMemberDao.isMember(groupID,
                otherID);
        if (isMemberResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isMemberResult.getErrorMessage());
        }
        boolean isMember = isMemberResult.getResponseData();
        if (!isMember) {
            return new RequestResult<>(null,
                    ExceptionMessages.NOT_MEMBER);
        }
        
        var result = groupMemberDao.removeMemberFromGroup(groupID, otherID);
        if (result.getErrorMessage() != null) {
            return result;
        }
        
        GroupCallback.removedFromGroup(otherID, groupID);
        GroupCallback.groupMemberRemoved(
                groupAdminID, groupID, otherID);
        return result;
    }

    public RequestResult<Boolean> leaveGroupAsMember(ClientToken token, int groupID) {
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
        
        var result = groupMemberDao.leaveGroupAsMember(
                token.getUserID(), groupID);
        if (result.getErrorMessage() != null) {
            return result;
        }
        
        GroupCallback.groupMemberLeft(groupID, token.getUserID());
        return result;
    }

    public RequestResult<Boolean> leaveGroupAsAdmin(ClientToken token,
            int groupID, int newAdminID) {
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
        
        var groupAdminResult = groupDao.getGroupAdminID(groupID);
        if (groupAdminResult.getErrorMessage() == null) {
            return new RequestResult<>(null,
                    groupAdminResult.getErrorMessage());
        }
        int groupAdminID = groupAdminResult.getResponseData();
        if (groupAdminID != token.getUserID()) {
            return new RequestResult<>(null,
                    ExceptionMessages.NOT_GROUP_ADMIN);
        }
        
        if (newAdminID == 0) {
            var groupCountResult = groupMemberDao.getNumberOfGroupMembers(
                    groupID);
            if (groupCountResult.getErrorMessage() != null) {
                return new RequestResult<>(null,
                        groupCountResult.getErrorMessage());
            }
            int groupCount = groupCountResult.getResponseData();
            if (groupCount > 1) {
                return new RequestResult<>(null,
                        ExceptionMessages.NOT_THE_LAST_MEMER);
            }
            
            return groupDao.deleteGroup(groupID);
        }
        
        var isUserExistsResult = usersQueryDao.isNormalUserExistsByID(
                newAdminID);
        if (isUserExistsResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isUserExistsResult.getErrorMessage());
        }
        boolean isUserExists = isUserExistsResult.getResponseData();
        if (!isUserExists) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_DOES_NOT_EXIST);
        }
        
        var isMemberResult = groupMemberDao.isMember(groupID,
                newAdminID);
        if (isMemberResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isMemberResult.getErrorMessage());
        }
        boolean isMember = isMemberResult.getResponseData();
        if (!isMember) {
            return new RequestResult<>(null,
                    ExceptionMessages.NOT_MEMBER);
        }
        
        var result = groupMemberDao.leaveGroupAsAdmin(
                token.getUserID(), groupID, newAdminID);
        if (result.getErrorMessage() != null) {
            return result;
        }
        
        GroupCallback.leadershipGained(newAdminID, groupID);
        GroupCallback.groupMemberLeft(groupID, token.getUserID());
        GroupCallback.adminChanged(
                token.getUserID(), groupID, newAdminID);
        return result;
    }

    public RequestResult<Boolean> assignGroupLeadership(ClientToken token,
            int groupID, int newAdminID) {
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
        
        var groupAdminResult = groupDao.getGroupAdminID(groupID);
        if (groupAdminResult.getErrorMessage() == null) {
            return new RequestResult<>(null,
                    groupAdminResult.getErrorMessage());
        }
        int groupAdminID = groupAdminResult.getResponseData();
        if (groupAdminID != token.getUserID()) {
            return new RequestResult<>(null,
                    ExceptionMessages.NOT_GROUP_ADMIN);
        }
        
        var isUserExistsResult = usersQueryDao.isNormalUserExistsByID(
                newAdminID);
        if (isUserExistsResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isUserExistsResult.getErrorMessage());
        }
        boolean isUserExists = isUserExistsResult.getResponseData();
        if (!isUserExists) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_DOES_NOT_EXIST);
        }
        
        var isMemberResult = groupMemberDao.isMember(groupID,
                newAdminID);
        if (isMemberResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isMemberResult.getErrorMessage());
        }
        boolean isMember = isMemberResult.getResponseData();
        if (!isMember) {
            return new RequestResult<>(null,
                    ExceptionMessages.NOT_MEMBER);
        }
        
        var result = groupDao.updateAdmin(groupID, newAdminID);
        if (result.getErrorMessage() != null) {
            return result;
        }
        
        GroupCallback.leadershipGained(newAdminID, groupID);
        GroupCallback.adminChanged(
                token.getUserID(), groupID, newAdminID);
        return result;
    }

    public RequestResult<Boolean> deleteGroup(ClientToken token, int groupID) {
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
        
        var groupAdminResult = groupDao.getGroupAdminID(groupID);
        if (groupAdminResult.getErrorMessage() == null) {
            return new RequestResult<>(null,
                    groupAdminResult.getErrorMessage());
        }
        int groupAdminID = groupAdminResult.getResponseData();
        if (groupAdminID != token.getUserID()) {
            return new RequestResult<>(null,
                    ExceptionMessages.NOT_GROUP_ADMIN);
        }
        
        var result = groupDao.deleteGroup(groupID);
        if (result.getErrorMessage() != null) {
            return result;
        }
        
        GroupCallback.groupDeleted(groupID);
        return result;
    }
}
