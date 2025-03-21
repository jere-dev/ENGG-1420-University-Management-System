package ca.uoguelph.frontend.user;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class CourseCatalogController {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private void handleSearch(ActionEvent event) {
        String searchTerm = searchField.getText();
        System.out.println("Searching for: " + searchTerm);
    }
}