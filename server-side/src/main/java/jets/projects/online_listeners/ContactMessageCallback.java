package jets.projects.online_listeners;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import jets.projects.api.ClientAPI;
import jets.projects.classes.Delays;
import jets.projects.classes.MyExecutorFactory;
import jets.projects.dao.ContactMessagesDao;
import jets.projects.entities.ContactMessage;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class ContactMessageCallback {
    private ExecutorService executor;
    
    ContactMessagesDao contactMessagesDao;
    
    private static boolean isInit = false;
    public ContactMessageCallback() {
        if (isInit) {
            throw new UnsupportedOperationException("Object has already been init.");
        }
        
        contactMessagesDao = new ContactMessagesDao();
        
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

    public void contactMessageReceived(ContactMessage message) {
        int receiverID = message.getReceiverID();

        ClientAPI client = OnlineNormalUserTable.getOnlineUsersTable().get(receiverID).getImpl();
        if (client != null) {
            executor.submit(() -> {
                try {
                    client.contactMessageReceived(message); 
                } catch (RemoteException e) {
                    System.err.println("Failed to send contact message callback: " + e.getMessage());
                }
            });
        }
    } 
    
    public void fileContactMessageReceived(int senderID , int receiverID , String file) {
        ClientAPI client = OnlineNormalUserTable.getOnlineUsersTable().get(receiverID).getImpl();
        if (client != null) {
            executor.submit(() -> {
                try {
                    client.contactFileMessageReceived(file); 
                } catch (Exception e) {
                    System.err.println("Failed to send file contact message callback: " + e.getMessage());
                }
            });
        }
    }
}
