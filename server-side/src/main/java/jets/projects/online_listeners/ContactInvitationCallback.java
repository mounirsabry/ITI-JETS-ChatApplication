package jets.projects.online_listeners;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import jets.projects.api.ClientAPI;
import jets.projects.classes.Delays;
import jets.projects.classes.MyExecutorFactory;
import jets.projects.dao.ContactInvitationDao;
import jets.projects.entities.ContactInvitation;

public class ContactInvitationCallback {
    private static ExecutorService executor;
    private static final ContactInvitationDao contactInvitationDao
            = new ContactInvitationDao();
    
    private static boolean isInit = false;
    public ContactCallback() {
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
        contactInvitationDao
        
        ClientAPI client = onlineUsers.get(receiverID).getImpl();
        executor.submit(()->{
            if(client != null){
                try {
                    client.contactInvitationReceived(invitation);
                } catch (RemoteException e) {
                    System.err.println("Failed to send invitation " 
                            + e.getMessage());
                }
            }
        });
    }
    
    public static void contactInvitationAccepted(ContactInvitation invitation) {
        int receiverID = invitation.getReceiverID();
        ClientAPI client = onlineUsers.get(receiverID).getImpl();
        executor.submit(()->{
            if(client!=null){
                try {
                    client.contactInvitationAccepted(invitation);
                } catch (RemoteException e) {
                    System.err.println("Failed to send invitation " + e.getMessage());
                }
            }
        });        
    }
    
    public static void contactInvitationRejected(ContactInvitation invitation) {
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
