package com.sarec.controllers;

import com.sarec.components.Book;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import com.sarec.components.Library;
import javafx.scene.shape.Rectangle;

public class MainController {
    @FXML
    BorderPane mainBorderPane;

    @FXML
    Label sidebarTitle;

    @FXML
    FlowPane booksFlowPane;

    public void displayLibraries(ArrayList<Library> libraries) {
        VBox nimekiri = new VBox();
        nimekiri.setPadding(new Insets(10));
        nimekiri.setBackground(new Background(
                new BackgroundFill(
                        Color.web("#fffffe"),
                        new CornerRadii(0.0),
                        new Insets(0))));

        nimekiri.getChildren().add(sidebarTitle);
        for (Library library : libraries) {
            String libraryName = library.getName();
            Label sidebarLibraryName = new Label(libraryName);
            sidebarLibraryName.getStyleClass().add("sidebarLibraryName");

            // text turns bold on hover
            sidebarLibraryName.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
                sidebarLibraryName.getStyleClass().add("sidebarLibraryNameOnHover");
            });

            // text turns back to normal when mouse leaves
            sidebarLibraryName.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
                sidebarLibraryName.getStyleClass().removeAll("sidebarLibraryNameOnHover");
            });

            // loads the contents of library when its label is clicked
            sidebarLibraryName.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
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
