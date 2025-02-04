package jets.projects.onlinelisteners;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jets.projects.dao.AnnouncementDao;
import jets.projects.entities.Announcement;
import jets.projects.sharedds.OnlineNormalUserInfo;
import jets.projects.sharedds.OnlineNormalUserTable;

public class AnnouncementCallback {
    Map<Integer, OnlineNormalUserInfo> onlineUsers;
    private final ExecutorService executor;
    AnnouncementDao announcementDao;

    public AnnouncementCallback(AnnouncementDao announcementDao){
        this.announcementDao = announcementDao;
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
        executor = Executors.newFixedThreadPool(onlineUsers.size());
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
