package jets.projects.online_listeners;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import jets.projects.api.ClientAPI;
import jets.projects.classes.Delays;
import jets.projects.classes.MyExecutorFactory;
import jets.projects.dao.GroupDao;
import jets.projects.dao.GroupMemberDao;
import jets.projects.entity_info.GroupMemberInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class GroupCallback {
    private static ExecutorService executor;
    
    private static final GroupDao groupDao 
            = new GroupDao();
    private static final GroupMemberDao groupMemberDao 
            = new GroupMemberDao();
    
    private static boolean isInit = false;
    public GroupCallback() {
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

    public static void addedToGroup(int userID, int groupID) {
        executor.submit(()->{
            var receiverUser = OnlineNormalUserTable.getTable().getOrDefault(
                    userID, null);
            if (receiverUser == null) {
                return;
            }
            ClientAPI client = receiverUser.getImpl();
            try {
                String groupName = groupDao.getGroupName(groupID).getResponseData();
                client.addedToGroup(groupName);
            } catch (RemoteException e) {
                System.err.println("Falied to notify added member: " + e.getMessage());
            }
        });
    }
    
    public static void groupPicChanged(int groupID, byte[] newPic) {
        some text to spike compiler error.
    }
    
    public static void removedFromGroup(int userID, int groupID) {
        executor.submit(()->{
            var receiverUser = OnlineNormalUserTable.getTable().getOrDefault(
                    userID, null);
            if (receiverUser == null) {
                return;
            }
            ClientAPI client = receiverUser.getImpl();
            try {
                String groupName = groupDao.getGroupName(groupID).getResponseData();
                client.removedFromGroup(groupName);
            } catch (RemoteException e) {
                System.err.println("Falied to notify removed member: " + e.getMessage());
            }
        });        
    }
    
    public static void leadershipGained(int userID, int groupID) {
        executor.submit(()->{
            var receiverUser = OnlineNormalUserTable.getTable().getOrDefault(
                    userID, null);
            if (receiverUser == null) {
                return;
            }
            ClientAPI client = receiverUser.getImpl();
            try {
                String groupName = groupDao.getGroupName(groupID).getResponseData();
                client.leadershipGained(groupName);
            } catch (RemoteException e) {
                System.err.println("Falied to notify member about assigned leadership: " + e.getMessage());
            }
        });        
    }
    
    public static void groupDeleted(int groupID) {
        executor.submit(()->{
            ClientAPI client;
            List<GroupMemberInfo> members = groupMemberDao.getAllMembers(groupID).getResponseData();
            String groupName = groupDao.getGroupName(groupID).getResponseData();
            for(GroupMemberInfo member : members){
                client = onlineUsers.get(member.getMemberID()).getImpl();
                if(client!=null){
                   try {
                    client.groupDeleted(groupName); //notify members about deleted group
                    } catch (RemoteException e) {
                        System.err.println("Falied to notify member about deleted group: " + e.getMessage());
                    } 
                }
            }
        });        
    }
}
