package jets.projects.Services.CallBack;

import datastore.DataCenter;
import javafx.application.Platform;
import jets.projects.entities.Announcement;
import jets.projects.entities.Notification;
import jets.projects.entity_info.AnnouncementInfo;

public class CallBackNotificationService {

    DataCenter dataCenter = DataCenter.getInstance();

    /*
    public void userStatusChangedNotification(Notification notification){
        Platform.runLater(()->{
           dataCenter.getNotificationList().add(notification);
        });
    }

    public void contactInvitationNotification(Notification notification){
        Platform.runLater(()->{
            dataCenter.getNotificationList().add(notification);
        });
    }*/
    public void newNotification(Notification notification){
        Platform.runLater(()->{
            dataCenter.getNotificationList().add(notification);
        });
    }

    public void newAnnouncementAdded(Announcement announcement){
        AnnouncementInfo announcementInfo = new AnnouncementInfo();
        announcementInfo.setAnnouncement(announcement);
        announcementInfo.setUserID(dataCenter.getMyProfile().getUserID());
        announcementInfo.setIsRead(false);
        Platform.runLater(()->{
            dataCenter.getAnnouncementList().add(announcementInfo);
        });
    }
}
