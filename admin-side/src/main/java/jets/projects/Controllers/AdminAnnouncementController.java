package jets.projects.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jets.projects.entities.Announcement;

import java.time.LocalDateTime;
import java.util.Date;

public class AdminAnnouncementController {
    private Stage stage;
    private Director director;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea contentField;
    @FXML
    private Button publishButton;
    @FXML
    private TableView<Announcement> tableView;
    @FXML
    private TableColumn<Announcement, Integer> idColumn;
    @FXML
    private TableColumn<Announcement, String> headerColumn;
    @FXML
    private TableColumn<Announcement, String> contentColumn;
    @FXML
    private TableColumn<Announcement, String> sentAtColumn; // Display Date as a String
    private final ObservableList<Announcement> announcements = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Bind table columns to Announcement properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("announcementID"));
        headerColumn.setCellValueFactory(new PropertyValueFactory<>("header"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        sentAtColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSentAt().toString()) // Convert Date to String
        );

        // Set TableView items
        tableView.setItems(announcements);
    }
    public void addAnnouncement(Announcement announcement) {
        announcements.add(announcement);
    }


    public void setStageDirector(Stage stage, Director director){
        this.director = director;
        this.stage = stage;
    }
    public void handlePublishButton(){
        String header = titleField.getText();
        String content = contentField.getText();
        if(header.isEmpty() || content.isEmpty()){
            AdminAlerts.invokeWarningAlert("Publish Warning", "Title field and content field of announcement can't be empty..");
        }else{
            Announcement newAnnouncement = new Announcement();
            newAnnouncement.setHeader(header);
            newAnnouncement.setContent(content);
            director.addNewAnnouncement(newAnnouncement);
        }
    }

    public TextField getTitleField() {
        return titleField;
    }

    public void setTitleField(TextField titleField) {
        this.titleField = titleField;
    }

    public TextArea getContentField() {
        return contentField;
    }

    public void setContentField(TextArea contentField) {
        this.contentField = contentField;
    }

}
