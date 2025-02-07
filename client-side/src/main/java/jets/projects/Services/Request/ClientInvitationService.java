package jets.projects.Services.Request;

import jets.projects.Controllers.ClientAlerts;
import jets.projects.Services.ServerConnectivityService;
import jets.projects.entities.ContactInvitation;
import jets.projects.entity_info.ContactInvitationInfo;
import jets.projects.session.ClientToken;

import java.rmi.RemoteException;
import java.util.List;

public class ClientInvitationService {
    public List<ContactInvitationInfo> getContactInvitations(){
        if(!ServerConnectivityService.check()){
            ClientAlerts.invokeWarningAlert("Get Contact Invitations", "Can't connect to server");
            return null;
        }
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try{
            return ServerConnectivityService.getServerAPI().getContactInvitations(myToken);
        } catch (Exception e) {
            ClientAlerts.invokeErrorAlert("Get Contact Invitations Error", e.getMessage());
            return null;
        }
    }

    public boolean sendContactInvitation(ContactInvitation invitation) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Send Contact Invitation", "Can't connect to server");
            return false;
        }
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            if(ServerConnectivityService.getServerAPI().sendContactInvitation(myToken, invitation)){
                return true;
            }
            ClientAlerts.invokeErrorAlert("Send Contact Invitation Error", "Failed to send invitation");
            return false;
        } catch (Exception e) {
            ClientAlerts.invokeErrorAlert("Send Contact Invitation Error", e.getMessage());
            return false;
        }
    }

    public boolean acceptContactInvitation(int invitationID) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Accept Contact Invitation", "Can't connect to server");
            return false;
        }
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            if(ServerConnectivityService.getServerAPI().acceptContactInvitation(myToken, invitationID)){
                return true;
            }
            ClientAlerts.invokeErrorAlert("Accept Contact Invitation Error", "Failed to accept invitation");
            return false;
        } catch (Exception e) {
            ClientAlerts.invokeErrorAlert("Accept Contact Invitation Error", e.getMessage());
            return false;
        }
    }

    public boolean rejectContactInvitation(int invitationID) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Reject Contact Invitation", "Can't connect to server");
            return false;
        }
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            if(ServerConnectivityService.getServerAPI().rejectContactInvitation(myToken, invitationID)){
                return true;
            }
            ClientAlerts.invokeErrorAlert("Reject Contact Invitation Error", "Failed to reject invitation");
            return false;
        } catch (Exception e) {
            ClientAlerts.invokeErrorAlert("Reject Contact Invitation Error", e.getMessage());
            return false;
        }
    }
}
