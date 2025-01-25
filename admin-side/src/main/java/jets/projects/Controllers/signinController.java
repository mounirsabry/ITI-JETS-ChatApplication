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
import javafx.stage.Stage;

public class signinController {

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/home.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
