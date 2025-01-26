package jets.projects;
import java.io.IOException;
import java.lang.ModuleLayer.Controller;
import java.net.URL;
import java.util.function.BiConsumer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
}
    
