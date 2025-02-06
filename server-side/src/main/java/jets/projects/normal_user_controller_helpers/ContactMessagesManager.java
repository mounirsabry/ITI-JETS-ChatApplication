package jets.projects.normal_user_controller_helpers;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.ContactDao;
import jets.projects.dao.ContactMessagesDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersDao;
import jets.projects.entities.ContactMessage;
import jets.projects.online_listeners.ContactMessageCallback;
import jets.projects.session.ClientToken;
import jets.projects.shared_ds.OnlineNormalUserInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class ContactMessagesManager {

    ContactDao contactsDao = new ContactDao();
    ContactMessagesDao contactMessagesDao = new ContactMessagesDao();
    UsersDao usersDao = new UsersDao();
    TokenValidatorDao tokenValidator = new TokenValidatorDao();
    ContactMessageCallback contactMessageCallback;
    Map<Integer, OnlineNormalUserInfo> onlineUsers;

    public ContactMessagesManager() {
        this.contactMessageCallback = new ContactMessageCallback(contactMessagesDao);
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
    }

    public RequestResult<List<ContactMessage>> getContactMessages(ClientToken token, int otherID) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isContactExists = usersDao.isNormalUserExists(otherID).getResponseData();
        if (!isContactExists) {
            return new RequestResult<>(null, ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), otherID).getResponseData();
        if (!isContacts) {
            return new RequestResult<>(null, ExceptionMessages.NOT_CONTACTS);
        }
        var result = contactMessagesDao.getContactMessages(token.getUserID(), otherID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    
    public RequestResult<byte[]> getContactMessageFile(ClientToken token, int contactID, int messageID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<List<ContactMessage>> getUnReadContactMessages(ClientToken token, int otherID) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isContactExists = usersDao.isNormalUserExists(otherID).getResponseData();
        if (!isContactExists) {
            return new RequestResult<>(null, ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), otherID).getResponseData();
        if (!isContacts) {
            return new RequestResult<>(null, ExceptionMessages.NOT_CONTACTS);
        }
        var result = contactMessagesDao.getUnReadContactMessages(otherID, token.getUserID());
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }

    public RequestResult<Boolean> sendContactMessage(ClientToken token, ContactMessage message) {
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
        boolean isContactExists = usersDao.isNormalUserExists(message.getReceiverID()).getResponseData();
        if (!isContactExists) {
            return new RequestResult<>(false, ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), message.getReceiverID()).getResponseData();
        if (!isContacts) {
            return new RequestResult<>(false, ExceptionMessages.NOT_CONTACTS);
        }
        var result = contactMessagesDao.sendContactMessage(message);   //save in database
        contactMessageCallback.contactMessageReceived(message);  //callback for receiver
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }

    public RequestResult<Boolean> sendContactFileMessage(ClientToken token, String file, int receiverID) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<Boolean>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        if (file == null || file.isBlank()) {
            return new RequestResult<Boolean>(false, ExceptionMessages.INVALID_MESSAGE);
        }
        boolean isContactExists = usersDao.isNormalUserExists(receiverID).getResponseData();
        if (!isContactExists) {
            return new RequestResult<>(false, ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), receiverID).getResponseData();
        if (!isContacts) {
            return new RequestResult<>(false, ExceptionMessages.NOT_CONTACTS);
        }
        var result = contactMessagesDao.sendContactFileMessage(token.getUserID(), file, receiverID);   //save in database
        contactMessageCallback.fileContactMessageReceived(token.getUserID(), receiverID, file); //callback for receiver
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }

    public RequestResult<Boolean> markContactMessagesAsRead(ClientToken token, List<Integer> messages) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        if (messages == null) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_MESSAGE);
        }
        var result = contactMessagesDao.markContactMessagesAsRead(messages);   //update in database
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }
}
