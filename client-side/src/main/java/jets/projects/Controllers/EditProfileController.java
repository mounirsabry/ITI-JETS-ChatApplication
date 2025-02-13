package jets.projects.Controllers;
import datastore.DataCenter;
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
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jets.projects.Services.Request.ClientProfileService;
import jets.projects.entities.NormalUser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class EditProfileController {
    private static HomeScreenController homeScreenController;
    public static void setHome(HomeScreenController home){
        homeScreenController = home;
    }
        byte[] imageBytes = null;
        @FXML
        private Circle profilePicture;
        @FXML
        private Button addprofilePictureButton;
        @FXML
        private TextField username;
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
        void initialize(){
            // read logged-in user info and initialize the text fields with it

            NormalUser myprofileInfo = DataCenter.getInstance().getMyProfile();
            if(myprofileInfo.getPic()!=null){
                profilePicture.setFill(new ImagePattern(new Image(new ByteArrayInputStream(myprofileInfo.getPic()))));
            }else{
                profilePicture.setFill(new ImagePattern(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm())));
            }
            username.setText(myprofileInfo.getDisplayName());
            emailField.setText(myprofileInfo.getEmail());
            phoneField.setText(myprofileInfo.getPhoneNumber());
            bioField.setText(myprofileInfo.getBio());
            genderComboBox.getItems().addAll("Female", "Male");
            genderComboBox.setValue(myprofileInfo.getGender().toString());
            if(myprofileInfo.getBirthDate() != null){
                LocalDate localDate = myprofileInfo.getBirthDate().toInstant() // Convert Date to Instant
                        .atZone(ZoneId.systemDefault()) // Convert Instant to ZonedDateTime
                        .toLocalDate();
                dobField.setValue(localDate);
            }
            if(myprofileInfo.getCountry() != null){
                countryComboBox.setValue(myprofileInfo.getCountry().name());
            }

        }
    
        @FXML
        void addProfilePictureButton(ActionEvent event) {
            FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
                File selectedFile = fileChooser.showOpenDialog((Stage) addprofilePictureButton.getScene().getWindow());

                if (selectedFile != null) {
                    try {
                        imageBytes = Files.readAllBytes(selectedFile.toPath());
                        //Image image = new Image(selectedFile.toURI().toString());

                        profilePicture.setFill(new ImagePattern(new Image(new ByteArrayInputStream(imageBytes))));
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
            String bio = bioField.getText().trim();
            java.time.LocalDate dob = dobField.getValue();
            Date birthDate = (dob != null) ? java.util.Date.from(dob.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()) : null;


          ClientProfileService profileService = new ClientProfileService();
          if(imageBytes == null){
              imageBytes = DataCenter.getInstance().getMyProfile().getPic();
          }
          boolean success = profileService.editProfile(username.getText(), birthDate, bio, imageBytes);

          if (success) {
              //DataCenter.getInstance().getMyProfile().setPic();
                  ClientAlerts.invokeInformationAlert("Saved","Edit Successfully");
                  homeScreenController.updateProfile();
          }
          else{

              ClientAlerts.invokeErrorAlert("Error", "Cant Save Edit Profile");
          }

          }
          private byte[] convertImageToByteArray(Image image) {
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();

            WritableImage writableImage = new WritableImage(width, height);
            PixelReader pixelReader = image.getPixelReader();
            PixelWriter pixelWriter = writableImage.getPixelWriter();

            // Copy pixels manually
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pixelWriter.setArgb(x, y, pixelReader.getArgb(x, y));
                }
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            try {
                // Manually encode the image as raw pixel data
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int argb = pixelReader.getArgb(x, y);
                        dataOutputStream.writeInt(argb);
                    }
                }
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return byteArrayOutputStream.toByteArray();
        }
        void initCountry(){
            // Get all country names
            String[] countryCodes = Locale.getISOCountries();
            ObservableList<String> countryList = FXCollections.observableArrayList();

            for (String countryCode : countryCodes) {
                Locale locale = new Locale("", countryCode);
                countryList.add(locale.getDisplayCountry().toString().toUpperCase());
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
