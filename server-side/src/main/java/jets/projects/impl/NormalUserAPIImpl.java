package jets.projects.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import jets.projects.entities.Announcement;
import jets.projects.session.ClientToken;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entities.ContactMessage;
import jets.projects.entities.Group;
import jets.projects.entities.GroupMessage;
import jets.projects.entities.Notification;
import jets.projects.session.ClientSessionData;
import jets.projects.entity_info.GroupMemberInfo;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;
import jets.projects.api.ClientAPI;
import jets.projects.api.NormalUserAPI;
import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.FileChecker;
import jets.projects.entities.ContactGroup;
import jets.projects.entity_info.AnnouncementInfo;
import jets.projects.entity_info.ContactInvitationInfo;
import jets.projects.top_controllers.NormalUserController;

public class NormalUserAPIImpl extends UnicastRemoteObject
        implements NormalUserAPI {
    private final NormalUserController controller;

    public NormalUserAPIImpl() throws RemoteException {
        super();
        controller = new NormalUserController();
    }

    private boolean validToken(ClientToken token) {
        return !(token == null
                || token.getPhoneNumber() == null
                || token.getPhoneNumber().isBlank()
                || token.getUserID() <= 0);
    }
    
    private void checkFile(byte[] file) throws RemoteException {
        if (file != null) {
            if (!FileChecker.isSizeValid(file)) {
                throw new RemoteException(
                        ExceptionMessages.FILE_TOO_BIG);
            }
            if (!FileChecker.isTypeValid(file)) {
                throw new RemoteException(
                        ExceptionMessages.FILE_TYPE_NOT_SUPPORTED);
            }
        }
    }

    @Override
    public ClientSessionData login(String phoneNumber, String password,
            ClientAPI impl) throws RemoteException {
        if (phoneNumber == null || phoneNumber.isBlank()
        || password == null || password.isBlank() 
        || impl == null) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = controller.login(phoneNumber, password, impl);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public ClientSessionData adminAccountCreatedFirstLogin(String phoneNumber,
            String oldPassword, String newPassword,
            ClientAPI impl) throws RemoteException {
        if (phoneNumber == null || phoneNumber.isBlank()
        || oldPassword == null || oldPassword.isBlank() 
        || newPassword == null || newPassword.isBlank() 
        || impl == null) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = controller.adminAccountCreatedFirstLogin(phoneNumber,
                oldPassword, newPassword, impl);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public boolean register(NormalUser user) throws RemoteException {
        if (user.getDisplayName() == null || user.getDisplayName().isBlank()
        ||  user.getPhoneNumber() == null || user.getPhoneNumber().isBlank()
        ||  user.getEmail() == null || user.getEmail().isBlank()
        ||  user.getPassword() == null || user.getPassword().isBlank()
        ||  user.getGender() == null
        ||  user.getCountry() == null
        ||  user.getBirthDate().compareTo(Date.from(Instant.now())) > 0
        ||  user.getIsAdminCreated() == true
        ||  user.getIsPasswordValid() == false) {
            System.out.println(user);
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        checkFile(user.getPic());
        
        if (user.getBio() == null) {
            user.setBio("");
        }
        
        var result = controller.registerNormalUser(user);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public boolean logout(ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        var result = controller.logout(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public void registerPulse(ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        var result = controller.registerPulse(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
    }
    
    @Override
    public NormalUser getMyProfile(ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        var result = controller.getMyProfile(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public boolean editProfile(ClientToken token, String displayName,
            Date birthDate,
            String bio, byte[] profilePic) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        if (displayName == null || displayName.isBlank()
        ||  (birthDate != null 
        &&  birthDate.compareTo(Date.from(Instant.now())) > 0)) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        checkFile(profilePic);
        
        if (bio == null) {
            bio = "";
        }
        
        var result = controller.editProfile(token, displayName, birthDate, bio, profilePic);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public boolean changePassword(ClientToken token, String oldPassword,
            String newPassword) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        var result = controller.changePassword(token, oldPassword, newPassword);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public byte[] getMyProfilePic(ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        var result = controller.getMyProfilePic(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public boolean setOnlineStatus(ClientToken token,
            NormalUserStatus newStatus) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (newStatus == null || newStatus == NormalUserStatus.OFFLINE) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = controller.setOnlineStatus(token, newStatus);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public List<ContactInfo> getContacts(ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        var result = controller.getContacts(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public NormalUser getContactProfile(ClientToken token,
            int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (contactID <= 0) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = controller.getContactProfile(token, contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public NormalUserStatus getContactOnlineStatus(ClientToken token,
            int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (contactID <= 0) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = controller.getContactOnlineStatus(token,
                contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public List<ContactMessage> getAllContactMessages(ClientToken token,
            int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (contactID <= 0) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = controller.getAllContactMessages(token, contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public byte[] getContactMessageFile(ClientToken token,
            int contactID, int messageID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (contactID <= 0 || messageID <= 0) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = controller.getContactMessageFile(token,
                contactID, messageID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public List<ContactMessage> getUnReadContactMessages(ClientToken token,
            int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (contactID <= 0) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = controller.getUnReadContactMessages(token, contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public int sendContactMessage(ClientToken token,
            ContactMessage message) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (message == null
        ||  message.getSenderID() <= 0
        ||  message.getReceiverID() <= 0
        ||  (message.getContent() == null && !message.getContainsFile())
        ||  (message.getContainsFile() && message.getFile() == null)) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        checkFile(message.getFile());
        
        var result = controller.sendContactMessage(token, message);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }


    @Override
    public boolean markContactMessagesAsRead(ClientToken token,
            int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (contactID <= 0) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = controller.markContactMessagesAsRead(token, contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public List<Group> getGroups(ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        var result = controller.getAllGroups(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public boolean setGroupPic(ClientToken token, int groupID,
            byte[] pic) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (groupID <= 0) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        checkFile(pic);

        var result = controller.setGroupPic(token, groupID, pic);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public int createGroup(ClientToken token, Group newGroup)
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (newGroup == null
        ||  newGroup.getGroupName() == null || newGroup.getGroupName().isBlank()) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        checkFile(newGroup.getPic());
        
        var result = controller.createGroup(token, newGroup);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public List<GroupMemberInfo> getGroupMembers(ClientToken token,
            int groupID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (groupID <= 0) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = controller.getGroupMembers(token, groupID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public boolean addMemberToGroup(ClientToken token, int groupID,
            int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (groupID <= 0 || contactID <= 0) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = controller.addMemberToGroup(token, groupID, contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public boolean removeMemberFromGroup(ClientToken token, int groupID,
            int contactID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (groupID <= 0 || contactID <= 0) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = controller.removeMemberFromGroup(token, groupID, contactID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public boolean leaveGroupAsMember(ClientToken token,
            int groupID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (groupID <= 0) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = controller.leaveGroupAsMember(token, groupID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public boolean leaveGroupAsAdmin(ClientToken token, int groupID,
            int newAdminID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (groupID <= 0 || newAdminID < 0
        ||  token.getUserID() == newAdminID) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
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
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (groupID <= 0 || newAdminID <= 0
        ||  token.getUserID() == newAdminID) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = controller.assignGroupLeadership(token, groupID, newAdminID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public boolean deleteGroup(ClientToken token,
            int groupID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (groupID <= 0) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
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
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (groupID <= 0) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = controller.getGroupMessages(token, groupID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public byte[] getGroupMessageFile(ClientToken token,
            int groupID, int messageID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (groupID <= 0 || messageID <= 0) {
            System.out.println("file error");
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = controller.getGroupMessageFile(token, groupID, messageID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public int sendGroupMessage(ClientToken token,
            GroupMessage message) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (message == null) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        checkFile(message.getFile());
        
        var result = controller.sendGroupMessage(token, message);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public List<AnnouncementInfo> getAllAnnouncements(ClientToken token)
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        var result = controller.getAllAnnouncements(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public List<Announcement> getUnReadAnnouncements(
            ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        var result = controller.getUnReadAnnouncements(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public List<ContactInvitationInfo> getContactInvitations(
            ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        var result = controller.getContactInvitations(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public boolean sendContactInvitation(ClientToken token,
            String userPhoneNumber,
            ContactGroup contactGroup) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        
        if (userPhoneNumber == null || userPhoneNumber.isBlank()
        || contactGroup == null) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        var result = controller.sendContactInvitation(token,
                userPhoneNumber, contactGroup);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public ContactInfo acceptContactInvitation(ClientToken token, int invitationID)
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        var result = controller.acceptContactInvitation(token, invitationID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public boolean rejectContactInvitation(ClientToken token, int invitationID)
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        var result = controller.rejectContactInvitation(token, invitationID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public List<Notification> getNotifications(ClientToken token)
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        var result = controller.getNotifications(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public List<Notification> getUnReadNotifications(
            ClientToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        var result = controller.getUnReadNotifications(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public boolean markNotificationsAsRead(ClientToken token) 
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        var result = controller.markNotificationsAsRead(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    @Override
    public boolean deleteNotification(ClientToken token, int notificationID)
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN);
        }
        var result = controller.deleteNotification(token, notificationID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
}
