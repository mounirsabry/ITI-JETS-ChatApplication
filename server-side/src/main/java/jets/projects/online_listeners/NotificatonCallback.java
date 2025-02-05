package jets.projects.online_listeners;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jets.projects.api.ClientAPI;
import jets.projects.dao.ContactDao;
import jets.projects.dao.NotificationDao;
import jets.projects.dao.UsersDao;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entities.NormalUserStatus;
import jets.projects.shared_ds.OnlineNormalUserInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class NotificatonCallback {
    Map<Integer, OnlineNormalUserInfo> onlineUsers;
    private final ExecutorService executor;
    NotificationDao notificationDao ;
    ContactDao contactDao;
    UsersDao usersDao;

    public NotificatonCallback(NotificationDao notificationDao , ContactDao contactDao) {
        this.notificationDao = notificationDao;
        this.contactDao = contactDao;
        usersDao = new UsersDao();
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
        int onlineCount = (onlineUsers != null) ? onlineUsers.size() : 1;
        executor = Executors.newFixedThreadPool(onlineCount);
    }
    public void userWentOnline(int userID) {
        executor.submit(()->{
            ClientAPI client;
            List<ContactInfo> contacts = contactDao.getAllContacts(userID).getResponseData();
            for(ContactInfo contact : contacts){
                client = onlineUsers.get(contact.getFirstID()).getImpl();
                if(client!=null){
                   try {
                    client.userWentOnline(usersDao.getUserById(userID).getDisplayName()); //notify contacts 
                    } catch (RemoteException e) {
                        System.err.println("Falied to notify contacts: " + e.getMessage());
                    } 
                }
            }
        });
    }
    public void userWentOffline(int userID) {
        executor.submit(()->{
            ClientAPI client;
            List<ContactInfo> contacts = contactDao.getAllContacts(userID).getResponseData();
            for(ContactInfo contact : contacts){
                client = onlineUsers.get(contact.getFirstID()).getImpl();
                if(client!=null){
                   try {
                    client.userWentOffline(usersDao.getUserById(userID).getDisplayName()); //notify contacts 
                    } catch (RemoteException e) {
                        System.err.println("Falied to notify contacts: " + e.getMessage());
                    } 
                }
            }
        });      
    }
    public void userStatusChanged(int userID, NormalUserStatus newStatus) {
        executor.submit(()->{
            ClientAPI client;
            List<ContactInfo> contacts = contactDao.getAllContacts(userID).getResponseData();
            for(ContactInfo contact : contacts){
                client = onlineUsers.get(contact.getFirstID()).getImpl();
                if(client!=null){
                   try {
                    client.userStatusChanged(usersDao.getUserById(userID).getDisplayName(), newStatus); //notify contacts 
                    } catch (RemoteException e) {
                        System.err.println("Falied to notify contacts: " + e.getMessage());
                    } 
                }
            }
        });       
    }
    
}
