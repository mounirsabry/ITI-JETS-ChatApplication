package jets.projects.online_listeners;

import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import jets.projects.classes.Delays;
import jets.projects.classes.MyExecutorFactory;
import jets.projects.classes.NotificationFactory;
import jets.projects.dao.ContactDao;
import jets.projects.dao.NotificationDao;
import jets.projects.dao.UsersQueryDao;
import jets.projects.entities.NormalUserStatus;
import jets.projects.entities.Notification;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class NotificationCallback {
    private static ExecutorService executor;
    private static final NotificationDao notificationDao
            = new NotificationDao();
   
    private static final UsersQueryDao usersQueryDao
            = new UsersQueryDao();
    
    private static final ContactDao contactDao = new ContactDao();
   
    
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
        executor.submit(() -> {
            var table = OnlineNormalUserTable.table;

            var receiverNameResult 
                    = usersQueryDao.getNormalUserDisplayNameByID(receiverID);
            if (receiverNameResult.getErrorMessage() != null) {
                System.err.println(receiverNameResult.getErrorMessage());
                return;
            }
            String receiverName = receiverNameResult.getResponseData();

            Notification notification
                    = NotificationFactory.createInvitationSenderNotification(
                            receiverName);
            notification.setUserID(senderID);

            var saveNotificationResult 
                    = notificationDao.saveNotification(notification);
            if (saveNotificationResult.getErrorMessage() != null) {
                System.err.println(saveNotificationResult.getErrorMessage());
                return;
            }
            Notification savedNotification = saveNotificationResult.getResponseData();
            notification.setNotificationID(
                    savedNotification.getNotificationID());
            notification.setSentAt(savedNotification.getSentAt());

            var userInfo = table.getOrDefault(senderID, null);

            // Sender is offline.
            if (userInfo == null) {
                return;
            }

            try {
                userInfo.getImpl().newNotification(notification);
            } catch (RemoteException ex) {
                System.err.println("Callback Error: "
                    + ex.getMessage());
            }
        });
    }
    
    public static void receivedContactInvitationReceiver(
            int receiverID, int senderID) {
        executor.submit(() -> {
            var table = OnlineNormalUserTable.table;
            
            var senderNameResult 
                    = usersQueryDao.getNormalUserDisplayNameByID(senderID);
            if (senderNameResult.getErrorMessage() != null) {
                System.err.println(senderNameResult.getErrorMessage());
                return;
            }
            String senderName = senderNameResult.getResponseData();

            Notification notification
                    = NotificationFactory.createInvitationReceiverNotification(
                            senderName);
            notification.setUserID(receiverID);

            var saveNotificationResult 
                    = notificationDao.saveNotification(notification);
            if (saveNotificationResult.getErrorMessage() != null) {
                System.err.println(saveNotificationResult.getErrorMessage());
                return;
            }
            Notification savedNotification = saveNotificationResult.getResponseData();
            notification.setNotificationID(
                    savedNotification.getNotificationID());
            notification.setSentAt(savedNotification.getSentAt());

            var userInfo = table.getOrDefault(receiverID, null);

            // Receiver is offline.
            if (userInfo == null) {
                return;
            }

            try {
                userInfo.getImpl().newNotification(notification);
            } catch (RemoteException ex) {
                System.err.println("Callback Error: "
                    + ex.getMessage());
            }
        });
    }
    
    public static void acceptedContactInvitationSender(
            int senderID, int receiverID) {
        executor.submit(() -> {
            var table = OnlineNormalUserTable.table;
            
            var receiverNameResult 
                    = usersQueryDao.getNormalUserDisplayNameByID(receiverID);
            if (receiverNameResult.getErrorMessage() != null) {
                System.err.println(receiverNameResult.getErrorMessage());
                return;
            }
            String receiverName = receiverNameResult.getResponseData();
            
            Notification notification
                    = NotificationFactory.createAcceptedInvitationSenderNotification(
                            receiverName);
            notification.setUserID(senderID);
            
            var saveNotificationResult 
                    = notificationDao.saveNotification(notification);
            if (saveNotificationResult.getErrorMessage() != null) {
                System.err.println(saveNotificationResult.getErrorMessage());
                return;
            }
            Notification savedNotification = saveNotificationResult.getResponseData();
            notification.setNotificationID(
                    savedNotification.getNotificationID());
            notification.setSentAt(savedNotification.getSentAt());

            var userInfo = table.getOrDefault(senderID, null);

            // Sender is offline.
            if (userInfo == null) {
                return;
            }

            try {
                userInfo.getImpl().newNotification(notification);
            } catch (RemoteException ex) {
                System.err.println("Callback Error: "
                    + ex.getMessage());
            }
        });
    }
    
    public static void acceptedContactInvitationReceiver(
            int senderID, int receiverID) {
        executor.submit(() -> {
            var table = OnlineNormalUserTable.table;
            
            var senderNameResult 
                    = usersQueryDao.getNormalUserDisplayNameByID(senderID);
            if (senderNameResult.getErrorMessage() != null) {
                System.err.println(senderNameResult.getErrorMessage());
                return;
            }
            String senderName = senderNameResult.getResponseData();

            Notification notification
                    = NotificationFactory.createAcceptedInvitationReceiverNotification(
                            senderName);
            notification.setUserID(receiverID);

            var saveNotificationResult 
                    = notificationDao.saveNotification(notification);
            if (saveNotificationResult.getErrorMessage() != null) {
                System.err.println(saveNotificationResult.getErrorMessage());
                return;
            }
            Notification savedNotification = saveNotificationResult.getResponseData();
            notification.setNotificationID(
                    savedNotification.getNotificationID());
            notification.setSentAt(savedNotification.getSentAt());

            var userInfo = table.getOrDefault(receiverID, null);

            // Receiver is offline.
            if (userInfo == null) {
                return;
            }

            try {
                userInfo.getImpl().newNotification(notification);
            } catch (RemoteException ex) {
                System.err.println("Callback Error: "
                    + ex.getMessage());
            }
        });
    }
    
    public static void userTimeout(int userID) {
        userStatusChanged(userID, NormalUserStatus.OFFLINE);
    }

    public static void userStatusChanged(int userID,
            NormalUserStatus newStatus) {
        executor.submit(()->{
            var table = OnlineNormalUserTable.table;
            
            var displayNameResult 
                    = usersQueryDao.getNormalUserDisplayNameByID(userID);
            if (displayNameResult.getErrorMessage() != null) {
                System.err.println(
                        displayNameResult.getErrorMessage());
                return;
            }
            String displayName = displayNameResult.getResponseData();
            
            Notification notification;
            switch (newStatus) {
                case AVAILABLE -> {
                    notification 
                            = NotificationFactory.createIsOnlineNotification(
                                    displayName);
                } case OFFLINE -> {
                    notification
                            = NotificationFactory.createIsOfflineNotification(
                                    displayName);
                } default -> {
                    notification
                            = NotificationFactory.createStatusChangeNotification(
                                    displayName, newStatus);
                }
            }
            notification.setUserID(userID);
            
            var contactsIDsResult = contactDao.getAllContactsIDs(userID);
            if (contactsIDsResult.getErrorMessage() != null) {
                System.err.println(contactsIDsResult.getErrorMessage());
                return;
            }
            List<Integer> contactsIDs = contactsIDsResult.getResponseData();
            
            for (int ID : contactsIDs) {
                var userInfo = table.getOrDefault(ID, null);
                
                // The contact is offline.
                if (userInfo == null) {
                    continue;
                }
                
                try {
                    userInfo.getImpl().newNotification(notification);
                } catch (RemoteException ex) {
                    System.err.println("Callback Error: "
                        + ex.getMessage());  
                }
            }
        });
    }
}
