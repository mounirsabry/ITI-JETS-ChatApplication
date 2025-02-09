package jets.projects.normal_user_controller_helpers;

import java.util.List;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.ContactDao;
import jets.projects.dao.ContactInvitationDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersDao;
import jets.projects.dao.UsersQueryDao;
import jets.projects.entities.ContactGroup;
import jets.projects.entities.ContactInvitation;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entity_info.ContactInvitationInfo;
import jets.projects.online_listeners.ContactInvitationCallback;
import jets.projects.online_listeners.OnlineTracker;
import jets.projects.session.ClientToken;

public class ContactInvitationsManager {
    private final TokenValidatorDao tokenValidator;
    private final UsersQueryDao usersQueryDao;
    private final UsersDao usersDao;
    private final ContactDao contactsDao;
    private final ContactInvitationDao contactInvitationDao;
    

    public ContactInvitationsManager() {
        tokenValidator = new TokenValidatorDao();
        usersQueryDao = new UsersQueryDao();
        usersDao = new UsersDao();
        contactsDao = new ContactDao();
        contactInvitationDao = new ContactInvitationDao();
    }

    public RequestResult<List<ContactInvitationInfo>> getContactInvitations(
            ClientToken token) {
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
        
        return contactInvitationDao.getAllInvitations(token.getUserID());
    }

    public RequestResult<Boolean> sendContactInvitation(
            ClientToken token, String userPhoneNumber,
            ContactGroup contactGroup) {
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
        
        var isUserExistsResult = usersQueryDao.isNormalUserExistsByPhoneNumber(
                userPhoneNumber);
        if (isUserExistsResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isUserExistsResult.getErrorMessage());
        }
        Integer userID = isUserExistsResult.getResponseData();
        if (userID == null) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_DOES_NOT_EXIST);
        }
        
        var isContactResult = contactsDao.isContacts(token.getUserID(),
                userID);
        if (isContactResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isContactResult.getErrorMessage());
        }
        boolean isContacts = isContactResult.getResponseData();
        if (isContacts) {
            return new RequestResult<>(null,
                    ExceptionMessages.ALREADY_CONTACTS);
        }
        
        var result = contactInvitationDao.sendContactInvitation(
                token.getUserID(), userID,
                contactGroup);
        if (result.getErrorMessage() != null) {
            return result;
        }
        ContactInvitationCallback.contactInvitationReceived(
                token.getUserID(), userID);
        return result;
    }

    public RequestResult<ContactInfo> acceptContactInvitation(ClientToken token,
            int invitationID) {
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
        
        var isInvitationExistsResult = contactInvitationDao.getContactInvitation(
                invitationID);
        if (isInvitationExistsResult.getErrorMessage() != null) {
            return new RequestResult<>(null, 
                    isInvitationExistsResult.getErrorMessage());
        }
        ContactInvitation invitation = 
                isInvitationExistsResult.getResponseData();
        if (invitation == null
        ||  invitation.getReceiverID() != token.getUserID()) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = contactInvitationDao.acceptContactInvitation(invitation);
        if (result.getErrorMessage() != null) {
            return result;
        }
        
        ContactInvitationCallback.contactInvitationAccepted(invitation);
        return result;
    }

    public RequestResult<Boolean> rejectContactInvitation(ClientToken token,
            int invitationID) {
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
        
        var isInvitationExistsResult = contactInvitationDao.getContactInvitation(
                invitationID);
        if (isInvitationExistsResult.getErrorMessage() != null) {
            return new RequestResult<>(null, 
                    isInvitationExistsResult.getErrorMessage());
        }
        ContactInvitation invitation = 
                isInvitationExistsResult.getResponseData();
        if (invitation == null
        ||  invitation.getReceiverID() != token.getUserID()) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = contactInvitationDao.rejectContactInvitation(invitation);
        if (result.getErrorMessage() != null) {
            return result;
        }
        
        ContactInvitationCallback.contactInvitationRejected(invitation);
        return result;
    }
}
