package jets.projects.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jets.projects.api.AdminAPI;
import jets.projects.entities.Announcement;
import jets.projects.entities.Country;
import jets.projects.entities.NormalUser;
import jets.projects.session.AdminSessionData;
import jets.projects.session.AdminToken;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;


public class Director{


    public Director(Stage stage){
        this.stage = stage;
        try{
            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/FXML/adminLogin.fxml"));
            adminLoginParent = loginLoader.load();
            adminloginController = loginLoader.getController();
            adminLoginScene = new Scene(adminLoginParent);
        }catch (IOException e){
            System.out.println("Error in loading the login page");
            System.out.println(e.getMessage());
            System.exit(1);
        }try{
            FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("/FXML/adminHome.fxml"));
            adminHomeParent = homeLoader.load();
            adminHomeController = homeLoader.getController();
            adminHomeScene = new Scene(adminHomeParent);
        }catch (IOException e){
            System.out.println("Error in loading the home page");
            System.out.println(e.getMessage());
            System.exit(1);
        }
        try{
            FXMLLoader overViewLoader = new FXMLLoader(getClass().getResource("/FXML/adminOverview.fxml"));
            overViewParent = overViewLoader.load();
            adminOverViewController = overViewLoader.getController();
        }catch (IOException e){
            System.out.println("Error in loading the overview page");
            System.out.println(e.getMessage());
            System.exit(1);
        }
        try{
            FXMLLoader statisticsLoader = new FXMLLoader(getClass().getResource("/FXML/adminStatistics.fxml"));
            statisticsParent = statisticsLoader.load();
            adminStatisticsController = statisticsLoader.getController();
        }catch (IOException e){
            System.out.println("Error in loading the statistics page");
            System.out.println(e.getMessage());
            System.exit(1);
        }
        try{
            FXMLLoader announcementLoader = new FXMLLoader(getClass().getResource("/FXML/adminAnnouncement.fxml"));
            announcementParent = announcementLoader.load();
            adminAnnouncementController = announcementLoader.getController();
        }catch (IOException e){
            System.out.println("Error in loading the announcement page");
            System.out.println(e.getMessage());
            System.exit(1);
        }
        try{
            FXMLLoader viewUserDataLoader = new FXMLLoader(getClass().getResource("/FXML/viewUserData.fxml"));
            viewUserDataParent = viewUserDataLoader.load();
            viewUserDataController = viewUserDataLoader.getController();
            viewUserDataScene = new Scene(viewUserDataParent);
        }catch (IOException e){
            System.out.println("Error in loading the View user data page");
            System.out.println(e.getMessage());
            System.exit(1);
        }
        try{
            FXMLLoader addNewUserLoader = new FXMLLoader(getClass().getResource("/FXML/addNewUser.fxml"));
            addNewUserParent = addNewUserLoader.load();
            addNewUserController = addNewUserLoader.getController();
            addNewUserScene = new Scene(addNewUserParent);
        }catch (IOException e){
            System.out.println("Error in loading the add new user page");
            System.out.println(e.getMessage());
            System.exit(1);
        }
        try{
            FXMLLoader deleteUserLoader = new FXMLLoader(getClass().getResource("/FXML/deleteUser.fxml"));
            deleteUserPatent = deleteUserLoader.load();
            deleteUserController = deleteUserLoader.getController();
            deleteUserScene = new Scene(deleteUserPatent);
        }catch (IOException e){
            System.out.println("Error in loading the delete user page");
        }
    }
    
    public void startWorking() {
        adminloginController.setStageDirector(stage, this);
        adminHomeController.setStageDirector(stage, this);
        adminAnnouncementController.setStageDirector(stage, this);
        adminOverViewController.setStageDirector(stage, this);
        adminStatisticsController.setStageDirector(stage, this);
        viewUserDataController.setStageDirector(stage, this);
        addNewUserController.setStageDirector(stage, this);
        deleteUserController.setStageDirector(stage, this);

        stage.setScene(adminLoginScene);
        stage.show();
    }
    
    public void login(int userID, String password) {
        if(connectToServer()){
            try{
                adminSessionData = adminAPI.login(userID, password);
                if(adminSessionData != null){
                    adminToken = new AdminToken(adminSessionData.getUserID());
                    checkUserServiceStatus();
                    stage.setScene(adminHomeScene);
                }else{
                    AdminAlerts.invokeWarningAlert("Login Failed", "This Number is not registered as Admin");
                }
            } catch (RemoteException e) {
                AdminAlerts.invokeErrorAlert("Login Failed", e.getMessage());
            }
        }

        //remove it
        stage.setScene(adminHomeScene);
    }

    private boolean connectToServer() {
        if( adminAPI == null){
            try {
                Registry reg = LocateRegistry.getRegistry("127.0.0.1", 1099);
                adminAPI = (AdminAPI) reg.lookup("ADMIN_SERVICE_NAME");
                return true;
            } catch (RemoteException | NotBoundException e) {
                AdminAlerts.invokeWarningAlert("Server Warning", "Server is not running " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    public void viewAnnouncementPage() {
        AnchorPane anchorPane = adminHomeController.getAnchorPane();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(announcementParent);
        AnchorPane.setTopAnchor(announcementParent, 0.0);
        AnchorPane.setBottomAnchor(announcementParent, 0.0);
        AnchorPane.setLeftAnchor(announcementParent, 0.0);
        AnchorPane.setRightAnchor(announcementParent, 0.0);
        if(adminToken == null || adminAPI == null){
            AdminAlerts.invokeErrorAlert("Error", "view announcement failed");
            return;
        }
        try{
            List<Announcement> announcements = adminAPI.getAllAnnouncements(adminToken);
            for(Announcement announcement : announcements){
                adminAnnouncementController.addAnnouncement(announcement);
            }
        } catch (RemoteException e) {
            AdminAlerts.invokeErrorAlert("Error", e.getMessage());
        }
    }

    public void viewStatisticsPage() {
        AnchorPane anchorPane = adminHomeController.getAnchorPane();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(statisticsParent);
        AnchorPane.setTopAnchor(statisticsParent, 0.0);
        AnchorPane.setBottomAnchor(statisticsParent, 0.0);
        AnchorPane.setLeftAnchor(statisticsParent, 0.0);
        AnchorPane.setRightAnchor(statisticsParent, 0.0);
    }

    public void viewOverviewPage() {
        userServiceStatus = checkUserServiceStatus();
        if(userServiceStatus == false){
            adminOverViewController.getStopServiceButton().setSelected(true);
            adminOverViewController.getStartServiceButton().setSelected(false);
            adminOverViewController.getStopServiceButton().setDisable(true);
            adminOverViewController.getStartServiceButton().setDisable(false);
            adminOverViewController.getStatusServiceText().setText("User service is not running");
            adminOverViewController.getStatusServiceIcon().setImage(new Image(getClass().getResource("/Assets/Dots/red_dot.png").toExternalForm()));
        }else{
            adminOverViewController.getStopServiceButton().setDisable(false);
            adminOverViewController.getStopServiceButton().setSelected(false);
            adminOverViewController.getStartServiceButton().setDisable(true);
            adminOverViewController.getStartServiceButton().setSelected(true);
            adminOverViewController.getStatusServiceText().setText("User service is running");
            adminOverViewController.getStatusServiceIcon().setImage(new Image(getClass().getResource("/Assets/Dots/green1_dot.png").toExternalForm()));
        }
        AnchorPane anchorPane = adminHomeController.getAnchorPane();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(overViewParent);
        AnchorPane.setTopAnchor(overViewParent, 0.0);
        AnchorPane.setBottomAnchor(overViewParent, 0.0);
        AnchorPane.setLeftAnchor(overViewParent, 0.0);
        AnchorPane.setRightAnchor(overViewParent, 0.0);
    }

    public void viewUserDataWindow() {
        Stage newstage = new Stage();
        newstage.setTitle("User Data View");
        newstage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
        newstage.initOwner(adminOverViewController.getViewUserButton().getScene().getWindow()); // Set the main window as owner

        newstage.setScene(viewUserDataScene);
        newstage.setResizable(false);
        newstage.showAndWait(); // Show and wait for the dialog to close
    }

    public void viewAddNewUserWindow() {
        Stage newstage = new Stage();
        newstage.setTitle("Add New User Window");
        newstage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
        newstage.initOwner(adminOverViewController.getAddNewUserButton().getScene().getWindow()); // Set the main window as owner

        newstage.setScene(addNewUserScene);
        newstage.setResizable(false);
        newstage.showAndWait(); // Show and wait for the dialog to close
    }

    public void viewDeleteUserWindow(){
        Stage newstage = new Stage();
        newstage.setTitle("Delete User Window");
        newstage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
        newstage.initOwner(adminOverViewController.getDeleteUserButton().getScene().getWindow()); // Set the main window as owner
        newstage.setScene(deleteUserScene);
        newstage.setResizable(false);
        newstage.showAndWait(); // Show and wait for the dialog to close
    }

    
    public void logOut() {
        if(adminToken == null || adminAPI == null){
            AdminAlerts.invokeErrorAlert("Logout Failed", "Logout failed");
            return;
        }
        try{
            if(adminAPI.logout(adminToken)){
                adminAPI = null;
                adminSessionData = null;
                adminToken = null;
                stage.setScene(adminLoginScene);
            }else{
                AdminAlerts.invokeErrorAlert("Logout Failed", "Logout failed");
            }
        } catch (RemoteException e) {
            AdminAlerts.invokeErrorAlert("Logout Failed", e.getMessage());
        }


    }

    public boolean checkUserServiceStatus() {
        if(adminToken == null || adminAPI == null){
            AdminAlerts.invokeErrorAlert("Error", "get user service status failed");
            return false;
        }
        try{
            if(adminAPI.getNormalUserServiceStatus(adminToken)){
                userServiceStatus = true;
                return true;
            }else{
                userServiceStatus = false;
                return false;
            }
        } catch (RemoteException e) {
            AdminAlerts.invokeErrorAlert("Error", e.getMessage());
            return false;
        }
    }
    
    public List<Integer> getOnlineOfflineStats(){
        if(adminToken == null || adminAPI == null){
            AdminAlerts.invokeErrorAlert("Error", "get online offline stats failed");
            return null;
        }
        try{
            return adminAPI.getOnlineOfflineStats(adminToken);
        } catch (RemoteException e) {
            AdminAlerts.invokeErrorAlert("Error", e.getMessage());
            return null;
        }
    }

    public List<Integer> getMaleFemaleStats(){
        if(adminToken == null || adminAPI == null){
            AdminAlerts.invokeErrorAlert("Error", "get male female stats failed");
            return null;
        }
        try{
            return adminAPI.getMaleFemaleStats(adminToken);
        } catch (RemoteException e) {
            AdminAlerts.invokeErrorAlert("Error", e.getMessage());
            return null;
        }
    }

    public Map<Country,Integer> getTopCountries(){
        if(adminToken == null || adminAPI == null){
            AdminAlerts.invokeErrorAlert("Error", "get top countries failed");
            return null;
        }
        try{
            return adminAPI.getTopCountries(adminToken);
        } catch (RemoteException e) {
            AdminAlerts.invokeErrorAlert("Error", e.getMessage());
            return null;
        }
    }

    public void addNewAnnouncement(Announcement newAnnouncement) {
        if(newAnnouncement == null || adminToken == null || adminAPI == null){
            AdminAlerts.invokeErrorAlert("Error", "add new announcement failed");
            return;
        }
        try{
            if(adminAPI.submitNewAnnouncement(adminToken, newAnnouncement)){
                AdminAlerts.invokeInformationAlert("New announcement", " announcement submitted successfully");
                adminAnnouncementController.addAnnouncement(newAnnouncement);
            }else{
                AdminAlerts.invokeErrorAlert("Error", "announcement not submitted");
            }
        } catch (RemoteException e) {
            AdminAlerts.invokeErrorAlert("Error", e.getMessage());
        }

    }
    
    public NormalUser getUserData(String phoneNumber) {
        if(adminToken == null || adminAPI == null){
            AdminAlerts.invokeErrorAlert("Error", "view user data failed");
            return null;
        }
        try{
            NormalUser normalUser = adminAPI.getNormalUserByPhoneNumber(adminToken, phoneNumber);
            if(normalUser != null){
                return normalUser;
            }else{
                AdminAlerts.invokeInformationAlert("Information", "user not found");
            }
        } catch (RemoteException e) {
            AdminAlerts.invokeErrorAlert("Error", e.getMessage());
        }
        return null;
    }
    
    public void createAccount(NormalUser newUser) {
        if(newUser == null || adminToken == null || adminAPI == null){
            AdminAlerts.invokeErrorAlert("Error", "add new user failed");
            return;
        }
        try{
            if(adminAPI.addNormalUser(adminToken, newUser)){
                AdminAlerts.invokeInformationAlert("Add new user", " user added successfully");
            }else{
                AdminAlerts.invokeErrorAlert("Error", "user not added");
                return;
            }
        }catch (RemoteException e){
            AdminAlerts.invokeErrorAlert("Error", e.getMessage());
        }
    }


    public void deleteAccount(NormalUser deletedUser) {
        if(deletedUser == null || adminAPI == null || adminToken == null){
            AdminAlerts.invokeErrorAlert("Error", "delete user failed");
            return;
        }
        try{
            if(adminAPI.deleteNormalUser(adminToken, deletedUser.getUserID())){
                AdminAlerts.invokeInformationAlert("Delete user", " user deleted successfully");
            }else{
                AdminAlerts.invokeErrorAlert("Error", "user not found");
            }
        }catch (RemoteException e){
            AdminAlerts.invokeErrorAlert("Error", e.getMessage());
        }
    }

    public void startUserService() {
        if(adminToken == null || adminAPI == null){
            AdminAlerts.invokeErrorAlert("Error", "start user service failed");
            return;
        }
        try{
            if(adminAPI.startNormalUserService(adminToken)){
                userServiceStatus = true;
                AdminAlerts.invokeInformationAlert("Start user service", " user service started successfully");
            }else{
                userServiceStatus = false;
                AdminAlerts.invokeErrorAlert("Start user service", " user service failed to start");
            }
        } catch (RemoteException e) {
            AdminAlerts.invokeErrorAlert("Start user service", e.getMessage());
            userServiceStatus = false;
        }
        if(userServiceStatus == false && adminOverViewController.getStopServiceButton().isSelected() == false){
            adminOverViewController.getStopServiceButton().setSelected(true);
            adminOverViewController.getStartServiceButton().setSelected(false);
            adminOverViewController.getStopServiceButton().setDisable(true);
            adminOverViewController.getStartServiceButton().setDisable(false);
            adminOverViewController.getStatusServiceText().setText("User service is not running");
            adminOverViewController.getStatusServiceIcon().setImage(new Image(getClass().getResource("/Assets/Dots/red_dot.png").toExternalForm()));
        }else if(userServiceStatus == true && adminOverViewController.getStartServiceButton().isSelected() == false){
            adminOverViewController.getStopServiceButton().setDisable(false);
            adminOverViewController.getStopServiceButton().setSelected(false);
            adminOverViewController.getStartServiceButton().setDisable(true);
            adminOverViewController.getStartServiceButton().setSelected(true);
            adminOverViewController.getStatusServiceText().setText("User service is running");
            adminOverViewController.getStatusServiceIcon().setImage(new Image(getClass().getResource("/Assets/Dots/green1_dot.png").toExternalForm()));
        }
    }

    public void stopUserService(){
        if(adminToken == null || adminAPI == null){
            AdminAlerts.invokeErrorAlert("Error", "start user service failed");
            return;
        }
        try{
            if(adminAPI.stopNormalUserService(adminToken)){
                userServiceStatus = false;
                AdminAlerts.invokeInformationAlert("Stop user service", " user service stopped successfully");
            }else{
                userServiceStatus = false;
                AdminAlerts.invokeErrorAlert("Stop user service", " user service failed to stop");
            }
        } catch (RemoteException e) {
            AdminAlerts.invokeErrorAlert("stop user service", e.getMessage());
            userServiceStatus = false;
        }
        if(userServiceStatus == false && adminOverViewController.getStopServiceButton().isSelected() == false){
            adminOverViewController.getStopServiceButton().setSelected(true);
            adminOverViewController.getStartServiceButton().setSelected(false);
            adminOverViewController.getStopServiceButton().setDisable(true);
            adminOverViewController.getStartServiceButton().setDisable(false);
            adminOverViewController.getStatusServiceText().setText("User service is not running");
            adminOverViewController.getStatusServiceIcon().setImage(new Image(getClass().getResource("/Assets/Dots/red_dot.png").toExternalForm()));
        }else if(userServiceStatus == true && adminOverViewController.getStartServiceButton().isSelected() == false){
            adminOverViewController.getStopServiceButton().setDisable(false);
            adminOverViewController.getStopServiceButton().setSelected(false);
            adminOverViewController.getStartServiceButton().setDisable(true);
            adminOverViewController.getStartServiceButton().setSelected(true);
            adminOverViewController.getStatusServiceText().setText("User service is running");
            adminOverViewController.getStatusServiceIcon().setImage(new Image(getClass().getResource("/Assets/Dots/green1_dot.png").toExternalForm()));
        }
    }

    private AdminAPI adminAPI = null;
    private AdminSessionData adminSessionData = null;
    private AdminToken adminToken = null;
    private boolean userServiceStatus;


    private final Stage stage;

    private Parent adminLoginParent;
    private AdminLoginController adminloginController;
    private Scene adminLoginScene;

    private Parent adminHomeParent;
    private AdminHomeController adminHomeController;
    private Scene adminHomeScene;

    private Parent overViewParent;
    private AdminOverviewController adminOverViewController;

    private Parent viewUserDataParent;
    private ViewUserDataController viewUserDataController;
    private Scene viewUserDataScene;

    private Parent addNewUserParent;
    private AddNewUserController addNewUserController;
    private Scene addNewUserScene;


    private Parent deleteUserPatent;
    private DeleteUserController deleteUserController;
    private Scene deleteUserScene;

    private Parent statisticsParent;
    private AdminStatisticsController adminStatisticsController;

    private Parent announcementParent;
    private AdminAnnouncementController adminAnnouncementController;


}
