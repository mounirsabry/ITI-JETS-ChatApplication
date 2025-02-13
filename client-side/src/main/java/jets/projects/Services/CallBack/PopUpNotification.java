package jets.projects.Services.CallBack;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.stage.Screen;

import java.util.concurrent.atomic.AtomicReference;

public class PopUpNotification {

    private static final double NOTIFICATION_HEIGHT = 100; // Height of each notification
    private static final double PADDING = 10; // Padding between notifications
    private static final double NOTIFICATION_WIDTH = 300; // Width of each notification
    private static final double DURATION_SECONDS = 5; // Duration to display the notification

    // Use AtomicReference for thread-safe updates to lastY
    private static final AtomicReference<Double> lastY = new AtomicReference<>(
            Screen.getPrimary().getVisualBounds().getHeight() - NOTIFICATION_HEIGHT - PADDING
    );

    public static void showNotification(String message) {
        // Ensure the notification stays within screen bounds
        if (lastY.get() < 0) {
            lastY.set(Screen.getPrimary().getVisualBounds().getHeight() - NOTIFICATION_HEIGHT - PADDING);
        }

        // Create a new stage for the pop-up
        Stage popupStage = new Stage();
        popupStage.initStyle(StageStyle.TRANSPARENT); // Remove all window decorations (border, close button, etc.)
        popupStage.setAlwaysOnTop(true); // Ensure the pop-up is always on top

        // Load the logo image from resources
        String logoPath = "/images/logo.png"; // Correct path to the logo image
        try {
            System.out.println("Loading image from: " + logoPath);
            Image logoImage = new Image(PopUpNotification.class.getResource(logoPath).toExternalForm());
            if (logoImage.isError()) {
                System.err.println("Failed to load logo image.");
            } else {
                System.out.println("Logo image loaded successfully.");
            }

            ImageView logoView = new ImageView(logoImage);
            logoView.setFitHeight(40); // Set logo height
            logoView.setFitWidth(40); // Set logo width
            logoView.setPreserveRatio(true); // Maintain aspect ratio

            // Create a label to display the notification message
            Label label = new Label(message);
            label.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");

            // Create a layout for the logo and message
            HBox contentBox = new HBox(10, logoView, label); // 10 is the spacing between logo and text
            contentBox.setAlignment(Pos.CENTER_LEFT); // Align logo and text to the left
            contentBox.setPadding(new Insets(10)); // Add padding around the content

            // Create a background for the pop-up
            Rectangle background = new Rectangle(NOTIFICATION_WIDTH, NOTIFICATION_HEIGHT);
            background.setArcWidth(20); // Rounded corners
            background.setArcHeight(20); // Rounded corners
            background.setFill(Color.rgb(50, 50, 50, 0.9)); // Dark semi-transparent background

            // Create a layout for the pop-up
            StackPane root = new StackPane(background, contentBox);
            root.setAlignment(Pos.CENTER);
            root.setStyle("-fx-background-color: transparent;"); // Ensure the root pane is transparent

            // Create the scene
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT); // Transparent background for the scene
            popupStage.setScene(scene);

            // Position the pop-up at the bottom-right corner of the screen
            double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
            popupStage.setX(screenWidth - NOTIFICATION_WIDTH - PADDING); // X position (right side)
            popupStage.setY(lastY.get()); // Y position (stacked above the last notification)

            // Update the lastY position for the next notification
            lastY.updateAndGet(y -> y - (NOTIFICATION_HEIGHT + PADDING));

            // Show the pop-up
            popupStage.show();

            // Fade-in animation
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            // Create a Timeline to close the pop-up after the specified duration
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(DURATION_SECONDS), e -> {
                // Fade-out animation
                FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), root);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(event -> {
                    popupStage.close();
                    // Reset the lastY position when the notification is closed
                    lastY.updateAndGet(y -> y + (NOTIFICATION_HEIGHT + PADDING));
                });
                fadeOut.play();
            }));
            timeline.setCycleCount(1); // Run once
            timeline.play();
        } catch (Exception e) {
            System.err.println("Error loading logo image: " + e.getMessage());
            e.printStackTrace();
        }
    }
}