package com.sarec.controllers;

import com.sarec.ConsoleInterface;
import com.sarec.Vars;
import com.sarec.components.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MainController {
    @FXML
    BorderPane mainBorderPane;

    @FXML
    Label sidebarTitle;

    @FXML
    FlowPane booksFlowPane;

    private ConsoleInterface consoleInterface;

    public void displayLibraries(ArrayList<Library> libraries) {

        // loob ScrollPane-i raamatute FlowPane ümber, et pikema nimekirja korral kerida saaks
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(booksFlowPane);
        scrollPane.styleProperty().set("-fx-background-color: "+Vars.BackgroundColor);

        // horisontaalselt kerida ei saa
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        // tirib oma contenti horisontaalses suunas kaasa
        scrollPane.fitToWidthProperty().set(true);
        // saab trackpadiga nimekirja kerida
        scrollPane.pannableProperty().set(true);

        mainBorderPane.setCenter(scrollPane);

        VBox nimekiri = new VBox();
        nimekiri.setPadding(new Insets(10));
        nimekiri.setBackground(new Background(
                new BackgroundFill(
                        Vars.BackgroundPaint,
                        new CornerRadii(0.0),
                        new Insets(0))));
        nimekiri.getChildren().add(sidebarTitle);

        //Raamatukogude genereerimine
        for (Library library : libraries) {
            String libraryName = library.getName();
            SecondaryButton sidebarLibraryName = new SecondaryButton(libraryName);
            sidebarLibraryName.setStyle(Vars.ButtonStyleSecondary);
            sidebarLibraryName.getStyleClass().add("sidebarLibraryName");

            // loads the contents of library when its button is clicked
            sidebarLibraryName.setOnMouseClicked(mouseEvent -> {
                if (this.consoleInterface.getSelectedLibrary() != library) {
                    clearLibrary();
                    loadLibrary(library);
                }
            });

            HBox spacer = new HBox();
            spacer.setMinHeight(10);

            nimekiri.getChildren().addAll(sidebarLibraryName, spacer);
        }

        //Uue Raamatukogu loomine
        PrimaryButton newLibNupp = new PrimaryButton("Loo uus raamatukogu");
        newLibNupp.getStyleClass().add("newLibNupp");
        nimekiri.getChildren().add(newLibNupp);

        newLibNupp.setOnMouseClicked(me -> {
            NewLibraryStage newLibAken = new NewLibraryStage(this);
            newLibAken.setTitle("Uus Raamatukogu");
            newLibAken.show();
        });

        mainBorderPane.setLeft(nimekiri);

    }//displayLibraries

    public void clearLibrary() {
        booksFlowPane.getChildren().clear();
    }

    // loads the books into booksFlowPane
    private void loadLibrary(Library library) {
        // uue raamatu lisamise nupp
        VBox addBookArea = new VBox();
        addBookArea.getStyleClass().add("book");
        addBookArea.setSpacing(8);

        //Raamatud
        HBox bookCover = new HBox();
        bookCover.getStyleClass().add("bookCover");
        bookCover.styleProperty().set("-fx-background-color: "+Vars.ButtonColor);
        bookCover.setAlignment(Pos.CENTER);
        bookCover.setMinHeight(130);
        bookCover.setMinWidth(100);

        ImageView plus = new ImageView(new Image("com\\sarec\\resources\\plus-solid.png"));
        plus.setFitHeight(32);
        plus.setFitWidth(32);
        bookCover.getChildren().add(plus);

        Label title = new Label("Uus raamat");
        title.getStyleClass().add("titleText");

        addBookArea.getChildren().addAll(bookCover, title);
        booksFlowPane.getChildren().add(addBookArea);

        // kõik raamatud raamatukogus
        for (Book book : library.getBooks()) {
            VBox vBook = createBookVBox(book);
            booksFlowPane.getChildren().add(vBook);
        }

        //Raamatu lisamise event
        addBookArea.setOnMouseClicked(me -> {
            Group abGroup = new Group();

            //Features
            Label infoLabel = new Label("Sisestage raamatu parameetrid. Pealkiri ja autor peavad olema sisestatud");
            TextField pealkiriTF = new TextField("Pealkiri");
            TextField autorTF = new TextField("");
            TextField ilmumineTF = new TextField("");
            TextField genreTF = new TextField("");
            TextField isbnTF = new TextField("");
            Button lisaNupp = new Button("Lisa raamat");
            Label lisatudLabel = new Label("");

            abGroup.getChildren().addAll(infoLabel,pealkiriTF,autorTF,ilmumineTF,genreTF,isbnTF,lisaNupp,lisatudLabel);

            lisaNupp.setOnMouseClicked(mee -> {
                if (!(pealkiriTF.getText().equals("") || pealkiriTF.getText().equals("Pealkiri"))
                        && !autorTF.getText().equals("")) {
                    try {
                        Book uusRaamt = new Book(pealkiriTF.getText(), autorTF.getText(), ilmumineTF.getText(),
                                                genreTF.getText(), new ISBN(), Status.DEFAULT);
                        library.addBook(uusRaamt);
                        lisatudLabel.setText("Raamat pealkirjaga "+ pealkiriTF.getText() +
                                            " ja autoriga "+autorTF.getText() +" on loodud");

                        //Et automaatselt vaade uueneks
                        VBox vBook = createBookVBox(uusRaamt);
                        booksFlowPane.getChildren().add(vBook);
                    } catch (Exception e) {
                        System.out.println("Tekkis tõrge, Raamtu loomisega ei saadud hakkama.");
                    }

                }
            });

            //Vaikimisitekstid, kui kast ei ole aktiivne
            pealkiriTF.setPromptText("Pealkiri");
            autorTF.setPromptText("Autor");
            ilmumineTF.setPromptText("Ilmumisaasta");
            genreTF.setPromptText("Žanr");
            isbnTF.setPromptText("ISBN");

            //Alignment
            infoLabel.setLayoutX(5);
            pealkiriTF.setLayoutX(5);
            autorTF.setLayoutX(5);
            ilmumineTF.setLayoutX(5);
            genreTF.setLayoutX(5);
            isbnTF.setLayoutX(5);
            lisaNupp.setLayoutX(5);
            lisatudLabel.setLayoutX(5);

            pealkiriTF.setLayoutY(20);
            autorTF.setLayoutY(45);
            ilmumineTF.setLayoutY(70);
            genreTF.setLayoutY(95);
            isbnTF.setLayoutY(120);
            lisaNupp.setLayoutY(145);
            lisatudLabel.setLayoutY(170);
            // /Alignment

            Scene abScene = new Scene(abGroup);
            Stage abStage = new Stage();

            abStage.setScene(abScene);
            abStage.setTitle("Uue raamatu loomine");
            abStage.setHeight(500);
            abStage.setWidth(500);

            abStage.show();
        });//Raamatu lisamise event

    }//loadLibrary

    public VBox createBookVBox(Book book) {
        VBox vBook = new VBox();
        vBook.setSpacing(8);
        vBook.getStyleClass().add("book");

        HBox bookCover = new HBox();
        bookCover.setAlignment(Pos.CENTER);
        Rectangle cover = new Rectangle();
        cover.setHeight(130);
        cover.setWidth(100);
        cover.getStyleClass().add("bookCover");
        bookCover.getChildren().add(cover);

        Label title = new Label(book.getTitle());
        title.getStyleClass().add("titleText");
        Label author = new Label(book.getAuthorName());
        author.getStyleClass().add("authorText");

        vBook.getChildren().addAll(bookCover, title, author);

        return vBook;
    }

    public void setConsoleInterface(ConsoleInterface consoleInterface) {
        this.consoleInterface = consoleInterface;
    }

    public ConsoleInterface getConsoleInterface() {
        return consoleInterface;
    }
}
