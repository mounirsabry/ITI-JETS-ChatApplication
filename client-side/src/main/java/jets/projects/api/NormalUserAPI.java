package jets.projects.api;

import jets.projects.entities.*;
import jets.projects.entity_info.AnnouncementInfo;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entity_info.ContactInvitationInfo;
import jets.projects.entity_info.GroupMemberInfo;
import jets.projects.session.ClientSessionData;
import jets.projects.session.ClientToken;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface NormalUserAPI extends Remote {

    public ClientSessionData login(String phoneNumber,
            String password, ClientAPI impl) throws RemoteException;
    
    public ClientSessionData adminAccountCreatedFirstLogin(String phoneNumber,
            String oldPassword, String newPassword, ClientAPI impl) 
            throws RemoteException;
    
    public boolean register(
            NormalUser normalUser) throws RemoteException;
    
    public boolean logout(ClientToken token) throws RemoteException;
    
    // Including the pic.
    public NormalUser getMyProfile(ClientToken token) throws RemoteException;
    
    public boolean editProfile(ClientToken token, String displayName,
            String bio, byte[] profilePic) throws RemoteException;
    
    public boolean changePassword(ClientToken token,
            String oldPassword, String newPassword) throws RemoteException;
    
    public byte[] getMyProfilePic(ClientToken token) throws RemoteException;

    public boolean setOnlineStatus(ClientToken token,
            NormalUserStatus newStatus) throws RemoteException;
    
    public List<ContactInfo> getContacts(ClientToken token) throws RemoteException;
    
    // Will return the profile including the pic.
    public NormalUser getContactProfile(ClientToken token,
            int contactID) throws RemoteException;
    
    // Files not included.
    public List<ContactMessage> getAllContactMessages(ClientToken token,
            int contactID) throws RemoteException;
    
    public byte[] getContactMessageFile(ClientToken token,
            int contactID, int messageID) throws RemoteException;
    
    // Files not included.
    public List<ContactMessage> getUnReadContactMessages(
            ClientToken token, int otherID) throws RemoteException;
    
    // File included.
    public boolean sendContactMessage(ClientToken token,
            ContactMessage message) throws RemoteException;
    
    public boolean markContactMessagesAsRead(ClientToken token, 
            List<Integer> messagesIDs) throws RemoteException;
    
    // Pic will be included.
    public List<Group> getGroups(ClientToken token) throws RemoteException;
    
    // Only avaiable for the admin of the group.
    public boolean setGroupPic(ClientToken token,
            int groupID, byte[] pic) throws RemoteException;
    
    // Pic included (Optional).
    public boolean createGroup(ClientToken token,
            Group newGroup) throws RemoteException;

    // Pic included.
    public List<GroupMemberInfo> getGroupMembers(ClientToken token,
            int groupID) throws RemoteException;
    
    public boolean addMemberToGroup(ClientToken token,
            int groupID, int contactID) throws RemoteException;

    public boolean removeMemberFromGroup(ClientToken token,
            int groupID, int contactID) throws RemoteException;

    public boolean leaveGroupAsMember(ClientToken token,
            int groupID) throws RemoteException;
    
    // If the admin is the last member in the group, then newAdminID must be zero.
    public boolean leaveGroupAsAdmin(ClientToken token,
            int groupID, int newAdminID) throws RemoteException;
    
    // newAdminID must be greater than zero.
    public boolean assignGroupLeadership(ClientToken token,
            int groupID, int newAdminID) throws RemoteException;
    
    // Will remove group, group members, group messages from the database.
    public boolean deleteGroup(ClientToken token,
            int groupID)throws RemoteException;

    // Files not included.
    public List<GroupMessage> getGroupMessages(ClientToken token,
            int groupID) throws RemoteException;
    
    public byte[] getGroupMessageFile(ClientToken token,
            int groupID, int messageID) throws RemoteException;
    
    // File included.
    public boolean sendGroupMessage(ClientToken token,
            GroupMessage message) throws RemoteException;
    
    public List<AnnouncementInfo> getAllAnnouncements(
            ClientToken token) throws RemoteException;
    
    public List<Announcement> getUnReadAnnouncements(
            ClientToken token) throws RemoteException;
    
    public List<ContactInvitationInfo> getContactInvitations(
            ClientToken token) throws RemoteException;
    
    public boolean sendContactInvitation(ClientToken token,
            ContactInvitation invitation) throws RemoteException;
    
    public boolean acceptContactInvitation( ClientToken token,
            int invitationID) throws RemoteException;
    
    public boolean rejectContactInvitation(ClientToken token,
            int invitationID) throws RemoteException;
    
    public List<Notification> getNotifications(
            ClientToken token)throws RemoteException;
    
    public List<Notification> getUnReadNotifications(
            ClientToken token) throws RemoteException;
    
    public boolean markNotificationsAsRead(ClientToken token)
            throws RemoteException;
    
    public boolean deleteNotification(ClientToken token,
            int notificationID) throws RemoteException;
}
