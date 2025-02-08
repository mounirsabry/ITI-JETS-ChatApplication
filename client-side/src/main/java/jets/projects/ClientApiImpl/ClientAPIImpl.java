package jets.projects.ClientApiImpl;

import jets.projects.Classes.ExceptionMessages;
import jets.projects.Services.CallBack.CallBackContactMessageService;
import jets.projects.Services.CallBack.CallBackInvitationService;
import jets.projects.api.ClientAPI;
import jets.projects.entities.*;
import jets.projects.entity_info.ContactInvitationInfo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientAPIImpl extends UnicastRemoteObject implements ClientAPI {

    private final CallBackInvitationService callBackInvitationService;
    private final CallBackContactMessageService callBackContactMessageService;
    private final CallBackContactMessageService callBackGroupMessageService;

    public ClientAPIImpl() throws RemoteException {
        callBackInvitationService = new CallBackInvitationService();
        callBackContactMessageService = new CallBackContactMessageService();
        callBackGroupMessageService = new CallBackContactMessageService();

    }

    @Override
    public void contactInvitationReceived(ContactInvitationInfo invitationInfo) throws RemoteException {
        if(invitationInfo == null){
            throw new RemoteException(ExceptionMessages.NULL_Data);
        }
        callBackInvitationService.contactInvitationReceived(invitationInfo);
    }

    @Override
    public void contactInvitationAccepted(int invitationID) throws RemoteException {
        if(invitationID < 0){
            throw new RemoteException(ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        callBackInvitationService.contactInvitationAccepted(invitationID);
    }

    @Override
    public void contactInvitationRejected(int invitationID) throws RemoteException {
        if(invitationID < 0){
            throw new RemoteException(ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        callBackInvitationService.contactInvitationRejected(invitationID);
    }

    @Override
    public void contactMessageReceived(ContactMessage message) throws RemoteException {
        if(message == null){
            throw new RemoteException(ExceptionMessages.NULL_Data);
        }
        callBackContactMessageService.contactMessageReceived(message);
    }

    @Override
    public void addedToGroup(Group group) throws RemoteException {

    }

    @Override
    public void removedFromGroup(int groupID) throws RemoteException {

    }

    @Override
    public void leadershipGained(int groupID) throws RemoteException {

    }

    @Override
    public void groupDeleted(int groupID) throws RemoteException {

    }

    @Override
    public void groupMessageReceived(GroupMessage message) throws RemoteException {

    }

    @Override
    public void userStatusChangedNotification(Notification notification) throws RemoteException {

    }

    @Override
    public void contactInvitationNotification(Notification notification) throws RemoteException {

    }

    @Override
    public void newAnnouncementAdded(Announcement announcement) throws RemoteException {

    }
}
