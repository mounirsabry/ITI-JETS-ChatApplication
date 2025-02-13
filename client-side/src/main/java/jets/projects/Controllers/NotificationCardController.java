package jets.projects.Controllers;
import datastore.DataCenter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import jets.projects.Services.Request.ClientNotificationService;
import jets.projects.entities.Notification;
import jets.projects.entities.NotificationType;

public class NotificationCardController {

    @FXML
    private HBox notificationHbox;  //the whole card container
    @FXML
    private HBox contentHbox;
    @FXML
    private Text content;
    @FXML
    private Button deleteButton;

    private Notification currentItem;

    public  void setData(Notification notification , ListView<Notification> listView){
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
