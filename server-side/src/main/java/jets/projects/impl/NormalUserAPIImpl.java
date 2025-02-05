package jets.projects.impl;

import jets.projects.top_controllers.GroupMessagesManager;
import jets.projects.top_controllers.NotificationManager;
import jets.projects.top_controllers.AuthenticationManager;
import jets.projects.top_controllers.GroupManager;
import jets.projects.top_controllers.ProfileManager;
import jets.projects.top_controllers.ContactsManager;
import jets.projects.top_controllers.AnnouncementManager;
import jets.projects.top_controllers.ContactMessagesManager;
import jets.projects.top_controllers.ContactInvitationManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Blob;
import java.util.Date;
import java.util.List;
import jets.projects.entities.Announcement;
import jets.projects.session.ClientToken;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entities.ContactInvitation;
import jets.projects.entities.ContactMessage;
import jets.projects.entities.Group;
import jets.projects.entities.GroupMessage;
import jets.projects.entities.Notification;
import jets.projects.session.ClientSessionData;
import jets.projects.entities.Gender;
import jets.projects.entity_info.GroupMemberInfo;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;
import jets.projects.api.ClientAPI;
import jets.projects.api.NormalUserAPI;
import jets.projects.classes.ExceptionMessages;

/** 
 * @author Mounir
 * If the ResultSet is null, then either the database is down, or
 * an unhandled problem happens in the server.
 */
public class NormalUserAPIImpl extends UnicastRemoteObject implements NormalUserAPI {
    private final AuthenticationManager authenticationManager;
    private final AnnouncementManager announcementManager;
    private final ContactInvitationManager contactInvitationManager;
    private final ContactMessagesManager contactMessagesManager;
    private final ContactsManager contactsManager;
    private final GroupMessagesManager groupMessagesManager;
    private final GroupManager groupManager;
    private final NotificationManager notificationManager;
    private final ProfileManager profileManager;
    
    public NormalUserAPIImpl() throws RemoteException {
        super();
        authenticationManager  = new AuthenticationManager();
        announcementManager = new AnnouncementManager();
        contactInvitationManager = new ContactInvitationManager();
        contactMessagesManager = new ContactMessagesManager();
        groupMessagesManager = new GroupMessagesManager();
        groupManager = new GroupManager();
        notificationManager = new NotificationManager();
        profileManager = new ProfileManager();   
        contactsManager  = new ContactsManager();     
    }
    
