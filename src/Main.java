import controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import components.Library;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // fxml loader
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("resources/main.fxml"));
        Parent root = fxmlLoader.load();
        // get the instance of MainController that was initialized by FXMLLoader
        MainController mainController = fxmlLoader.getController();

        Scene mainScene = new Scene(root, 600, 400, Color.WHITE);
        ConsoleInterface console = ConsoleInterface.getInstance();

        // load library selection to primaryStage
        ArrayList<Library> libraries = console.getLibraries();
        mainController.displayLibraries(libraries);

        primaryStage.setTitle("Raamatukogu");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}