package com.sarec.controllers;

import com.sarec.Console;
import com.sarec.ConsoleInterface;
import com.sarec.Vars;
import com.sarec.components.Book;
import com.sarec.components.PrimaryButton;
import com.sarec.components.Status;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
    @FXML Label uuendatudLabel;
    @FXML Label errorLabel;
    @FXML Label staatusLabel;
    @FXML Label selgitusLabel;

    private final ConsoleInterface consoleInterface;
    private final MainController mainController;
    private final Book book;
    private final FXMLLoader updateBookLoader;

    public UpdateBookController(Book book, ConsoleInterface consoleInterface, MainController mainController) {
        this.book = book;
        this.consoleInterface = consoleInterface;
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

    public ConsoleInterface getConsoleInterface() {
        return consoleInterface;
    }

    public void displayWindow() {
        Stage paramStage = new Stage();
        Scene paramScene = new Scene(updateBookLoader.getRoot());
        paramStage.setScene(paramScene);

        uuendaNupp.setStyle(Vars.ButtonStylePrimary);
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
