package jets.projects.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

import jets.projects.entities.Announcement;
import jets.projects.entities.ContactMessage;
import jets.projects.entities.GroupMessage;
import jets.projects.entities.NormalUserStatus;

public interface ClientAPI extends Remote {
    public void contactInvitationReceived(int senderID, int receiverID) throws RemoteException;
    public void contactInvitationAccepted(int senderID, int receiverID) throws RemoteException;
    public void contactInvitationRejected(int senderID, int receiverID) throws RemoteException;
    
    public void contactMessageReceived(ContactMessage message) throws RemoteException;
    public void fileContactMessageReceived(ContactMessage message) throws RemoteException;
    
    public void addedToGroup(int userID, int groupID) throws RemoteException;
    public void removedFromGroup(int userID, int groupID) throws RemoteException;
    public void leadershipGained(int userID, int groupID) throws RemoteException;
    public void groupDeleted(int groupID) throws RemoteException;
    
    public void groupMessageReceived(GroupMessage message) throws RemoteException;
    
    public void userWentOnline(int userID) throws RemoteException;
    public void userWentOffline(int userID) throws RemoteException;
    public void userStatusChanged(int userID, NormalUserStatus newStatus) throws RemoteException;
    public void contactInvitationReceivedNotification(int senderID, int receiverID) throws RemoteException;
    
    public void contactInvitationAcceptedNotification(int senderID, int receiverID) throws RemoteException;
    
    public void contactInvitationRejectedNotification(int senderID, int receiverID) throws RemoteException;
    
    public void newAnnouncementAdded(Announcement announcement) throws RemoteException;
}
