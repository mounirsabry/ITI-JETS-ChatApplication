package jets.projects.Controllers;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import jets.projects.Services.Request.ClientInvitationService;
import jets.projects.entities.ContactInvitation;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entity_info.ContactInvitationInfo;
import datastore.DataCenter;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class InvitationCardController {
    @FXML
    private Circle userprofilepicture;
    @FXML
    private Label userName;
    private ContactInvitation invitation;
    ClientInvitationService invitationService;

    void setData(ContactInvitationInfo invitationInfo) {
        this.invitation = invitationInfo.getInvitation();
        invitationService = new ClientInvitationService();
        if (invitationInfo.getSenderPic()!= null){
            userprofilepicture.setFill(new ImagePattern(new Image(new ByteArrayInputStream(invitationInfo.getSenderPic()))));
        } else {
            userprofilepicture.setFill(new ImagePattern(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm())));
        }
        userName.setText(invitationInfo.getSenderDisplayName());
    }
    @FXML
    void initialize(){}

    @FXML
    void handleAcceptButton(ActionEvent event) {
    ContactInfo contactInfo = invitationService.acceptContactInvitation(invitation.getInvitationID());
    if (contactInfo != null) {
        DataCenter.getInstance().getContactMessagesMap().put(contactInfo.getContact().getSecondID()
                , FXCollections.synchronizedObservableList(FXCollections.observableArrayList(new ArrayList<>())));
        DataCenter.getInstance().getContactInfoMap().putIfAbsent(invitation.getSenderID(),contactInfo);
        DataCenter.getInstance().getContactList().add(contactInfo);

        DataCenter.getInstance().getContactInvitationList().removeIf(inv -> inv.getInvitation().getInvitationID() == invitation.getInvitationID());

        DataCenter.getInstance().getUnreadContactMessages().putIfAbsent(invitation.getSenderID(), new SimpleIntegerProperty());
        }
    }
    @FXML
    void handleRejectButton(ActionEvent event) {
        boolean isRejected = invitationService.rejectContactInvitation(invitation.getInvitationID());
        if(isRejected) {
            DataCenter.getInstance().getContactInvitationList().removeIf(inv -> inv.getInvitation().getInvitationID() == invitation.getInvitationID());
        }
    }

}
