package jets.projects.Controllers;

import datastore.DataCenter;
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

import java.time.format.DateTimeFormatter;

public class AnnouncementCardController {

    @FXML
    private Label title;
    @FXML
    private Text content;
    @FXML
    private Button deleteButton;
    @FXML
    private Label sent_at;

    private AnnouncementInfo currentItem;
    private ListView<AnnouncementInfo> parentListView;

    public void setData(AnnouncementInfo announcement, ListView<AnnouncementInfo> listView) {
        sent_at.setStyle("-fx-font-size: 10px; -fx-text-fill: #A0A0A0;");
        sent_at.setText(announcement.getAnnouncement().getSentAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        title.setText(announcement.getAnnouncement().getHeader());
        content.setText(announcement.getAnnouncement().getContent());
        this.currentItem = announcement;
        this.parentListView = listView;
    }

    @FXML
    void handleDeleteButton(ActionEvent event) {
        DataCenter.getInstance().getAnnouncementList().remove(currentItem);
    }
}
