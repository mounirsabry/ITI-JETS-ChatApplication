package jets.projects.online_listeners;

import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import jets.projects.classes.Delays;
import jets.projects.classes.MyExecutorFactory;
import jets.projects.dao.GroupMemberDao;
import jets.projects.entities.GroupMessage;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class GroupMessageCallback {
    private static ExecutorService executor;
    
    private static final GroupMemberDao groupMemberDao 
            = new GroupMemberDao();
    
    private static boolean isInit = false;
    public GroupMessageCallback() {
        if (isInit) {
            throw new UnsupportedOperationException("Object has already been init.");
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
    
    public static void groupMessageReceived(GroupMessage message) {
        executor.submit(()->{
            int groupID = message.getGroupID();
            var table = OnlineNormalUserTable.table;
           
            var groupMembersIDsResult 
                    = groupMemberDao.getGroupMembersIDs(groupID);
            if (groupMembersIDsResult.getErrorMessage() != null) {
                System.err.println(
                        groupMembersIDsResult.getErrorMessage());
                return;
            }
            List<Integer> membersIDs 
                    = groupMembersIDsResult.getResponseData();
            
            for (int ID : membersIDs) {
                var user = table.getOrDefault(ID, null);
                if (user == null) {
                    continue;
                }

                try {
                    user.getImpl().groupMessageReceived(message);
                } catch (RemoteException ex) {
                    System.err.println("Callback Error: " 
                            + ex.getMessage());
                }
            }
        });
    }
}
