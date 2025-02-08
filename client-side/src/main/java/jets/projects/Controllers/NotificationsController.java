package jets.projects.Controllers;
import java.net.URL;

import datastore.DataCenter;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jets.projects.Director;
import jets.projects.Utilities;
import jets.projects.entities.Notification;
import jets.projects.entity_info.AnnouncementInfo;


public class NotificationsController {

    @FXML
    private ListView<Notification> notificationsList;
    private ObservableList<Notification> notificationsObservableList = DataCenter.getInstance().getNotificationList();

    private Stage stage;
    private Director director;

    public void setDirector(Stage stage, Director director) {
        this.stage = stage;
        this.director = director;
    }
    @FXML
    private void initialize() {
        // Bind the ListView to the observable list
        notificationsList.setItems(notificationsObservableList);

        URL fxmlURL = getClass().getResource("/fxml/notificationCard.fxml");
        if (fxmlURL == null) {
            System.err.println("Error: notificationCard.fxml not found!");
            return;
        }
        // Set up the cell factory
        notificationsList.setCellFactory(lv -> Utilities.createCustomCell(
                fxmlURL, (NotificationCardController controller, Notification item) -> controller.setData(item, notificationsList)
        ));
    }
}

