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
import jets.projects.Services.Request.ClientInvitationService;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entity_info.ContactInvitationInfo;
import datastore.DataCenter;

import java.io.ByteArrayInputStream;

public class InvitationCardController {
    private int invitationID;

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

        invitationID= senderInfo.getInvitation().getInvitationID();

       

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

      
    ClientInvitationService invitationService = new ClientInvitationService();
    ContactInfo contactInfo = invitationService.acceptContactInvitation(invitationID);

    if (contactInfo != null) {
        
        DataCenter.getInstance().getContactList().add(contactInfo);
    }

    
  


    }
    @FXML
    void handleRejectButton(ActionEvent event) {

        ClientInvitationService invitationService = new ClientInvitationService();
        invitationService.rejectContactInvitation(invitationID);

    }

}
