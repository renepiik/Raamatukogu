package com.sarec;

import com.sarec.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
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
        root.getStyleClass().add("root");
        // get the instance of MainController that was initialized by FXMLLoader
        MainController mainController = mainFxmlLoader.getController();

        Scene mainScene = new Scene(root, 800, 600);
        ConsoleInterface console = ConsoleInterface.getInstance();
        mainController.setConsoleInterface(console);

        // load library selection to primaryStage
        ArrayList<Library> libraries = console.getLibraries();
        mainController.displayLibraries(libraries);

        //Raamatukoguse salvestameine toimub ka siis, kui peaaken ylevalt ristist kinni pannakse
        primaryStage.setOnCloseRequest(eh -> {
            try {
                console.quit();
                System.exit(1);
            } catch (IOException e) {
                System.out.println("Tekkis tõrge, programmi ei suudetud sulgeda.");
            }
        });

        primaryStage.getIcons().add(new Image("com/sarec/resources/icon.png"));
        primaryStage.setTitle("Raamatukogu");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}