package jets.projects.onlinelisteners;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jets.projects.dao.ContactDao;
import jets.projects.dao.NotificationDao;
import jets.projects.entities.NormalUserStatus;
import jets.projects.sharedds.OnlineNormalUserInfo;
import jets.projects.sharedds.OnlineNormalUserTable;

public class NotificatonCallback {
    Map<Integer, OnlineNormalUserInfo> onlineUsers;
    private final ExecutorService executor;
    NotificationDao notificationDao ;
    ContactDao contactDao;

    public NotificatonCallback(NotificationDao notificationDao , ContactDao contactDao) {
        this.notificationDao = notificationDao;
        this.contactDao = contactDao;
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
        executor = Executors.newFixedThreadPool(onlineUsers.size());
    }
    public void userWentOnline(int userID) {
        executor.submit(()->{});
    }
    
    public void userWentOffline(int userID) {
        executor.submit(()->{});        
    }
    
    public void userStatusChanged(int userID, NormalUserStatus newStatus) {
        executor.submit(()->{});        
    }
    
}
