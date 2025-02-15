package jets.projects.Controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import jets.projects.entity_info.ContactInfo;

import java.io.ByteArrayInputStream;

public class ContactCard extends ListCell<ContactInfo> {
    private final HBox content;
    private final ImageView profilePic;
    private final Label name;
    private final Circle clip;

    public ContactCard() {
        // Create circular clip for profile picture
        clip = new Circle(25, 25, 25);
        clip.setStroke(Color.WHITE);
        clip.setStrokeWidth(1);

        profilePic = new ImageView();
        profilePic.setFitWidth(50);
        profilePic.setFitHeight(50);
        profilePic.setPreserveRatio(true);
        profilePic.setClip(clip);

        // Name label styling
        name = new Label();
        name.setFont(Font.font("System", FontWeight.BOLD, 13));
        name.setTextFill(Color.BLACK);
        name.setMaxWidth(180);
        name.setStyle("-fx-padding: 5 12 5 5;");


        // Content container styling
        content = new HBox(15);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(7));
        content.setStyle("-fx-background-color: #f8f9fa;"
                + "-fx-background-radius: 8;"
                + "-fx-border-radius: 8;"
                + "-fx-border-color: #e0e0e0;"
                + "-fx-border-width: 0.5;");
        content.getChildren().addAll(profilePic, name);
    }

    @Override
    protected void updateItem(ContactInfo contact, boolean empty) {
        super.updateItem(contact, empty);

        if (empty || contact == null) {
            setGraphic(null);
            setText(null);
        } else {
            name.setText(contact.getName());
            name.setMaxWidth(180);
            name.setWrapText(true);

            try {
                if (contact.getPic() != null) {
                    profilePic.setImage(new Image(new ByteArrayInputStream(contact.getPic())));
                } else {
                    profilePic.setImage(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm()));
                }
            } catch (Exception e) {
                profilePic.setImage(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm()));
            }

            setGraphic(content);
            setText(null);
        }
    }
}