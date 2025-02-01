package jets.projects.onlinelisteners;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jets.projects.dao.GroupMemberDao;
import jets.projects.dao.GroupMessagesDao;
import jets.projects.entities.GroupMessage;
import jets.projects.sharedds.OnlineNormalUserInfo;
import jets.projects.sharedds.OnlineNormalUserTable;

public class GroupMessageCallback {
    Map<Integer, OnlineNormalUserInfo> onlineUsers;
    private final ExecutorService executor;
    GroupMessagesDao groupMessagesDao;
    GroupMemberDao groupMemberDao;

    public GroupMessageCallback(GroupMessagesDao groupMessagesDao , GroupMemberDao groupMemberDao) {
        this.groupMessagesDao = groupMessagesDao;
        this.groupMemberDao = groupMemberDao;
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
        executor = Executors.newFixedThreadPool(onlineUsers.size());
    }
    public void groupMessageReceived(GroupMessage message) {
        executor.submit(()->{});
    }
}
