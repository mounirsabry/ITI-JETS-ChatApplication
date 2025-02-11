package jets.projects.Controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import jets.projects.Director;
import jets.projects.Services.Request.ClientAuthenticationService;

public class SigninController {

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


    private ClientAuthenticationService clientAuthenticationService;
    public void setDirector(Stage stage, Director myDirector) {
        this.stage = stage;
        this.myDirector = myDirector;
    }

    public void perform() {
        clientAuthenticationService = new ClientAuthenticationService();
    }

    @FXML
    void handleSignInButton(ActionEvent event) throws IOException {
        //validate data and navigate to loading screen
        if(clientAuthenticationService.login(phoneField.getText(), passwordField.getText())){
            myDirector.loading();
        }
        // remove it
        myDirector.loading();
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
