package jets.projects.Controllers;

import datastore.DataCenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import jets.projects.Services.Request.ClientContactMessageService;
import jets.projects.entities.ContactMessage;
import javafx.concurrent.Task;
import javafx.application.Platform;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class MessageContactCard extends ListCell<ContactMessage> {
    private HBox content;
    private VBox messageBox;
    private Label messageContent;
    private Label timestamp;
    private ImageView fileIcon;
    private ClientContactMessageService contactMessageService;

    public MessageContactCard() {
        messageContent = new Label();
        messageContent.setWrapText(true);
        messageContent.setMaxWidth(250);

        timestamp = new Label();
        timestamp.setStyle("-fx-font-size: 10px; -fx-text-fill: #A0A0A0;");

        fileIcon = new ImageView(new Image(getClass().getResource("/images/file-icon.png").toExternalForm()));
        fileIcon.setFitWidth(30);
        fileIcon.setFitHeight(30);
        fileIcon.setPreserveRatio(true);

        messageBox = new VBox(5); // Added missing initialization
        messageBox.getChildren().addAll(messageContent, timestamp);

        content = new HBox(10);
        content.setPadding(new Insets(5));

        // Prevent selection effect
        setStyle("-fx-background-color: transparent;");
        selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (!isSelected) {
                setStyle("-fx-background-color: transparent;");
            }
        });
    }

    @Override
    protected void updateItem(ContactMessage message, boolean empty) {
        super.updateItem(message, empty);

        if (empty || message == null) {
            setGraphic(null);
            setStyle("-fx-background-color: transparent;");
            setCursor(Cursor.DEFAULT);
        } else {
            setStyle("-fx-background-color: transparent;");

            int myID = DataCenter.getInstance().getMyProfile().getUserID();
            timestamp.setText(message.getSentAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

            if (message.getContainsFile()) {
                messageContent.setText(message.getContent());
                messageContent.setStyle("-fx-background-color: #7D5BA6; -fx-padding: 8px; -fx-background-radius: 8px; -fx-text-fill: #FFFFFF;");
                messageContent.setGraphic(fileIcon);
                messageContent.setCursor(Cursor.HAND);
                messageContent.setOnMouseClicked(event -> downloadFile(message));
            } else {
                messageContent.setText(message.getContent());
                messageContent.setStyle("-fx-background-color: #B0B0C3; -fx-padding: 8px; -fx-background-radius: 8px; -fx-text-fill: #000000;");
                messageContent.setGraphic(null);
                messageContent.setCursor(Cursor.DEFAULT);
                messageContent.setOnMouseClicked(null);
            }

            content.getChildren().clear();
            if (message.getSenderID() == myID) {
                content.setAlignment(Pos.CENTER_RIGHT);
                messageContent.setStyle("-fx-background-color: #9B5DE5; -fx-padding: 8px; -fx-background-radius: 8px; -fx-text-fill: #FFFFFF;");
                messageBox.setAlignment(Pos.CENTER_RIGHT);
                content.getChildren().addAll(messageBox);
            } else {
                content.setAlignment(Pos.CENTER_LEFT);
                messageContent.setStyle("-fx-background-color: #B0B0C3; -fx-padding: 8px; -fx-background-radius: 8px; -fx-text-fill: #000000;");
                messageBox.setAlignment(Pos.CENTER_LEFT);
                content.getChildren().addAll(messageBox);
            }
            setGraphic(content);
        }
    }

    private void downloadFile(ContactMessage message) {
        int contactID = message.getSenderID();
        int messageID = message.getID();

        Task<byte[]> downloadTask = new Task<>() {
            @Override
            protected byte[] call() {
                return contactMessageService.getContactMessageFile(contactID, messageID);
            }
        };

        downloadTask.setOnSucceeded(event -> {
            byte[] fileData = downloadTask.getValue();
            if (fileData == null) {
                System.out.println("File download failed: No data received.");
                return;
            }

            Platform.runLater(() -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save File");
                fileChooser.setInitialFileName(message.getContent());

                File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(fileData);
                        System.out.println("File saved successfully at: " + file.getAbsolutePath());
                    } catch (IOException e) {
                        System.out.println("File save failed: " + e.getMessage());
                    }
                }
            });
        });

        downloadTask.setOnFailed(event -> {
            System.out.println("File download failed.");
        });

        new Thread(downloadTask).start();
    }
}
