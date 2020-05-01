package com.sarec;

import com.sarec.controllers.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

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