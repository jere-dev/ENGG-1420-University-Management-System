package ca.uoguelph.frontend.objects.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Common methods that would be found in a list controller such as {@code SubjectListController} and {@code CourseCatalogController}.
 * @author  180Sai
 */
public abstract class AbstractListController {
    /**
     * Updates the table with a search term.
     * @param search    parameter to search for
     */
    protected abstract void updateTable(String search);

    /**
     * Initialization of the controller.
     * <p>
     * The starting state of the table contains no rows or columns.
     * Loading data to the table requires JavaFX to call the controller's
     * {@code initialize()} method when loading the .fxml file.
     */
    @FXML
    protected void initialize() {
        updateTable("");
    }

    /**
     * Handles the user searching for an entry in the table.
     */
    @FXML
    protected abstract void handleSearch(ActionEvent event);
}
