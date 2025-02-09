package jets.projects.online_listeners;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import jets.projects.api.ClientAPI;
import jets.projects.classes.Delays;
import jets.projects.classes.MyExecutorFactory;
import jets.projects.dao.ContactInvitationDao;
import jets.projects.entities.ContactInvitation;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class ContactInvitationCallback {
    private static ExecutorService executor;
    private static final ContactInvitationDao contactInvitationDao
            = new ContactInvitationDao();
    
    private static boolean isInit = false;
    public ContactInvitationCallback() {
        if (isInit) {
            throw new UnsupportedOperationException(
                    "Object has already been init.");
        }
        isInit = true;
    }
    
    public void start() {
        if (executor != null) {
            throw new IllegalStateException(
                    "The executor is already running.");
        }
        executor = MyExecutorFactory.getExecutorService();
    }
    
    public void shutDown() {
        if (executor == null) {
            throw new IllegalStateException(
                    "The executor is already shutdown.");
        }
        try {
            executor.shutdown();
            if (!executor.awaitTermination(
                    Delays.EXECUTOR_AWAIT_TERMINATION_TIMEOUT,
                    TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            System.err.println("Thread interrupted while waiting "
                    + "to terminate the executor.");
        } finally {
            executor = null;
        }
    }
    
    // Use the ContactInvitationDao to query the contact invitation.
    // If the invitation does not exist, then maybe it was a two way
    // contact invtation, in this case, do nothing.
    public static void contactInvitationReceived(int senderID, int receiverID) {
        executor.submit(()->{
            contactInvitationDao.
            var receiverUser = OnlineNormalUserTable.getTable().getOrDefault(
                    receiverID, null);
            if (receiverUser == null) {
                return;
            }
            ClientAPI client = receiverUser.getImpl();
            try {
                client.contactInvitationReceived(invitation);
            } catch (RemoteException e) {
                System.err.println("Failed to send invitation " 
                        + e.getMessage());
            }
        });
    }
    
    public static void contactInvitationAccepted(ContactInvitation invitation) {
        executor.submit(()->{
            int receiverID = invitation.getReceiverID();
            var receiverUser = OnlineNormalUserTable.getTable().getOrDefault(
                    receiverID, null);
            if (receiverUser == null) {
                return;
            }
            ClientAPI client = receiverUser.getImpl();
            try {
                client.contactInvitationAccepted(
                        invitation.getInvitationID());
            } catch (RemoteException e) {
                System.err.println("Failed to send invitation " + e.getMessage());
            }
        });        
    }
    
    public static void contactInvitationRejected(ContactInvitation invitation) {
        executor.submit(()->{
            int receiverID = invitation.getReceiverID();
            var receiverUser = OnlineNormalUserTable.getTable().getOrDefault(
                    receiverID, null);
            if (receiverUser == null) {
                return;
            }
            ClientAPI client = receiverUser.getImpl();
            try {
                client.contactInvitationRejected(
                        invitation.getInvitationID());
            } catch (RemoteException e) {
                System.err.println("Failed to send invitation " + e.getMessage());
            }
        });        
    }
}
