package jets.projects.ClientApiImpl;

import jets.projects.Classes.ExceptionMessages;
import jets.projects.Services.CallBack.*;
import jets.projects.api.ClientAPI;
import jets.projects.entities.*;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entity_info.ContactInvitationInfo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientAPIImpl extends UnicastRemoteObject implements ClientAPI {

    private final CallBackInvitationService callBackInvitationService;
    private final CallBackContactMessageService callBackContactMessageService;
    private final CallBackGroupService callBackGroupService;
    private final CallBackNotificationService callBackNotificationService;
    private final CallBackUpdateService callBackUpdateService;

    public ClientAPIImpl() throws RemoteException {
        callBackInvitationService = new CallBackInvitationService();
        callBackContactMessageService = new CallBackContactMessageService();
        callBackGroupService = new CallBackGroupService();
        callBackNotificationService = new CallBackNotificationService();
        callBackUpdateService = new CallBackUpdateService();
    }

    @Override
    public void contactInvitationReceived(ContactInvitationInfo invitationInfo) throws RemoteException {
        if(invitationInfo == null){
            throw new RemoteException(ExceptionMessages.NULL_DATA_SENT_FORM_SERVER);
        }
        callBackInvitationService.contactInvitationReceived(invitationInfo);
    }

    @Override
    public void contactInvitationAccepted(ContactInfo newContact) throws RemoteException {
        if(newContact == null){
            throw new RemoteException(ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        callBackInvitationService.contactInvitationAccepted(newContact);
    }


    @Override
    public void contactMessageReceived(ContactMessage message) throws RemoteException {
        if(message == null){
            throw new RemoteException(ExceptionMessages.NULL_DATA_SENT_FORM_SERVER);
        }
        callBackContactMessageService.contactMessageReceived(message);
    }

    @Override
    public void addedToGroup(Group group) throws RemoteException {
        if(group == null){
            throw new RemoteException(ExceptionMessages.NULL_DATA_SENT_FORM_SERVER);
        }
        callBackGroupService.addedToGroup(group);
    }

    @Override
    public void removedFromGroup(int groupID) throws RemoteException {
        callBackGroupService.removedFromGroup(groupID);
    }

    @Override
    public void leadershipGained(int groupID) throws RemoteException {
        callBackGroupService.leadershipGained(groupID);
    }

    @Override
    public void groupDeleted(int groupID) throws RemoteException {
        callBackGroupService.groupDeleted(groupID);
    }

    @Override
    public void groupMessageReceived(GroupMessage message) throws RemoteException {
        if(message == null){
            throw new RemoteException(ExceptionMessages.NULL_DATA_SENT_FORM_SERVER);
        }
        callBackGroupService.groupMessageReceived(message);
    }

    @Override
    public void userStatusChangedNotification(Notification notification) throws RemoteException {
        if(notification == null){
            throw new RemoteException(ExceptionMessages.NULL_DATA_SENT_FORM_SERVER);
        }
        callBackNotificationService.userStatusChangedNotification(notification);
    }

    @Override
    public void contactInvitationNotification(Notification notification) throws RemoteException {
        if(notification == null){
            throw new RemoteException(ExceptionMessages.NULL_DATA_SENT_FORM_SERVER);
        }
        callBackNotificationService.contactInvitationNotification(notification);
    }

    @Override
    public void newAnnouncementAdded(Announcement announcement) throws RemoteException {
        if(announcement == null){
            throw new RemoteException(ExceptionMessages.NULL_DATA_SENT_FORM_SERVER);
        }
        callBackNotificationService.newAnnouncementAdded(announcement);
    }


    @Override
    public void contactUpdateInfo(int contactID, String newDisplayName, byte[] newPic) throws RemoteException {
        callBackUpdateService.contactUpdateInfo(contactID, newDisplayName, newPic);
    }

    @Override
    public void groupPicChanged(int groupID, byte[] newPic) throws RemoteException {
        callBackUpdateService.groupPicChanged(groupID, newPic);
    }
}
