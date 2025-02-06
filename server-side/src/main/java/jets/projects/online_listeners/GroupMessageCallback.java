package jets.projects.online_listeners;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import jets.projects.api.ClientAPI;
import jets.projects.dao.GroupMemberDao;
import jets.projects.dao.GroupMessagesDao;
import jets.projects.entity_info.GroupMemberInfo;
import jets.projects.entities.GroupMessage;
import jets.projects.shared_ds.OnlineNormalUserInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class GroupMessageCallback {
    Map<Integer, OnlineNormalUserInfo> onlineUsers;
    private final ExecutorService executor;
    GroupMessagesDao groupMessagesDao;
    GroupMemberDao groupMemberDao;

    public GroupMessageCallback(GroupMessagesDao groupMessagesDao , GroupMemberDao groupMemberDao) {
        this.groupMessagesDao = groupMessagesDao;
        this.groupMemberDao = groupMemberDao;
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
        int onlineCount = (onlineUsers != null) ? onlineUsers.size() : 1;
        executor = Executors.newFixedThreadPool(onlineCount);
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
