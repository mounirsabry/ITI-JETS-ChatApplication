package jets.projects.Services.Request;

import jets.projects.Controllers.ClientAlerts;
import jets.projects.Services.ServerConnectivityService;
import jets.projects.api.NormalUserAPI;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;
import jets.projects.session.ClientToken;

import java.rmi.RemoteException;
import java.util.Date;


/**
 * Provides functionality to manage and interact with the client profile on the server.
 * This includes retrieving profile details, editing profile data, changing passwords,
 * managing profile pictures, and updating online statuses.
 */
public class ClientProfileService {

    public NormalUser getMyProfile(){
        if(!ServerConnectivityService.check()){
            ClientAlerts.invokeWarningAlert("Get My Profile", "Can't connect to server");
            return null;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try{
            NormalUser myProfile = serverAPI.getMyProfile(myToken);
            if(myProfile == null){
                ClientAlerts.invokeErrorAlert("Profile Loading Error", "Non expecting error");
            }
            return myProfile;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Profile Loading Error: from get my profile", e.getMessage());
            return null;
        }
    }

    public boolean editProfile(String displayName,
                               Date birthDate,
                               String bio, byte[] profilePic){
        if(!ServerConnectivityService.check()){
            ClientAlerts.invokeWarningAlert("Edit My Profile", "Can't connect to server");
            return false;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try{
            if(serverAPI.editProfile(myToken, displayName, birthDate, bio, profilePic)){
                ClientAlerts.invokeInformationAlert("Edit Profile", "Profile edited successfully");
                return true;
            }
            ClientAlerts.invokeErrorAlert("Profile Editing Error", "Non expecting error");
            return false;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Profile Editing Error", e.getMessage());
            return false;
        }
    }
    public boolean changePassword(String oldPassword, String newPassword){
        if(!ServerConnectivityService.check()){
            ClientAlerts.invokeWarningAlert("Edit My Profile", "Can't connect to server");
            return false;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try{
            if(serverAPI.changePassword(myToken, oldPassword, newPassword)){
                ClientAlerts.invokeInformationAlert("Password Editing", "Password changed successfully");
                return true;
            }
            ClientAlerts.invokeErrorAlert("Password Editing Error", "Non expecting error");
            return false;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Password Editing Error", e.getMessage());
            return false;
        }
    }
    
    public byte[] getMyProfilePic(){
        if(!ServerConnectivityService.check()){
            ClientAlerts.invokeWarningAlert("Get My Profile Pic", "Can't connect to server");
            return null;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try{
            return serverAPI.getMyProfilePic(myToken);
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Profile Pic Loading Error", e.getMessage());
            return null;
        }
    }

    public boolean setOnlineStatusABoolean(NormalUserStatus onlineStatus) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Set Online Status", "Can't connect to server");
            return false;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            if (serverAPI.setOnlineStatus(myToken, onlineStatus)) {
                ClientAlerts.invokeInformationAlert("Set Online Status", "Status updated successfully");
                return true;
            }
            ClientAlerts.invokeErrorAlert("Set Online Status Error", "Non expecting error occurred");
            return false;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Set Online Status Error", e.getMessage());
            return false;
        }
    }

}
