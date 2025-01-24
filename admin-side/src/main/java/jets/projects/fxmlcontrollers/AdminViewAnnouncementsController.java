package jets.projects.fxmlcontrollers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class AdminViewAnnouncementsController implements Initializable {
    private Stage stage;
    private Director myDirector;
    
    @FXML
    private TableView announcementsTableView;
    
    @FXML
    private TableColumn headerColumn;
    
    @FXML
    private TableColumn DateAndTimeColumn;
    
    @FXML
    private TableColumn contentColumn;

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
        myDirector.goBackToHome();
    }
    
    @FXML
    public void handleAddNewAnnouncement() {
        myDirector.addNewAnnouncement();
    }   
}
