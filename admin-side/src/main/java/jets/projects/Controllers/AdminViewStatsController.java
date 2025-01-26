package jets.projects.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Chart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminViewStatsController implements Initializable {
    private Stage stage;
    private Director myDirector;
    
    @FXML
    private Chart offlineOnlineChart;
    
    @FXML
    private Chart maleFemaleChart;
    
    @FXML
    private Chart topCountriesChart;
    
    @FXML
    private ComboBox chooseCountryComboBox;
    
    @FXML
    private TextField choosenCountryTextField;

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
    public void handleCountrySelected() {
        
    }
}
