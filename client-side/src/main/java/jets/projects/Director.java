package jets.projects;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jets.projects.Controllers.homeScreenController;
import jets.projects.Controllers.signUpController;
import jets.projects.Controllers.signinController;

public class Director {
    private final Parent signInParent;
    private final signinController signinController;
    private Scene signInScene;
    
    private final Parent signUpParent;
    private final signUpController signUpController;
    private Scene signUpScene;

    private final Parent homeParent;
    private final homeScreenController homeScreenController;
    private Scene homeScene;
    final Stage stage;

    public Director(Stage stage, Parent signInParent, signinController signinController, Parent signUpParent,
            signUpController signUpController, Parent homeParent, homeScreenController homeScreenController) {
        this.stage = stage;
        this.signInParent = signInParent;
        this.signinController = signinController;
        this.signUpParent = signUpParent;
        this.signUpController = signUpController;
        this.homeParent = homeParent;
        this.homeScreenController = homeScreenController;
    }

    public void startWorking() {
       signInScene = new Scene(signInParent);
       signinController.setDirector(stage ,this);
       signUpScene = new Scene(signUpParent);
       signUpController.setDirector(stage,this);
       homeScene = new Scene(homeParent);
       homeScreenController.setDirector(stage,this);

       // set launch scene
       signinController.perform();
       stage.setScene(signInScene);
       stage.show();
    }

    public void signin(){
        signinController.perform();
        stage.setScene(signInScene);
    }
    public void signup(){
        signUpController.perform();
        stage.setScene(signUpScene);
    }
    public void home(){
        homeScreenController.perform();
        stage.setScene(homeScene);
    }

}
