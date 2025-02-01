package jets.projects.onlinelisteners;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jets.projects.dao.ContactMessagesDao;
import jets.projects.entities.ContactMessage;
import jets.projects.sharedds.OnlineNormalUserInfo;
import jets.projects.sharedds.OnlineNormalUserTable;

public class ContactMessageCallback {
    
    Map<Integer, OnlineNormalUserInfo> onlineUsers;
    private final ExecutorService executor;
    ContactMessagesDao contactMessagesDao;

    public ContactMessageCallback(ContactMessagesDao contactMessagesDao) {
        this.contactMessagesDao = contactMessagesDao;
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
        executor = Executors.newFixedThreadPool(onlineUsers.size());
    }

    public void contactMessageReceived(ContactMessage message) {
        executor.submit(()->{});
    }    
    
    public void fileContactMessageReceived(int senderID , String file , int receiverID) {
        executor.submit(()->{});        
    }
}
