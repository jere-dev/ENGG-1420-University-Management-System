package ca.uoguelph.frontend.admin;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;

public class EventManagerController implements Initializable {
    private final List<Event> eventList = new ArrayList<>();
    private int page = 0;
    private int pageRowCount = 20;

    @FXML private GridPane tableGrid;
    @FXML private TextField searchEvents;
    @FXML private ComboBox<String> eventTypeFilter;
    @FXML private ComboBox<String> timeRangeFilter;
    @FXML private ComboBox<String> sortOrderCombo;
    @FXML private Button refreshButton;
    @FXML private Button leftPageButton;
    @FXML private Button rightPageButton;
    @FXML private TextField pageText;
    @FXML private TextField rowCountText;
    @FXML private Label errorLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupGridPane();
        setupFilters();
        loadSampleData();
        updateTable("");

        // Setup search field behavior
        searchEvents.textProperty().addListener((obs, old, newText) -> {
            if (searchEvents.isFocused()) {
                javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(300));
                pause.setOnFinished(e -> updateTable(newText.trim()));
                pause.play();
            }
        });

        pageText.setText("1");
        rowCountText.setText(String.valueOf(pageRowCount));
    }

    private void setupGridPane() {
        tableGrid.setHgap(20);
        tableGrid.setVgap(5);
        tableGrid.setPadding(new Insets(5, 15, 15, 15));
        tableGrid.setStyle("-fx-background-color: white; -fx-background-radius: 3; " +
                          "-fx-border-radius: 3; -fx-border-color: #E0E0E0; " +
                          "-fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.05), 3, 0, 0, 0);");

        // Make grid fill parent
        tableGrid.setMaxWidth(Double.MAX_VALUE);
        tableGrid.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(tableGrid, Priority.ALWAYS);
        HBox.setHgrow(tableGrid, Priority.ALWAYS);

        // Column constraints with explicit percentages
        double[] columnWidths = {25, 15, 15, 20, 15, 10};
        tableGrid.getColumnConstraints().clear();
        for (double width : columnWidths) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(width);
            column.setHgrow(Priority.ALWAYS);
            column.setFillWidth(true);
            tableGrid.getColumnConstraints().add(column);
        }
    }

    private void addHeaderRow() {
        Label[] headers = {
            new Label("Event Name"),
            new Label("Date"),
            new Label("Time"),
            new Label("Location"),
            new Label("Type"),
            new Label("Status")
        };

        String headerStyle = "-fx-font-weight: bold; -fx-font-size: 14px; " +
                           "-fx-text-fill: #941B0C; -fx-padding: 12 5 12 5; " +
                           "-fx-font-family: System; -fx-background-color: transparent;";

        for (int i = 0; i < headers.length; i++) {
            headers[i].setStyle(headerStyle);
            headers[i].setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(headers[i], Priority.ALWAYS);
            tableGrid.add(headers[i], i, 0);
        }

        Separator headerSeparator = new Separator();
        headerSeparator.setPadding(new Insets(5, 0, 5, 0));
        tableGrid.add(headerSeparator, 0, 1, tableGrid.getColumnCount(), 1);
    }

    private void addEventRow(Event event, int rowIndex) {
        // Create container for row highlighting
        HBox rowContainer = new HBox();
        rowContainer.setMaxWidth(Double.MAX_VALUE);
        rowContainer.getStyleClass().add("table-row");
        rowContainer.setStyle("-fx-background-color: transparent;");

        Label[] labels = {
            new Label(event.getName()),
            new Label(event.getDate().toString()),
            new Label(event.getTime().toString()),
            new Label(event.getLocation()),
            new Label(event.getType()),
            new Label(event.getStatus())
        };

        String labelStyle = "-fx-padding: 10 5 10 5; -fx-font-size: 13px; " +
                          "-fx-text-fill: #333333; -fx-font-family: System;";

        // Add hover effects
        rowContainer.setOnMouseEntered(e -> {
            rowContainer.setStyle("-fx-background-color: #F6F6F6;");
            for (Label label : labels) {
                label.setStyle(labelStyle);
            }
        });

        rowContainer.setOnMouseExited(e -> {
            rowContainer.setStyle("-fx-background-color: transparent;");
            for (Label label : labels) {
                label.setStyle(labelStyle);
            }
        });

        tableGrid.add(rowContainer, 0, rowIndex, tableGrid.getColumnCount(), 1);

        // Add labels to grid
        for (int i = 0; i < labels.length; i++) {
            labels[i].setStyle(labelStyle);
            labels[i].setMaxWidth(Double.MAX_VALUE);
            labels[i].setMaxHeight(Double.MAX_VALUE);
            GridPane.setHgrow(labels[i], Priority.ALWAYS);
            GridPane.setVgrow(labels[i], Priority.ALWAYS);
            tableGrid.add(labels[i], i, rowIndex);
        }

        // Add row separator
        Separator rowSeparator = new Separator();
        rowSeparator.setPadding(new Insets(0));
        rowSeparator.setStyle("-fx-background-color: #E0E0E0;");
        rowSeparator.setMaxWidth(Double.MAX_VALUE);
        tableGrid.add(rowSeparator, 0, rowIndex + 1, tableGrid.getColumnCount(), 1);
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
        updateTable("");
    }

    private void setupFilters() {
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

        // Set up listeners for filters
        eventTypeFilter.setOnAction(event -> applyFilters());
        timeRangeFilter.setOnAction(event -> applyFilters());
        sortOrderCombo.setOnAction(event -> applyFilters());
    }

    private void applyFilters() {
        updateTable(searchEvents.getText().trim());
    }

    private void updateTable(String searchText) {
        // Create a new filtered list
        List<Event> filteredEvents = new ArrayList<>(eventList);
        
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
        int startIndex = page * pageRowCount;
        int endIndex = Math.min(startIndex + pageRowCount, filteredEvents.size());

        tableGrid.getChildren().clear();
        addHeaderRow();

        int rowIndex = 1;
        for (int i = startIndex; i < endIndex; i++) {
            addEventRow(filteredEvents.get(i), rowIndex++);
        }
    }

    @FXML
    private void handleAddEvent(ActionEvent event) {
        System.out.println("Add Event clicked");
        // Add logic to open a dialog to add a new event
    }

    @FXML
    private void handleEditEvent(ActionEvent event) {
        // Add logic to open a dialog to edit the selected event
    }

    @FXML
    private void handleEventDetails(ActionEvent event) {
        // Add logic to display detailed information about the selected event
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
        Event selectedEvent = null;
        
        // Clear and reload data
        eventList.clear();
        loadSampleData();
        
        // Restore selection if the event still exists
        if (selectedEvent != null) {
            for (Event e : eventList) {
                if (e.getName().equals(selectedEvent.getName())) {
                    // Restore selection
                    break;
                }
            }
        }
        
        // Reapply filters
        applyFilters();
    }

    @FXML
    private void handlePrevPage(ActionEvent event) {
        if (page > 0) {
            page--;
            pageText.setText(String.valueOf(page + 1));
            updateTable(searchEvents.getText().trim());
        }
    }

    @FXML
    private void handleNextPage(ActionEvent event) {
        int totalPages = (int) Math.ceil((double) eventList.size() / pageRowCount);
        if (page < totalPages - 1) {
            page++;
            pageText.setText(String.valueOf(page + 1));
            updateTable(searchEvents.getText().trim());
        }
    }

    @FXML
    private void handleKeyPage(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
            try {
                int newPage = Integer.parseInt(pageText.getText()) - 1;
                int totalPages = (int) Math.ceil((double) eventList.size() / pageRowCount);
                if (newPage >= 0 && newPage < totalPages) {
                    page = newPage;
                    updateTable(searchEvents.getText().trim());
                    errorLabel.setText("");
                } else {
                    errorLabel.setText("Invalid page number");
                    pageText.setText(String.valueOf(page + 1));
                }
            } catch (NumberFormatException e) {
                errorLabel.setText("Please enter a valid number");
                pageText.setText(String.valueOf(page + 1));
            }
        }
    }

    @FXML
    private void handleKeyCount(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
            try {
                int newCount = Integer.parseInt(rowCountText.getText());
                if (newCount > 0 && newCount <= 100) {
                    pageRowCount = newCount;
                    page = 0;
                    pageText.setText("1");
                    updateTable(searchEvents.getText().trim());
                    errorLabel.setText("");
                } else {
                    errorLabel.setText("Please enter a number between 1 and 100");
                    rowCountText.setText(String.valueOf(pageRowCount));
                }
            } catch (NumberFormatException e) {
                errorLabel.setText("Please enter a valid number");
                rowCountText.setText(String.valueOf(pageRowCount));
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