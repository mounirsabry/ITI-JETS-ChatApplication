package jets.projects.Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class notificationCardController {

    @FXML
    private HBox notificationHbox;  //the whole card container
    @FXML
    private HBox contentHbox;
    @FXML
    private Circle userprofilepicture;
    @FXML
    private Text content;
    @FXML
    private Button deleteButton;

    private HBox currentItem;
    private ListView<?> parentListView;

    <T>void setData(HBox currentItem , ListView<T> listView){    
        // Extract the child components from the provided Hbox
        Circle profilePicture = (Circle) currentItem.getChildren().get(0);
        Text contentText = (Text) currentItem.getChildren().get(1);
        userprofilepicture.setFill(profilePicture.getFill());
        content.setText(contentText.getText());  
        this.currentItem = currentItem; 
        parentListView = listView;    
    }

    @FXML
    void handleDeleteButton(ActionEvent event) {
        parentListView.getItems().remove(currentItem);
    }

}
