package jets.projects.Controllers;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;


public class friendRequestsController {

    @FXML
    private ListView<HBox> requestsList;

    @FXML
    private void initialize(){
        URL fxmlURL= getClass().getResource("/fxml/requestCard.fxml");
        requestsList.setCellFactory(lv->jets.projects.Utilities.createCustomCell(fxmlURL, 
        (requestCardController controller , HBox item )-> controller.setData(item)));
        populateWithDummyData(requestsList);
    }
    // for testing purposes only
    private void populateWithDummyData(ListView<HBox> friendRequestsList) {
        HBox userHBox = new HBox();
        Circle userProfile = new Circle();
        userProfile.setFill(new ImagePattern(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm())));
        Label userName = new Label("Mariam Amer");
        userHBox.getChildren().addAll(userProfile,userName);
        friendRequestsList.getItems().add(userHBox);
    }
}

