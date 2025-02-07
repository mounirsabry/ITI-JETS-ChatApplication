package jets.projects.online_listeners;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import jets.projects.classes.Delays;

public class OnlineCleaner {
    private ScheduledExecutorService executor;
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
                Delays.TIMEOUT_USER_CHECK_DELAY, TimeUnit.SECONDS);
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
        System.err.println("Online cleaner check not implemented.");
    }
}
