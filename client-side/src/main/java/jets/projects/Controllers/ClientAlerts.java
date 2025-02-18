package jets.projects.Controllers;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class ClientAlerts {
    public static void invokeInformationAlert(String title, String content){
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wasla");
            Image logoImage = new Image(ClientAlerts.class.getResource("/images/small-logo.png").toExternalForm()); // Path to your logo
            ImageView logoImageView = new ImageView(logoImage);
            logoImageView.setFitWidth(50); // Set the size of the logo
            logoImageView.setFitHeight(50); // Adjust as needed

            // Set the graphic (logo)
            alert.setGraphic(logoImageView);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.setResizable(true);
            Image iconImage = new Image(ClientAlerts.class.getResource("/images/small-logo.png").toExternalForm()); // Path to your window icon
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(iconImage); // Set the icon on the stage
            alert.showAndWait();
        });
    }
    public static void invokeWarningAlert(String title, String content){
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Wasla");
            Image logoImage = new Image(ClientAlerts.class.getResource("/images/small-logo.png").toExternalForm()); // Path to your logo
            ImageView logoImageView = new ImageView(logoImage);
            logoImageView.setFitWidth(50); // Set the size of the logo
            logoImageView.setFitHeight(50); // Adjust as needed

            // Set the graphic (logo)
            alert.setGraphic(logoImageView);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.setResizable(true);
            Image iconImage = new Image(ClientAlerts.class.getResource("/images/small-logo.png").toExternalForm()); // Path to your window icon
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(iconImage); // Set the icon on the stage
            alert.showAndWait();
        });
    }
    public static void invokeErrorAlert(String title, String content){
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wasla");
            Image logoImage = new Image(ClientAlerts.class.getResource("/images/small-logo.png").toExternalForm()); // Path to your logo
            ImageView logoImageView = new ImageView(logoImage);
            logoImageView.setFitWidth(50); // Set the size of the logo
            logoImageView.setFitHeight(50); // Adjust as needed

            // Set the graphic (logo)
            alert.setGraphic(logoImageView);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.setResizable(true);
            Image iconImage = new Image(ClientAlerts.class.getResource("/images/small-logo.png").toExternalForm()); // Path to your window icon
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(iconImage); // Set the icon on the stage
            alert.showAndWait();
        });
    }
}
