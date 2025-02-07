package jets.projects.Controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Locale;

public class EditProfileController {
    
        @FXML
        private Circle profilePicture;
    
        @FXML
        private Button addprofilePictureButton;
    
        @FXML
        private Label username;
    
        @FXML
        private TextField emailField;
    
        @FXML
        private TextField phoneField;
    
        @FXML
        private TextField bioField;
    
        @FXML
        private ComboBox<String> genderComboBox;
    
        @FXML
        private DatePicker dobField;
    
        @FXML
        private ComboBox<String> countryComboBox;
    
        @FXML
        private Button saveButton;
        
        private Stage owner; 
        private Stage popupStage; 

        public void setOriginalStage(Stage stage) {
            this.owner = stage;
        }
    
        public void setPopupStage(Stage stage) {
            this.popupStage = stage;
        }
        @FXML
        void initialize(){
            // read logged in user info and initialize the textfields with it
            initCountry();
            genderComboBox.getItems().addAll("Female" , "Male");
        }
    
        @FXML
        void addProfilePictureButton(ActionEvent event) {
            FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
                File selectedFile = fileChooser.showOpenDialog((Stage) addprofilePictureButton.getScene().getWindow());

                if (selectedFile != null) {
                    try {
                        Image image = new Image(selectedFile.toURI().toString());
                        profilePicture.setFill(new ImagePattern(image));
                    } catch (Exception e) {
                        System.out.println("Could not load the image: " + e.getMessage());
                    }
                } else {
                    System.out.println("No file selected.");
                }
        }
    
        @FXML
        void handleSaveButton(ActionEvent event) {
            // save changes and close popup
            if (owner != null) {
                owner.getScene().getRoot().setEffect(null); // Remove blur effect
            }
            if (popupStage != null) {
                popupStage.close();
            }    
        }
    
        @FXML
        void handleStatusComboBox(ActionEvent event) {
            //update status and notify contacts
        }

        void initCountry(){
            // Get all country names
            String[] countryCodes = Locale.getISOCountries();
            ObservableList<String> countryList = FXCollections.observableArrayList();

            for (String countryCode : countryCodes) {
                Locale locale = new Locale("", countryCode);
                countryList.add(locale.getDisplayCountry());
            }
            FXCollections.sort(countryList);
            countryComboBox.setItems(countryList);
            addSearchFeature();
        }
        private void addSearchFeature() {
            countryComboBox.setEditable(true); 
    
            // Add a listener to filter items based on user input
            countryComboBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
                ObservableList<String> filteredList = FXCollections.observableArrayList();
    
                for (String country : countryComboBox.getItems()) {
                    if (country.toLowerCase().contains(newValue.toLowerCase())) {
                        filteredList.add(country);
                    }
                }
                countryComboBox.setItems(filteredList);
                if (!filteredList.isEmpty()) {
                    countryComboBox.show();
                }
            });
            // Restore the original list when focus is lost
            countryComboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused) {
                    countryComboBox.setItems(FXCollections.observableArrayList(Locale.getISOCountries())
                            .stream()
                            .map(code -> new Locale("", code).getDisplayCountry())
                            .sorted()
                            .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll));
                }
            });
        }
}
