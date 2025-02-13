package jets.projects.Controllers;

import java.net.URL;
import datastore.DataCenter;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import jets.projects.Utilities;
import jets.projects.Director;
import jets.projects.entity_info.AnnouncementInfo;

public class AnnouncementsController {

    @FXML
    private ListView<AnnouncementInfo> announcementsList;

    private Stage stage;
    private Director director;

    public void setDirector(Stage stage, Director director) {
        this.stage = stage;
        this.director = director;
    }
    @FXML
    private void initialize() {
        // Bind the ListView to the observable list
        announcementsList.setItems(DataCenter.getInstance().getAnnouncementList());

        URL fxmlURL = getClass().getResource("/fxml/announcementCard.fxml");
        if (fxmlURL == null) {
            System.err.println("Error: announcementCard.fxml not found!");
            return;
        }
        // Set up the cell factory
        announcementsList.setCellFactory(lv -> Utilities.createCustomCell(
                fxmlURL, (AnnouncementCardController controller, AnnouncementInfo item) -> controller.setData(item, announcementsList)
        ));
    }
}



