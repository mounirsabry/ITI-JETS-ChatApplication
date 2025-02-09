package jets.projects.normal_user_controller_helpers;

import java.util.List;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.ContactDao;
import jets.projects.dao.ContactMessagesDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersQueryDao;
import jets.projects.entities.ContactMessage;
import jets.projects.online_listeners.ContactMessageCallback;
import jets.projects.online_listeners.OnlineTracker;
import jets.projects.session.ClientToken;

public class ContactMessagesManager {

    private final UsersQueryDao usersQueryDao;
    private final ContactDao contactsDao;
    private final ContactMessagesDao contactMessagesDao;
    private final TokenValidatorDao tokenValidator;
    
    public ContactMessagesManager() {
        usersQueryDao = new UsersQueryDao();
        contactsDao = new ContactDao();
        contactMessagesDao = new ContactMessagesDao();
        tokenValidator = new TokenValidatorDao();
    }

    public RequestResult<List<ContactMessage>> getContactMessages(
            ClientToken token, int otherID) {
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
        
        if (!OnlineTracker.isOnline(true)) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_TIMEOUT);
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
        
        var isContactResult = contactsDao.isContacts(token.getUserID(),
                otherID);
        if (isContactResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isContactResult.getErrorMessage());
        }
        boolean isContacts = isContactResult.getResponseData();
        if (!isContacts) {
            return new RequestResult<>(null,
                    ExceptionMessages.NOT_CONTACTS);
        }
        
        return contactMessagesDao.getContactMessages(token.getUserID(), otherID);
    }
    
    public RequestResult<byte[]> getContactMessageFile(ClientToken token,
            int contactID, int messageID) {
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
        
        if (!OnlineTracker.isOnline(true)) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_TIMEOUT);
        }
        
        var isUserExistsResult = usersQueryDao.isNormalUserExistsByID(
                contactID);
        if (isUserExistsResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isUserExistsResult.getErrorMessage());
        }
        boolean isUserExists = isUserExistsResult.getResponseData();
        if (!isUserExists) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_DOES_NOT_EXIST);
        }
        
        var isContactResult = contactsDao.isContacts(token.getUserID(),
                contactID);
        if (isContactResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isContactResult.getErrorMessage());
        }
        boolean isContacts = isContactResult.getResponseData();
        if (!isContacts) {
            return new RequestResult<>(null,
                    ExceptionMessages.NOT_CONTACTS);
        }
        
        return contactMessagesDao.getContactMessageFile(messageID);
    }

    public RequestResult<List<ContactMessage>> getUnReadContactMessages(
            ClientToken token, int otherID) {
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
        
        if (!OnlineTracker.isOnline(true)) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_TIMEOUT);
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
        
        var isContactResult = contactsDao.isContacts(token.getUserID(),
                otherID);
        if (isContactResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isContactResult.getErrorMessage());
        }
        boolean isContacts = isContactResult.getResponseData();
        if (!isContacts) {
            return new RequestResult<>(null,
                    ExceptionMessages.NOT_CONTACTS);
        }
        
        return contactMessagesDao.getUnReadContactMessages(otherID,
                token.getUserID());
    }

    public RequestResult<Boolean> sendContactMessage(ClientToken token,
            ContactMessage message) {
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
        
        if (!OnlineTracker.isOnline(true)) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_TIMEOUT);
        }
        
        var isUserExistsResult = usersQueryDao.isNormalUserExistsByID(
                message.getReceiverID());
        if (isUserExistsResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isUserExistsResult.getErrorMessage());
        }
        boolean isUserExists = isUserExistsResult.getResponseData();
        if (!isUserExists) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_DOES_NOT_EXIST);
        }
        
        var isContactResult = contactsDao.isContacts(token.getUserID(),
                message.getReceiverID());
        if (isContactResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isContactResult.getErrorMessage());
        }
        boolean isContacts = isContactResult.getResponseData();
        if (!isContacts) {
            return new RequestResult<>(null,
                    ExceptionMessages.NOT_CONTACTS);
        }
        
        var result = contactMessagesDao.sendContactMessage(message);
        if (result.getErrorMessage() != null) {
            return result;
        }
        
        message.setFile(null);
        ContactMessageCallback.contactMessageReceived(message);
        return result;
    }

    public RequestResult<Boolean> markContactMessagesAsRead(ClientToken token,
            int contactID) {
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
        
        if (!OnlineTracker.isOnline(true)) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_TIMEOUT);
        }
        
        var isUserExistsResult = usersQueryDao.isNormalUserExistsByID(
                contactID);
        if (isUserExistsResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isUserExistsResult.getErrorMessage());
        }
        boolean isUserExists = isUserExistsResult.getResponseData();
        if (!isUserExists) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_DOES_NOT_EXIST);
        }
        
        var isContactResult = contactsDao.isContacts(token.getUserID(),
                contactID);
        if (isContactResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isContactResult.getErrorMessage());
        }
        boolean isContacts = isContactResult.getResponseData();
        if (!isContacts) {
            return new RequestResult<>(null,
                    ExceptionMessages.NOT_CONTACTS);
        }
        
        return contactMessagesDao.markContactMessagesAsRead(contactID);
    }
}
