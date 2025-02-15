package jets.projects.Services.CallBack;

import datastore.DataCenter;
import javafx.application.Platform;
import jets.projects.Controllers.HomeScreenController;
import jets.projects.entities.Announcement;
import jets.projects.entities.Notification;
import jets.projects.entities.NotificationType;
import jets.projects.entity_info.AnnouncementInfo;

public class CallBackNotificationService {

    DataCenter dataCenter = DataCenter.getInstance();
    public static HomeScreenController homeScreenController;
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
            PopUpNotification.showNotification(notification.getContent());
            dataCenter.getNotificationList().addFirst(notification);
            if(notification.getType() == NotificationType.USER_STATUS){
                homeScreenController.updateStatus();
            }
        });
    }

    public void newAnnouncementAdded(Announcement announcement){
        AnnouncementInfo announcementInfo = new AnnouncementInfo();
        announcementInfo.setAnnouncement(announcement);
        announcementInfo.setUserID(dataCenter.getMyProfile().getUserID());
        announcementInfo.setIsRead(false);
        Platform.runLater(()->{
            PopUpNotification.showNotification(announcement.getContent());
            dataCenter.getAnnouncementList().addFirst(announcementInfo);
        });
    }
}
