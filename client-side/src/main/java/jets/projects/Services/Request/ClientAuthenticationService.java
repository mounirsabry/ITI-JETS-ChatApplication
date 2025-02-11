package jets.projects.Services.Request;

import javafx.application.Platform;
import jets.projects.Classes.ExceptionMessages;
import jets.projects.Controllers.ClientAlerts;
import jets.projects.ServiceManager;
import jets.projects.Services.ServerConnectivityService;
import jets.projects.api.NormalUserAPI;
import jets.projects.entities.NormalUser;
import jets.projects.session.ClientSessionData;
import jets.projects.session.ClientToken;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The ClientAuthenticationService class provides functionality for user authentication,
 * including login, registration, and logout processes.
 * login with admin created account need to be handled*
 */
public class ClientAuthenticationService {

    public boolean login(String phoneNumber, String password) {
         ServiceManager serviceManager = ServiceManager.getInstance();
         if(serviceManager == null){
             ClientAlerts.invokeWarningAlert("Server Warning", "Can't connect to server");
             return false;
         }
         NormalUserAPI normalUserAPI = serviceManager.getNormalUserAPI();
         if(normalUserAPI == null){
             ClientAlerts.invokeWarningAlert("Server Warning", "Can't connect to server");
             return false;
         }
         try{
             ClientSessionData mySession = normalUserAPI.login(phoneNumber, password, serviceManager.getMyClientAPIImpl());
             if(mySession == null){
                 ClientAlerts.invokeWarningAlert("Server Warning", "User not found");
                 return false;
             }
             serviceManager.setClientSessionData(mySession);
             ServerConnectivityService.getExecutorService().submit(() -> {
                 while (!Thread.currentThread().isInterrupted()) {
                     try {
                         ServerConnectivityService.getServerAPI().sendPulse(ServerConnectivityService.getMyToken());
                         Thread.sleep(500);
                     } catch (InterruptedException | RemoteException e) {
                         Platform.runLater(()->{
                             ClientAlerts.invokeWarningAlert("Server Warning", e.getMessage());
                         });
                         Thread.currentThread().interrupt();
                     }
                 }
             });

             return true;
         } catch (RemoteException e) {
             if(e.getMessage().equals((ExceptionMessages.USER_MUST_CHANGE_PASSWORD_FOR_FIRST_LOGIN))){
                 ClientAlerts.invokeWarningAlert("Password Change Required", "You must change your password before logging in for the first time.");
             }
             ClientAlerts.invokeWarningAlert("Server Warning", e.getMessage());
             return false;
         }
    }

    public boolean register(NormalUser newAccount) {
        ServiceManager serviceManager = ServiceManager.getInstance();
        if(serviceManager == null){
            ClientAlerts.invokeWarningAlert("Server Warning", "Can't connect to server");
            return false;
        }
        NormalUserAPI normalUserAPI = serviceManager.getNormalUserAPI();
        if(normalUserAPI == null){
            ClientAlerts.invokeWarningAlert("Server Warning", "Can't connect to server");
            return false;
        }
        try{
            if(normalUserAPI.register(newAccount)){
                ClientAlerts.invokeInformationAlert("Account creation", "Account created successfully, Welcome!");
                return true;
            }
            ClientAlerts.invokeWarningAlert("Account creation", "Account creation failed, please try again");
            return false;
        } catch (RemoteException e) {
            ClientAlerts.invokeWarningAlert("Server Warning", e.getMessage());
            return false;
        }
    }
    public boolean logout() {
        ServiceManager serviceManager = ServiceManager.getInstance();
        if(serviceManager == null){
            ClientAlerts.invokeWarningAlert("Server Warning", "Can't connect to server");
            return false;
        }
        NormalUserAPI normalUserAPI = serviceManager.getNormalUserAPI();
        ClientToken clientToken = serviceManager.getClientToken();
        if(normalUserAPI == null || clientToken == null){
            ClientAlerts.invokeErrorAlert("Server Warning", "Can't connect to server");
            return false;
        }
        try{
            if(normalUserAPI.logout(clientToken)){
                ServerConnectivityService.getExecutorService().shutdownNow();
                return true;
            }else{
                ClientAlerts.invokeWarningAlert("Server Warning", "Logout failed");
                return false;
            }
        } catch (RemoteException e) {
            ClientAlerts.invokeWarningAlert("Server Warning", e.getMessage());
            return false;
        }
    }
}
