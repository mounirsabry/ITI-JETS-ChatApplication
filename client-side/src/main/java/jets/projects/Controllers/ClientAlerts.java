package jets.projects.Controllers;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class ClientAlerts {
    public static void invokeInformationAlert(String title, String content){
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.setResizable(true);
            alert.showAndWait();
        });
    }
    public static void invokeWarningAlert(String title, String content){
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.setResizable(true);
            alert.showAndWait();
        });
    }
    public static void invokeErrorAlert(String title, String content){
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.setResizable(true);
            alert.showAndWait();
        });
    }
}
