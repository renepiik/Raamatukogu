package com.sarec.components;

import com.sarec.ConsoleInterface;
import com.sarec.controllers.MainController;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NewLibraryStage extends Stage {
    MainController mainController;

    public NewLibraryStage(MainController mainController) {
        super();
        this.mainController = mainController;
        buildStage();
    }

    public void buildStage() {
        Group layout = new Group();
        Scene newLibStseen = new Scene(layout, 240, 120);

        //Scene features
        Label tekst = new Label("Sisestage uue Raamatukogu nimi siia:");
        tekst.setLayoutX(5);
        layout.getChildren().add(tekst);

        TextField newLibNimi = new TextField("Nimi");
        newLibNimi.setPromptText("Nimi");
        newLibNimi.setPrefWidth(newLibStseen.getWidth()-10);
        newLibNimi.setLayoutY(20);
        newLibNimi.setLayoutX(5);
        layout.getChildren().add(newLibNimi);

        PrimaryButton looNupp = new PrimaryButton("Loo");
        looNupp.setLayoutY(50);
        looNupp.setLayoutX(5);
        layout.getChildren().add(looNupp);

        Text loomiseTekst = new Text("");
        loomiseTekst.setLayoutY(90);
        loomiseTekst.setLayoutX(5);
        layout.getChildren().add(loomiseTekst);

        this.setScene(newLibStseen);

        //Raamatukogu Loomise event
        looNupp.setOnMouseClicked(me -> {
            createLibraryButtonHandler(newLibNimi.getText());
        });
    }

    private void createLibraryButtonHandler(String newLibNimi) {
        //Ei saa luua ilma nimeta (ega vaikimisi sätestatud nimega) raamatukogu
        if (!(newLibNimi.equals("") || newLibNimi.equals("Nimi"))) {
            this.mainController.getConsoleInterface().createLibraryWithName(newLibNimi);

            //Notification
            Group notifGroup = new Group();
            Label notif = new Label("Uus Raamatukogu nimega " + newLibNimi + " on loodud.");
            notif.setLayoutX(5);
            Label rida2 = new Label("Vajutage nuppu \"Sulge\" või klaviatuuril Esc, et see aken sulgeda.");
            rida2.setLayoutY(15);
            rida2.setLayoutX(5);
            PrimaryButton notifButton = new PrimaryButton("Sulge");
            notifButton.setLayoutY(40);
            notifButton.setLayoutX(5);
            notifGroup.getChildren().addAll(notif, rida2, notifButton);

            Scene notifScene = new Scene(notifGroup);
            Stage notifStage = new Stage();
            notifStage.setScene(notifScene);
            notifStage.setTitle("Uus raamatukogu on loodud");

            notifStage.setHeight(120.0);
            notifStage.setWidth(350.0 + newLibNimi.length() * 7);
            notifStage.show();

            //Sulgemise eventid (keyEvent ja mouseEvent)
            notifScene.setOnKeyPressed(ke -> {
                if (ke.getCode() == KeyCode.ESCAPE) {
                    notifStage.close();
                    this.close();
                }
            });
            notifButton.setOnMousePressed(me -> {
                notifStage.close();
                this.close();
            });

            //Et loetelu automaatselt uueneks
            this.mainController.displayLibraries(this.mainController.getConsoleInterface().getLibraries());
        }
    }
}
