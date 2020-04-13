package com.sarec;

import com.sarec.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.sarec.components.Library;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // fxml loader
        final FXMLLoader mainFxmlLoader = new FXMLLoader(getClass().getResource("resources/main.fxml"));
        Parent root = mainFxmlLoader.load();
        // get the instance of MainController that was initialized by FXMLLoader
        MainController mainController = mainFxmlLoader.getController();

        Scene mainScene = new Scene(root, 600, 400);
        ConsoleInterface console = ConsoleInterface.getInstance();

        // load library selection to primaryStage
        ArrayList<Library> libraries = console.getLibraries();
        mainController.displayLibraries(libraries);

        primaryStage.setTitle("Raamatukogu");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public void loadLibrary(Library library) {

    }

    public static void main(String[] args) {
        launch(args);
    }
}