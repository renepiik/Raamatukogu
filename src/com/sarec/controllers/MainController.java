package com.sarec.controllers;

import com.sarec.ConsoleInterface;
import com.sarec.Vars;
import com.sarec.components.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.swing.text.LabelView;
import java.io.IOException;
import java.util.ArrayList;

public class MainController {
    @FXML BorderPane mainBorderPane;
    @FXML Label sidebarTitle;
    @FXML FlowPane booksFlowPane;
    @FXML HBox topBar;
    @FXML Button deleteLibraryButton;
    @FXML Button updateLibraryButton;
    @FXML Label selectedLibraryLabel;

    private final ConsoleInterface consoleInterface;

    private static MainController instance = null;

    public static void getInstance(ConsoleInterface consoleInterface, Stage primaryStage) {
        if (instance == null) instance = new MainController(consoleInterface, primaryStage);
    }

    public MainController(ConsoleInterface consoleInterface, Stage primaryStage) {
        final FXMLLoader mainFxmlLoader = new FXMLLoader(getClass().getResource("../resources/main.fxml"));
        mainFxmlLoader.setController(this);
        this.consoleInterface = consoleInterface;

        try {
            mainFxmlLoader.load();

            // load library selection to primaryStage
            displayLibraries(this.consoleInterface.getLibraries());

            //Raamatukoguse salvestamine toimub ka siis, kui peaaken ülevalt ristist kinni pannakse
            primaryStage.setOnCloseRequest(eh -> {
                try {
                    this.consoleInterface.quit();
                    System.exit(1);
                } catch (IOException e) {
                    System.out.println("Tekkis tõrge, programmi ei suudetud sulgeda.");
                }
            });

            //primaryStage.getIcons().add(new Image("../resources/icon.png"));
            primaryStage.setScene(new Scene(mainFxmlLoader.getRoot()));
            primaryStage.setTitle("Raamatukogu");
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Programmi ei suudetud avada.");
            throw new RuntimeException(e);
        }
    }

    public void displayLibraries(ArrayList<Library> libraries) {
        // loob ScrollPane-i raamatute FlowPane ümber, et pikema nimekirja korral kerida saaks
        ScrollPane scrollPane = new ScrollPane();

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topBar);
        deleteLibraryButton.setStyle(Vars.ButtonStyleDanger);
        updateLibraryButton.setStyle(Vars.ButtonStyleSecondary);
        topBar.setVisible(false);
        borderPane.setCenter(booksFlowPane);

        scrollPane.setContent(borderPane);
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
                    this.consoleInterface.setSelectedLibrary(library);
                    clearLibraryPane();
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

    public void clearLibraryPane() {
        booksFlowPane.getChildren().clear();
        topBar.setVisible(false);
    }

    // loads the books into booksFlowPane
    private void loadLibrary(Library library) {
        // muuda raamatukogu nimi ülemisel ribal ja muuda riba nähtavaks
        selectedLibraryLabel.setText(library.getName());
        topBar.setVisible(true);
        deleteLibraryButton.setOnMouseClicked(mouseEvent -> {
            // TODO: kinnitamise akna jaoks teha uus klass

            VBox root = new VBox();
            root.setSpacing(16);
            root.setPadding(new Insets(8));
            Scene confirmScene = new Scene(root);
            Stage confirmWindow = new Stage();

            Button confirmButton = new Button("Jah");
            // kui kasutaja kinnitab kustutamise
            confirmButton.setOnMouseClicked(mouseEvent1 -> {
                // kustutab faili ja info libraries ArrayListist
                consoleInterface.deleteLibraryWithoutConfirmation();
                // displayb uuesti
                clearLibraryPane();
                displayLibraries(consoleInterface.getLibraries());
                confirmWindow.close();
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

            root.getChildren().add(new Label("Kas oled kindel, et soovid raamatukogu kustutada?"));
            root.getChildren().add(row);

            confirmWindow.setScene(confirmScene);
            confirmWindow.show();
        }); // raamatukogu kustutamine

        // raamatukogu info uuendamine
        updateLibraryButton.setOnMouseClicked(mouseEvent -> {
            Stage updateLibraryStage = new Stage();
            VBox root = new VBox();
            root.setSpacing(8);
            root.setPadding(new Insets(8));
            Scene updateLibraryScene = new Scene(root);

            Label kirjeldus = new Label("Sisesta uus pealkiri");

            TextField pealkiriField = new TextField();
            pealkiriField.setText(library.getName());

            PrimaryButton updateButton = new PrimaryButton("Uuenda");
            updateButton.setOnMouseClicked(mouseEvent1 -> {
                library.setName(pealkiriField.getText());
                updateLibraryStage.close();
                clearLibraryPane();
                displayLibraries(consoleInterface.getLibraries());
                loadLibrary(library);
            });
            updateLibraryScene.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    library.setName(pealkiriField.getText());
                    updateLibraryStage.close();
                    clearLibraryPane();
                    displayLibraries(consoleInterface.getLibraries());
                    loadLibrary(library);
                }
            });


            root.getChildren().addAll(kirjeldus, pealkiriField, updateButton);

            updateLibraryStage.setScene(updateLibraryScene);
            updateLibraryStage.setTitle("Uuenda raamatukogu");
            updateLibraryStage.show();
        });

