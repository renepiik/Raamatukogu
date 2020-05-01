package com.sarec.controllers;

import com.sarec.Vars;
import com.sarec.components.Book;
import com.sarec.components.Library;
import com.sarec.components.PrimaryButton;
import com.sarec.components.Status;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdateBookController {
    @FXML TextField pealkiriPar;
    @FXML TextField autorPar;
    @FXML TextField ilmuminePar;
    @FXML TextField genrePar;
    @FXML TextField isbnPar;
    @FXML ComboBox<Status> statusPar;
    @FXML Button uuendaNupp;
    @FXML Button kustutaNupp;
    @FXML Label errorLabel;

    private final MainController mainController;
    private final Book book;
    private final Library library;
    private final FXMLLoader updateBookLoader;

    public UpdateBookController(Book book, Library library, MainController mainController) {
        this.book = book;
        this.library = library;
        this.mainController = mainController;

        this.updateBookLoader = new FXMLLoader(getClass().getResource("../resources/updateBook.fxml"));
        updateBookLoader.setController(this);

        try {
            // ürita avada uus aken, kus raamatut muuta
            updateBookLoader.load();
            displayWindow();

        } catch (IOException e) {
            // kui vastavat fxml faili ei leia
            // TODO: error akna avamine

            e.printStackTrace();
        }
    }

    public void displayWindow() {
        Stage paramStage = new Stage();
        Scene paramScene = new Scene(updateBookLoader.getRoot());
        paramStage.setScene(paramScene);

        uuendaNupp.setStyle(Vars.ButtonStylePrimary);
        kustutaNupp.setStyle(Vars.ButtonStyleDanger);
        statusPar.setStyle(Vars.ButtonStyleSecondary);

        pealkiriPar.setText(book.getTitle());
        autorPar.setText(book.getAuthorName());
        ilmuminePar.setText(book.getPublicationDate());
        genrePar.setText(book.getGenre());
        isbnPar.setText(book.getISBN().toString());
        statusPar.setValue(book.getStatus());

        // raamatu uuendamise event
        uuendaNupp.setOnMouseClicked(mouseEvent -> {
            book.setTitle(pealkiriPar.getText());
            book.setAuthorName(autorPar.getText());
            book.setPublicationDate(ilmuminePar.getText());
            book.setGenre(genrePar.getText());
            book.setStatus(statusPar.getValue());
            try {
                book.setISBN(isbnPar.getText());
                paramStage.close();
                // uuenda valitud raamatukogu listi
                mainController.refreshLibrary();
            } catch (Exception e) {
                errorLabel.setText("Tekkis tõrge, ei suudetud ISBN-i muuta");
            }
        });

        // raamatu kustutamise event
        kustutaNupp.setOnMouseClicked(mouseEvent -> {
            // TODO: kinnitamise akna jaoks teha uus klass

            VBox root = new VBox();
            root.setSpacing(16);
            root.setPadding(new Insets(8));
            Scene confirmScene = new Scene(root);
            Stage confirmWindow = new Stage();

            Button confirmButton = new Button("Jah");
            confirmButton.setOnMouseClicked(mouseEvent1 -> {
                library.removeBook(book);
                confirmWindow.close();
                paramStage.close();
                mainController.refreshLibrary();
            });

            PrimaryButton closeButton = new PrimaryButton("Ei");
            confirmButton.setStyle(Vars.ButtonStyleDanger);
            closeButton.setOnMouseClicked(mouseEvent1 -> confirmWindow.close());

            HBox row = new HBox();
            row.setPadding(new Insets(8));
            row.setSpacing(16);
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            row.getChildren().addAll(confirmButton, spacer, closeButton);

            root.getChildren().add(new Label("Kas oled kindel, et soovid raamatu kustutada?"));
            root.getChildren().add(row);

            confirmWindow.setScene(confirmScene);
            confirmWindow.show();

        });

        pealkiriPar.setPromptText("Pealkiri");
        autorPar.setPromptText("Autor");
        ilmuminePar.setPromptText("Ilmumisaasta");
        genrePar.setPromptText("Žanr");
        isbnPar.setPromptText("ISBN, võite tühjaks jätta");
        statusPar.getItems().addAll(Status.values());
        statusPar.setPlaceholder(new Text("Vali staatus"));

        paramStage.show();
    }
}
