package jets.projects;

import javafx.application.Application;
import javafx.stage.Stage;
import jets.projects.fxmlcontrollers.Director;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Director myDirector = new Director(stage);
        myDirector.startWorking();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}