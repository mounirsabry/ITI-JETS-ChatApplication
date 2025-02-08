package jets.projects.Services.Request;

import jets.projects.Controllers.ClientAlerts;
import jets.projects.Services.ServerConnectivityService;
import jets.projects.api.NormalUserAPI;
import jets.projects.entities.ContactMessage;
import jets.projects.entities.NormalUser;
import jets.projects.entity_info.ContactInfo;
import jets.projects.session.ClientToken;

import java.rmi.RemoteException;
import java.util.List;

public class ClientContactService {
    public List<ContactInfo> getContacts(){
        if(!ServerConnectivityService.check()){
            ClientAlerts.invokeWarningAlert("Get Contacts", "Can't connect to server");
            return null;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try{
            return serverAPI.getContacts(myToken);
        } catch (Exception e) {
            ClientAlerts.invokeErrorAlert("Get Contacts Error", e.getMessage());
            return null;
        }
    }
    

    public NormalUser getContactProfile(int contactId) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Get Contact", "Can't connect to server");
            return null;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            NormalUser contact = serverAPI.getContactProfile(myToken, contactId);
            if (contact == null) {
                ClientAlerts.invokeErrorAlert("Get Contact", "Non Expecting Error");
            }
            return contact;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Get Contact Error", e.getMessage());
            return null;
        }
    }




}
