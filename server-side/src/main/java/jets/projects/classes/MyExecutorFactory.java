package jets.projects.classes;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyExecutorFactory {
    private MyExecutorFactory() {
        throw new UnsupportedOperationException("Do not create an object.");
    }
    
    public static ExecutorService getExecutorService() {
        return Executors.newFixedThreadPool(2);
    }
}
