package jets.projects;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import datastore.DataCenter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jets.projects.entities.Group;
import jets.projects.entity_info.ContactInfo;

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
            popupStage.setResizable(true);

            // Remove blur effect when popup closes
            popupStage.setOnCloseRequest(ev -> owner.getScene().getRoot().setEffect(null));
            popupStage.showAndWait(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void showPopup(Stage owner, URL fxmlURL, double width, double height, Consumer<Object> controllerSetup) {
        try {
            // Load the popup content
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            Parent popupContent = loader.load();
            Object controller = loader.getController();
            if (controllerSetup != null && controller != null) {
                controllerSetup.accept(controller);
            }
            Stage popupStage = new Stage();
            popupStage.initOwner(owner);
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Apply blur to the owner stage
            GaussianBlur blur = new GaussianBlur();
            owner.getScene().getRoot().setEffect(blur);
            Scene scene = new Scene(popupContent, width, height);
            popupStage.setScene(scene);
            popupStage.setResizable(true);

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

    // creates contacts list treeview based on category
    public static void populateTree(TreeItem<String> rootItem, List<ContactInfo> contactList) {
        for (ContactInfo contact : contactList) {
            rootItem.getChildren().add(createContactItem(contact));
        }
    }
    public static TreeItem<String> createContactItem(ContactInfo contact){
        Map<Integer , IntegerProperty> unreadMessagesMap = DataCenter.getInstance().getUnreadContactMessages();

        Circle profileImage = new Circle();
        if (contact.getPic() != null){
            profileImage.setFill(new ImagePattern(new Image(new ByteArrayInputStream(contact.getPic()))));
        } else {
            profileImage.setFill(new ImagePattern(new Image(Utilities.class.getResource("/images/blank-profile.png").toExternalForm())));
        }
        profileImage.setRadius(25);
        Label nameLabel = new Label(contact.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // Unread messages icon
        int unread = unreadMessagesMap.getOrDefault(contact.getContact().getSecondID(),new SimpleIntegerProperty(0)).get();
        Label unreadLabel = new Label(String.valueOf(unread));
        unreadLabel.setStyle("-fx-text-fill: white; -fx-background-color: #80ced7;" +
                "-fx-min-width: 20px; -fx-min-height: 20px;" +
                "-fx-background-radius: 50%; -fx-border-radius: 50%; " +
                "-fx-alignment: center");
        if(unread==0) unreadLabel.setVisible(false);

        HBox contactBox = new HBox(5, profileImage, nameLabel, unreadLabel);

        // Create TreeItem with HBox as its display node
        return new TreeItem<>(String.valueOf(contact.getContact().getSecondID()), contactBox);
    }
    // populate group List with all user's groups
    public static void populateGroupsList(ListView<HBox> groupListView , List<Group> groupsList) {
        for(Group group : groupsList){
            HBox groupHbox = new HBox(10);
            Circle groupPic = new Circle();
            if (group.getPic() != null){
                groupPic.setFill(new ImagePattern(new Image(new ByteArrayInputStream(group.getPic()))));
            } else {
                groupPic.setFill(new ImagePattern(new Image(Utilities.class.getResource("/images/blank-group-picture.png").toExternalForm())));
            }
            groupPic.setRadius(25);
            Label groupname = new Label(group.getGroupName());
            groupname.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            Text groupID = new Text(String.valueOf(group.getGroupID()));
            groupID.setVisible(false);
            groupHbox.getChildren().addAll(groupPic,groupname,groupID);
            groupListView.getItems().add(groupHbox);
        }

    }
}
    
