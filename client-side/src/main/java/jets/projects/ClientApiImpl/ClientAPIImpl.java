package jets.projects.ClientApiImpl;

import jets.projects.api.ClientAPI;
import jets.projects.entities.*;
import jets.projects.entity_info.ContactInvitationInfo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientAPIImpl extends UnicastRemoteObject implements ClientAPI {

    public ClientAPIImpl() throws RemoteException {

    }
    @Override
    public void contactInvitationReceived(ContactInvitationInfo invitationInfo) throws RemoteException {

    }

    @Override
    public void contactInvitationAccepted(int invitationID) throws RemoteException {

    }

    @Override
    public void contactInvitationRejected(int invitationID) throws RemoteException {

    }

    @Override
    public void contactMessageReceived(ContactMessage message) throws RemoteException {

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
