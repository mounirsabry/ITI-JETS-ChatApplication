package jets.projects.online_listeners;

import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import jets.projects.api.ClientAPI;
import jets.projects.classes.Delays;
import jets.projects.classes.MyExecutorFactory;
import jets.projects.dao.ContactDao;
import jets.projects.dao.UsersDao;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entities.NormalUserStatus;
import jets.projects.entities.Notification;

public class NotificationCallback {
    private static ExecutorService executor;
    
    private static final ContactDao contactDao = new ContactDao();
    private static final UsersDao usersDao = new UsersDao();
    
    private static boolean isInit = false;
    public NotificationCallback() {
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
    
    public static void userStatusChangedNotification(Notification notification) {
        
    }

    public static void contactInvitationNotification(Notification notification) {
        
    }
    

    public static void userStatusChanged(int userID, NormalUserStatus newStatus) {
        /*
        executor.submit(()->{
            ClientAPI client;
            List<ContactInfo> contacts = contactDao.getAllContacts(userID).getResponseData();
            for(ContactInfo contact : contacts){
                client = onlineUsers.get(contact.getFirstID()).getImpl();
                if(client!=null){
                   try {
                    client.userStatusChanged(usersDao.getNormalUserByID(userID).getDisplayName(), newStatus); //notify contacts 
                    } catch (RemoteException e) {
                        System.err.println("Falied to notify contacts: " + e.getMessage());
                    } 
                }
            }
        });
        */
    }
}
