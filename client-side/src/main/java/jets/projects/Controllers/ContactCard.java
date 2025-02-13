package jets.projects.Controllers;

import datastore.DataCenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.effect.DropShadow;
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
    private final DropShadow shadowEffect;

    public ContactCard() {
        // Create circular clip for profile picture with border
        clip = new Circle(30, 30, 30);
        clip.setStroke(Color.WHITE);
        clip.setStrokeWidth(2);

        profilePic = new ImageView();
        profilePic.setFitWidth(60);
        profilePic.setFitHeight(60);
        profilePic.setPreserveRatio(true);
        profilePic.setClip(clip);

        // Name label styling
        name = new Label();
        name.setFont(Font.font("System", FontWeight.BOLD, 14));
        name.setTextFill(Color.WHITE);
        name.setMaxWidth(200);
        name.setStyle("-fx-padding: 8 15 8 15;"
                + "-fx-background-radius: 15;"
                + "-fx-background-color: linear-gradient(to right, #2c3e50, #3498db);"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);");

        // Content container styling
        content = new HBox(20);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(10));
        content.setStyle("-fx-background-color: #f8f9fa;"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;"
                + "-fx-border-color: #e0e0e0;"
                + "-fx-border-width: 1;");
        content.getChildren().addAll(profilePic, name);

        // Hover effect
        shadowEffect = new DropShadow();
        shadowEffect.setColor(Color.rgb(0, 0, 0, 0.2));
        shadowEffect.setRadius(10);
        shadowEffect.setSpread(0.05);

        content.setOnMouseEntered(e -> {
            content.setEffect(shadowEffect);
            content.setStyle(content.getStyle() + "-fx-background-color: #ffffff;");
        });

        content.setOnMouseExited(e -> {
            content.setEffect(null);
            content.setStyle(content.getStyle().replace("-fx-background-color: #ffffff;", "-fx-background-color: #f8f9fa;"));
        });
    }

    @Override
    protected void updateItem(ContactInfo contact, boolean empty) {
        super.updateItem(contact, empty);

        if (empty || contact == null) {
            setGraphic(null);
            setText(null);
        } else {
            name.setText(contact.getName());

            // Text overflow handling
            name.setMaxWidth(200);
            name.setWrapText(true);
            name.setEllipsisString("...");

            try {
                if (contact.getPic() != null) {
                    profilePic.setImage(new Image(new ByteArrayInputStream(contact.getPic())));
                } else {
                    profilePic.setImage(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm()));
                }
            } catch (Exception e) {
                profilePic.setImage(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm()));
            }

            // Add subtle animation when loading
            profilePic.setOpacity(0);
            javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(
                    javafx.util.Duration.millis(300), profilePic
            );
            ft.setToValue(1);
            ft.play();

            setGraphic(content);
            setText(null);
        }
    }
}