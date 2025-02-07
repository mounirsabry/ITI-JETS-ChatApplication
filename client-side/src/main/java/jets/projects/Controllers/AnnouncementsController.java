package jets.projects.Controllers;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Label;;

public class AnnouncementsController {

    @FXML
    private ListView<HBox> announcementsList;
    
    @FXML
    private void initialize(){
        URL fxmlURL= getClass().getResource("/fxml/announcementCard.fxml");
        announcementsList.setCellFactory(lv->jets.projects.Utilities.createCustomCell(fxmlURL, 
        (AnnouncementCardController controller , HBox item )-> controller.setData(item , announcementsList)));
        populateWithDummyData(announcementsList);
    }
    // for testing purposes only
    private void populateWithDummyData(ListView<HBox> announcementListView) {
        HBox announcement = new HBox();
        VBox announcementContent = new VBox(5);
        Label titleLabel = new Label("New Update");
        Text content = new Text("Update to version 3.5 and check out the new features");
        announcementContent.getChildren().addAll(titleLabel,content);
        announcement.getChildren().add(announcementContent);
        announcementListView.getItems().add(announcement);
    }
}