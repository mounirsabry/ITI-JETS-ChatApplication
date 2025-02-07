package jets.projects;

import jets.projects.online_listeners.*;

class ExecutorsManager {
    private final OnlineCleaner onlineCleaner;
    private final ContactMessageCallback contactMessageCallback;
    private final ContactInvitationCallback contactInvitationCallback;
    private final GroupCallback groupCallback;
    private final GroupMessageCallback groupMessageCallback;
    private final NotificationCallback notificationCallback;
    private final AnnouncementCallback announcementCallback;
    
    private boolean isRunning = false;
    
    ExecutorsManager() {
        onlineCleaner = new OnlineCleaner();
        contactMessageCallback = new ContactMessageCallback();
        contactInvitationCallback = new ContactInvitationCallback();
        groupCallback = new GroupCallback();
        groupMessageCallback = new GroupMessageCallback();
        notificationCallback = new NotificationCallback();
        announcementCallback = new AnnouncementCallback();
    }
    
    void startExecutors() {
        if (isRunning) {
            return;
        }
        
        onlineCleaner.start();
        contactMessageCallback.start();
        contactInvitationCallback.start();
        groupCallback.start();
        groupMessageCallback.start();
        notificationCallback.start();
        announcementCallback.start();
        
        isRunning = true;
    }
    
    void stopExecutors() {
        if (!isRunning) {
            return;
        }
        
        onlineCleaner.shutDown();
        contactMessageCallback.shutDown();
        contactInvitationCallback.shutDown();
        groupCallback.shutDown();
        groupMessageCallback.shutDown();
        notificationCallback.shutDown();
        announcementCallback.shutDown();
        
        isRunning = false;
    }
}
