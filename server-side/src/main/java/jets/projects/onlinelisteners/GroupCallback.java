package jets.projects.onlinelisteners;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import jets.projects.dao.GroupDao;
import jets.projects.dao.GroupMemberDao;
import jets.projects.sharedds.OnlineNormalUserInfo;
import jets.projects.sharedds.OnlineNormalUserTable;

public class GroupCallback {
    Map<Integer, OnlineNormalUserInfo> onlineUsers;
    private final ExecutorService executor;
    GroupDao groupDao;
    GroupMemberDao groupMemberDao;

    public GroupCallback(GroupDao groupDao , GroupMemberDao groupMemberDao) {
        this.groupDao = groupDao;
        this.groupMemberDao = groupMemberDao;
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
        executor = Executors.newFixedThreadPool(onlineUsers.size());
    }

    public void addedToGroup(int userID, int groupID) {
        executor.submit(()->{});
    }
    
    public void removedFromGroup(int userID, int groupID) {
        executor.submit(()->{});        
    }
    
    public void leadershipGained(int userID, int groupID) {
        executor.submit(()->{});        
    }
    
    public void groupDeleted(int groupID) {
        executor.submit(()->{});        
    }

    public void leaveGroupAsMember(int userID, int groupID) {
        executor.submit(()->{});
    }
}
