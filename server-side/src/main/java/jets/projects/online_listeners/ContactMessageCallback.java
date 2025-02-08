package jets.projects.online_listeners;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import jets.projects.classes.Delays;
import jets.projects.classes.MyExecutorFactory;
import jets.projects.entities.ContactMessage;

public class ContactMessageCallback {
    private static ExecutorService executor;
    
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
    
    public static void contactMessageReceived(ContactMessage message) {
        int receiverID = message.getReceiverID();

        //ClientAPI client = OnlineNormalUserTable.getOnlineUsersTable().get(receiverID).getImpl();
        if (client != null) {
            executor.submit(() -> {
                try {
                    client.contactMessageReceived(message); 
                } catch (RemoteException e) {
                    System.err.println("Failed to send contact message callback: " 
                            + e.getMessage());
                }
            });
        }
    }
}
