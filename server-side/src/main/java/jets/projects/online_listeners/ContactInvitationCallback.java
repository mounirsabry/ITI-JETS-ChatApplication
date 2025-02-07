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
    private ExecutorService executor;
    
    ContactInvitationDao contactInvitationDao;
    
    private static boolean isInit = false;
    public ContactInvitationCallback() {
        if (isInit) {
            throw new UnsupportedOperationException("Object has already been init.");
        }
        
        contactInvitationDao = new ContactInvitationDao();
        
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
            executor.awaitTermination(Delays.EXECUTOR_AWAIT_TERMINATION_TIMEOUT,
                    TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            System.err.println("Thread interrupted while waiting to terminate the executor.");
        }
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
