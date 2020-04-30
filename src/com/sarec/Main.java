package com.sarec;

import com.sarec.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.sarec.components.Library;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        ConsoleInterface consoleInterface = ConsoleInterface.getInstance();
        MainController.getInstance(consoleInterface, primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}