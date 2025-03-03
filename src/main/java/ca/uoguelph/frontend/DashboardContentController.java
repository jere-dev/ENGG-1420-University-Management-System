package ca.uoguelph.frontend;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import java.util.List;

public class DashboardContentController {
    @FXML private VBox eventsContainer;

    @FXML
    private void initialize() {
        loadEvents();
    }

    @FXML
    private void handleCourses() {
        // Handle courses button click
    }

    @FXML
    private void handleStudents() {
        // Handle students button click
    }

    @FXML
    private void handleFaculty() {
        // Handle faculty button click
    }

    @FXML
    private void addEvent() {
        // Handle add event button click
    }

    private void loadEvents() {
        List<String> events = getEvents();
        for (String event : events) {
            Label eventLabel = new Label(event);
            eventsContainer.getChildren().add(eventLabel);
        }
    }

    private List<String> getEvents() {
        return List.of("Event 1", "Event 2", "Event 3");
    }
}
