package jets.projects.Controllers;
import datastore.DataCenter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import jets.projects.Services.Request.ClientNotificationService;
import jets.projects.entities.Notification;
import jets.projects.entities.NotificationType;

import java.time.format.DateTimeFormatter;

public class NotificationCardController {

    @FXML
    private HBox notificationHbox;  //the whole card container
    @FXML
    private HBox contentHbox;
    @FXML
    private Text content;
    @FXML
    private Button deleteButton;
    @FXML
    private Label sent_at;
    private Notification currentItem;

    public  void setData(Notification notification , ListView<Notification> listView){
        sent_at.setStyle("-fx-font-size: 10px; -fx-text-fill: #A0A0A0;");
        sent_at.setText(notification.getSentAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        content.setText(notification.getContent());
        this.currentItem = notification;
    }
    
    @FXML
    void handleDeleteButton(ActionEvent event) {
       ClientNotificationService notificationService = new ClientNotificationService();
       int notificationID = currentItem.getNotificationID();
        boolean markedAsRead = notificationService.deleteNotification(notificationID);

       DataCenter.getInstance().getNotificationList().remove(currentItem);

       if (!markedAsRead) {
        ClientAlerts.invokeErrorAlert("Error", "Failed To Delete Notification");
       } 

    }

}
