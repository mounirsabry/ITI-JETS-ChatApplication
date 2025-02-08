package jets.projects.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import jets.projects.entities.Announcement;
import jets.projects.entity_info.AnnouncementInfo;

public class AnnouncementCardController {

    @FXML
    private Label title;
    @FXML
    private Text content;
    @FXML
    private Button deleteButton;

    private AnnouncementInfo currentItem;
    private ListView<AnnouncementInfo> parentListView;

    public void setData(AnnouncementInfo announcement, ListView<AnnouncementInfo> listView) {
        System.out.println("Setting data for: " + announcement.getAnnouncement().getHeader());
        title.setText(announcement.getAnnouncement().getHeader());
        content.setText(announcement.getAnnouncement().getContent());
        this.currentItem = announcement;
        this.parentListView = listView;
    }

    @FXML
    void handleDeleteButton(ActionEvent event) {
        parentListView.getItems().remove(currentItem);
    }
}
