package jets.projects.Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SettingsController {

    @FXML
    private TextField passwordField;

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private ComboBox<HBox> statusComboBox;
    @FXML
    private Button saveButton;

    @FXML
    void initialize(){
        initStatus();
    }
    private Stage owner; 
    private Stage popupStage;

    public void setOriginalStage(Stage stage) {
        this.owner = stage;
    }

    public void setPopupStage(Stage stage) {
        this.popupStage = stage;
    }

    @FXML
    void handleSave(ActionEvent event) {
        //save changes
        if (owner != null) {
            owner.getScene().getRoot().setEffect(null); // Remove blur effect
        }
        if (popupStage != null) {
            popupStage.close();
        } 
    }
    void initStatus() {
        HBox available = new HBox(10); 
        ImageView availableIcon = new ImageView(new Image(getClass().getResource("/images/available.png").toExternalForm()));
        Text availableStatus = new Text("Available");
        available.getChildren().addAll(availableIcon, availableStatus);

        HBox busy = new HBox(10);
        ImageView busyIcon = new ImageView(new Image(getClass().getResource("/images/busy.png").toExternalForm()));
        Text busyStatus = new Text("Busy");
        busy.getChildren().addAll(busyIcon, busyStatus);

        // Define the away status
        HBox away = new HBox(10);
        ImageView awayIcon = new ImageView(new Image(getClass().getResource("/images/away.png").toExternalForm()));
        Text awayStatus = new Text("Away");
        away.getChildren().addAll(awayIcon, awayStatus);

        HBox offline = new HBox(10);
        ImageView offlineIcon = new ImageView(new Image(getClass().getResource("/images/offline.png").toExternalForm()));
        Text offlineStatus = new Text("Offline");
        offline.getChildren().addAll(offlineIcon, offlineStatus);

        statusComboBox.getItems().addAll(available, busy, away, offline);

        // Set a custom cell factory to render the HBoxes
        statusComboBox.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item); 
                }
            }
        });
    }
}
