package jets.projects.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
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

    public ClientSessionData login(String phoneNumber, String password) 
            throws RemoteException;
    
    public boolean register(
            String displayName, String phoneNumber, String email, String pic,
            String password, Gender gender, String country, Date birthDate,
            String bio) throws RemoteException;
    
    public boolean logout(ClientToken token) throws RemoteException;
    
    // String represents the image for now.
    public String getProfilePic(ClientToken token) 
            throws RemoteException;

    public boolean setOnlineStatus(ClientToken token, 
            NormalUserStatus newStatus) throws RemoteException;
    
    public List<Contact> getContacts(ClientToken token) 
            throws RemoteException;
    
    public Contact getContactProfile(ClientToken token,
            int contactID) throws RemoteException;
    
    public String getContactProfilePic(ClientToken token,
            int contactID) throws RemoteException;
    
    public List<ContactMessage> getAllContactMessages(ClientToken token,
            int contactID) throws RemoteException;
    
    public List<ContactMessage> getUnReadContactMessages(
            ClientToken token, int otherID) throws RemoteException;
    
    public boolean sendContactMessage(ClientToken token,
            ContactMessage message) throws RemoteException;
    
    public boolean sendContactFileMessage(ClientToken token,
            String file) throws RemoteException;
    
    public boolean markContactMessagesAsRead(ClientToken token,
            List<ContactMessage> messages) 
            throws RemoteException;
    
    public List<Group> getGroups(ClientToken token) 
            throws RemoteException;

    public String getGroupPic(ClientToken token, int groupID) 
            throws RemoteException;
    
    public String setGroupPic(ClientToken token, int groupID,
            String pic) throws RemoteException;
    
    public boolean createGroup(ClientToken token, Group newGroup) 
            throws RemoteException;

    public List<GroupMember> getGroupMembers(ClientToken token,
            int groupID) throws RemoteException;
    
    public boolean addMemberToGroup(ClientToken token, 
            int groupID, int contactID) throws RemoteException;

    public boolean removeMemberFromGroup(ClientToken token, 
            int groupID, int contactID) throws RemoteException;

    public boolean leaveGroupAsMember(ClientToken token, 
            int groupID) throws RemoteException;
    
    public boolean leaveGroupAsAdmin(ClientToken token,
            int groupID, int newAdminID) throws RemoteException;
    
    public boolean assignGroupLeadership(ClientToken token,
            int groupID, int newAdminID) throws RemoteException;
    
    public boolean deleteGroup(ClientToken token, int groupID) 
            throws RemoteException;

    public List<GroupMessage> getGroupMessages(ClientToken token,
            int groupID) throws RemoteException;
    
    public boolean addGroupMessage(ClientToken token, 
            GroupMessage message) throws RemoteException;
    
    public List<Announcement> getAllAnnouncements(ClientToken token)
            throws RemoteException;
    
    public List<Announcement> getUnReadAnnouncements(
            ClientToken token) throws RemoteException;
    
    public List<ContactInvitation> getContactInvitations(
            ClientToken token) throws RemoteException;
    
    public boolean sendContactInvitation(
            ClientToken token, int otherID) throws RemoteException;
    
    public boolean acceptContactInvitation(
            ClientToken token, int invitationID) throws RemoteException;
    
    public boolean rejectContactInvitation(
            ClientToken token, int invitationID) throws RemoteException;
    
    public List<Notification> getNotifications(ClientToken token) 
            throws RemoteException;
    
    public List<Notification> getUnReadNotifications(ClientToken token)
            throws RemoteException;
    
    public boolean markNotificationsAsRead(ClientToken token,
            List<Notification> notifications) throws RemoteException;
    
    public boolean deleteNotification(ClientToken token,
            int notificationID) throws RemoteException;
    /**
     * Will return all the data except the password and the pic.
     * @param token
     * @return 
     * @throws java.rmi.RemoteException 
     */
    public NormalUser getMyProfile(ClientToken token) 
            throws RemoteException;
    
    // ToDo: add the rest of the parameters.
    public boolean saveProfileChanges(ClientToken token) 
            throws RemoteException;
    
    public boolean changePassword(ClientToken token, String oldPassword,
            String newPassword) throws RemoteException;
}
