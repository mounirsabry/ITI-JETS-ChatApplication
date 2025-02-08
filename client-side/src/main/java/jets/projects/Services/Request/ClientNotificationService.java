package jets.projects.Services.Request;

import jets.projects.Controllers.ClientAlerts;
import jets.projects.Services.ServerConnectivityService;
import jets.projects.entities.Notification;
import jets.projects.session.ClientToken;

import java.rmi.RemoteException;
import java.util.List;

public class ClientNotificationService {
    public List<Notification> getNotifications(){
        if(!ServerConnectivityService.check()){
            ClientAlerts.invokeWarningAlert("Get Notifications", "Can't connect to server");
            return null;
        }
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try{
            return ServerConnectivityService.getServerAPI().getNotifications(myToken);
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Get Notifications Error", e.getMessage());
            return null;
        }
    }

    public List<Notification> getUnReadNotifications() {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Get Unread Notifications", "Can't connect to server");
            return null;
        }
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            return ServerConnectivityService.getServerAPI().getUnReadNotifications(myToken);
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Get Unread Notifications Error", e.getMessage());
            return null;
        }
    }

    public boolean markNotificationsAsRead() {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Mark Notifications As Read", "Can't connect to server");
            return false;
        }
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            if(ServerConnectivityService.getServerAPI().markNotificationsAsRead(myToken)){
                return true;
            }
            ClientAlerts.invokeErrorAlert("Mark Notifications As Read Error", "Failed to mark notifications as read");
            return false;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Mark Notifications As Read Error", e.getMessage());
            return false;
        }
    }

    public boolean deleteNotification(int notificationID) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Delete Notification", "Can't connect to server");
            return false;
        }
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            if(ServerConnectivityService.getServerAPI().deleteNotification(myToken, notificationID)){
                return true;
            }
            ClientAlerts.invokeErrorAlert("Delete Notification Error", "Failed to delete notification");
            return false;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Delete Notification Error", e.getMessage());
            return false;
        }
    }


}
