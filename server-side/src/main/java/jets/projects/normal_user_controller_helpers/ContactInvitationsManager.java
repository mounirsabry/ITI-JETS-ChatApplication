package jets.projects.normal_user_controller_helpers;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.ContactDao;
import jets.projects.dao.ContactInvitationDao;
import jets.projects.dao.ContactMessagesDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersDao;
import jets.projects.entities.ContactInvitation;
import jets.projects.entity_info.ContactInvitationInfo;
import jets.projects.online_listeners.ContactInvitationCallback;
import jets.projects.session.ClientToken;
import jets.projects.shared_ds.OnlineNormalUserInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class ContactInvitationsManager {

    ContactDao contactsDao = new ContactDao();
    ContactInvitationDao contactInvitationDao = new ContactInvitationDao();
    ContactMessagesDao contactMessagesDao = new ContactMessagesDao();
    UsersDao usersDao = new UsersDao();
    TokenValidatorDao tokenValidator = new TokenValidatorDao();
    ContactInvitationCallback contactInvitationCallback;
    Map<Integer, OnlineNormalUserInfo> onlineUsers;

    public ContactInvitationsManager() {
        this.contactInvitationCallback = new ContactInvitationCallback(contactInvitationDao);
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
    }

    public RequestResult<List<ContactInvitationInfo>> getContactInvitations(ClientToken token) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.USER_TIMEOUT);
        }
        var result = contactInvitationDao.getAllInvitations(token.getUserID());
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }

    public RequestResult<Boolean> sendContactInvitation(ClientToken token, 
            String userPhoneNumber) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(false, ExceptionMessages.USER_TIMEOUT);
        }
        if (invitation == null) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_INPUT_DATA);
        }
        boolean isUserExists = usersDao.isNormalUserExists(invitation.getReceiverID()).getResponseData();
        if (!isUserExists) {
            return new RequestResult<>(false, ExceptionMessages.USER_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), invitation.getReceiverID()).getResponseData();
        if (isContacts) {
            return new RequestResult<>(false, ExceptionMessages.ALREADY_CONTACTS);
        }
        var result = contactInvitationDao.sendContactInvitation(invitation);  //save in database
        contactInvitationCallback.contactInvitationReceived(invitation);  //callback for receiver
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }

    public RequestResult<Boolean> acceptContactInvitation(ClientToken token, int invitationID) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_TOKEN);
        }
        if (invitation == null) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_INPUT_DATA);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(false, ExceptionMessages.USER_TIMEOUT);
        }
        boolean isUserExists = usersDao.isNormalUserExists(invitation.getReceiverID()).getResponseData();
        if (!isUserExists) {
            return new RequestResult<>(false, ExceptionMessages.USER_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), invitation.getReceiverID()).getResponseData();
        if (isContacts) {
            return new RequestResult<>(false, ExceptionMessages.ALREADY_CONTACTS);
        }
        var result = contactInvitationDao.acceptContactInvitation(invitation);  //update in database
        contactInvitationCallback.contactInvitationAccepted(invitation);  //callback for sender
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }

    public RequestResult<Boolean> rejectContactInvitation(ClientToken token, int invitationID) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(false, ExceptionMessages.USER_TIMEOUT);
        }
        if (invitation == null) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_INPUT_DATA);
        }
        boolean isUserExists = usersDao.isNormalUserExists(invitation.getReceiverID()).getResponseData();
        if (!isUserExists) {
            return new RequestResult<>(false, ExceptionMessages.USER_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), invitation.getReceiverID()).getResponseData();
        if (isContacts) {
            return new RequestResult<>(false, ExceptionMessages.ALREADY_CONTACTS);
        }
        var result = contactInvitationDao.rejectContactInvitation(invitation);  //update in database
        contactInvitationCallback.contactInvitationRejected(invitation);  //callback for sender
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }
}
