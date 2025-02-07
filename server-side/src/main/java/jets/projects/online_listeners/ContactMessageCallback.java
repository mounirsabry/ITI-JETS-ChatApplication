package jets.projects.online_listeners;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jets.projects.api.ClientAPI;
import jets.projects.dao.ContactMessagesDao;
import jets.projects.entities.ContactMessage;
import jets.projects.shared_ds.OnlineNormalUserInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class ContactMessageCallback {
    
    Map<Integer, OnlineNormalUserInfo> onlineUsers;
    private final ExecutorService executor;
    ContactMessagesDao contactMessagesDao;

    public ContactMessageCallback(ContactMessagesDao contactMessagesDao) {
        this.contactMessagesDao = contactMessagesDao;
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
        int onlineCount = (onlineUsers != null) ? onlineUsers.size() : 1;
        executor = Executors.newFixedThreadPool(onlineCount);
    }

     public void contactMessageReceived(ContactMessage message) {
        int receiverID = message.getReceiverID();

        ClientAPI client = OnlineNormalUserTable.getOnlineUsersTable().get(receiverID).getImpl();
        if (client != null) {
            executor.submit(() -> {
                try {
                    client.contactMessageReceived(message); 
                } catch (Exception e) {
                    System.err.println("Failed to send contact message callback: " + e.getMessage());
                }
            });
        }
    } 
    
    public void fileContactMessageReceived(int senderID , int receiverID , String file) {
        ClientAPI client = OnlineNormalUserTable.getOnlineUsersTable().get(receiverID).getImpl();
        if (client != null) {
            executor.submit(() -> {
                try {
                    client.contactFileMessageReceived(file); 
                } catch (Exception e) {
                    System.err.println("Failed to send file contact message callback: " + e.getMessage());
                }
            });
        }
    }
}
