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
    
    // Buttons
    @FXML
    private Button refreshButton;

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
        eventList.add(new Event("Student Club Fair", LocalDate.now().plusDays(7),
                LocalTime.of(12, 0), "University Center", "Club", "Confirmed"));
        eventList.add(new Event("Research Symposium", LocalDate.now().plusDays(45),
                LocalTime.of(9, 0), "Science Building", "Conference", "Pending"));

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
        // Create a new filtered list
        ObservableList<Event> filteredEvents = FXCollections.observableArrayList(eventList);
        
        // Apply event type filter
        String selectedType = eventTypeFilter.getValue();
        if (selectedType != null && !selectedType.equals("All Types")) {
            filteredEvents.removeIf(event -> !event.getType().equals(selectedType));
        }
        
        // Apply time range filter
        String timeRange = timeRangeFilter.getValue();
        if (timeRange != null) {
            LocalDate today = LocalDate.now();
            
            switch (timeRange) {
                case "Today":
                    filteredEvents.removeIf(event -> !event.getDate().equals(today));
                    break;
                case "This Week":
                    LocalDate weekEnd = today.plusDays(7);
                    filteredEvents.removeIf(event -> 
                        event.getDate().isBefore(today) || event.getDate().isAfter(weekEnd));
                    break;
                case "This Month":
                    LocalDate monthEnd = today.plusMonths(1).withDayOfMonth(1).minusDays(1);
                    filteredEvents.removeIf(event -> 
                        event.getDate().isBefore(today) || event.getDate().isAfter(monthEnd));
                    break;
                case "Next Month":
                    LocalDate nextMonthStart = today.plusMonths(1).withDayOfMonth(1);
                    LocalDate nextMonthEnd = nextMonthStart.plusMonths(1).minusDays(1);
                    filteredEvents.removeIf(event -> 
                        event.getDate().isBefore(nextMonthStart) || event.getDate().isAfter(nextMonthEnd));
                    break;
                case "Past Events":
                    filteredEvents.removeIf(event -> !event.getDate().isBefore(today));
                    break;
            }
        }
        
        // Apply search filter
        String searchText = searchEvents.getText();
        if (searchText != null && !searchText.isEmpty()) {
            String lowerCaseSearch = searchText.toLowerCase();
            filteredEvents.removeIf(event -> 
                !event.getName().toLowerCase().contains(lowerCaseSearch) &&
                !event.getLocation().toLowerCase().contains(lowerCaseSearch) &&
                !event.getType().toLowerCase().contains(lowerCaseSearch));
        }
        
        // Apply sorting
        String sortOrder = sortOrderCombo.getValue();
        if (sortOrder != null) {
            switch (sortOrder) {
                case "Date (Ascending)":
                    filteredEvents.sort((e1, e2) -> e1.getDate().compareTo(e2.getDate()));
                    break;
                case "Date (Descending)":
                    filteredEvents.sort((e1, e2) -> e2.getDate().compareTo(e1.getDate()));
                    break;
                case "Name (A-Z)":
                    filteredEvents.sort((e1, e2) -> e1.getName().compareTo(e2.getName()));
                    break;
                case "Name (Z-A)":
                    filteredEvents.sort((e1, e2) -> e2.getName().compareTo(e1.getName()));
                    break;
                case "Event Type":
                    filteredEvents.sort((e1, e2) -> e1.getType().compareTo(e2.getType()));
                    break;
            }
        }
        
        // Update the table
        eventsTable.setItems(filteredEvents);
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
        
        // Reapply filters
        applyFilters();
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