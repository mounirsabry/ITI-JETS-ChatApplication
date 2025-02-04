package jets.projects.onlinelisteners;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jets.projects.api.ClientAPI;
import jets.projects.dao.ContactInvitationDao;
import jets.projects.entities.ContactInvitation;
import jets.projects.sharedds.OnlineNormalUserInfo;
import jets.projects.sharedds.OnlineNormalUserTable;

public class ContactInvitationCallback {
    Map<Integer, OnlineNormalUserInfo> onlineUsers;
    private final ExecutorService executor;
    ContactInvitationDao contactInvitationDao;

    public ContactInvitationCallback(ContactInvitationDao contactInvitationDao) {
        this.contactInvitationDao = contactInvitationDao;
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
        executor = Executors.newFixedThreadPool(onlineUsers.size());
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
