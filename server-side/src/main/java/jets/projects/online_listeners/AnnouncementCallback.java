package jets.projects.online_listeners;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import jets.projects.classes.Delays;
import jets.projects.classes.MyExecutorFactory;

import jets.projects.dao.AnnouncementDao;
import jets.projects.entities.Announcement;
import jets.projects.shared_ds.OnlineNormalUserInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class AnnouncementCallback {
    private ExecutorService executor;
    
    AnnouncementDao announcementDao;
    
    private static boolean isInit = false;
    public AnnouncementCallback() {
        if (isInit) {
            throw new UnsupportedOperationException("Object has already been init.");
        }
        
        announcementDao = new AnnouncementDao();
        
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
