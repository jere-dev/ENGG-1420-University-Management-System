package ca.uoguelph.frontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class StudentListController {

    // FXML Injected Components
    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> programFilter;

    @FXML
    private ComboBox<String> yearFilter;

    @FXML
    private ComboBox<String> sortOrderCombo;

    @FXML
    private Button refreshButton;

    @FXML
    private Button printButton;

    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TableColumn<Student, String> idColumn;

    @FXML
    private TableColumn<Student, String> nameColumn;

    @FXML
    private TableColumn<Student, String> programColumn;

    @FXML
    private TableColumn<Student, String> yearColumn;

    @FXML
    private TableColumn<Student, String> coursesColumn;

    @FXML
    private TableColumn<Student, String> statusColumn;

    // Model data
    private ObservableList<Student> studentData = FXCollections.observableArrayList();
    private FilteredList<Student> filteredStudents;

    /**
     * Initialize the controller
     */
    @FXML
    public void initialize() {
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        programColumn.setCellValueFactory(new PropertyValueFactory<>("program"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        coursesColumn.setCellValueFactory(new PropertyValueFactory<>("coursesEnrolled"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Initialize filter comboboxes
        programFilter.setItems(FXCollections.observableArrayList(
                "All Programs", "Computer Science", "Engineering", "Business", "Arts", "Science"));
        programFilter.setValue("All Programs");

        yearFilter.setItems(FXCollections.observableArrayList(
                "All Years", "Year 1", "Year 2", "Year 3", "Year 4", "Graduate"));
        yearFilter.setValue("All Years");

        sortOrderCombo.setItems(FXCollections.observableArrayList(
                "Name (A-Z)", "Name (Z-A)", "ID (Ascending)", "ID (Descending)", "Program", "Year"));
        sortOrderCombo.setValue("Name (A-Z)");

        // Setup search and filtering
        setupFiltering();

        // Load sample data
        loadSampleData();

        // Setup event listeners for filters
        programFilter.setOnAction(e -> applyFilters());
        yearFilter.setOnAction(e -> applyFilters());
        sortOrderCombo.setOnAction(e -> applySort());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
    }

    /**
     * Setup filtering for the student table
     */
    private void setupFiltering() {
        // Wrap the ObservableList in a FilteredList
        filteredStudents = new FilteredList<>(studentData, p -> true);

        // Wrap the FilteredList in a SortedList
        SortedList<Student> sortedData = new SortedList<>(filteredStudents);

        // Bind the SortedList comparator to the TableView comparator
        sortedData.comparatorProperty().bind(studentTable.comparatorProperty());

        // Add sorted (and filtered) data to the table
        studentTable.setItems(sortedData);
    }

    /**
     * Apply filters based on search field, program filter, and year filter
     */
    private void applyFilters() {
        String searchText = searchField.getText().toLowerCase();
        String program = programFilter.getValue();
        String year = yearFilter.getValue();

        filteredStudents.setPredicate(student -> {
            // If search field is empty and all filters are set to "All", show all students
            if ((searchText == null || searchText.isEmpty()) &&
                    program.equals("All Programs") &&
                    year.equals("All Years")) {
                return true;
            }

            // Filter by search text
            boolean matchesSearch = true;
            if (searchText != null && !searchText.isEmpty()) {
                matchesSearch = student.getName().toLowerCase().contains(searchText) ||
                        student.getId().toLowerCase().contains(searchText);
            }

            // Filter by program
            boolean matchesProgram = program.equals("All Programs") ||
                    student.getProgram().equals(program);

            // Filter by year
            boolean matchesYear = year.equals("All Years") ||
                    student.getYear().equals(year);

            return matchesSearch && matchesProgram && matchesYear;
        });
    }

    /**
     * Apply sorting based on the selected sort option
     */
    private void applySort() {
        String sortOption = sortOrderCombo.getValue();

        switch(sortOption) {
            case "Name (A-Z)":
                studentTable.getSortOrder().clear();
                nameColumn.setSortType(TableColumn.SortType.ASCENDING);
                studentTable.getSortOrder().add(nameColumn);
                break;
            case "Name (Z-A)":
                studentTable.getSortOrder().clear();
                nameColumn.setSortType(TableColumn.SortType.DESCENDING);
                studentTable.getSortOrder().add(nameColumn);
                break;
            case "ID (Ascending)":
                studentTable.getSortOrder().clear();
                idColumn.setSortType(TableColumn.SortType.ASCENDING);
                studentTable.getSortOrder().add(idColumn);
                break;
            case "ID (Descending)":
                studentTable.getSortOrder().clear();
                idColumn.setSortType(TableColumn.SortType.DESCENDING);
                studentTable.getSortOrder().add(idColumn);
                break;
            case "Program":
                studentTable.getSortOrder().clear();
                programColumn.setSortType(TableColumn.SortType.ASCENDING);
                studentTable.getSortOrder().add(programColumn);
                break;
            case "Year":
                studentTable.getSortOrder().clear();
                yearColumn.setSortType(TableColumn.SortType.ASCENDING);
                studentTable.getSortOrder().add(yearColumn);
                break;
        }

        studentTable.sort();
    }

    /**
     * Load sample student data
     */
    private void loadSampleData() {
        studentData.addAll(
                new Student("1000001", "John Smith", "Computer Science", "Year 3", "5", "Active"),
                new Student("1000002", "Emily Johnson", "Engineering", "Year 2", "4", "Active"),
                new Student("1000003", "Michael Wong", "Business", "Year 4", "3", "Active"),
                new Student("1000004", "Sarah Davis", "Arts", "Year 1", "6", "Active"),
                new Student("1000005", "Daniel Brown", "Science", "Year 3", "5", "Probation"),
                new Student("1000006", "Jessica Wilson", "Computer Science", "Year 2", "4", "Active"),
                new Student("1000007", "James Taylor", "Engineering", "Year 4", "2", "Inactive"),
                new Student("1000008", "Jennifer Martinez", "Business", "Year 1", "6", "Active"),
                new Student("1000009", "Robert Anderson", "Arts", "Year 3", "5", "Active"),
                new Student("1000010", "Maria Garcia", "Science", "Year 2", "4", "Active"),
                new Student("1000001", "John Smith", "Computer Science", "Year 3", "5", "Active"),
                new Student("1000002", "Emily Johnson", "Engineering", "Year 2", "4", "Active"),
                new Student("1000003", "Michael Wong", "Business", "Year 4", "3", "Active"),
                new Student("1000004", "Sarah Davis", "Arts", "Year 1", "6", "Active"),
                new Student("1000005", "Daniel Brown", "Science", "Year 3", "5", "Probation"),
                new Student("1000006", "Jessica Wilson", "Computer Science", "Year 2", "4", "Active"),
                new Student("1000007", "James Taylor", "Engineering", "Year 4", "2", "Inactive"),
                new Student("1000008", "Jennifer Martinez", "Business", "Year 1", "6", "Active"),
                new Student("1000009", "Robert Anderson", "Arts", "Year 3", "5", "Active"),
                new Student("1000010", "Maria Garcia", "Science", "Year 2", "4", "Active")
        );
    }

    /**
     * Handle the refresh button action
     */
    @FXML
    private void handleRefreshStudents(ActionEvent event) {
        // In a real application, this would fetch fresh data from a database or service
        // For this example, we'll just reset the filters and re-apply them
        searchField.clear();
        programFilter.setValue("All Programs");
        yearFilter.setValue("All Years");
        sortOrderCombo.setValue("Name (A-Z)");

        applyFilters();
        System.out.println("Student list refreshed");
    }

    /**
     * Handle the print button action
     */
    @FXML
    private void handlePrintList(ActionEvent event) {
        System.out.println("Printing student list...");
        // In a real application, this would generate a print job
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Print List");
        alert.setHeaderText("Print Feature");
        alert.setContentText("The print feature would generate a printable report of the current student list.");
        alert.showAndWait();
    }

    /**
     * Handle the student details button action
     */
    @FXML
    private void handleStudentDetails(ActionEvent event) {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            System.out.println("Viewing details for: " + selectedStudent.getName());
            // In a real application, this would open a details view for the selected student
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Student Details");
            alert.setHeaderText("Details for " + selectedStudent.getName());
            alert.setContentText("ID: " + selectedStudent.getId() + "\n" +
                    "Program: " + selectedStudent.getProgram() + "\n" +
                    "Year: " + selectedStudent.getYear() + "\n" +
                    "Courses Enrolled: " + selectedStudent.getCoursesEnrolled() + "\n" +
                    "Status: " + selectedStudent.getStatus());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Student Selected");
            alert.setContentText("Please select a student from the table.");
            alert.showAndWait();
        }
    }

    /**
     * Handle the bulk actions button
     */
    @FXML
    private void handleBulkActions(ActionEvent event) {
        System.out.println("Opening bulk actions menu");
        // In a real application, this might open a dialog for batch operations
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bulk Actions");
        alert.setHeaderText("Bulk Actions Feature");
        alert.setContentText("This would allow batch operations on multiple selected students.");
        alert.showAndWait();
    }

    /**
     * Handle the export list button
     */
    @FXML
    private void handleExportList(ActionEvent event) {
        System.out.println("Exporting student list");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Student List");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
        );

        // Get the stage from a control in the scene
        Stage stage = (Stage) searchField.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            // In a real application, this would export the data to the selected file
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Successful");
            alert.setHeaderText("Export Complete");
            alert.setContentText("Student list has been exported to:\n" + file.getAbsolutePath());
            alert.showAndWait();
        }
    }

    /**
     * Student model class
     */
    public static class Student {
        private final String id;
        private final String name;
        private final String program;
        private final String year;
        private final String coursesEnrolled;
        private final String status;

        public Student(String id, String name, String program, String year, String coursesEnrolled, String status) {
            this.id = id;
            this.name = name;
            this.program = program;
            this.year = year;
            this.coursesEnrolled = coursesEnrolled;
            this.status = status;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getProgram() { return program; }
        public String getYear() { return year; }
        public String getCoursesEnrolled() { return coursesEnrolled; }
        public String getStatus() { return status; }
    }
}