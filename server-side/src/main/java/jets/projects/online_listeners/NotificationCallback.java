package jets.projects.online_listeners;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import jets.projects.classes.Delays;
import jets.projects.classes.MyExecutorFactory;
import jets.projects.dao.ContactDao;
import jets.projects.dao.NotificationDao;
import jets.projects.dao.UsersDao;
import jets.projects.entities.NormalUserStatus;
import jets.projects.entities.Notification;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class NotificationCallback {
    private static ExecutorService executor;
    private static final NotificationDao notificationDao
            = new NotificationDao();
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

    public static void receivedContactInvitationSender(
            int senderID, int receiverID) {
        var table = OnlineNormalUserTable.table;
        
        Notification notification;
        notificationDao.saveNotification(notification);
    }
    
    public static void receivedContactInvitationReceiver(
            int senderID, int receiverID) {
        
    }
    
    public static void acceptedContactInvitationSender(
            int senderID, int receiverID) {
        
    }
    
    public static void acceptedContactInvitationReceiver(
            int senderID, int recieverID) {
        
    }
    
    public static void userTimeout(int userID) {
        
    }

    public static void userStatusChanged(int userID,
            NormalUserStatus newStatus) {
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
