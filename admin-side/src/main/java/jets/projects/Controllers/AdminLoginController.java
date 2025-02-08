package jets.projects.Controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminLoginController {
    private Stage stage;
    private Director director;
    @FXML
    private TextField userIDField;

    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button signinButton;


    public void setStageDirector(Stage stage, Director director) {
        this.stage = stage;
        this.director = director;
    }

    @FXML
    void handleSignInButton() {
        String userIDStr = userIDField.getText().trim();
        String password = passwordField.getText().trim();

        // checking pass and phone
        if (!userIDStr.isEmpty() && !password.isEmpty() && isValidCredentials(userIDStr, password)) {
            try{
                int userID = Integer.parseInt(userIDStr);
                director.login(userID, password);
            }catch(NumberFormatException e){
                AdminAlerts.invokeErrorAlert("Login Failed","Invalid user id, must be number.");
            }
        } else {
            AdminAlerts.invokeErrorAlert("Login Failed","Invalid userID or password.");
        }
    }

    // validation method on pass and phone fields
    private boolean isValidCredentials(String userIDStr, String password) {
        // need to implement
        return true;
    }

}
