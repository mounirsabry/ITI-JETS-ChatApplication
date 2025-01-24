package jets.projects.api;

import java.util.List;
import java.util.Optional;
import jets.projects.dao.DaoClientController;
import jets.projects.entities.Announcement;
import jets.projects.classes.ClientToken;
import jets.projects.classes.RequestResult;
import jets.projects.entities.Contact;
import jets.projects.entities.ContactInvitation;
import jets.projects.entities.ContactMessage;
import jets.projects.entities.Group;
import jets.projects.entities.GroupMessage;
import jets.projects.entities.Notification;
import jets.projects.classes.ClientSessionData;

/** 
 * @author Mounir
 * If the optional is null, then either the database is down, or
 * an unhandled problem happens in the server.
 */
public class ClientServerAPI {
    private DaoClientController controller = new DaoClientController();
    
    private boolean validToken(ClientToken token) {
        return !(token.getPhoneNumber() == null
            ||  token.getPhoneNumber().isBlank()
            ||  token.getUserID() <= 0);
    }
    
    public Optional<ClientSessionData> login(String phoneNumber, String password) {
        if (phoneNumber == null || phoneNumber.isBlank()
        ||  password == null || password.isBlank()) {
            ClientSessionData invalidLoginData = new ClientSessionData
                (-1, null, null);
            return Optional.ofNullable(invalidLoginData);
        }
        return controller.login(phoneNumber, password);
    }
    
    public Optional<RequestResult<Boolean>> register(/* List of parameters. */) {
        // Check the whole list of input.
        return controller.registerNormalUser();
    }
    
    public Optional<RequestResult<Boolean>> logout(ClientToken token) {
        if (!validToken(token)) {
            RequestResult<Boolean> pair = new RequestResult<>(false, null);
            return Optional.ofNullable(pair);
        }
        return controller.logout();
    }
    
    // String represents the image for now.
    public Optional<RequestResult<String>> getProfilePic(ClientToken token) {
        if (!validToken(token)) {
            RequestResult<String> pair = new RequestResult<>(false, null);
            return Optional.ofNullable(pair);
        }
        return controller.getProfilePic(token);
    }
    
    public Optional<RequestResult<List<Contact>>> getContacts(ClientToken token) {
        if (!validToken(token)) {
            RequestResult<List<Contact>> pair = new RequestResult<>(false, null);
            return Optional.ofNullable(pair);
        }
        return controller.getContacts(token);
    }
    
    public Optional<RequestResult<List<ContactMessage>>> getContactMessages(ClientToken token,
            String otherPhoneNumber) {
        if (!validToken(token)) {
            RequestResult<List<ContactMessage>> pair = new RequestResult<>(false, null);
            return Optional.ofNullable(pair);
        }
        return controller.getContactMessages(token, otherPhoneNumber);
    }
    
    public Optional<RequestResult<List<Group>>> getGroups(ClientToken token) {
        if (!validToken(token)) {
            RequestResult<List<Group>> pair = new RequestResult<>(false, null);
            return Optional.ofNullable(pair);
        }
        return controller.getGroups(token);
    }
    
    public Optional<RequestResult<List<GroupMessage>>> getGroupMessages(ClientToken token) {
        if (!validToken(token)) {
            RequestResult<List<GroupMessage>> pair = new RequestResult<>(false, null);
            return Optional.ofNullable(pair);
        }
        return controller.getGroupMessages(token);
    }
    
    public Optional<RequestResult<List<Announcement>>> getAnnouncements(ClientToken token) {
        if (!validToken(token)) {
            RequestResult<List<Announcement>> pair = new RequestResult<>(false, null);
            return Optional.ofNullable(pair);
        }
        return controller.getAnnouncements();
    }
    
    public Optional<RequestResult<List<ContactInvitation>>> getContactInvitations(
            ClientToken token) {
        if (!validToken(token)) {
            RequestResult<List<ContactInvitation>> pair = new RequestResult<>(false, null);
            return Optional.ofNullable(pair);
        }
        return controller.getContactInvitations(token);
    }
    
    public Optional<RequestResult<List<Notification>>> getNotifications(ClientToken token) {
        if (!validToken(token)) {
            RequestResult<List<Notification>> pair = new RequestResult<>(false, null);
            return Optional.ofNullable(pair);
        }
        return controller.getNotifications(token);
    }
    
    public Optional<RequestResult<Boolean>> saveProfileChanges(ClientToken token) {
        if (!validToken(token)) {
            RequestResult<Boolean> pair = new RequestResult<>(false, null);
            return Optional.ofNullable(pair);
        }
        return controller.saveProfileChanges(token);
    }
}
