package jets.projects.Services.Request;

import jets.projects.Controllers.ClientAlerts;
import jets.projects.Services.ServerConnectivityService;
import jets.projects.api.NormalUserAPI;
import jets.projects.entities.Announcement;
import jets.projects.entity_info.AnnouncementInfo;
import jets.projects.session.ClientToken;

import java.rmi.RemoteException;
import java.util.List;

public class ClientAnnouncementService{
    public List<AnnouncementInfo> getAllAnnouncements(){
        if(!ServerConnectivityService.check()){
            ClientAlerts.invokeWarningAlert("Get Announcements", "Can't connect to server");
            return null;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try{
            return serverAPI.getAllAnnouncements(myToken);
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Get Announcements Error", e.getMessage());
            return null;
        }
    }
    public boolean markAnnouncementsAsRead() {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Mark Announcements As Read", "Can't connect to server");
            return false;
        }
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            if(ServerConnectivityService.getServerAPI().markAnnouncementsAsRead(myToken)){
                return true;
            }
            ClientAlerts.invokeErrorAlert("Mark Announcements As Read Error", "Failed to mark Announcements as read");
            return false;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Mark Announcements As Read Error", e.getMessage());
            return false;
        }
    }

    public List<Announcement> getUnReadAnnouncements(){
        if(!ServerConnectivityService.check()){
            ClientAlerts.invokeWarningAlert("Get Unread Announcements", "Can't connect to server");
            return null;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try{
            return serverAPI.getUnReadAnnouncements(myToken);
        } catch (RemoteException e){
            ClientAlerts.invokeErrorAlert("Get Unread Announcements Error", e.getMessage());
            return null;
        }
    }


}
