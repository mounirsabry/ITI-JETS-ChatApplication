package jets.projects.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

import jets.projects.entities.Announcement;
import jets.projects.entities.ContactInvitation;
import jets.projects.entities.ContactMessage;
import jets.projects.entities.GroupMessage;
import jets.projects.entities.NormalUserStatus;

public interface ClientAPI extends Remote {
    public void contactInvitationReceived(ContactInvitation invitation) throws RemoteException;
    public void contactInvitationAccepted(ContactInvitation invitation) throws RemoteException;
    public void contactInvitationRejected(ContactInvitation invitation) throws RemoteException;
    
    public void contactMessageReceived(ContactMessage message) throws RemoteException;
    public void fileContactMessageReceived(String file) throws RemoteException;
    
    public void addedToGroup(String groupName) throws RemoteException;
    public void removedFromGroup(String groupName) throws RemoteException;
    public void leadershipGained(String groupName) throws RemoteException;
    public void groupDeleted(String groupName) throws RemoteException;
    
    public void groupMessageReceived(GroupMessage message) throws RemoteException;
    public void fileGroupMessageReceived(int senderID ,int groupID, String file) throws RemoteException;
    
    public void userWentOnline(String username) throws RemoteException;
    public void userWentOffline(String username) throws RemoteException;
    public void userStatusChanged(String username, NormalUserStatus newStatus) throws RemoteException;
    
    public void contactInvitationReceivedNotification(int senderID, int receiverID) throws RemoteException;
    public void contactInvitationAcceptedNotification(int senderID, int receiverID) throws RemoteException;
    public void contactInvitationRejectedNotification(int senderID, int receiverID) throws RemoteException;
    
    public void newAnnouncementAdded(Announcement announcement) throws RemoteException;
}
