package jets.projects.Controllers;
import java.net.URL;

import datastore.DataCenter;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import jets.projects.Director;
import jets.projects.Utilities;
import jets.projects.entity_info.AnnouncementInfo;
import jets.projects.entity_info.ContactInvitationInfo;


public class InvitationsController {

    @FXML
    private ListView<ContactInvitationInfo> invitationList;

    private Stage stage;
    private Director director;

    public void setDirector(Stage stage, Director director) {
        this.stage = stage;
        this.director = director;
    }
    @FXML
    private void initialize() {
        // Bind the ListView to the observable list
        invitationList.setItems(DataCenter.getInstance().getContactInvitationList());

        URL fxmlURL = getClass().getResource("/fxml/requestCard.fxml");
        if (fxmlURL == null) {
            System.err.println("Error: requestCard.fxml not found!");
            return;
        }
        // Set up the cell factory
        invitationList.setCellFactory(lv -> Utilities.createCustomCell(
                fxmlURL, (InvitationCardController controller, ContactInvitationInfo item) -> controller.setData(item)
        ));
    }
}

