package jets.projects.normal_user_controller_helpers;

import java.rmi.RemoteException;
import java.sql.Blob;
import java.util.List;
import java.util.Map;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.ContactDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersDao;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entities.NormalUser;
import jets.projects.online_listeners.ContactInvitationCallback;
import jets.projects.online_listeners.ContactMessageCallback;
import jets.projects.online_listeners.GroupCallback;
import jets.projects.online_listeners.GroupMessageCallback;
import jets.projects.online_listeners.NotificationCallback;
import jets.projects.session.ClientToken;
import jets.projects.shared_ds.OnlineNormalUserInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class ContactsManager {

    ContactDao contactsDao = new ContactDao();
    UsersDao usersDao = new UsersDao();
    TokenValidatorDao tokenValidator = new TokenValidatorDao();
    //  listener classes
    ContactInvitationCallback contactInvitationCallback;
    ContactMessageCallback contactMessageCallback;
    GroupMessageCallback groupMessageCallback;
    GroupCallback groupCallback;
    NotificationCallback notificatonCallback;
    Map<Integer, OnlineNormalUserInfo> onlineUsers;

    public ContactsManager() {
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
    }

    public RequestResult<List<ContactInfo>> getContacts(ClientToken token) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.USER_TIMEOUT);
        }
        var result = contactsDao.getAllContacts(token.getUserID());
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }

    public RequestResult<NormalUser> getContactProfile(ClientToken token, int contactID) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.USER_TIMEOUT);
        }
        boolean isContactExists = usersDao.isNormalUserExists(contactID).getResponseData();
        if (!isContactExists) {
            return new RequestResult<>(null, ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), contactID).getResponseData();
        if (!isContacts) {
            return new RequestResult<>(null, ExceptionMessages.NOT_CONTACTS);
        }
        var result = contactsDao.getContactProfile(contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }

    public RequestResult<byte[]> getContactProfilePic(ClientToken token, int contactID) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.USER_TIMEOUT);
        }
        boolean isContactExists = usersDao.isNormalUserExists(contactID).getResponseData();
        if (!isContactExists) {
            return new RequestResult<>(null, ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), contactID).getResponseData();
        if (!isContacts) {
            return new RequestResult<>(null, ExceptionMessages.NOT_CONTACTS);
        }
        var result = contactsDao.getContactProfilePic(contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
}
