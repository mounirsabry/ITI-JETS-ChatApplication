package jets.projects.normal_user_controller_helpers;

import java.util.List;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.ContactDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersQueryDao;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entities.NormalUser;
import jets.projects.online_listeners.OnlineTracker;
import jets.projects.session.ClientToken;

public class ContactsManager {
    private final ContactDao contactsDao;
    private final UsersQueryDao usersQueryDao;
    private final TokenValidatorDao tokenValidator;

    public ContactsManager() {
        contactsDao = new ContactDao();
        usersQueryDao = new UsersQueryDao();
        tokenValidator = new TokenValidatorDao();
    }

    public RequestResult<List<ContactInfo>> getContacts(ClientToken token) {
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
        
        return contactsDao.getAllContacts(token.getUserID());
    }

    public RequestResult<NormalUser> getContactProfile(ClientToken token, int contactID) {
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
        
        return contactsDao.getContactProfile(contactID);
    }

    /*
    public RequestResult<byte[]> getContactProfilePic(ClientToken token, int contactID) {
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
        
        return contactsDao.getContactProfilePic(contactID);
    }
    */
}
