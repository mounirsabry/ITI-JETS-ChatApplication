package jets.projects.api;

import jets.projects.entities.*;
import jets.projects.entity_info.ContactInvitationInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientAPI extends Remote {
    public void contactInvitationReceived(
            ContactInvitationInfo invitationInfo) throws RemoteException;
    
    // The action should be removing the contact invitation from the list.
    public void contactInvitationAccepted(
            int invitationID) throws RemoteException;
    
    public void contactInvitationRejected(
            int invitationID) throws RemoteException;
    
    // The message will be either normal message or file message,
    // If it is a file message, then I will NOT contain the file data.
    public void contactMessageReceived(ContactMessage message) throws RemoteException;
    
    // The whole group info will be sent (including the pic) to be added
    // to the list.
    public void addedToGroup(Group group) throws RemoteException;
    
    // Do the action based on the groupID.
    // The group, groupMembers, groupMessages should be removed from the client.
    public void removedFromGroup(int groupID) throws RemoteException;
    public void leadershipGained(int groupID) throws RemoteException;
    
    // Same as removed from group.
    public void groupDeleted(int groupID) throws RemoteException;
    
    // Same idea as contactMessage.
    public void groupMessageReceived(GroupMessage message) throws RemoteException;
    
    public void userStatusChangedNotification
        (Notification notification) throws RemoteException;
    
    public void contactInvitationNotification(
            Notification notification) throws RemoteException;
    
    public void newAnnouncementAdded(
            Announcement announcement) throws RemoteException;
}