    private boolean validToken(ClientToken token) {
        return !(token == null
            ||  token.getPhoneNumber() == null
            ||  token.getPhoneNumber().isBlank()
            ||  token.getUserID() <= 0);
    }
    @Override
    public ClientSessionData login(String phoneNumber, String password, ClientAPI impl) throws RemoteException {
        if (phoneNumber == null || phoneNumber.isBlank()
        ||  password == null || password.isBlank() || impl == null) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        var result = authenticationManager.login(phoneNumber, password,impl);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public ClientSessionData adminAccountCreatedFirstLogin(String phoneNumber, String oldPassword, String newPassword,
            ClientAPI impl) throws RemoteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'adminAccountCreatedFirstLogin'");
    }
    @Override
    public boolean register(
            String displayName, String phoneNumber, String email, Blob pic,
            String password, Gender gender, String country, Date birthDate,
            String bio) throws RemoteException {
        
        if (displayName == null || displayName.isBlank()
        ||  phoneNumber == null || phoneNumber.isBlank()
        ||  email == null || email.isBlank()
        ||  password == null || password.isBlank()
        || gender == null
        || country == null || country.isBlank()) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        var result = authenticationManager.registerNormalUser(
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
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = authenticationManager.logout(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public Blob getMyProfilePic(ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = profileManager.getProfilePic(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean setOnlineStatus(ClientToken token, 
            NormalUserStatus newStatus) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        if (newStatus == null || newStatus == NormalUserStatus.OFFLINE) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        var result = profileManager.setOnlineStatus(token, newStatus);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public List<ContactInfo> getContacts(ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = contactsManager.getContacts(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public NormalUser getContactProfile(ClientToken token,int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = contactsManager.getContactProfile(token, contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public Blob getContactProfilePic(ClientToken token, int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = contactsManager.getContactProfilePic(token, contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public List<ContactMessage> getAllContactMessages(ClientToken token,int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = contactMessagesManager.getContactMessages(token, contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public List<ContactMessage> getUnReadContactMessages(ClientToken token, int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = contactMessagesManager.getUnReadContactMessages(token, contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean sendContactMessage(ClientToken token,
            ContactMessage message) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        if (message == null) {
            throw new RemoteException(ExceptionMessages.INVALID_MESSAGE);
        }
        var result = contactMessagesManager.sendContactMessage(token, message);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public boolean sendContactMessageFile(ClientToken token,int receiverID,String file) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        if (file == null) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        if (file.isBlank()) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        var result = contactMessagesManager.sendContactFileMessage(token, file , receiverID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public Blob getContactMessageFile(ClientToken token, int contactID, int messageID) throws RemoteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getContactFileMessage'");
    }
    @Override
    public boolean markContactMessagesAsRead(ClientToken token,
            List<ContactMessage> messages) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        if (messages == null) {
            throw new RemoteException(ExceptionMessages.INVALID_MESSAGE);
        }
        if ( messages.isEmpty()) {
            throw new RemoteException(ExceptionMessages.INVALID_MESSAGE);
        }
        var result = contactMessagesManager.markContactMessagesAsRead(token, messages);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public List<Group> getGroups(ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = groupManager.getAllGroups(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public Blob getGroupPic(ClientToken token, int groupID) 
            throws RemoteException{
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = groupManager.getGroupPic(token, groupID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean setGroupPic(ClientToken token, int groupID, Blob pic) throws RemoteException {
        if(!validToken(token)){
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        if(pic==null){
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        var result =  groupManager.setGroupPic(token , groupID , pic);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public boolean createGroup(ClientToken token, Group newGroup) 
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        if (newGroup == null) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        var result = groupManager.createGroup(token, newGroup);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public List<GroupMemberInfo> getGroupMembers(ClientToken token,int groupID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }        
        var result = groupManager.getGroupMembers(token, groupID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean addMemberToGroup(ClientToken token, int groupID, int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = groupManager.addMemberToGroup(token, groupID, contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public boolean removeMemberFromGroup(ClientToken token,int groupID, int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = groupManager.removeMemberFromGroup(token, groupID,contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean leaveGroupAsMember(ClientToken token, int groupID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = groupManager.leaveGroupAsMember(token, groupID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean leaveGroupAsAdmin(ClientToken token,int groupID, int newAdminID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = groupManager.leaveGroupAsAdmin(token, groupID, newAdminID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean assignGroupLeadership(ClientToken token,
            int groupID, int newAdminID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = groupManager.assignGroupLeadership(token, groupID,newAdminID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public boolean deleteGroup(ClientToken token, int groupID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = groupManager.deleteGroup(token, groupID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public List<GroupMessage> getGroupMessages(ClientToken token,int groupID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = groupMessagesManager.getGroupMessages(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean sendGroupMessage(ClientToken token, GroupMessage message) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        if (message==null) {
            throw new RemoteException(ExceptionMessages.INVALID_MESSAGE);            
        }        
        var result = groupMessagesManager.sendGroupMessage(token,message);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }    
    @Override
    public boolean sendGroupMessageFile(ClientToken token,int groupID,String file) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        if (file == null) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        if (file.isBlank()) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        var result = groupMessagesManager.sendGroupFileMessage(token, groupID , file);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public List<Announcement> getAllAnnouncements(ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = announcementManager.getAnnouncements(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public List<Announcement> getUnReadAnnouncements(
            ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = announcementManager.getUnReadAnnouncements(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public List<NormalUser> getContactInvitations(ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = contactInvitationManager.getContactInvitations(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public boolean sendContactInvitation(ClientToken token, ContactInvitation invitation) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = contactInvitationManager.sendContactInvitation(token , invitation);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public boolean acceptContactInvitation(ClientToken token,ContactInvitation invitation) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = contactInvitationManager.acceptContactInvitation(token , invitation);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public boolean rejectContactInvitation(ClientToken token, ContactInvitation invitation) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = contactInvitationManager.rejectContactInvitation(token , invitation);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public List<Notification> getNotifications(ClientToken token) 
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = notificationManager.getNotifications(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public List<Notification> getUnReadNotifications(ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = notificationManager.getUnReadNotifications(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public boolean markNotificationsAsRead(ClientToken token,
            List<Notification> notifications) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = notificationManager.markNotificationsAsRead(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public boolean deleteNotification(ClientToken token,int notificationID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = notificationManager.deleteNotification(token , notificationID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public NormalUser getMyProfile(ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = profileManager.getMyProfile(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    @Override
    public boolean editProfile(ClientToken token ,String username ,String bio ,Blob profilePic) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = profileManager.editProfile(token , username , bio , profilePic);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    public boolean changePassword(ClientToken token, String oldPassword,String newPassword) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.UNREGISTERED_USER);
        }
        var result = profileManager.changePassword(token , oldPassword , newPassword);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
}
