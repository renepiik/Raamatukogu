package com.sarec.controllers;

import com.sarec.ConsoleInterface;
import com.sarec.Vars;
import com.sarec.components.Book;
import com.sarec.components.ISBN;
import com.sarec.components.Library;
import com.sarec.components.Status;
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

        //Uue Raamatukogu loomine
        Button newLibNupp = new Button("Loo uus raamatukogu");
        nimekiri.getChildren().add(newLibNupp);

        newLibNupp.setOnMouseClicked(me -> {

            Group layout = new Group();
            Scene newLibStseen = new Scene(layout, 400, 120);

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

            Button looNupp = new Button("Loo");
            looNupp.setLayoutY(50);
            looNupp.setLayoutX(5);
            layout.getChildren().add(looNupp);

            Text loomiseTekst = new Text("");
            loomiseTekst.setLayoutY(90);
            loomiseTekst.setLayoutX(5);
            layout.getChildren().add(loomiseTekst);

            Stage newLibAken = new Stage();
            newLibAken.setTitle("Uus Raamatukogu");
            newLibAken.setScene(newLibStseen);
            newLibAken.show();

            //RK Loomise event
            looNupp.setOnMouseClicked(mouseEvent -> {
                //Ei saa luua ilma nimeta (ega vaikimisi sätestatud nimega) raamatukogu
                if (!(newLibNimi.getText().equals("") || newLibNimi.getText().equals("Nimi"))) {
                    this.consoleInterface.createLibraryWithName(newLibNimi.getText());

                    //Notification
                    Group notifGroup = new Group();
                    Label notif = new Label("Uus Raamatukogu nimega " + newLibNimi.getText() + " on loodud.");
                    notif.setLayoutX(5);
                    Label rida2 = new Label("Vajutage nuppu \"Sulge\" või klaviatuuril Esc, et see aken sulgeda.");
                    rida2.setLayoutY(15);
                    rida2.setLayoutX(5);
                    Button notifButton = new Button("Sulge");
                    notifButton.setLayoutY(40);
                    notifButton.setLayoutX(5);
                    notifGroup.getChildren().addAll(notif, rida2, notifButton);

                    Scene notifScene = new Scene(notifGroup);
                    Stage notifStage = new Stage();
                    notifStage.setScene(notifScene);
                    notifStage.setTitle("Uus raamatukogu on loodud");

                    notifStage.setHeight(100.0);
                    notifStage.setWidth(350.0 + newLibNimi.getText().length() * 7);
                    notifStage.show();

                    //Sulgemise eventid (keyEvent ja mouseEvent)
                    notifScene.setOnKeyPressed(ke -> {
                        if (ke.getCode() == KeyCode.ESCAPE) notifStage.close();
                    });
                    notifButton.setOnMousePressed(mee -> notifStage.close());


                    //Et loetelu automaatselt uueneks
                    displayLibraries(libraries);
                }
            });//RK Loomise event

        });//NewLib nupu Event

        Text text = new Text();
        nimekiri.getChildren().add(text);

        //Raamatukogude genereerimine
        for (Library library : libraries) {
            String libraryName = library.getName();
            Label sidebarLibraryName = new Label(libraryName);
            sidebarLibraryName.getStyleClass().add("sidebarLibraryName");

            // text turns bold on hover
            sidebarLibraryName.setOnMouseEntered(mouseEvent ->
                    sidebarLibraryName.getStyleClass().add("sidebarLibraryNameOnHover"));

            // text turns back to normal when mouse leaves
            sidebarLibraryName.setOnMouseExited(mouseEvent ->
                    sidebarLibraryName.getStyleClass().removeAll("sidebarLibraryNameOnHover"));

            // loads the contents of library when its label is clicked
            sidebarLibraryName.setOnMouseClicked(mouseEvent -> {
                if (this.consoleInterface.getSelectedLibrary() != library) {
                    clearLibrary();
                    loadLibrary(library);
                }
            });

            nimekiri.getChildren().add(sidebarLibraryName);
        }

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
}
