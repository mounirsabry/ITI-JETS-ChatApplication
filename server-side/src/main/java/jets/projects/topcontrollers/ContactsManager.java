package jets.projects.topcontrollers;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.ContactDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersDao;
import jets.projects.entities.Contact;
import jets.projects.entities.NormalUser;
import jets.projects.onlinelisteners.ContactInvitationCallback;
import jets.projects.onlinelisteners.ContactMessageCallback;
import jets.projects.onlinelisteners.GroupCallback;
import jets.projects.onlinelisteners.GroupMessageCallback;
import jets.projects.onlinelisteners.NotificatonCallback;
import jets.projects.session.ClientToken;
import jets.projects.sharedds.OnlineNormalUserInfo;
import jets.projects.sharedds.OnlineNormalUserTable;

public class ContactsManager {
    ContactDao contactsDao = new ContactDao();
    UsersDao usersDao = new UsersDao();
    TokenValidatorDao tokenValidator = new TokenValidatorDao();
    //  listener classes
    ContactInvitationCallback contactInvitationCallback;
    ContactMessageCallback contactMessageCallback;
    GroupMessageCallback groupMessageCallback;
    GroupCallback groupCallback;
    NotificatonCallback notificatonCallback;
    Map<Integer, OnlineNormalUserInfo> onlineUsers;

    public ContactsManager(){
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
    }
    public RequestResult<List<Contact>> getContacts(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = contactsDao.getAllContacts(token.getUserID());
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<NormalUser> getContactProfile(ClientToken token, int contactID) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
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
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }    
    public RequestResult<String> getContactProfilePic(ClientToken token, int contactID) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
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
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
}
