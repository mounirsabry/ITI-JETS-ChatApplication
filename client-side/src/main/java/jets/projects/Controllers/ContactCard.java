package jets.projects.Controllers;

import datastore.DataCenter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import jets.projects.entities.ContactMessage;
import jets.projects.entity_info.ContactInfo;

import java.io.ByteArrayInputStream;

public class ContactCard extends ListCell<ContactInfo> {
    private final HBox content;
    private final ImageView profilePic;
    private final Label name;
    private final Label unreadCountBadge;
    private final Circle clip;
    private final StackPane unreadContainer;
    private final Region spacer;

    public ContactCard() {
        // Profile Picture Setup
        clip = new Circle(22, 22, 22); // Smaller radius
        clip.setStroke(Color.WHITE);
        clip.setStrokeWidth(1);

        profilePic = new ImageView();
        profilePic.setFitWidth(44); // Reduced width
        profilePic.setFitHeight(44); // Reduced height
        profilePic.setPreserveRatio(true);
        profilePic.setClip(clip);

        // Name Label Styling
        name = new Label();
        name.setFont(Font.font("System", FontWeight.BOLD, 12)); // Slightly smaller font
        name.setTextFill(Color.BLACK);
        name.setMaxWidth(170); // Reduced width
        name.setWrapText(true);
        name.setStyle("-fx-padding: 3 10 3 3;"); // Adjusted padding

        // Unread Messages Badge
        unreadCountBadge = new Label();
        unreadCountBadge.setFont(Font.font("System", FontWeight.BOLD, 11)); // Smaller font
        unreadCountBadge.setTextFill(Color.web("#0077C8")); // Blue color
        unreadCountBadge.setAlignment(Pos.CENTER);

        // Circular Badge
        Circle badgeCircle = new Circle(9, Color.WHITE); // Smaller badge
        badgeCircle.setStroke(Color.web("#0077C8"));
        badgeCircle.setStrokeWidth(1.8);

        unreadContainer = new StackPane();
        unreadContainer.getChildren().addAll(badgeCircle, unreadCountBadge);
        unreadContainer.setMinSize(18, 18);
        unreadContainer.setMaxSize(18, 18);
        unreadContainer.setAlignment(Pos.CENTER);
        unreadContainer.setPadding(new Insets(0, 8, 0, 0)); // Smaller padding

        // Spacer
        spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Container Styling
        content = new HBox(8); // Reduced spacing
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(5, 5, 5, 5)); // Reduced padding
        content.setStyle("-fx-background-color: #f8f9fa;"
                + "-fx-background-radius: 6;" // Smaller radius
                + "-fx-border-radius: 6;"
                + "-fx-border-color: #e0e0e0;"
                + "-fx-border-width: 0.5;");

        // Adding Elements
        content.getChildren().addAll(profilePic, name, spacer, unreadContainer);
    }

    @Override
    protected void updateItem(ContactInfo contact, boolean empty) {
        super.updateItem(contact, empty);

        if (empty || contact == null) {
            setGraphic(null);
            setText(null);
        } else {
            name.setText(contact.getName());

            try {
                if (contact.getPic() != null) {
                    profilePic.setImage(new Image(new ByteArrayInputStream(contact.getPic())));
                } else {
                    profilePic.setImage(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm()));
                }
            } catch (Exception e) {
                profilePic.setImage(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm()));
            }

            DataCenter.getInstance().getUnreadContactMessages()
                    .putIfAbsent(contact.getContact().getSecondID(), new SimpleIntegerProperty());

            DataCenter.getInstance().getUnreadContactMessages().get(contact.getContact().getSecondID())
                    .addListener((obs, oldVal, newVal) -> {
                        int total = DataCenter.getInstance().getUnreadContactMessages().values()
                                .stream().mapToInt(prop -> prop.get()).sum();
                        DataCenter.getInstance().getTotalUnreadMessages().set(total);
                    });
            System.out.println(DataCenter.getInstance().getContactMessagesMap().get(contact.getContact().getSecondID()));
            DataCenter.getInstance().getContactMessagesMap().get(contact.getContact().getSecondID())
                    .addListener((ListChangeListener<ContactMessage>) change -> {
                        DataCenter.getInstance().getUnreadContactMessages().get(contact.getContact().getSecondID())
                                .set((int) DataCenter.getInstance().getContactMessagesMap().get(contact.getContact().getSecondID())
                                        .stream()
                                        .filter(m -> m.getSenderID() == contact.getContact().getSecondID() && !m.getIsRead())
                                        .count());

                    });

            // Bind unread messages count
            unreadCountBadge.textProperty().bind(Bindings.createStringBinding(
                    () -> {
                        int count = DataCenter.getInstance().getUnreadContactMessages()
                                .get(contact.getContact().getSecondID()).get();
                        return count > 0 ? String.valueOf(count) : "";
                    },
                    DataCenter.getInstance().getUnreadContactMessages()
                            .get(contact.getContact().getSecondID())
            ));

            // Hide the badge if no unread messages
            unreadContainer.visibleProperty().bind(
                    DataCenter.getInstance().getUnreadContactMessages()
                            .get(contact.getContact().getSecondID())
                            .greaterThan(0)
            );

            setGraphic(content);
            setText(null);
        }
    }
}
