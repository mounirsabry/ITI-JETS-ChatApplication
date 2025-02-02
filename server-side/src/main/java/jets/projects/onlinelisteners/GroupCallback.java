package jets.projects.onlinelisteners;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jets.projects.api.ClientAPI;
import jets.projects.dao.GroupDao;
import jets.projects.dao.GroupMemberDao;
import jets.projects.dao.UsersDao;
import jets.projects.entities.GroupMember;
import jets.projects.sharedds.OnlineNormalUserInfo;
import jets.projects.sharedds.OnlineNormalUserTable;

public class GroupCallback {
    Map<Integer, OnlineNormalUserInfo> onlineUsers;
    private final ExecutorService executor;
    GroupDao groupDao;
    GroupMemberDao groupMemberDao;
    UsersDao usersDao;

    public GroupCallback(GroupDao groupDao , GroupMemberDao groupMemberDao, UsersDao usersDao ) {
        this.groupDao = groupDao;
        this.groupMemberDao = groupMemberDao;
        this.usersDao = usersDao;
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
        executor = Executors.newFixedThreadPool(onlineUsers.size());
    }

    public void addedToGroup(int userID, int groupID) {
        executor.submit(()->{
            ClientAPI client = OnlineNormalUserTable.getOnlineUsersTable().get(userID).getImpl();
            if (client!=null) {
                try {
                    String groupName = groupDao.getGroupName(groupID).getResponseData();
                    client.addedToGroup(groupName);
                } catch (RemoteException e) {
                    System.err.println("Falied to notify added member: " + e.getMessage());
                }
            }
        });
    }
    public void removedFromGroup(int userID, int groupID) {
        executor.submit(()->{
            ClientAPI client = OnlineNormalUserTable.getOnlineUsersTable().get(userID).getImpl();
            if (client!=null) {
                try {
                    String groupName = groupDao.getGroupName(groupID).getResponseData();
                    client.removedFromGroup(groupName);
                } catch (RemoteException e) {
                    System.err.println("Falied to notify removed member: " + e.getMessage());
                }
            }
        });        
    }
    public void leadershipGained(int userID, int groupID) {
        executor.submit(()->{
            ClientAPI client = OnlineNormalUserTable.getOnlineUsersTable().get(userID).getImpl();
            if (client!=null) {
                try {
                    String groupName = groupDao.getGroupName(groupID).getResponseData();
                    client.leadershipGained(groupName);
                } catch (RemoteException e) {
                    System.err.println("Falied to notify member about assigned leadership: " + e.getMessage());
                }
            }
        });        
    }
    public void groupDeleted(int groupID) {
        executor.submit(()->{
            ClientAPI client;
            List<GroupMember> members = groupMemberDao.getAllMembers(groupID).getResponseData();
            String groupName = groupDao.getGroupName(groupID).getResponseData();
            for(GroupMember member : members){
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
