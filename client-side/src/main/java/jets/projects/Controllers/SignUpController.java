package jets.projects.Controllers;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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
import jets.projects.Services.Request.ClientAuthenticationService;
import jets.projects.entities.Country;
import jets.projects.entities.Gender;
import jets.projects.entities.NormalUser;

public class SignUpController {
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
    private byte[] imageBytes;

    ClientAuthenticationService authenticationService;


    public void setDirector(Stage stage, Director myDirector) {
        this.stage = stage;
        this.myDirector = myDirector;
    }

    public void perform() {
        authenticationService = new ClientAuthenticationService();
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
                imageBytes = convertImageFileToByteArray(selectedFile.getAbsolutePath());
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
        if (!passwordField.getText().equals(confirmPasswordFiled.getText())) {
            ClientAlerts.invokeErrorAlert("Invalid Password", "Passwords do not match");
        }else{
            // save data
            NormalUser user = new NormalUser();
            user.setDisplayName(nameField.getText());
            user.setEmail(emailField.getText());
            user.setPhoneNumber(phoneField.getText());
            user.setPassword(passwordField.getText());
            user.setGender(Gender.valueOf(genderComboBox.getValue().toUpperCase()));
            user.setBirthDate(convertLocalDateToDate(dobField.getValue()));
            user.setCountry(Country.valueOf(countryComboBox.getValue().toUpperCase()));
            if (imageBytes != null)
                user.setPic(imageBytes);
            System.out.println(user);
            authenticationService.register(user);
        }
        myDirector.signin();
    }


    public static byte[] convertImageFileToByteArray(String imagePath) throws IOException {
        Path path = Paths.get(imagePath);
        return Files.readAllBytes(path);
    }
    public static Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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
    }
}
