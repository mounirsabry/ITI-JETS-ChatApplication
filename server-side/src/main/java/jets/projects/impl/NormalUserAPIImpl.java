package jets.projects.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;
import jets.projects.topcontrollers.NormalUserManager;
import jets.projects.entities.Announcement;
import jets.projects.session.ClientToken;
import jets.projects.entities.Contact;
import jets.projects.entities.ContactInvitation;
import jets.projects.entities.ContactMessage;
import jets.projects.entities.Group;
import jets.projects.entities.GroupMessage;
import jets.projects.entities.Notification;
import jets.projects.session.ClientSessionData;
import jets.projects.entities.Gender;
import jets.projects.entities.GroupMember;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;
import jets.projects.api.NormalUserAPI;

/** 
 * @author Mounir
 * If the ResultSet is null, then either the database is down, or
 * an unhandled problem happens in the server.
 */
public class NormalUserAPIImpl extends UnicastRemoteObject
        implements NormalUserAPI {
    private final NormalUserManager controller;
    
    public NormalUserAPIImpl() throws RemoteException {
        super();
        controller = new NormalUserManager();
    }
    
    private boolean validToken(ClientToken token) {
        return !(token == null
            ||  token.getPhoneNumber() == null
            ||  token.getPhoneNumber().isBlank()
            ||  token.getUserID() <= 0);
    }
    
    @Override
    public ClientSessionData login(String phoneNumber,
            String password) throws RemoteException {
        if (phoneNumber == null || phoneNumber.isBlank()
        ||  password == null || password.isBlank()) {
            throw new RemoteException("Invalid login data.");
        }
        var result = controller.login(phoneNumber, password);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean register(
            String displayName, String phoneNumber, String email, String pic,
            String password, Gender gender, String country, Date birthDate,
            String bio) throws RemoteException {
        
        if (displayName == null || displayName.isBlank()
        ||  phoneNumber == null || phoneNumber.isBlank()
        ||  email == null || email.isBlank()
        ||  password == null || password.isBlank()
        || gender == null
        || country == null || country.isBlank()) {
            throw new RemoteException("Invalid register data.");
        }
        
        var result = controller.registerNormalUser(
                displayName, phoneNumber, email, pic,
                password, gender, country, birthDate, bio);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean logout(ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        var result = controller.logout(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    // String represents the image for now.
    @Override
    public String getProfilePic(ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        var result = controller.getProfilePic(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean setOnlineStatus(ClientToken token, 
            NormalUserStatus newStatus) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        if (newStatus == null || newStatus == NormalUserStatus.OFFLINE) {
            throw new RemoteException("Invalid status input.");
        }
        var result = controller.setOnlineStatus(token, newStatus);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public List<Contact> getContacts(ClientToken token) 
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        var result = controller.getContacts(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public Contact getContactProfile(ClientToken token,
            int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        if (contactID <= 0) {
            throw new RemoteException("Invalid contact ID.");
        }
        
        var result = controller.getContactProfile(token, contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public String getContactProfilePic(ClientToken token,
            int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        if (contactID <= 0) {
            throw new RemoteException("Invalid contact ID.");
        }
        
        var result = controller.getContactProfilePic(token, contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public List<ContactMessage> getAllContactMessages(ClientToken token,
            int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        if (contactID <= 0) {
            throw new RemoteException("Invalid contact ID.");
        }
        
        var result = controller.getContactMessages(token, contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public List<ContactMessage> getUnReadContactMessages(
            ClientToken token, int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        if (contactID <= 0) {
            throw new RemoteException("Invalid otherID ID.");
        }
        
        var result = controller.getUnReadContactMessages(token, contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean sendContactMessage(ClientToken token,
            ContactMessage message) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        if (message == null) {
            throw new RemoteException("Message cannot be null.");
        }
        
        var result = controller.sendContactMessage(token, message);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean sendContactFileMessage(ClientToken token,
            String file) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        if (file == null) {
            throw new RemoteException("File cannot be null.");
        }
        if (file.isBlank()) {
            throw new RemoteException("File cannot be empty.");
        }

        var result = controller.sendContactFileMessage(token, file);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean markContactMessagesAsRead(ClientToken token,
            List<ContactMessage> messages) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        if (messages == null) {
            throw new RemoteException("Message list cannot be null.");
        }
        if ( messages.isEmpty()) {
            throw new RemoteException("Message list cannot be empty.");
        }
        
        var result = controller.markContactMessagesAsRead(token, messages);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public List<Group> getGroups(ClientToken token) 
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        
        var result = controller.getGroups(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public String getGroupPic(ClientToken token, int groupID) 
            throws RemoteException{
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        if (groupID <= 0) {
            throw new RemoteException("Invalid group ID.");
        }
        
        var result = controller.getGroupPic(token, groupID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean createGroup(ClientToken token, Group newGroup) 
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        if (newGroup == null) {
            throw new RemoteException("New group cannot be null.");
        }
        
        var result = controller.createGroup(token, newGroup);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public List<GroupMember> getGroupMembers(ClientToken token,
            int groupID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        if (groupID <= 0) {
            throw new RemoteException("Invalid group ID.");
        }
        
        var result = controller.getGroupMembers(token, groupID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean addMemberToGroup(ClientToken token, 
            int groupID, int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        if (groupID <= 0) {
            throw new RemoteException("Invalid group ID.");
        }
        if (contactID <= 0) {
            throw new RemoteException("Invalid contact ID.");
        }
        
        var result = controller.addMemberToGroup(token, groupID, contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean removeMemberFromGroup(ClientToken token, 
            int groupID, int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        if (groupID <= 0) {
            throw new RemoteException("Invalid group ID.");
        }
        if (contactID <= 0) {
            throw new RemoteException("Invalid contact ID.");
        }
        
        var result = controller.removeMemberFromGroup(token, groupID,
                contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean leaveGroupAsMember(ClientToken token, 
            int groupID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        if (groupID <= 0) {
            throw new RemoteException("Invalid group ID.");
        }
        
        var result = controller.leaveGroupAsMember(token, groupID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean leaveGroupAsAdmin(ClientToken token,
            int groupID, int newAdminID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        if (groupID <= 0) {
            throw new RemoteException("Invalid group ID.");
        }
        if (newAdminID <= 0) {
            throw new RemoteException("Invalid new admin ID.");
        }
        
        var result = controller.leaveGroupAsAdmin(token, groupID, newAdminID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean assignGroupLeadership(ClientToken token,
            int groupID, int newAdminID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        if (groupID <= 0) {
            throw new RemoteException("Invalid group ID.");
        }
        if (newAdminID <= 0) {
            throw new RemoteException("Invalid new admin ID.");
        }
        
        var result = controller.assignGroupLeadership(token, groupID,
                newAdminID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean deleteGroup(ClientToken token, int groupID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        if (groupID <= 0) {
            throw new RemoteException("Invalid group ID.");
        }
        
        var result = controller.deleteGroup(token, groupID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public List<GroupMessage> getGroupMessages(ClientToken token,
            int groupID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        
        var result = controller.getGroupMessages(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public List<Announcement> getAllAnnouncements(ClientToken token) 
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        
        var result = controller.getAnnouncements(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public List<Announcement> getUnReadAnnouncements(
            ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        
        var result = controller.getAnnouncements(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public List<ContactInvitation> getContactInvitations(
            ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        
        var result = controller.getContactInvitations(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public List<Notification> getNotifications(ClientToken token) 
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        
        var result = controller.getNotifications(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean markNotificationsAsRead(ClientToken token,
            List<Notification> notifications) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        
        var result = controller.markNotificationsAsRead(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean deleteNotification(ClientToken token,
            int notificationID) throws RemoteException {
        return false;
    }
    /**
    
    /**
     * Will return all the data except the password and the pic.
     * @param token
     * @return 
     */
    @Override
    public NormalUser getMyProfile(ClientToken token) 
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        
        var result = controller.getMyProfile(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean saveProfileChanges(ClientToken token) 
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        
        var result = controller.saveProfileChanges(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    public boolean changePassword(ClientToken token, String oldPassword,
            String newPassword) throws RemoteException {
        return false;
    }
}
