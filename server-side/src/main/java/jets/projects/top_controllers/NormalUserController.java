package jets.projects.top_controllers;

import java.util.Date;
import jets.projects.normal_user_controller_helpers.AnnouncementManager;
import jets.projects.normal_user_controller_helpers.ContactsManager;
import jets.projects.normal_user_controller_helpers.NotificationManager;
import jets.projects.normal_user_controller_helpers.ContactInvitationManager;
import jets.projects.normal_user_controller_helpers.GroupManager;
import jets.projects.normal_user_controller_helpers.ProfileManager;
import jets.projects.normal_user_controller_helpers.GroupMessagesManager;
import jets.projects.normal_user_controller_helpers.AuthenticationManager;
import jets.projects.normal_user_controller_helpers.ContactMessagesManager;
import java.util.List;
import jets.projects.api.ClientAPI;
import jets.projects.classes.RequestResult;
import jets.projects.entities.Announcement;
import jets.projects.entities.ContactInvitation;
import jets.projects.entities.ContactMessage;
import jets.projects.entities.Group;
import jets.projects.entities.GroupMessage;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;
import jets.projects.entities.Notification;
import jets.projects.entity_info.AnnouncementInfo;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entity_info.ContactInvitationInfo;
import jets.projects.entity_info.GroupMemberInfo;
import jets.projects.session.ClientSessionData;
import jets.projects.session.ClientToken;

public class NormalUserController {
    private final AuthenticationManager authenticationManager;
    private final ProfileManager profileManager;
    private final ContactsManager contactsManager;
    private final ContactMessagesManager contactMessagesManager;
    private final GroupManager groupManager;
    private final GroupMessagesManager groupMessagesManager;
    private final AnnouncementManager announcementManager;
    private final ContactInvitationManager contactInvitationManager;
    private final NotificationManager notificationManager;

    public NormalUserController() {
        authenticationManager = new AuthenticationManager();
        profileManager = new ProfileManager();
        contactsManager = new ContactsManager();
        contactMessagesManager = new ContactMessagesManager();
        groupManager = new GroupManager();
        groupMessagesManager = new GroupMessagesManager();
        announcementManager = new AnnouncementManager();
        contactInvitationManager = new ContactInvitationManager();
        notificationManager = new NotificationManager();  
    }
    
    public RequestResult<ClientSessionData> login(String phoneNumber, String password,
            ClientAPI impl) {
        return authenticationManager.login(phoneNumber, password, impl);
    }
    
    public RequestResult<ClientSessionData> adminAccountCreatedFirstLogin(
            String phoneNumber, 
            String oldPassword, String newPassword,
            ClientAPI impl) {
        return authenticationManager.adminAccountCreatedFirstLogin(phoneNumber,
                oldPassword, newPassword, impl);
    }
    
    public RequestResult<Boolean> registerNormalUser(NormalUser user) {
        return authenticationManager.registerNormalUser(user);
    }
    
    public RequestResult<Boolean> logout(ClientToken token) {
        return authenticationManager.logout(token);
    }
    
    public RequestResult<NormalUser> getMyProfile(ClientToken token) {
        return profileManager.getMyProfile(token);
    }
    
    public RequestResult<Boolean> editProfile(ClientToken token,
            String username,
            Date birthDate,
            String bio, byte[] profilePic) {
        return profileManager.editProfile(token, username, birthDate, 
                bio, profilePic);
    }
    
    public RequestResult<Boolean> changePassword(ClientToken token, String oldPassword,
            String newPassword) {
        return profileManager.changePassword(token, oldPassword, newPassword);
    }
    
    public RequestResult<byte[]> getMyProfilePic(ClientToken token) {
        return profileManager.getProfilePic(token);
    }
    
    public RequestResult<Boolean> setOnlineStatus(ClientToken token,
            NormalUserStatus newStatus) {
        return profileManager.setOnlineStatus(token, newStatus);
    }
    
    public RequestResult<List<ContactInfo>> getContacts(ClientToken token) {
        return contactsManager.getContacts(token);
    }
    
    public RequestResult<NormalUser> getContactProfile(ClientToken token, int contactID) {
        return contactsManager.getContactProfile(token, contactID);
    }
    
    public RequestResult<List<ContactMessage>> getAllContactMessages(ClientToken token,
            int contactID) {
        return contactMessagesManager.getContactMessages(token,
                contactID);
    }
    
    public RequestResult<byte[]> getContactMessageFile(ClientToken token, 
            int contactID, int messageID) {
        return contactMessagesManager.getContactMessageFile(token,
                contactID, messageID);
    }
    
