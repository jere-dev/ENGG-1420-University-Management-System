package ca.uoguelph.frontend.objects.controller;

import ca.uoguelph.frontend.objects.DisplayError;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * An admin-specific interface that all management controllers must implement.
 * <p>
 * Includes functions to add and edit entries.
 * @author 180Sai
 */
public abstract class AbstractAdminListController extends AbstractListController {
    /**
     * Handles adding an entry to the list.
     */
    protected abstract void handleAdd(ActionEvent event);

    /**
     * Handles editing an entry on the list.
     */
    protected abstract void handleEdit(ActionEvent event);


    /**
     * Handles opening the editor to a null entry (adds an entry).
     */
    protected abstract void handleLoadEditor(ActionEvent event);
}
