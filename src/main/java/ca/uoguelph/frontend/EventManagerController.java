package ca.uoguelph.frontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.SelectionMode;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class EventManagerController implements Initializable {

    // Table and table columns
    @FXML
    private TableView<Event> eventsTable;

    @FXML
    private TableColumn<Event, String> eventNameColumn;

    @FXML
    private TableColumn<Event, LocalDate> dateColumn;

    @FXML
    private TableColumn<Event, LocalTime> timeColumn;

    @FXML
    private TableColumn<Event, String> locationColumn;

    @FXML
    private TableColumn<Event, String> typeColumn;

    @FXML
    private TableColumn<Event, String> statusColumn;

    // Filter and search components
    @FXML
    private ComboBox<String> eventTypeFilter;

    @FXML
    private TextField searchEvents;

    @FXML
    private ComboBox<String> sortOrderCombo;

    @FXML
    private ComboBox<String> timeRangeFilter;

    // Sample data for demonstration
    private ObservableList<Event> eventList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize table columns
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Make columns non-resizable
        eventNameColumn.setResizable(false);
        dateColumn.setResizable(false);
        timeColumn.setResizable(false);
        locationColumn.setResizable(false);
        typeColumn.setResizable(false);
        statusColumn.setResizable(false);

        // Enable table selection
        eventsTable.getSelectionModel().setCellSelectionEnabled(false);
        eventsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Initialize filter combo boxes
        eventTypeFilter.setItems(FXCollections.observableArrayList(
                "All Types", "Academic", "Sports", "Club", "Special", "Conference"));
        eventTypeFilter.setValue("All Types");

        sortOrderCombo.setItems(FXCollections.observableArrayList(
                "Date (Ascending)", "Date (Descending)", "Name (A-Z)", "Name (Z-A)", "Event Type"));
        sortOrderCombo.setValue("Date (Ascending)");

        timeRangeFilter.setItems(FXCollections.observableArrayList(
                "All Events", "Today", "This Week", "This Month", "Next Month", "Past Events"));
        timeRangeFilter.setValue("All Events");

        // Load sample data
        loadSampleData();

        // Set up listeners for filters
        setupFilterListeners();
    }

    private void loadSampleData() {
        // Add sample events for demonstration
        eventList.add(new Event("Spring Graduation Ceremony", LocalDate.now().plusDays(30),
                LocalTime.of(10, 0), "Main Auditorium", "Academic", "Confirmed"));
        eventList.add(new Event("Faculty Meeting", LocalDate.now().plusDays(2),
                LocalTime.of(14, 30), "Conference Room A", "Academic", "Pending"));
        eventList.add(new Event("Basketball Tournament", LocalDate.now().plusDays(15),
                LocalTime.of(18, 0), "Sports Complex", "Sports", "Confirmed"));

        // Set the items to the table
        eventsTable.setItems(eventList);
    }

    private void setupFilterListeners() {
        // Add listeners to filter components
        eventTypeFilter.setOnAction(event -> applyFilters());
        timeRangeFilter.setOnAction(event -> applyFilters());
        sortOrderCombo.setOnAction(event -> applyFilters());

        searchEvents.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilters();
        });
    }

    private void applyFilters() {
        // This would be implemented to filter the events based on selection
        System.out.println("Applying filters");
        System.out.println("Event Type: " + eventTypeFilter.getValue());
        System.out.println("Time Range: " + timeRangeFilter.getValue());
        System.out.println("Sort Order: " + sortOrderCombo.getValue());
        System.out.println("Search Term: " + searchEvents.getText());

        // Actual implementation would filter the eventList based on selections
        // and update the table
    }

    @FXML
    private void handleAddEvent(ActionEvent event) {
        System.out.println("Add Event clicked");
        // Add logic to open a dialog to add a new event
    }

    @FXML
    private void handleEditEvent(ActionEvent event) {
        Event selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            System.out.println("Edit Event clicked for: " + selectedEvent.getName());
            // Add logic to open a dialog to edit the selected event
        } else {
            System.out.println("No event selected for editing");
            // Show an alert that no event is selected
        }
    }

    @FXML
    private void handleEventDetails(ActionEvent event) {
        Event selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            System.out.println("View Details clicked for: " + selectedEvent.getName());
            // Add logic to display detailed information about the selected event
        } else {
            System.out.println("No event selected to view details");
            // Show an alert that no event is selected
        }
    }

    @FXML
    private void handleSendReminders(ActionEvent event) {
        System.out.println("Send Reminders clicked");
        // Add logic to send reminders for upcoming events
    }

    // Remove or comment out the unused export method
    // @FXML
    // private void handleExportEvents(ActionEvent event) {
    //     System.out.println("Export Calendar clicked");
    //     // Export logic
    // }

    // Rename refreshEventList to handleRefreshEvents and update its logic
    @FXML
    private void handleRefreshEvents(ActionEvent event) {
        System.out.println("Refresh clicked");
        // Store the selected item before refresh
        Event selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        
        // Clear and reload data
        eventList.clear();
        loadSampleData();
        
        // Restore selection if the event still exists
        if (selectedEvent != null) {
            for (Event e : eventList) {
                if (e.getName().equals(selectedEvent.getName())) {
                    eventsTable.getSelectionModel().select(e);
                    break;
                }
            }
        }
    }

    // Inner class for Event model
    public static class Event {
        private String name;
        private LocalDate date;
        private LocalTime time;
        private String location;
        private String type;
        private String status;

        public Event(String name, LocalDate date, LocalTime time, String location, String type, String status) {
            this.name = name;
            this.date = date;
            this.time = time;
            this.location = location;
            this.type = type;
            this.status = status;
        }

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }

        public LocalTime getTime() { return time; }
        public void setTime(LocalTime time) { this.time = time; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}