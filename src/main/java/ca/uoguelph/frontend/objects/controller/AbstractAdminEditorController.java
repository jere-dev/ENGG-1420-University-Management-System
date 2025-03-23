package ca.uoguelph.frontend.objects.controller;

import javafx.event.ActionEvent;

public abstract class AbstractAdminEditorController{

    /**
     * Handles save and delete for a row on the table.
     */
    protected void handleSave() {
        handleSave(null);
    }

    /**
     * Handles save and delete for a row on the table.
     */
    protected abstract void handleSave(ActionEvent event);


    protected abstract void handleDelete(ActionEvent event);
}
