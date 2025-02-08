package jets.projects;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jets.projects.Controllers.*;

public class Director {
    private final Parent signInParent;
    private final SigninController signinController;
    private Scene signInScene;
    
    private final Parent signUpParent;
    private final SignUpController signUpController;
    private Scene signUpScene;

    private final Parent homeParent;
    private final HomeScreenController homeScreenController;
    private Scene homeScene;

    private final Parent loadingParent;
    private final LoadingController loadingController;
    private Scene loadingScene;

    final Stage stage;

    public Director(Stage stage, Parent signInParent, SigninController signinController, Parent signUpParent,
                    SignUpController signUpController, Parent homeParent, HomeScreenController homeScreenController, Parent loadingParent, LoadingController loadingController){
        this.stage = stage;
        this.signInParent = signInParent;
        this.signinController = signinController;
        this.signUpParent = signUpParent;
        this.signUpController = signUpController;
        this.homeParent = homeParent;
        this.homeScreenController = homeScreenController;
        this.loadingParent = loadingParent;
        this.loadingController = loadingController;
    }

    public void startWorking() {
       signInScene = new Scene(signInParent);
       signinController.setDirector(stage ,this);
       signUpScene = new Scene(signUpParent);
       signUpController.setDirector(stage,this);
       homeScene = new Scene(homeParent);
       homeScreenController.setDirector(stage,this);
       loadingScene = new Scene(loadingParent);
       loadingController.setDirector(stage,this);

       // set launch scene
       loadingController.perform();
       stage.setScene(loadingScene);
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
    public void loading(){
        loadingController.perform();
        stage.setScene(loadingScene);
    }
}
