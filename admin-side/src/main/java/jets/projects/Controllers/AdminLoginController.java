package jets.projects.Controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminLoginController {
    private Stage stage;
    private Director director;
    @FXML
    private TextField phoneField;

    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button signinButton;

    public TextField getPhoneField() {
        return phoneField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public void setStageDirector(Stage stage, Director director) {
        this.stage = stage;
        this.director = director;
    }

    @FXML
    void handleSignInButton() {
        String phone = phoneField.getText().trim();
        String password = passwordField.getText().trim();

        // checking pass and phone
        if (!phone.isEmpty() && !password.isEmpty() && isValidCredentials(phone, password)) {
            director.login(phone, password);
        } else {
            AdminAlerts.invokeErrorAlert("Login Failed","Invalid phone number or password.");
        }
    }

    // validation method on pass and phone fields
    private boolean isValidCredentials(String phone, String password) {
        // need to implement
        return true;
    }

}
