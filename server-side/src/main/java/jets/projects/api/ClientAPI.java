package jets.projects.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

import jets.projects.entities.Announcement;
import jets.projects.entities.ContactMessage;
import jets.projects.entities.Group;
import jets.projects.entities.GroupMessage;
import jets.projects.entities.Notification;
import jets.projects.entity_info.ContactInvitationInfo;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entity_info.GroupMemberInfo;

public interface ClientAPI extends Remote {
    public void contactInvitationReceived(
            ContactInvitationInfo invitationInfo) throws RemoteException;
    
    // This will be called on the sender of the contact invitation.
    public void contactInvitationAccepted(
            ContactInfo receiverContactInfo) throws RemoteException;
    
    // The message will be either normal message or file message,
    // If it is a file message, then I will NOT contain the file data.
    public void contactMessageReceived(ContactMessage message) throws RemoteException;
    
    public void contactUpdateInfo(int contactID,
            String newDisplayName, byte[] newPic) throws RemoteException;
    
    // The whole group info will be sent (including the pic) to be added
    // to the list.
    public void addedToGroup(Group group) throws RemoteException;
    // Do the action based on the groupID.
    // The group, groupMembers, groupMessages should be removed from the client.
    public void removedFromGroup(int groupID) throws RemoteException;
    public void leadershipGained(int groupID) throws RemoteException;
    
    public void groupMemberLeft(int groupID, int memberID);
    // Should not called on the admin nor the member affected.
    public void newGroupMemberAdded(GroupMemberInfo newMember);
    public void groupMemberRemoved(int groupID, int memberID);
    public void adminChanged(int groupID, int newAdminID);
    
    public void groupPicChanged(int groupID,
            byte[] newPic) throws RemoteException;
    // Same as removed from group.
    public void groupDeleted(int groupID) throws RemoteException;
    
    // Same idea as contactMessage.
    public void groupMessageReceived(GroupMessage message) throws RemoteException;
    
    public void newNotification(
            Notification notification) throws RemoteException;
    
    public void newAnnouncementAdded(
            Announcement announcement) throws RemoteException;
}
