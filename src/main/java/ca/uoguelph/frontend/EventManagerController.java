package ca.uoguelph.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class EventManagerController {

    @FXML
    private Button addEventButton;

    @FXML
    private Button editEventButton;

    @FXML
    private Button deleteEventButton;

    @FXML
    private void handleAddEvent(ActionEvent event) {
        System.out.println("Add Event clicked");
        // Add logic to handle adding an event
    }

    @FXML
    private void handleEditEvent(ActionEvent event) {
        System.out.println("Edit Event clicked");
        // Add logic to handle editing an event
    }

    @FXML
    private void handleDeleteEvent(ActionEvent event) {
        System.out.println("Delete Event clicked");
        // Add logic to handle deleting an event
    }
}