package jets.projects.fxmlcontrollers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminAddAnnouncementController implements Initializable {
    private Stage stage;
    private Director myDirector;
    
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
        
    }

    @FXML
    public void handleLogOut() {
        myDirector.logOut();
    }
    
    @FXML
    public void handleGoBack() {
        // Code to clear the data that the user entered in the text.
        myDirector.viewAllAnnouncemnts();
    }
    
    @FXML
    public void handleSubmit() {
        // Code to submit the new announcement.
        myDirector.viewAllAnnouncemnts();
    }
}
