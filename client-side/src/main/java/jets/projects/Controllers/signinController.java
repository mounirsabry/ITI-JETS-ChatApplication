package jets.projects.Controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import jets.projects.Director;

public class signinController {

    @FXML
    private ToggleButton signinToggleButton;
    @FXML
    private ToggleButton signUpToggleButton;
    @FXML
    private TextField phoneField;

    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button signinButton;
    private Stage stage;
    private Director myDirector;

    public void setDirector(Stage stage, Director myDirector) {
        this.stage = stage;
        this.myDirector = myDirector;
    }

    public void perform() {
    }

    @FXML
    void handleSignInButton(ActionEvent event) throws IOException {
        //validate data and navigate to home screen
        myDirector.home();
    }

    @FXML
    void handleSignInToggle(ActionEvent event){
        myDirector.signin();
    }
    @FXML
    void handleSignUpToggle(ActionEvent event) throws IOException {
        myDirector.signup();
    }

}
