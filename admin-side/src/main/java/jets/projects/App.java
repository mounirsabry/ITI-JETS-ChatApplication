package jets.projects;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jets.projects.fxmlcontrollers.AdminAddAnnouncementController;
import jets.projects.fxmlcontrollers.AdminHomePageController;
import jets.projects.fxmlcontrollers.AdminLoginController;
import jets.projects.fxmlcontrollers.AdminViewAnnouncementsController;
import jets.projects.fxmlcontrollers.AdminViewStatsController;
import jets.projects.fxmlcontrollers.Director;

@SuppressWarnings("CallToPrintStackTrace")
public class App extends Application {
    private Parent adminLoginParent;
    private AdminLoginController adminLoginController;
    
    private Parent adminHomePageParent;
    private AdminHomePageController adminHomePageController;
    
    private Parent adminViewStatsParent;
    private AdminViewStatsController adminViewStatsController;
    
    private Parent adminViewAnnouncementsParent;
    private AdminViewAnnouncementsController adminViewAnnouncementsController;
    
    private Parent adminAddAnnouncementParent;
    private AdminAddAnnouncementController adminAddAnnouncementController;

    @Override
    public void start(Stage stage) {
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        
        stage.setWidth(1280);
        stage.setHeight(720);
        
        stage.setMaxWidth(1920);
        stage.setMaxHeight(1080);
        
    	final String adminLoginName = "/FXML/AdminLogIn.fxml";
        final String adminHomePageName = "/FXML/AdminHomePage.fxml";
        final String adminViewStatsName = "/FXML/AdminViewStats.fxml";
        final String adminViewAnnouncementsName = "/FXML/AdminViewAnnouncements.fxml";
        final String adminAddAnnouncementName = "/FXML/AdminAddAnnouncement.fxml";
        
        loadAdminLogin(adminLoginName);
        loadAdminHomePage(adminHomePageName);
        loadAdminViewStats(adminViewStatsName);
        loadAdminViewAnnouncements(adminViewAnnouncementsName);
        loadAdminAddAnnouncement(adminAddAnnouncementName);
        
        if (adminLoginParent == null || adminLoginController == null 
        ||  adminHomePageParent == null || adminHomePageController == null
        ||  adminViewStatsParent == null || adminViewStatsController == null
        ||  adminViewAnnouncementsParent == null || adminViewAnnouncementsController == null
        ||  adminAddAnnouncementParent == null || adminAddAnnouncementController == null) {
            System.out.println("Could not load some of the resources.");
            Text text = new Text("Could not load resources.");
            Scene errorScene = new Scene(new StackPane(text));
            stage.setScene(errorScene);
            stage.show();
        } else {
            Director myDirector = new Director(stage,
                adminLoginParent, adminLoginController,
                adminHomePageParent, adminHomePageController,
                adminViewStatsParent, adminViewStatsController,
                adminViewAnnouncementsParent, adminViewAnnouncementsController,
                adminAddAnnouncementParent, adminAddAnnouncementController
            );
            myDirector.startWorking();
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
    
    public void loadAdminLogin(String pageName) {
        try { 
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName));
            adminLoginParent = loader.load();
            adminLoginController = loader.getController();
        } catch (IOException ex) {
            System.out.println("Error in load " + pageName + " function.");
            ex.printStackTrace();
        }      
    }
    
    public void loadAdminHomePage(String pageName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName));
            adminHomePageParent = loader.load();
            adminHomePageController = loader.getController();
        } catch (IOException ex) {
            System.out.println("Error in load " + pageName + " function.");
            ex.printStackTrace();
        } 
    }
    
    public void loadAdminViewStats(String pageName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName));
            adminViewStatsParent = loader.load();
            adminViewStatsController = loader.getController();
        } catch (IOException ex) {
            System.out.println("Error in load " + pageName + " function.");
            ex.printStackTrace();
        } 
    }
    
    public void loadAdminViewAnnouncements(String pageName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName));
            adminViewAnnouncementsParent = loader.load();
            adminViewAnnouncementsController = loader.getController();
        } catch (IOException ex) {
            System.out.println("Error in load " + pageName + " function.");
            ex.printStackTrace();
        } 
    }
    
    public void loadAdminAddAnnouncement(String pageName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName));
            adminAddAnnouncementParent = loader.load();
            adminAddAnnouncementController = loader.getController();
        } catch (IOException ex) {
            System.out.println("Error in load " + pageName + " function.");
            ex.printStackTrace();
        } 
    }
}