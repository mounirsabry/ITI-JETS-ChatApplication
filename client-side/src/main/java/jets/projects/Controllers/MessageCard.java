package jets.projects.Controllers;

import datastore.DataCenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.scene.shape.Circle;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entities.ContactMessage;

public class MessageCard extends ListCell<ContactMessage> {
    private HBox content;
    private ImageView profilePic;
    private VBox messageBox;
    private Label senderName;
    private Label messageContent;
    private Label timestamp;

    public MessageCard() {

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

        messageBox = new VBox(senderName, messageContent, timestamp);
        messageBox.setPadding(new Insets(5));

        content = new HBox(10);
        content.setPadding(new Insets(5));
    }

    @Override
    protected void updateItem(ContactMessage message, boolean empty) {
        super.updateItem(message, empty);

        if (empty || message == null) {
            setGraphic(null);
        } else {
            int myID = DataCenter.getInstance().getMyProfile().getUserID();
            String displayName;
            Image profileImage;

            if (message.getSenderID() == myID) {
                // If I am the sender, show my profile details
                displayName = DataCenter.getInstance().getMyProfile().getDisplayName();
                if (DataCenter.getInstance().getMyProfile().getPic() != null) {
                    profileImage = new Image(new ByteArrayInputStream(DataCenter.getInstance().getMyProfile().getPic()));
                } else {
                    profileImage = new Image(getClass().getResource("/images/blank-profile.png").toExternalForm());
                }
            } else {
                // If I am the receiver, show the sender's details
                int contactID = message.getSenderID();
                ContactInfo contactInfo = DataCenter.getInstance().getContactInfoMap().get(contactID);

                if (contactInfo != null) {
                    displayName = contactInfo.getName();
                    if (contactInfo.getPic() != null) {
                        profileImage = new Image(new ByteArrayInputStream(contactInfo.getPic()));
                    } else {
                        profileImage = new Image(getClass().getResource("/images/blank-profile.png").toExternalForm());
                    }
                } else {
                    displayName = "Unknown User";
                    profileImage = new Image(getClass().getResource("/images/blank-profile.png").toExternalForm());
                }
            }

            // Update UI components
            senderName.setText(displayName);

            Circle clip = new Circle(20, 20, 20); // Center (20,20) and Radius 20
            profilePic.setClip(clip);
            profilePic.setImage(profileImage);

            messageContent.setText(message.getContent());
            timestamp.setText(message.getSentAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

            // Adjust alignment
            content.getChildren().clear();
            if (message.getSenderID() == myID) {
                // Sent messages (Align Right)
                content.setAlignment(Pos.CENTER_RIGHT);
                messageContent.setStyle("-fx-background-color: #DCF8C6; -fx-padding: 8px; -fx-background-radius: 8px;");
                messageBox.setAlignment(Pos.CENTER_RIGHT);
                content.getChildren().addAll(messageBox, profilePic);

            } else {
                // Received messages (Align Left)
                content.setAlignment(Pos.CENTER_LEFT);
                messageContent.setStyle("-fx-background-color: lightgray; -fx-padding: 8px; -fx-background-radius: 8px;");
                content.getChildren().addAll(profilePic, messageBox);
            }
            setGraphic(content);
        }
    }

}