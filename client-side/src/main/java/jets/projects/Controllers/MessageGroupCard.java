package jets.projects.Controllers;

import datastore.DataCenter;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import jets.projects.Services.Request.ClientContactMessageService;
import jets.projects.Services.Request.ClientGroupMessageService;
import jets.projects.entities.ContactMessage;
import javafx.concurrent.Task;
import javafx.application.Platform;
import jets.projects.entities.GroupMessage;
import jets.projects.entity_info.GroupMemberInfo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class MessageGroupCard extends ListCell<GroupMessage> {
    private HBox content;
    private ImageView profilePic;
    private VBox messageBox;
    private Label senderName;
    private Label messageContent;
    private Label timestamp;
    private Circle clip;
    private ImageView fileIcon;
    private ClientGroupMessageService groupMessageService;

    public MessageGroupCard() {

        clip = new Circle(20, 20, 20);
        profilePic = new ImageView();
        profilePic.setFitWidth(40);
        profilePic.setFitHeight(40);
        profilePic.setPreserveRatio(true);

        senderName = new Label();
        senderName.setStyle("-fx-font-weight: bold; -fx-text-fill: #2a2a2a;");

        messageContent = new Label();
        messageContent.setWrapText(true);
        messageContent.setMaxWidth(250);
        messageContent.setStyle("-fx-background-color: lightgray; -fx-padding: 8px; -fx-background-radius: 8px;");

        timestamp = new Label();
        timestamp.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

        fileIcon = new ImageView(new Image(getClass().getResource("/images/file-icon.png").toExternalForm()));
        fileIcon.setFitWidth(30);
        fileIcon.setFitHeight(30);
        fileIcon.setPreserveRatio(true);

        messageBox = new VBox(senderName, messageContent, timestamp);
        messageBox.setPadding(new Insets(5));

        content = new HBox(10);
        content.setPadding(new Insets(5));
    }

    @Override
    protected void updateItem(GroupMessage message, boolean empty) {
        super.updateItem(message, empty);

        if (empty || message == null) {
            setGraphic(null);
        } else {
            int myID = DataCenter.getInstance().getMyProfile().getUserID();
            String displayName;
            Image profileImage;

            if (message.getSenderID() == myID) {
                // Sent messages (show my profile)
                displayName = DataCenter.getInstance().getMyProfile().getDisplayName();
                if (DataCenter.getInstance().getMyProfile().getPic() != null) {
                    profileImage = new Image(new ByteArrayInputStream(DataCenter.getInstance().getMyProfile().getPic()));
                } else {
                    profileImage = new Image(getClass().getResource("/images/blank-profile.png").toExternalForm());
                }
            } else {
                // Received messages (show sender profile)
                int contactID = message.getSenderID();
                ObservableList<GroupMemberInfo> list = DataCenter.getInstance().getGroupMembersMap().get(message.getGroupID());
                GroupMemberInfo groupMemberInfo = list.stream().filter(member -> member.getMember().getMemberID() == contactID).findFirst().orElse(null);

                if(groupMemberInfo != null){
                    displayName = groupMemberInfo.getName();
                    if(groupMemberInfo.getPic() != null)
                        profileImage = new Image(new ByteArrayInputStream(groupMemberInfo.getPic()));
                    else{
                        profileImage = new Image(getClass().getResource("/images/blank-profile.png").toExternalForm());
                    }
                }else{
                    displayName = "Unknown Sender";
                    profileImage = new Image(getClass().getResource("/images/blank-profile.png").toExternalForm());
                }
            }

            senderName.setText(displayName);
            profilePic.setClip(clip);
            profilePic.setImage(profileImage);
            timestamp.setText(message.getSentAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

            // Check if message contains a file
            if (message.getContainsFile()) {
                messageContent.setText(message.getContent()); // File name
                messageContent.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 8px; -fx-background-radius: 8px;");
                messageContent.setGraphic(fileIcon);
                groupMessageService = new ClientGroupMessageService();

                // Enable file download on click
                messageContent.setOnMouseClicked(event -> downloadFile(message));
            } else {
                // Display normal text message
                messageContent.setText(message.getContent());
                messageContent.setStyle("-fx-background-color: lightgray; -fx-padding: 8px; -fx-background-radius: 8px;");
                messageContent.setGraphic(null);
                messageContent.setOnMouseClicked(null);
            }

            // Adjust alignment
            content.getChildren().clear();
            if (message.getSenderID() == myID) {
                content.setAlignment(Pos.CENTER_RIGHT);
                messageContent.setStyle("-fx-background-color: #DCF8C6; -fx-padding: 8px; -fx-background-radius: 8px;");
                messageBox.setAlignment(Pos.CENTER_RIGHT);
                content.getChildren().addAll(messageBox, profilePic);
            } else {
                content.setAlignment(Pos.CENTER_LEFT);
                messageBox.setAlignment(Pos.CENTER_LEFT);
                content.getChildren().addAll(profilePic, messageBox);
            }
            setGraphic(content);
        }
    }

    private void downloadFile(GroupMessage message) {
        int groupID = message.getGroupID();
        int messageID = message.getMessageID();

        Task<byte[]> downloadTask = new Task<>() {
            @Override
            protected byte[] call() {
                return groupMessageService.getGroupMessageFile(groupID, messageID);
            }
        };

        downloadTask.setOnSucceeded(event -> {
            byte[] fileData = downloadTask.getValue();
            if (fileData == null) {
                return; // Server error already handled
            }

            // File chooser must be run on JavaFX Application Thread
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

        // Run the task on a background thread
        new Thread(downloadTask).start();
    }

}
