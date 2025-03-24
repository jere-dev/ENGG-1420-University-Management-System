package ca.uoguelph.frontend.objects.controller;

import javafx.fxml.FXML;


public abstract class AbstractAdminEditorController{

    /**
     * Handles save and delete for a row on the table.
     */

    @FXML
    protected abstract void handleSave(javafx.event.ActionEvent event);

    @FXML
    protected abstract void handleDelete(javafx.event.ActionEvent event);
}
