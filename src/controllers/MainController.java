package controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.util.ArrayList;
import components.Library;

public class MainController {
    @FXML
    BorderPane mainBorderPane;

    @FXML
    Text sidebarTitle;

    @FXML
    Label statusLabel;

    public void displayLibraries(ArrayList<Library> libraries) {
        VBox nimekiri = new VBox();
        nimekiri.setBackground(new Background(
                new BackgroundFill(
                        Color.BEIGE,
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
                statusLabel.setText("Valitud raamatukogu: "+libraryName);
            });

            nimekiri.getChildren().add(sidebarLibraryName);
        }

        mainBorderPane.setLeft(nimekiri);
    }
}
