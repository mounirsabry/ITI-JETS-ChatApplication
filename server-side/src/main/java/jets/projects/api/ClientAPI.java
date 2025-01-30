package jets.projects.api;

import java.rmi.Remote;
import jets.projects.entities.Announcement;
import jets.projects.entities.ContactMessage;
import jets.projects.entities.GroupMessage;
import jets.projects.entities.NormalUserStatus;

public interface ClientAPI extends Remote {
    public void contactInvitationReceived(int senderID, int receiverID);
    public void contactInvitationAccepted(int senderID, int receiverID);
    public void contactInvitationRejected(int senderID, int receiverID);
    
    public void contactMessageReceived(ContactMessage message);
    public void fileContactMessageReceived(ContactMessage message);
    
    public void addedToGroup(int userID, int groupID);
    public void removedFromGroup(int userID, int groupID);
    public void leadershipGained(int userID, int groupID);
    public void groupDeleted(int groupID);
    
    public void groupMessageReceived(GroupMessage message);
    
    public void userWentOnline(int userID);
    public void userWentOffline(int userID);
    public void userStatusChanged(int userID, NormalUserStatus newStatus);
    public void contactInvitationReceivedNotification(
            int senderID, int receiverID);
    
    public void contactInvitationAcceptedNotification(
            int senderID, int receiverID);
    
    public void contactInvitationRejectedNotification(
            int senderID, int receiverID);
    
    public void newAnnouncementAdded(Announcement announcement);
}
