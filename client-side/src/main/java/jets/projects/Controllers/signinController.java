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

    @FXML
    void handleSignInButton(ActionEvent event) throws IOException {
        //validate data and navigate to home screen
        Node currentNode = (Node)event.getSource();
        Stage stage = (Stage)currentNode.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/homeScreen.fxml"));
        Scene scene = new Scene(root,stage.getWidth(),stage.getHeight());
        stage.setScene(scene);
    }

 @FXML
    void handleSignInToggle(ActionEvent event) throws IOException {
        // Node currentNode = (Node)event.getSource();
        // Stage stage = (Stage)currentNode.getScene().getWindow();
        // Parent root = FXMLLoader.load(getClass().getResource("/fxml/signin.fxml"));
        // Scene scene = new Scene(root,stage.getWidth(),stage.getHeight());
        // stage.setScene(scene);
    }
    @FXML
    void handleSignUpToggle(ActionEvent event) throws IOException {
        // Node currentNode = (Node)event.getSource();
        // Stage stage = (Stage)currentNode.getScene().getWindow();
        // Parent root = FXMLLoader.load(getClass().getResource("/fxml/signup.fxml"));
        // Scene scene = new Scene(root,stage.getWidth(),stage.getHeight());
        // stage.setScene(scene);
    }


}
