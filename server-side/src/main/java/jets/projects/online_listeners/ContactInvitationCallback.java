package jets.projects.online_listeners;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import jets.projects.classes.Delays;
import jets.projects.classes.MyExecutorFactory;
import jets.projects.dao.ContactDao;
import jets.projects.dao.ContactInvitationDao;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entity_info.ContactInvitationInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class ContactInvitationCallback {
    private static ExecutorService executor;
    private static final ContactDao contactDao
            = new ContactDao();
    
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
    
    // Send the contact invitation info to the receiver, after
    // the sender has registered the invitation.
    public static void contactInvitationReceived(
            int senderID, int receiverID) {
        executor.submit(()-> {
            var table = OnlineNormalUserTable.table;
            
            var invitationInfoResult 
                    = contactInvitationDao.getContactInvitationInfo(
                    senderID, receiverID);
            if (invitationInfoResult.getErrorMessage() != null) {
                System.err.println(
                        invitationInfoResult.getErrorMessage());
                return;
            }
            ContactInvitationInfo invitationInfo 
                    = invitationInfoResult.getResponseData();
            
            var userInfo = table.getOrDefault(receiverID, null);
            
            // Receiver is offline.
            if (userInfo == null) {
                return;
            }
            
            try {
                userInfo.getImpl().contactInvitationReceived(
                        invitationInfo);
            } catch (RemoteException e) {
                System.err.println("Callback Error: " 
                        + e.getMessage());
            }
        });
    }
    
    // Sends the contact info of the receiver to the sender after
    // the sender has accepted the invitation.
    public static void contactInvitationAccepted(int senderID, int receiverID) {
        executor.submit(()->{
            var table = OnlineNormalUserTable.table;
            
            var contactInfoResult = contactDao.getContactInfo(
                    senderID, receiverID);
            if (contactInfoResult.getErrorMessage() != null) {
                System.err.println(contactInfoResult.getErrorMessage());
            }
            ContactInfo contactInfo = contactInfoResult.getResponseData();
            
            var userInfo = table.getOrDefault(senderID, null);
            
            // Sender is offline.
            if (userInfo == null) {
                return;
            }
            
            try {
                userInfo.getImpl().contactInvitationAccepted(
                        contactInfo);
            } catch (RemoteException e) {
                System.err.println("Callback Error: " + e.getMessage());
            }
        });        
    }
}
