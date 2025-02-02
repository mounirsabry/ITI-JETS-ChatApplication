package jets.projects.onlinelisteners;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jets.projects.api.ClientAPI;
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
        int receiverID = message.getReceiverID();

        ClientAPI client = OnlineNormalUserTable.getOnlineUsersTable().get(receiverID).getImpl();
        if (client != null) {
            executor.submit(() -> {
                try {
                    client.contactMessageReceived(message); // Callback to notify the receiver
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
                    client.fileContactMessageReceived(file); 
                } catch (Exception e) {
                    System.err.println("Failed to send file contact message callback: " + e.getMessage());
                }
            });
        }
    }
}
