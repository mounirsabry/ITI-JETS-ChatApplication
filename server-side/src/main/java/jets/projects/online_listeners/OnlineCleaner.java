package jets.projects.online_listeners;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import jets.projects.classes.Delays;
import jets.projects.dao.UsersDao;
import jets.projects.shared_ds.OnlineNormalUserInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class OnlineCleaner {
    private ScheduledExecutorService executor;
    private static final UsersDao usersDao = new UsersDao();
    private final Runnable checkTask;
    
    private static boolean isInit = false;
    public OnlineCleaner() {
        if (isInit) {
            throw new UnsupportedOperationException("Object has already been init.");
        }
        
        checkTask = () -> {
            clean();
        };
        isInit = true;
    }
    
    public void start() {
        if (executor != null) {
            throw new IllegalStateException(
                    "The executor is already running.");
        }
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(checkTask, 0,
                Delays.TIMEOUT_USER_CHECK_DELAY,
                TimeUnit.SECONDS);
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
        } finally {
            executor = null;
        }
    }
    
    private void clean() {
        var table = OnlineNormalUserTable.table;
        for (Map.Entry<Integer, OnlineNormalUserInfo> entry : table.entrySet()) {
            int userID = entry.getKey();
            var info = entry.getValue();
            
            Duration difference = Duration.between(
                    info.getLastRefreshed(),
                    LocalDateTime.now());
            if (difference.getSeconds() >= Delays.USER_TIMEOUT_DELAY) {
                table.remove(entry.getKey());
                var logoutResult = usersDao.clientLogout(userID);
                if (logoutResult.getErrorMessage() != null) {
                    System.err.println(logoutResult.getErrorMessage());
                }
                NotificationCallback.userTimeout(userID);
            }
        }
    }
}
