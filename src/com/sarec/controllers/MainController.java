package com.sarec.controllers;

import com.sarec.ConsoleInterface;
import com.sarec.Vars;
import com.sarec.components.Book;
import com.sarec.components.Library;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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


        //********************************************************

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

            //Loomise event
            looNupp.setOnMouseClicked(mouseEvent -> {
                //Ei saa luua ilma nimeta (ega vaikimisi sätestatud nimega) raamatukogu
                if (!(newLibNimi.getText().equals("") || newLibNimi.getText().equals("Nimi"))) {
                    this.consoleInterface.createLibraryWithName(newLibNimi.getText());
                    loomiseTekst.setText("Uus Raamatukogu nimega " + newLibNimi.getText() + " on loodud.");

                    //Et loetelu automaatselt uueneks
                    displayLibraries(libraries);
                }
            });//event

            //****************************************************


            Stage newLibAken = new Stage();
            newLibAken.setTitle("Uus Raamatukogu");
            newLibAken.setScene(newLibStseen);
            newLibAken.show();

        });//Uus aken

        Text text = new Text();
        nimekiri.getChildren().add(text);

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
    }

    public void clearLibrary() {
        booksFlowPane.getChildren().clear();
    }

    // loads the books into booksFlowPane
    private void loadLibrary(Library library) {
        // uue raamatu lisamise nupp
        VBox addBookArea = new VBox();
        addBookArea.getStyleClass().add("book");
        addBookArea.setSpacing(8);

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
    }

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
