package jets.projects.onlinelisteners;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jets.projects.dao.ContactInvitationDao;
import jets.projects.entities.ContactInvitation;
import jets.projects.sharedds.OnlineNormalUserInfo;
import jets.projects.sharedds.OnlineNormalUserTable;

public class ContactInvitationCallback {
    Map<Integer, OnlineNormalUserInfo> onlineUsers;
    private final ExecutorService executor;
    ContactInvitationDao contactInvitationDao;

    public ContactInvitationCallback(ContactInvitationDao contactInvitationDao) {
        this.contactInvitationDao = contactInvitationDao;
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
        executor = Executors.newFixedThreadPool(onlineUsers.size());
    }

    public void contactInvitationReceived(ContactInvitation invitation) {
        executor.submit(()->{});
    }
    
    public void contactInvitationAccepted(ContactInvitation invitation) {
        executor.submit(()->{});        
    }
    
    public void contactInvitationRejected(ContactInvitation invitation) {
        executor.submit(()->{});        
    }
}
