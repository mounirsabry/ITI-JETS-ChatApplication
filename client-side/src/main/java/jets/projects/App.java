package jets.projects;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jets.projects.Controllers.*;

public class App extends Application {

    private Parent signInParent;
    private signinController signinController;

    private Parent signUpParent;
    private signUpController signUpController;

    private Parent homeParent;
    private homeScreenController homeScreenController;

    @Override
    public void start(Stage stage) {
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        
        final String signinName = "/fxml/signin.fxml";
        final String signupName = "/fxml/signup.fxml";
        final String homeName = "/fxml/homeScreen.fxml";
        loadHomePage(homeName);
    	loadSignInPage(signinName);
        loadSignUpPage(signupName);
        
        if (signInParent == null || signinController == null 
        ||  signUpParent == null || signUpController == null
        ||  homeParent == null || homeScreenController == null) {
            System.out.println("Could not load some of the resources.");
            Text text = new Text("Could not load resources.");
            Scene errorScene = new Scene(new StackPane(text));
            stage.setScene(errorScene);
            stage.show();
        } else {
            Director myDirector = new Director(stage,
                signInParent, signinController,
                signUpParent, signUpController,
                homeParent, homeScreenController);
            myDirector.startWorking();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public void loadSignInPage(String pageName) {
        try { 
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName));
            signInParent = loader.load();
            signinController = loader.getController();
        } catch (IOException ex) {
            System.out.println("Error in load " + pageName + " function.");
            ex.printStackTrace();
        }      
    }
    
    public void loadSignUpPage(String pageName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName));
            signUpParent = loader.load();
            signUpController = loader.getController();
        } catch (IOException ex) {
            System.out.println("Error in load " + pageName + " function.");
            ex.printStackTrace();
        } 
    }
    
    public void loadHomePage(String pageName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName));
            homeParent = loader.load();
            homeScreenController = loader.getController();
        } catch (IOException ex) {
            System.out.println("Error in load " + pageName + " function.");
            ex.printStackTrace();
        } 
    }
}