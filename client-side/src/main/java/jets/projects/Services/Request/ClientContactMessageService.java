package jets.projects.Services.Request;

import javafx.application.Platform;
import jets.projects.Controllers.ClientAlerts;
import jets.projects.Services.ServerConnectivityService;
import jets.projects.api.NormalUserAPI;
import jets.projects.entities.ContactMessage;
import jets.projects.session.ClientToken;

import java.rmi.RemoteException;
import java.util.List;

public class ClientContactMessageService {
    public List<ContactMessage> getAllContactMessages(int contactID){
        if(!ServerConnectivityService.check()){
            ClientAlerts.invokeWarningAlert("Get Contact Messages", "Can't connect to server");
            return null;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try{
            return serverAPI.getAllContactMessages(myToken, contactID);
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Get Contact Messages Error", e.getMessage());
            return null;
        }
    }

    public byte[] getContactMessageFile(int contactID, int messageID){
        if(!ServerConnectivityService.check()){
            Platform.runLater(()->{
                ClientAlerts.invokeWarningAlert("Get Contact Message File", "Can't connect to server");
            });
            return null;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try{
            return serverAPI.getContactMessageFile(myToken, contactID, messageID);
        } catch (RemoteException e) {
            Platform.runLater(()->{
                ClientAlerts.invokeErrorAlert("Get Contact Message File Error", e.getMessage());
            });

            return null;
        }
    }
    public List<ContactMessage> getUnReadContactMessages(int contactID){
        if(!ServerConnectivityService.check()){
            ClientAlerts.invokeWarningAlert("Get Unread Contact Messages", "Can't connect to server");
            return null;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try{
            return serverAPI.getUnReadContactMessages(myToken, contactID);
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Get Unread Contact Messages Error", e.getMessage());
            return null;
        }
    }

    public boolean sendContactMessage(ContactMessage message) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Send Contact Message", "Can't connect to server");
            return false;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            if (serverAPI.sendContactMessage(myToken, message)) {
                return true;
            }
            ClientAlerts.invokeErrorAlert("Send Contact Message Error", "Failed to send message");
            return false;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Send Contact Message Error", e.getMessage());
            return false;
        }
    }

    public boolean markContactMessagesAsRead(int contactID) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Mark Messages As Read", "Can't connect to server");
            return false;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            if (serverAPI.markContactMessagesAsRead(myToken, contactID)) {
                return true;
            }
            ClientAlerts.invokeErrorAlert("Mark Messages As Read Error", "Failed to mark messages as read");
            return false;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Mark Messages As Read Error", e.getMessage());
            return false;
        }
    }


    
    
}
