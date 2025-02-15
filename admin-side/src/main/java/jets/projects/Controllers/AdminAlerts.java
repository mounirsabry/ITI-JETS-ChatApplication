package jets.projects.Controllers;

import javafx.scene.control.Alert;

public class AdminAlerts {
    static void invokeInformationAlert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setResizable(true);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();

    }
    static void invokeWarningAlert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setResizable(true);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();

    }
    static void invokeErrorAlert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setResizable(true);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
