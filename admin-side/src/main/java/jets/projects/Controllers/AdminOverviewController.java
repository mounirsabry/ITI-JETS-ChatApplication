package jets.projects.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdminOverviewController {
    private Stage stage;
    private Director director;

    @FXML
    private Text statusServiceText;
    @FXML
    private ImageView statusServiceIcon;
    @FXML
    private ToggleButton startServiceButton;
    @FXML
    private ToggleButton stopServiceButton;
    @FXML
    private Button viewUserButton;
    @FXML
    private Button addNewUserButton;
    @FXML
    private Button updateUserButton;
    @FXML
    private Button deleteUserButton;

    public void setStageDirector(Stage stage, Director director){
        this.director = director;
        this.stage = stage;
    }
    @FXML
    public void handleViewUserDataButton(){
        director.viewUserDataWindow();
    }
    @FXML
    public void handleAddNewUserButton(){
        director.viewAddNewUserWindow();
    }
    @FXML
    public void handleUpdateUserButton(){
        //director.viewUpdateUserWindow();
    }
    @FXML
    public void handleDeleteUserButton(){
        director.viewDeleteUserWindow();
    }
    @FXML
    public void handleStartServiceButton(){
        director.startUserService();
    }
    @FXML
    public void handleStopServiceButton(){
        director.stopUserService();
    }

    public Button getViewUserButton() {
        return viewUserButton;
    }

    public Button getAddNewUserButton() {
        return addNewUserButton;
    }

    public Button getDeleteUserButton() {
        return deleteUserButton;
    }

    public Button getUpdateUserButton() {
        return updateUserButton;
    }

    public Text getStatusServiceText() {
        return statusServiceText;
    }

    public ImageView getStatusServiceIcon() {
        return statusServiceIcon;
    }

    public ToggleButton getStartServiceButton() {
        return startServiceButton;
    }

    public ToggleButton getStopServiceButton() {
        return stopServiceButton;
    }
}
