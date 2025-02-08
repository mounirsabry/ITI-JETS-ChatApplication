package jets.projects.Controllers;

import datastore.DataCenter;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jets.projects.entity_info.ContactInfo;
import jets.projects.entities.ContactMessage;

public class MessageCard extends ListCell<ContactMessage> {
    private HBox content;
    private ImageView profilePic;
    private VBox messageBox;
    private Label senderName;
    private Label messageContent;
    private Label timestamp;

    private List<ContactInfo> contactList; // Holds contact info for name and profile pic

    public MessageCard() {
        this.contactList = DataCenter.getInstance().getContactList();

        profilePic = new ImageView();
        profilePic.setFitWidth(40);
        profilePic.setFitHeight(40);
        profilePic.setPreserveRatio(true);

        senderName = new Label();
        senderName.setStyle("-fx-font-weight: bold; -fx-text-fill: #2a2a2a;");

        messageContent = new Label();
        messageContent.setWrapText(true);
        messageContent.setMaxWidth(250);

        timestamp = new Label();
        timestamp.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

        messageBox = new VBox(senderName, messageContent, timestamp);
        messageBox.setPadding(new Insets(5));

        content = new HBox(10, profilePic, messageBox);
        content.setPadding(new Insets(5));
    }

    @Override
    protected void updateItem(ContactMessage message, boolean empty) {
        super.updateItem(message, empty);

        if (empty || message == null) {
            setGraphic(null);
        } else {
            int myID = DataCenter.getInstance().getMyProfile().getUserID();
            int contactID = 0;
            if(message.getReceiverID() != myID){
                contactID = message.getReceiverID();
            }else {
                contactID = message.getSenderID();
            }
            // Find ContactInfo using senderID
            int finalContactID = contactID;
            ContactInfo contactInfo = contactList.stream()
                    .filter(c -> c.getContact().getFirstID() == finalContactID || c.getContact().getSecondID() == finalContactID)
                    .findFirst()
                    .orElse(null);

            // Set sender name
            if (contactInfo != null) {
                senderName.setText(contactInfo.getName());

                // Load profile picture from byte array
                if (contactInfo.getPic() != null) {
                    Image image = new Image(new ByteArrayInputStream(contactInfo.getPic()));
                    profilePic.setImage(image);
                } else {
                    profilePic.setImage(new Image("file:default_profile.png")); // Default image
                }
            } else {
                senderName.setText("Unknown User");
                profilePic.setImage(new Image("file:default_profile.png")); // Default image
            }

            // Set message content and timestamp
            messageContent.setText(message.getContent());
            timestamp.setText(message.getSentAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

            setGraphic(content);
        }
    }
}

