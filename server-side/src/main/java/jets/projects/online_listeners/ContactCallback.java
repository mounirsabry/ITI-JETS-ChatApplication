package jets.projects.online_listeners;

import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import jets.projects.classes.Delays;
import jets.projects.classes.MyExecutorFactory;
import jets.projects.dao.ContactDao;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class ContactCallback {
    private static ExecutorService executor;
    private static final ContactDao contactDao
            = new ContactDao();
    
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
        executor.submit(() -> {
            var table = OnlineNormalUserTable.table;
            
            var result = contactDao.getAllContactsIDs(contactID);
            if (result.getErrorMessage() != null) {
                System.err.println("DB Error: " 
                        + result.getErrorMessage());
            }
            List<Integer> IDs = result.getResponseData();
            for (int ID : IDs) {
                try {
                    var user = table.getOrDefault(ID, null);
                    if (user == null) {
                        continue;
                    }
                    user.getImpl().contactUpdateInfo(
                            contactID, newDisplayName, newPic);
                } catch (RemoteException ex) {
                    System.err.println("Callback Error: "
                            + ex.getMessage());
                            
                }
            }
        });
    }
}
