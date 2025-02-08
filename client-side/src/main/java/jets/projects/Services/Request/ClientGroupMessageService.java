package jets.projects.Services.Request;

import jets.projects.Controllers.ClientAlerts;
import jets.projects.Services.ServerConnectivityService;
import jets.projects.api.NormalUserAPI;
import jets.projects.entities.GroupMessage;
import jets.projects.session.ClientToken;

import java.rmi.RemoteException;
import java.util.List;

public class ClientGroupMessageService {
    public List<GroupMessage> getGroupMessages(int groupID){
        if(!ServerConnectivityService.check()){
            ClientAlerts.invokeWarningAlert("Get Group Messages", "Can't connect to server");
            return null;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try{
            return serverAPI.getGroupMessages(myToken, groupID);
        }catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Get Group Messages Error", e.getMessage());
            return null;
        }
    }


    public byte[] getGroupMessageFile(int groupID, int messageID) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Get Group Message File", "Can't connect to server");
            return null;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            return serverAPI.getGroupMessageFile(myToken, groupID, messageID);
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Get Group Message File Error", e.getMessage());
            return null;
        }
    }


    public boolean sendGroupMessage(GroupMessage message) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Send Group Message", "Can't connect to server");
            return false;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            if( serverAPI.sendGroupMessage(myToken, message)){
                return true;
            }
            ClientAlerts.invokeErrorAlert("Send Group Message Error", "Failed to send message");
            return false;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Send Group Message Error", e.getMessage());
            return false;
        }
    }


}
