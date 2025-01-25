package jets.projects.Controllers;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;


public class notificationsController {

    @FXML
    private ListView<HBox> notificationsList;

    @FXML
    private void initialize(){
        URL fxmlURL= getClass().getResource("/fxml/notificationCard.fxml");
        notificationsList.setCellFactory(lv->jets.projects.Utilities.createCustomCell(fxmlURL, 
        (notificationCardController controller , HBox item )-> controller.setData(item , notificationsList)));
        populateWithDummyData(notificationsList);
    }
    // for testing purposes only
    private void populateWithDummyData(ListView<HBox> notificationsList) {
        HBox contentHBox1 = new HBox();
        Circle userProfile1 = new Circle();
        userProfile1.setFill(new ImagePattern(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm())));
        Text content1 = new Text("Mariam has accepted your request.");
        contentHBox1.getChildren().addAll(userProfile1,content1);
        
        HBox contentHBox2 = new HBox();
        Circle userProfile2 = new Circle();
        userProfile2.setFill(new ImagePattern(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm())));
        Text content2 = new Text("Mariam is now online.");
        contentHBox2.getChildren().addAll(userProfile2,content2);

        notificationsList.getItems().addAll(contentHBox1,contentHBox2);
    }
}

