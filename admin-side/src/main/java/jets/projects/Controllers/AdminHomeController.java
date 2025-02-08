package jets.projects.Controllers;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AdminHomeController {
    private Director director;
    private Stage stage;

    @FXML
    private AnchorPane anchorPane;

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public void setStageDirector(Stage stage, Director director) {
        this.director = director;
        this.stage = stage;
    }
    @FXML
    public void handleAnnouncementButton() throws Exception{
        director.viewAnnouncementPage();
    }
    @FXML
    public void handleStatisticsButton() throws Exception{
        director.viewStatisticsPage();
    }
    @FXML 
    public void handleOverviewButton() throws Exception{
        director.viewOverviewPage();
    }
    @FXML
    public void handleLogoutButton() throws Exception{
        director.logOut();
    }

}
