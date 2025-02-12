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
import jets.projects.Services.Request.ClientNotificationService;
import jets.projects.Utilities;
import jets.projects.entities.Notification;
import jets.projects.entity_info.AnnouncementInfo;


public class NotificationsController {

    @FXML
    private ListView<Notification> notificationsList;
    private ClientNotificationService notificationService = new ClientNotificationService();
    private Stage stage;
    private Director director;

    @FXML
    private void initialize() {
        // Bind the ListView to the observable list
        notificationsList.setItems(DataCenter.getInstance().getNotificationList());

        URL fxmlURL = getClass().getResource("/fxml/notificationCard.fxml");
        if (fxmlURL == null) {
            System.err.println("Error: notificationCard.fxml not found!");
            return;
        }
        notificationsList.setCellFactory(lv -> Utilities.createCustomCell(
                fxmlURL, (NotificationCardController controller, Notification item) -> controller.setData(item, notificationsList)
        ));
        
    }
}

