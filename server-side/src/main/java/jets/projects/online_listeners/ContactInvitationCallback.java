package jets.projects.online_listeners;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jets.projects.api.ClientAPI;
import jets.projects.dao.ContactInvitationDao;
import jets.projects.entities.ContactInvitation;
import jets.projects.shared_ds.OnlineNormalUserInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class ContactInvitationCallback {
    Map<Integer, OnlineNormalUserInfo> onlineUsers;
    private final ExecutorService executor;
    ContactInvitationDao contactInvitationDao;

    public ContactInvitationCallback(ContactInvitationDao contactInvitationDao) {
        this.contactInvitationDao = contactInvitationDao;
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
        int onlineCount = (onlineUsers != null) ? onlineUsers.size() : 1;
        executor = Executors.newFixedThreadPool(onlineCount);
    }
    public void contactInvitationReceived(ContactInvitation invitation) {
        int receiverID = invitation.getReceiverID();
        ClientAPI client = onlineUsers.get(receiverID).getImpl();
        executor.submit(()->{
            if(client!=null){
                try {
                    client.contactInvitationReceived(invitation);
                } catch (RemoteException e) {
                    System.err.println("Failed to send invitation " + e.getMessage());
                }
            }
        });
    }
    public void contactInvitationAccepted(ContactInvitation invitation) {
        int receiverID = invitation.getReceiverID();
        ClientAPI client = onlineUsers.get(receiverID).getImpl();
        executor.submit(()->{
            if(client!=null){
                try {
                    client.contactInvitationAccepted(invitation);;
                } catch (RemoteException e) {
                    System.err.println("Failed to send invitation " + e.getMessage());
                }
            }
        });        
    }
    public void contactInvitationRejected(ContactInvitation invitation) {
        int receiverID = invitation.getReceiverID();
        ClientAPI client = onlineUsers.get(receiverID).getImpl();
        executor.submit(()->{
            if (client!=null) {
                try {
                    client.contactInvitationRejected(invitation);
                } catch (RemoteException e) {
                    System.err.println("Failed to send invitation " + e.getMessage());
                }
            }
        });        
    }
}
