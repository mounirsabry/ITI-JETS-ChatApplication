package jets.projects.Controllers;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jets.projects.Director;

public class SignUpController {

    @FXML
    private ToggleButton signInToggleButton;

    @FXML
    private ToggleButton signUpToggleButton;

    @FXML
    private Circle profilepicture;

    @FXML
    private Button addprofilePictureButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordFiled;

    @FXML
    private TextField phoneField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private DatePicker dobField;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private Button signUpButton;
    private Stage stage;
    private Director myDirector;

    public void setDirector(Stage stage, Director myDirector) {
        this.stage = stage;
        this.myDirector = myDirector;
    }

    public void perform() {
        genderComboBox.getItems().addAll("Female" , "Male");
        profilepicture.setFill(new ImagePattern(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm())));
        initCountry();
    }
    @FXML
    void addProfilePictureButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog((Stage) addprofilePictureButton.getScene().getWindow());

        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                profilepicture.setFill(new ImagePattern(image));
            } catch (Exception e) {
                System.out.println("Could not load the image: " + e.getMessage());
            }
        } else {
            System.out.println("No file selected.");
        }
    }

    @FXML
    void handleSignInToggle(ActionEvent event) throws IOException {
        myDirector.signin();
    }
    @FXML
    void handleSignUpToggle(ActionEvent event) throws IOException {
        myDirector.signup();
    }
    @FXML
    void handleSignUpButton(ActionEvent event) throws IOException {
       myDirector.signin();
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
