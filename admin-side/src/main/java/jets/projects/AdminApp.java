package jets.projects;

import javafx.application.Application;
import javafx.stage.Stage;
import jets.projects.Controllers.*;

public class AdminApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Director director = new Director(primaryStage);
        director.startWorking();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