        // uue raamatu lisamise nupp
        VBox addBookArea = new VBox();
        addBookArea.getStyleClass().add("book");
        addBookArea.setSpacing(8);

        //Raamatud
        HBox bookCover = new HBox();
        bookCover.getStyleClass().add("bookCover");
        bookCover.styleProperty().set("-fx-background-color: "+Vars.SecondaryColor);
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

            //Raamatu parameetrite muutmise event
            vBook.setOnMouseClicked(me -> new UpdateBookController(book, library, this));
        }

        //Raamatu lisamise event
        addBookArea.setOnMouseClicked(me -> {
            Group abGroup = new Group();

            //Features
            Label infoLabel = new Label("Sisestage raamatu parameetrid. Pealkiri ja autor peavad olema sisestatud");
            infoLabel.setWrapText(true);
            infoLabel.setPrefWidth(220);
            TextField pealkiriTF = new TextField("Pealkiri");
            TextField autorTF = new TextField("");
            TextField ilmumineTF = new TextField("");
            TextField genreTF = new TextField("");
            TextField isbnTF = new TextField("");
            PrimaryButton lisaNupp = new PrimaryButton("Lisa raamat");
            Label lisatudLabel = new Label("");

            VBox container = new VBox();
            container.getChildren().addAll(infoLabel, pealkiriTF, autorTF, ilmumineTF, genreTF, isbnTF, lisaNupp, lisatudLabel);
            container.setSpacing(8);
            container.setStyle("-fx-padding: 16");

            abGroup.getChildren().add(container);

            lisaNupp.setOnMouseClicked(mee -> {
                if (!(pealkiriTF.getText().equals("") || pealkiriTF.getText().equals("Pealkiri"))
                        && !autorTF.getText().equals("")) {
                    try {
                        Book uusRaamt = new Book(pealkiriTF.getText(), autorTF.getText(), ilmumineTF.getText(),
                                                genreTF.getText(), new ISBN(), Status.AVAILABLE);
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
            isbnTF.setPromptText("ISBN, võite tühjaks jätta");

            Scene abScene = new Scene(abGroup);
            Stage abStage = new Stage();

            abStage.setScene(abScene);
            abStage.setTitle("Uus raamat");

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
        Label author = new Label(shortenName(book.getAuthorName()));
        author.getStyleClass().add("authorText");

        vBook.getChildren().addAll(bookCover, title, author);

        return vBook;
    }

    public ConsoleInterface getConsoleInterface() {
        return consoleInterface;
    }

    public void refreshLibrary() {
        clearLibraryPane();
        loadLibrary(this.consoleInterface.getSelectedLibrary());
    }

    private String shortenName(String fullname) {
        StringBuilder newName = new StringBuilder();
        String[] parts = fullname.split(" ");

        for (int i = 0; i < parts.length; i++) {
            if (i == parts.length-1) {
                newName.append(parts[i]);
            } else {
                newName.append(parts[i].substring(0,1)).append(". ");
            }
        }

        return newName.toString();
    }
}
