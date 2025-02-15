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
import jets.projects.entities.Group;

import java.io.ByteArrayInputStream;

public class GroupCard extends ListCell<Group> {
    private final HBox content;
    private final ImageView profilePic;
    private final Label name;
    private final Circle clip;

    public GroupCard() {
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
    protected void updateItem(Group group, boolean empty) {
        super.updateItem(group, empty);

        if (empty || group == null) {
            setGraphic(null);
            setText(null);
        } else {
            name.setText(group.getGroupName());
            name.setMaxWidth(180);
            name.setWrapText(true);

            try {
                if (group.getPic() != null) {
                    profilePic.setImage(new Image(new ByteArrayInputStream(group.getPic())));
                } else {
                    profilePic.setImage(new Image(getClass().getResource("/images/blank-group-picture.png").toExternalForm()));
                }
            } catch (Exception e) {
                profilePic.setImage(new Image(getClass().getResource("/images/blank-group-picture.png").toExternalForm()));
            }

            setGraphic(content);
            setText(null);
        }
    }
}