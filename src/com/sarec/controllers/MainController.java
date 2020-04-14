package com.sarec.controllers;

import com.sarec.ConsoleInterface;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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

    public void displayLibraries(ArrayList<Library> libraries, ConsoleInterface console) {
        VBox nimekiri = new VBox();
        nimekiri.setPadding(new Insets(10));
        nimekiri.setBackground(new Background(
                new BackgroundFill(
                        Color.web("#fffffe"),
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
                    console.createLibraryWithName(newLibNimi.getText());
                    for (Library library : libraries) {
                        try {
                            library.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    loomiseTekst.setText("Uus Raamatukogu nimega " + newLibNimi.getText() + " on loodud.");

                    //Et loetelu automaatselt uueneks
                    displayLibraries(libraries, console);
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
                loadLibrary(library);
                mainBorderPane.setCenter(booksFlowPane);
            });

            nimekiri.getChildren().add(sidebarLibraryName);
        }

        mainBorderPane.setLeft(nimekiri);
    }

    // loads the books into booksFlowPane
    private void loadLibrary(Library library) {
        for (Book book : library.getBooks()) {
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

            booksFlowPane.getChildren().add(vBook);
        }
    }
}
