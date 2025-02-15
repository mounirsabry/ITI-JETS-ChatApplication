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
import jets.projects.session_saving.NormalUserSavedSession;
import jets.projects.session_saving.SessionSaver;

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
        
        SessionSaver sessionSaver = new SessionSaver();
        NormalUserSavedSession savedSession = sessionSaver.load();
        if (savedSession != null) {
            sessionSaver.deleteSessionFile();
            
            String phoneNumber = savedSession.getPhoneNumber();
            String password = savedSession.getPassword();
            
            boolean isLogin = clientAuthenticationService.login(phoneNumber, password);
            if (isLogin == false) {
                ClientAlerts.invokeErrorAlert("Error", "Could not login from the saved session.");
                return;
            }
            sessionSaver.save(savedSession);
            myDirector.loading();
        }
    }

    @FXML
    void handleSignInButton(ActionEvent event) throws IOException {
        String phone = phoneField.getText();
        String pass = passwordField.getText();
        if(phone.trim().isEmpty() || pass.trim().isEmpty()){
            ClientAlerts.invokeInformationAlert("Login", "please enter valid data");
            return;
        }
        //validate data and navigate to loading screen
        String phoneNumber = phoneField.getText();
        String password = passwordField.getText();
        
        if (phoneNumber.isBlank() || password.isBlank()) {
            ClientAlerts.invokeErrorAlert("Error", 
                    "Not the phone number nor the password could be empty.");
            return;
        }
        
        if(clientAuthenticationService.login(phoneNumber, password)){
            SessionSaver sessionSaver = new SessionSaver();
            sessionSaver.save(new NormalUserSavedSession(phoneNumber, password));
            myDirector.loading();
        }

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
