package jets.projects;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entity_info.ContactMessagesInfo;

public class Utilities {
    
    public static ListCell<HBox> createCustomCell(){
        return new ListCell<>(){
            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);
        
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(item); // Set the custom layout
                }
            }
        };
    }
    public static <T, U> ListCell<T> createCustomCell(URL fxmlURL, BiConsumer<U, T> controllerSetup) {
        System.out.println("custom cell is called");
        return new ListCell<T>(){
            @Override
            protected void updateItem(T item,boolean empty){
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                }else{
                    try {
                        FXMLLoader loader = new FXMLLoader(fxmlURL);
                        Node cellContent = loader.load();
                        U controller = loader.getController();
                        controllerSetup.accept(controller , item);
                        setGraphic(cellContent); 
                    } catch (IOException e) {
                        e.printStackTrace();
                        setText("Error loading item.");
                    }
                }
            }
        };
    }
    public static void showPopup(Stage owner, URL fxmlURL, double width, double height) {
        try {
            // Load the popup content
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            Parent popupContent = loader.load();
            Stage popupStage = new Stage();
            popupStage.initOwner(owner); 
            popupStage.initModality(Modality.APPLICATION_MODAL); 

            // Apply blur to the owner stage
            GaussianBlur blur = new GaussianBlur();
            owner.getScene().getRoot().setEffect(blur);

            Scene scene = new Scene(popupContent, width, height);
            popupStage.setScene(scene);
            popupStage.setResizable(false);

            // Remove blur effect when popup closes
            popupStage.setOnCloseRequest(ev -> owner.getScene().getRoot().setEffect(null));
            popupStage.showAndWait(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // utility function to replace nodes based on clicked button
    public static Node getExistingNode(GridPane gridPane,int column, int row) {
        for (Node node : gridPane.getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);
            if (colIndex == null) colIndex = 0;
            if (rowIndex == null) rowIndex = 0;
            if (colIndex == column && rowIndex == row) {
                return node;
            }
        }
        return null; //node not found
    }
    public static void populateTree(TreeItem<String> rootItem, List<ContactInfo> contactList, Map<Integer, ContactMessagesInfo> messagesInfoMap) {
        for (ContactInfo contact : contactList) {
            ContactMessagesInfo currentMessagesInfo = messagesInfoMap.get(contact.getContact().getSecondID());
            rootItem.getChildren().add(createContactItem(contact,currentMessagesInfo.getUnread()));
        }
    }
    private static TreeItem<String> createContactItem(ContactInfo contact , int unread) {
        // Load profile picture (default if not available)
        ImageView profileImage = new ImageView();
        if (contact.getPic() != null){
            profileImage.setImage(new Image(new ByteArrayInputStream(contact.getPic())));
        } else {
            profileImage.setImage(new Image(Utilities.class.getResource("/images/blank-profile.png").toExternalForm()));
        }
        profileImage.setFitWidth(25);
        profileImage.setFitHeight(25);
        
        Label nameLabel = new Label(contact.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // Unread messages icon
        Label unreadLabel = new Label(String.valueOf(unread));
        unreadLabel.setStyle("-fx-text-fill: white; -fx-background-color: #80ced7;" +
                "-fx-min-width: 20px; -fx-min-height: 20px;" +
                "-fx-background-radius: 50%; -fx-border-radius: 50%; " +
                "-fx-alignment: center");

        HBox contactBox = new HBox(5, profileImage, nameLabel, unreadLabel);

        // Create TreeItem with HBox as its display node
        return new TreeItem<>(String.valueOf(contact.getContact().getSecondID()), contactBox);
    }
}
    
