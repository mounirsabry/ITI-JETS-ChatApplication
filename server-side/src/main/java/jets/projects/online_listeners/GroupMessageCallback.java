package jets.projects.online_listeners;

import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import jets.projects.api.ClientAPI;
import jets.projects.classes.Delays;
import jets.projects.classes.MyExecutorFactory;
import jets.projects.dao.GroupMemberDao;
import jets.projects.dao.GroupMessagesDao;
import jets.projects.entity_info.GroupMemberInfo;
import jets.projects.entities.GroupMessage;

public class GroupMessageCallback {
    private ExecutorService executor;
    
    GroupMessagesDao groupMessagesDao;
    GroupMemberDao groupMemberDao;
    
    private static boolean isInit = false;
    public GroupMessageCallback() {
        if (isInit) {
            throw new UnsupportedOperationException("Object has already been init.");
        }
        
        groupMessagesDao = new GroupMessagesDao();
        groupMemberDao = new GroupMemberDao();
        
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
    
    public void groupMessageReceived(GroupMessage message) {
        executor.submit(()->{
            ClientAPI client;
            List<GroupMemberInfo> members = groupMemberDao.getAllMembers(message.getGroupID()).getResponseData();
            for(GroupMemberInfo member : members){
                client = onlineUsers.get(member.getMemberID()).getImpl();
                if(client!=null){
                   try {
                    client.groupMessageReceived(message);  //notify group members 
                    } catch (RemoteException e) {
                        System.err.println("Falied to notify member about new group message: " + e.getMessage());
                    } 
                }
            }
        });
    }
    
    public void fileGroupMessageReceived(int senderID ,int groupID, String file) {
        executor.submit(()->{
            ClientAPI client;
            List<GroupMemberInfo> members = groupMemberDao.getAllMembers(groupID).getResponseData();
            for(GroupMemberInfo member : members){
                client = onlineUsers.get(member.getMemberID()).getImpl();
                if(client!=null){
                   try {
                    client.groupFileMessageReceived(senderID, groupID, file);  //notify group members 
                    } catch (RemoteException e) {
                        System.err.println("Falied to notify member about new group file message: " + e.getMessage());
                    } 
                }
            }
        });     
    }
}
