package jets.projects.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminHomePageController implements Initializable {
    private Stage stage;
    private Director myDirector;
    
    @FXML
    private Label serviceStatsLabel;
    
    @FXML
    private TextField headerField;
    
    @FXML
    private TextArea announcementTextArea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {   
    }

    public void setDirector(Stage stage, Director myDirector) {
        this.stage = stage;
        this.myDirector = myDirector;
    }
    
    public void perform() {
        // Fetch the service status.
        // Fetch the last announcement.
    }

    @FXML
    public void handleLogOut() {
        myDirector.logOut();
    }
    
    @FXML
    public void handleStartService() {
        // Call the server.
    }
    
    @FXML
    public void handleStopService() {
        // Call the server.
    }
    
    @FXML
    public void handleManageAllAccounts() {
        myDirector.manageAllAccounts();
    }
    
    @FXML
    public void handleViewServerStats() {
        myDirector.viewServerStats();
    }
    
    @FXML
    public void handleViewAllAnnouncements() {
        myDirector.viewAllAnnouncemnts();
    }
}
