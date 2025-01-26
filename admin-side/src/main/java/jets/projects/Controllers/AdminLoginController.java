package jets.projects.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AdminLoginController implements Initializable {
    private Stage stage;
    private Director myDirector;
    
    @FXML
    TextField userIDField;
    
    @FXML
    PasswordField passwordField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void setDirector(Stage stage, Director myDirector) {
        this.stage = stage;
        this.myDirector = myDirector;
    }
    
    public void perform() {
        // Read last session for the user and maybe auto login for them.
        // Check if the user has exit or sign out after he called logout.
        // If exit, then auto fail the userID with his data.
    }
    
    @FXML
    public void handleLogin() {
        myDirector.login();
    }
}
