package jets.projects.Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

public class requestCardController {

    @FXML
    private HBox requestHbox;

    @FXML
    private Circle userprofilepicture;

    @FXML
    private HBox userInfoHbox;

    @FXML
    private Label userName;

    @FXML
    private Button acceptButton;

    @FXML
    private Button rejectButton;

    void setData(HBox userInfo) {
        // Extract the child components from the provided Hbox
        Circle profilePicture = (Circle) userInfo.getChildren().get(0);
        Label nameLabel = (Label) userInfo.getChildren().get(1);
        userprofilepicture.setFill(profilePicture.getFill());
        userName.setText(nameLabel.getText());
    }
    

    @FXML
    void initialize(){}

    @FXML
    void handleAcceptButton(ActionEvent event) {

    }

    @FXML
    void handleRejectButton(ActionEvent event) {

    }

}
