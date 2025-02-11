package jets.projects.Controllers;
import datastore.DataCenter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import jets.projects.Services.Request.ClientNotificationService;
import jets.projects.entities.Notification;

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
    private ListView<Notification> parentListView;

    

    public  void setData(Notification notification , ListView<Notification> listView){
        content.setText(notification.getContent());
        this.currentItem = notification;
        parentListView = listView;    
    }
    @FXML
    void handleDeleteButton(ActionEvent event) {
        
       ClientNotificationService notificationService = new ClientNotificationService();
       boolean markedAsRead = notificationService.deleteNotification(currentItem.getNotificationID());
       DataCenter.getInstance().getNotificationList().remove(currentItem);

       if (!markedAsRead) {
        ClientAlerts.invokeErrorAlert("Error", "Failed To Delete Notification");
          
         
       } 

    }

}
