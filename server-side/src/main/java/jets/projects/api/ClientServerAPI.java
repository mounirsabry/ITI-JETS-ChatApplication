package jets.projects.api;

import java.util.Date;
import java.util.List;
import jets.projects.topcontrollers.DaoClientManager;
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
import jets.projects.entities.Gender;
import jets.projects.entities.GroupMember;
import jets.projects.entities.NormalUserStatus;

/** 
 * @author Mounir
 * If the ResultSet is null, then either the database is down, or
 * an unhandled problem happens in the server.
 */
public class ClientServerAPI {
    private final DaoClientManager controller = new DaoClientManager();
    
    private boolean validToken(ClientToken token) {
        return !(token == null
            ||  token.getPhoneNumber() == null
            ||  token.getPhoneNumber().isBlank()
            ||  token.getUserID() <= 0);
    }
    
    public RequestResult<ClientSessionData> login(String phoneNumber, String password) {
        if (phoneNumber == null || phoneNumber.isBlank()
        ||  password == null || password.isBlank()) {
            ClientSessionData invalidLoginData = new ClientSessionData
                (-1, null, null);
            return new RequestResult<>(true, invalidLoginData);
        }
        return controller.login(phoneNumber, password);
    }
    
    public RequestResult<Boolean> register(
            String displayName, String phoneNumber, String email, String pic,
            String password, Gender gender, String country, Date birthDate,
            String bio) {
        
        if (displayName == null || displayName.isBlank()
        ||  phoneNumber == null || phoneNumber.isBlank()
        ||  email == null || email.isBlank()
        ||  password == null || password.isBlank()
        || gender == null
        || country == null || country.isBlank()) {
            return new RequestResult<>(false, null);
        }
        
        return controller.registerNormalUser();
    }
    
    public RequestResult<Boolean> logout(ClientToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.logout();
    }
    
    // String represents the image for now.
    public RequestResult<String> getProfilePic(ClientToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.getProfilePic(token);
    }
    
    public RequestResult<Boolean> setOnlineStatus(ClientToken token, 
            NormalUserStatus newStatus) {
        if (!validToken(token) 
        ||  newStatus == null || newStatus == newStatus.OFFLINE) {
            return new RequestResult<>(false, null);
        }
        return controller.setOnlineStatus(token, newStatus);
    }
    
    public RequestResult<List<Contact>> getContacts(ClientToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.getContacts(token);
    }
    
    public RequestResult<Contact> getContactProfile(ClientToken token,
            String contactPhoneNumber) {
        if (!validToken(token)
        || contactPhoneNumber == null || contactPhoneNumber.isBlank()) {
            return new RequestResult<>(false, null);
        }
        return controller.getContactProfile(token, contactPhoneNumber);
    }
    
    public RequestResult<String> getContactProfilePic(ClientToken token,
            String contactPhoneNumber) {
        if (!validToken(token)
        || contactPhoneNumber == null || contactPhoneNumber.isBlank()) {
            return new RequestResult<>(false, null);
        }
        return controller.getContactProfilePic(token, contactPhoneNumber);
    }
    
    public RequestResult<List<ContactMessage>> getAllContactMessages(ClientToken token,
            String otherPhoneNumber) {
        if (!validToken(token) 
        || otherPhoneNumber == null || otherPhoneNumber.isBlank()) {
            return new RequestResult<>(false, null);
        }
        return controller.getContactMessages(token, otherPhoneNumber);
    }
    
    public RequestResult<List<ContactMessage>> getUnReadContactMessages(
            ClientToken token, String otherPhoneNumber) {
        if (!validToken(token) 
        || otherPhoneNumber == null || otherPhoneNumber.isBlank()) {
            return new RequestResult<>(false, null);
        }
        return controller.getUnReadContactMessages(token, otherPhoneNumber);
    }
    
    public RequestResult<Boolean> sendContactMessage(ClientToken token,
            ContactMessage message) {
        if (!validToken(token) || message == null) {
            return new RequestResult<>(false, null);
        }
        return controller.sendContactMessage(token, message);
    }
    
    public RequestResult<Boolean> sendContactFileMessage(ClientToken token,
            String file) {
        if (!validToken(token) || file == null) {
            return new RequestResult<>(false, null);
        }
        return controller.sendContactFileMessage(token, file);
    }
    
