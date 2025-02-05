package jets.projects.online_listeners;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jets.projects.dao.AnnouncementDao;
import jets.projects.entities.Announcement;
import jets.projects.shared_ds.OnlineNormalUserInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class AnnouncementCallback {
    Map<Integer, OnlineNormalUserInfo> onlineUsers;
    private final ExecutorService executor;
    AnnouncementDao announcementDao;

    public AnnouncementCallback(AnnouncementDao announcementDao){
        this.announcementDao = announcementDao;
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
        int onlineCount = (onlineUsers != null) ? onlineUsers.size() : 1;
        executor = Executors.newFixedThreadPool(onlineCount);
    }
    
    public void newAnnouncementAdded(Announcement announcement){
        executor.submit(() -> {
            for (OnlineNormalUserInfo userInfo : onlineUsers.values()) {
                try {
                    userInfo.getImpl().newAnnouncementAdded(announcement);
                } catch (RemoteException e) {
                    System.err.println("Failed to send announcement to user: " + userInfo.toString());
                }
            }
        });
    }
}
