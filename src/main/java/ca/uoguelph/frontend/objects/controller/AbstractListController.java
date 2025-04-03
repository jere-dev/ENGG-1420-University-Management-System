package ca.uoguelph.frontend.objects.controller;

import javafx.event.ActionEvent;

/**
 * Common methods that would be found in a list controller such as {@code SubjectListController} and {@code CourseCatalogController}.
 * @author 180Sai
 */
public abstract class AbstractListController {
    /**
     * Updates the table with a search term.
     * @param search    parameter to search for
     */
    protected abstract void updateTable(String search);

    /**
     * Handles the user searching for an entry in the table.
     */
    protected abstract void handleSearch(ActionEvent event);
}