    public RequestResult<Boolean> markContactMessagesAsRead(ClientToken token,
            List<ContactMessage> messages) {
        if (!validToken(token) 
        ||  messages == null || messages.isEmpty()) {
            return new RequestResult<>(false, null);
        }
        return controller.markContactMessagesAsRead(token, messages);
    }
    
    public RequestResult<List<Group>> getGroups(ClientToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.getGroups(token);
    }
    
    public RequestResult<String> getGroupPic(ClientToken token, int groupID) {
        if (!validToken(token) || groupID <= 0) {
            return new RequestResult<>(false, null);
        }
        return controller.getGroupPic(token, groupID);
    }
    
    public RequestResult<Boolean> createGroup(ClientToken token, Group newGroup) {
        if (!validToken(token) || newGroup == null) {
            return new RequestResult<>(false, null);
        }
        return controller.createGroup(token, newGroup);
    }
    
    public RequestResult<List<GroupMember>> getGroupMembers(ClientToken token,
            int groupID) {
        if (!validToken(token) || groupID <= 0) {
            return new RequestResult<>(false, null);
        }
        return controller.getGroupMembers(token, groupID);
    }
    
    public RequestResult<Boolean> addMemberToGroup(ClientToken token, 
            int groupID, String contactPhoneNumber) {
        if (!validToken(token) || groupID <= 0 
        || contactPhoneNumber == null || contactPhoneNumber.isBlank()) {
            return new RequestResult<>(false, null);
        }
        return controller.addMemberToGroup(token, groupID, contactPhoneNumber);
    }
    
    public RequestResult<Boolean> removeMemberFromGroup(ClientToken token, 
            int groupID, String contactPhoneNumber) {
        if (!validToken(token) || groupID <= 0 || contactPhoneNumber.isBlank()) {
            return new RequestResult<>(false, null);
        }
        return controller.removeMemberFromGroup(token, groupID, contactPhoneNumber);
    }
    
    public RequestResult<Boolean> leaveGroupAsMember(ClientToken token, 
            int groupID) {
        if (!validToken(token) || groupID <= 0) {
            return new RequestResult<>(false, null);
        }
        return controller.leaveGroupAsMember(token, groupID);
    }
    
    public RequestResult<Boolean> leaveGroupAsAdmin(ClientToken token,
            int groupID, String newAdminPhoneNumber) {
        if (!validToken(token) || groupID <= 0 
        ||  (newAdminPhoneNumber.isBlank() && !newAdminPhoneNumber.equals("LAST"))) {
            return new RequestResult<>(false, null);
        }
        return controller.leaveGroupAsAdmin(token, groupID, newAdminPhoneNumber);
    }
    
    public RequestResult<Boolean> assignGroupLeadership(ClientToken token,
            int groupID, String newAdminPhoneNumber) {
        if (!validToken(token) || groupID <= 0 
        ||  newAdminPhoneNumber.isBlank()) {
            return new RequestResult<>(false, null);
        }
        return controller.assignGroupLeadership(token, groupID, newAdminPhoneNumber);
    }
    
    public RequestResult<Boolean> deleteGroup(ClientToken token, int groupID) {
        if (!validToken(token) || groupID <= 0) {
            return new RequestResult<>(false, null);
        }
        return controller.deleteGroup(token, groupID);
    }
    
    public RequestResult<List<GroupMessage>> getGroupMessages(ClientToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.getGroupMessages(token);
    }
    
    public RequestResult<List<Announcement>> getAllAnnouncements(ClientToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.getAnnouncements();
    }
    
    public RequestResult<List<Announcement>> getUnReadAnnouncements(
            ClientToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.getAnnouncements();
    }
    
    public RequestResult<List<ContactInvitation>> getContactInvitations(
            ClientToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.getContactInvitations(token);
    }
    
    public RequestResult<List<Notification>> getNotifications(ClientToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.getNotifications(token);
    }
    
    public RequestResult<Boolean> markNotificationsAsRead(ClientToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.markNotificationsAsRead(token);
    }
    
    public RequestResult<Boolean> saveProfileChanges(ClientToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.saveProfileChanges(token);
    }
}
