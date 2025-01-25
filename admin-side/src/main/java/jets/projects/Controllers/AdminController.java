package jets.projects.Controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class AdminController {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    public void announcementAction() throws Exception{
        FXMLLoader announcementLoader = new FXMLLoader(getClass().getResource("/FXML/announcement.fxml"));
        VBox vBox = announcementLoader.load();

        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(vBox);

        AnchorPane.setTopAnchor(vBox, 0.0);
        AnchorPane.setBottomAnchor(vBox, 0.0);
        AnchorPane.setLeftAnchor(vBox, 0.0);
        AnchorPane.setRightAnchor(vBox, 0.0);
    }
    @FXML
    public void statisticsAction() throws Exception{
        FXMLLoader statisticsLoader = new FXMLLoader(getClass().getResource("/FXML/statistics.fxml"));
        VBox vBox = statisticsLoader.load();

        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(vBox);

        AnchorPane.setTopAnchor(vBox, 0.0);
        AnchorPane.setBottomAnchor(vBox, 0.0);
        AnchorPane.setLeftAnchor(vBox, 0.0);
        AnchorPane.setRightAnchor(vBox, 0.0);
    }
    @FXML 
    public void overviewAction() throws Exception{
        FXMLLoader statisticsLoader = new FXMLLoader(getClass().getResource("/FXML/overview.fxml"));
        VBox vBox = statisticsLoader.load();

        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(vBox);

        AnchorPane.setTopAnchor(vBox, 0.0);
        AnchorPane.setBottomAnchor(vBox, 0.0);
        AnchorPane.setLeftAnchor(vBox, 0.0);
        AnchorPane.setRightAnchor(vBox, 0.0);
    }

}
