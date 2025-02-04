package jets.projects.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Blob;
import java.util.Date;
import java.util.List;
import jets.projects.session.ClientSessionData;
import jets.projects.session.ClientToken;
import jets.projects.entities.Announcement;
import jets.projects.entities.Contact;
import jets.projects.entities.ContactInvitation;
import jets.projects.entities.ContactMessage;
import jets.projects.entities.Gender;
import jets.projects.entities.Group;
import jets.projects.entities.GroupMember;
import jets.projects.entities.GroupMessage;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;
import jets.projects.entities.Notification;

public interface NormalUserAPI extends Remote {

    public ClientSessionData login(String phoneNumber,
            String password, ClientAPI impl) throws RemoteException;
    
    public ClientSessionData adminAccountCreatedFirstLogin(String phoneNumber,
            String oldPassword, String newPassword, ClientAPI impl) throws RemoteException;
    
    public boolean register(
            String displayName, String phoneNumber, String email, Blob pic,
            String password, Gender gender, String country, Date birthDate,
            String bio) throws RemoteException;
    
    public boolean logout(ClientToken token) throws RemoteException;
    
    public Blob getProfilePic(ClientToken token) throws RemoteException;

    public boolean setOnlineStatus(ClientToken token, NormalUserStatus newStatus) throws RemoteException;
    
    public List<Contact> getContacts(ClientToken token) throws RemoteException;
    
    public NormalUser getContactProfile(ClientToken token,int contactID) throws RemoteException;
    
    public Blob getContactProfilePic(ClientToken token,int contactID) throws RemoteException;
    
    public List<ContactMessage> getAllContactMessages(ClientToken token,int contactID) throws RemoteException;
    
    public Blob getContactFileMessage(ClientToken token,
            int contactID, int messageID) throws RemoteException;
    
    public List<ContactMessage> getUnReadContactMessages(
            ClientToken token, int otherID) throws RemoteException;
    
    public boolean sendContactMessage(ClientToken token, ContactMessage message) throws RemoteException;
    
    public boolean sendContactFileMessage(ClientToken token, int receiverID,String file) throws RemoteException;
    
    public boolean markContactMessagesAsRead(ClientToken token,List<ContactMessage> messages) throws RemoteException;
    
    public List<Group> getGroups(ClientToken token) throws RemoteException;

    public Blob getGroupPic(ClientToken token, int groupID) throws RemoteException;
    
    public boolean setGroupPic(ClientToken token, int groupID, Blob pic) throws RemoteException;
    
    public boolean createGroup(ClientToken token, Group newGroup) throws RemoteException;

    public List<GroupMember> getGroupMembers(ClientToken token,int groupID) throws RemoteException;
    
    public boolean addMemberToGroup(ClientToken token, int groupID, int contactID) throws RemoteException;

    public boolean removeMemberFromGroup(ClientToken token, int groupID, int contactID) throws RemoteException;

    public boolean leaveGroupAsMember(ClientToken token,int groupID) throws RemoteException;
    
    public boolean leaveGroupAsAdmin(ClientToken token,int groupID, int newAdminID) throws RemoteException;
    
    public boolean assignGroupLeadership(ClientToken token,int groupID, int newAdminID) throws RemoteException;
    
    public boolean deleteGroup(ClientToken token, int groupID)throws RemoteException;

    public List<GroupMessage> getGroupMessages(ClientToken token,int groupID) throws RemoteException;
    
    public boolean sendGroupMessage(ClientToken token,GroupMessage message) throws RemoteException;
    public boolean sendGroupFileMessage (ClientToken token , int groupID , String file) throws RemoteException;
    
    public List<Announcement> getAllAnnouncements(ClientToken token) throws RemoteException;
    
    public List<Announcement> getUnReadAnnouncements(ClientToken token) throws RemoteException;
    
    public List<ContactInvitation> getContactInvitations(ClientToken token) throws RemoteException;
    
    public boolean sendContactInvitation(ClientToken token, ContactInvitation invitation) throws RemoteException;
    
    public boolean acceptContactInvitation( ClientToken token, ContactInvitation invitation) throws RemoteException;
    
    public boolean rejectContactInvitation(ClientToken token, ContactInvitation invitation) throws RemoteException;
    
    public List<Notification> getNotifications(ClientToken token)throws RemoteException;
    
    public List<Notification> getUnReadNotifications(ClientToken token) throws RemoteException;
    
    public boolean markNotificationsAsRead(ClientToken token,List<Notification> notifications) throws RemoteException;
    
    public boolean deleteNotification(ClientToken token,int notificationID) throws RemoteException;
    public NormalUser getMyProfile(ClientToken token) throws RemoteException;
    
    public boolean editProfile(ClientToken token , String username , String bio ,Blob profilePic) throws RemoteException;
    
    public boolean changePassword(ClientToken token, String oldPassword,String newPassword) throws RemoteException;
}
