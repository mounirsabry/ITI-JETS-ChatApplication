package jets.projects.Controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class AddContactController {
    
@FXML
    private Button addContactButton;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TextField customCategoryTextField;
    
    private Stage owner; 
    private Stage popupStage; 

    @FXML
    private void initialize(){
        categoryComboBox.getItems().addAll("Family", "Friends", "Work", "Other");
    }
    public void setOriginalStage(Stage stage) {
        this.owner = stage;
    }

    public void setPopupStage(Stage stage) {
        this.popupStage = stage;
    }

    @FXML
    void handleAddContact(ActionEvent event) {
        // save data
        if (owner != null) {
            owner.getScene().getRoot().setEffect(null); // Remove blur effect
        }
        if (popupStage != null) {
            popupStage.close();
        }
    }

    @FXML
    void handleCategoryComboBox(ActionEvent event) {
        if ("Other".equals(categoryComboBox.getValue())) {
            customCategoryTextField.setVisible(true);
        } else {
            customCategoryTextField.setVisible(false);
        }
    }

    @FXML
    void handleCustomCategory(ActionEvent event) {
        //save choice
    }
    
}
