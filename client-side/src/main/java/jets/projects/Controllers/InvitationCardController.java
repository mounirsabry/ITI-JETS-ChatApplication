package jets.projects.Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import jets.projects.entity_info.ContactInvitationInfo;

import java.io.ByteArrayInputStream;

public class InvitationCardController {

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

    void setData(ContactInvitationInfo senderInfo) {
        if (senderInfo.getSenderPic()!= null){
            userprofilepicture.setFill(new ImagePattern(new Image(new ByteArrayInputStream(senderInfo.getSenderPic()))));
        } else {
            userprofilepicture.setFill(new ImagePattern(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm())));
        }
        userName.setText(senderInfo.getSenderDisplayName());
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
