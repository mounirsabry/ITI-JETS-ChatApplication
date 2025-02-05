package jets.projects;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import jets.projects.Controllers.*;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        Director director = new Director(primaryStage);
        director.startWorking();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

