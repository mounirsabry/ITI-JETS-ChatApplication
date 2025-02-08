package jets.projects.online_listeners;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import jets.projects.classes.Delays;
import jets.projects.classes.MyExecutorFactory;

public class ContactCallback {
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
    
    public static void contactUpdateInfo(int contactID,
            String newDisplayName, byte[] newPic) {
        some invalid code.   
    }
}
