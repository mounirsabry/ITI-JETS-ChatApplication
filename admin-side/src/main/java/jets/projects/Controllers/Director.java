package jets.projects.Controllers;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Director {
    private final Stage stage;
    
    private final Parent adminLoginParent;
    private final AdminLoginController adminLoginController;
    private Scene adminLoginScene;
    
    private final Parent adminHomePageParent;
    private final AdminHomePageController adminHomePageController;
    private Scene adminHomePageScene;
    
    private final Parent adminViewStatsParent;
    private final AdminViewStatsController adminViewStatsController;
    private Scene adminViewStatsScene;
    
    private final Parent adminViewAnnouncementsParent;
    private final AdminViewAnnouncementsController adminViewAnnouncementsController;
    private Scene adminViewAnnouncementsScene;
    
    private final Parent adminAddAnnouncementParent;
    private final AdminAddAnnouncementController adminAddAnnouncementController;
    private Scene adminAddAnnouncementScene;
    
    public Director(Stage stage,
            Parent adminLoginParent,
            AdminLoginController adminLoginController,
            Parent adminHomePageParent,
            AdminHomePageController adminHomePageController,
            Parent adminViewStatsParent,
            AdminViewStatsController adminViewStatsController,
            Parent adminViewAnnouncementsParent, 
            AdminViewAnnouncementsController adminViewAnnouncementsController,
            Parent adminAddAnnouncementParent,
            AdminAddAnnouncementController adminAddAnnouncementController) {
        this.stage = stage;
        
        this.adminLoginParent = adminLoginParent;
        this.adminLoginController = adminLoginController;
        
        this.adminHomePageParent = adminHomePageParent;
        this.adminHomePageController = adminHomePageController;
        
        this.adminViewStatsParent = adminViewStatsParent;
        this.adminViewStatsController = adminViewStatsController;
        
        this.adminViewAnnouncementsParent = adminViewAnnouncementsParent;
        this.adminViewAnnouncementsController = adminViewAnnouncementsController;
        
        this.adminAddAnnouncementParent = adminAddAnnouncementParent;
        this.adminAddAnnouncementController = adminAddAnnouncementController;
    }
    
    public void startWorking() {
        adminLoginScene = new Scene(adminLoginParent);
        adminLoginController.setDirector(stage, this);
        
        adminHomePageScene = new Scene(adminHomePageParent);
        adminHomePageController.setDirector(stage, this);
        
        adminViewStatsScene = new Scene(adminViewStatsParent);
        adminViewStatsController.setDirector(stage, this);
        
        adminViewAnnouncementsScene = new Scene(adminViewAnnouncementsParent);
        adminViewAnnouncementsController.setDirector(stage, this);
        
        adminAddAnnouncementScene = new Scene(adminAddAnnouncementParent);
        adminAddAnnouncementController.setDirector(stage, this);
        
        adminLoginController.perform();
        stage.setScene(adminLoginScene);
        stage.show();
    }
    
    public void login() {
        adminHomePageController.perform();
        stage.setScene(adminHomePageScene);
    }
    
    public void goBackToHome() {
        adminHomePageController.perform();
        stage.setScene(adminHomePageScene);
    }
    
    public void logOut() {
        // Call the server and notify it.
        // Delete the user session from the device.
        adminLoginController.perform();
        stage.setScene(adminLoginScene);
    }
    
    public void viewServerStats() {
        adminViewStatsController.perform();
        stage.setScene(adminViewStatsScene);
    }
    
    public void viewAllAnnouncemnts() {
        adminViewAnnouncementsController.perform();
        stage.setScene(adminViewAnnouncementsScene);
    }
    
    public void addNewAnnouncement() {
        adminAddAnnouncementController.perform();
        stage.setScene(adminAddAnnouncementScene);
    }
    
    public void manageAllAccounts() {
        /*
        adminManageAllAccountsController.perform();
        stage.setScene(adminManageAllAccountsParent);
        */
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Not Implemented.");
        alert.showAndWait();
    }
    
    public void viewAccount() {
        
    }
    
    public void createAccount() {
        
    }
    
    public void deleteAccount() {
        
    }
}