    public RequestResult<List<ContactMessage>> getUnReadContactMessages(ClientToken token,
            int contactID) {
        return contactMessagesManager.getUnReadContactMessages(token,
                contactID);
    }

    public RequestResult<Boolean> sendContactMessage(ClientToken token,
            ContactMessage message) {
        return contactMessagesManager.sendContactMessage(token, message);
    }
    
    public RequestResult<Boolean> markContactMessagesAsRead(ClientToken token,
            int contactID) {
        return contactMessagesManager.markContactMessagesAsRead(token, contactID);
    }
    
    public RequestResult<List<Group>> getAllGroups(ClientToken token) {
        return groupManager.getAllGroups(token);
    }
    
    public RequestResult<Boolean> setGroupPic(ClientToken token, int groupID,
            byte[] pic) {
        return groupManager.setGroupPic(token, groupID, pic);
    }
    
    public RequestResult<Boolean> createGroup(ClientToken token, Group newGroup) {
        return groupManager.createGroup(token, newGroup);
    }
    
    public RequestResult<List<GroupMemberInfo>> getGroupMembers(ClientToken token,
            int groupID) {
        return groupManager.getGroupMembers(token, groupID);
    }
    
    public RequestResult<Boolean> addMemberToGroup(ClientToken token, int groupID,
            int contactID) {
        return groupManager.addMemberToGroup(token, groupID, contactID);
    }
    
    public RequestResult<Boolean> removeMemberFromGroup(ClientToken token, int groupID,
            int contactID) {
        return groupManager.removeMemberFromGroup(token, groupID, contactID);
    }
    
    public RequestResult<Boolean> leaveGroupAsMember(ClientToken token,
            int groupID) {
        return groupManager.leaveGroupAsMember(token, groupID);
    }
    
    public RequestResult<Boolean> leaveGroupAsAdmin(ClientToken token, int groupID,
            int newAdminID) {
        return groupManager.leaveGroupAsAdmin(token, groupID, newAdminID);
    }
    
    public RequestResult<Boolean> assignGroupLeadership(ClientToken token,
            int groupID, int newAdminID) {
        return groupManager.assignGroupLeadership(token, groupID, newAdminID);
    }
    
    public RequestResult<Boolean> deleteGroup(ClientToken token,
            int groupID) {
        return groupManager.deleteGroup(token, groupID);
    }
    
    public RequestResult<List<GroupMessage>> getGroupMessages(ClientToken token,
            int groupID) {
        return groupMessagesManager.getGroupMessages(token, groupID);
    }
    
    public RequestResult<byte[]> getGroupMessageFile(ClientToken token,
            int groupID, int messageID) {
        return groupMessagesManager.getGroupMessageFile(token,
                groupID, messageID);
    }
    
    public RequestResult<Boolean> sendGroupMessage(ClientToken token, GroupMessage message) {
        return groupMessagesManager.sendGroupMessage(token, message);
    }
    
    public RequestResult<List<AnnouncementInfo>> getAllAnnouncements(ClientToken token) {
        return announcementManager.getAllAnnouncements(token);
    }
    
    public RequestResult<List<Announcement>> getUnReadAnnouncements(
            ClientToken token) {
        return announcementManager.getUnReadAnnouncements(token);
    }
    
    public RequestResult<List<ContactInvitationInfo>> getContactInvitations(
            ClientToken token) {
        return contactInvitationManager.getContactInvitations(token);
    }
    
    public RequestResult<Boolean> sendContactInvitation(ClientToken token, ContactInvitation invitation) {
        return contactInvitationManager.sendContactInvitation(token, invitation);
    }
    
    public RequestResult<Boolean> acceptContactInvitation(ClientToken token, int invitationID) {
        return contactInvitationManager.acceptContactInvitation(token, invitationID);
    }
    
    public RequestResult<Boolean> rejectContactInvitation(ClientToken token, int invitationID) {
        return contactInvitationManager.rejectContactInvitation(token, invitationID);
    }
    
    public RequestResult<List<Notification>> getNotifications(ClientToken token) {
        return notificationManager.getNotifications(token);
    }
    
    public RequestResult<List<Notification>> getUnReadNotifications(ClientToken token) {
        return notificationManager.getUnReadNotifications(token);
    }
    
    public RequestResult<Boolean> markNotificationsAsRead(ClientToken token) {
        return notificationManager.markNotificationsAsRead(token);
    }
    
    public RequestResult<Boolean> deleteNotification(ClientToken token,
            int notificationID) {
        return notificationManager.deleteNotification(token, notificationID);
    }
}
