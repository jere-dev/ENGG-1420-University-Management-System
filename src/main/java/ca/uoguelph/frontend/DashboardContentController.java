package ca.uoguelph.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class DashboardContentController {
    @FXML
    private ListView<String> eventsList;

    @FXML
    public void initialize() {
        // Initialize the events list
        eventsList.getItems().addAll("No upcoming events");
    }

    @FXML
    private void handleCourses() {
        // TODO: Implement courses view
        System.out.println("Courses button clicked");
    }

    @FXML
    private void handleStudents() {
        // TODO: Implement students view
        System.out.println("Students button clicked");
    }

    @FXML
    private void handleFaculty() {
        // TODO: Implement faculty view
        System.out.println("Faculty button clicked");
    }

    @FXML
    private void addEvent() {
        // TODO: Implement add event functionality
        System.out.println("Add event clicked");
    }
}
