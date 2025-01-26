package jets.projects.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class announcementCardController {

    @FXML
    private HBox announcementHbox;   //whole container
    @FXML
    private VBox announcementVbox;

    @FXML
    private Label title;
    @FXML
    private Text content;

    @FXML
    private Button deleteButton;

    private HBox currentItem;
    private ListView<?> parentListView;

    <T> void setData(HBox currentItem , ListView<T> listView){
        VBox announcementContent = (VBox)currentItem.getChildren().get(0);
        Label currentTitle= (Label)announcementContent.getChildren().get(0);
        Text currentContent = (Text) announcementContent.getChildren().get(1);
        title.setText(currentTitle.getText());
        content.setText(currentContent.getText());
        this.currentItem = currentItem;
        parentListView = listView;
    }

    @FXML
    void handleDeleteButton(ActionEvent event) {
        parentListView.getItems().remove(currentItem);
    }

}
