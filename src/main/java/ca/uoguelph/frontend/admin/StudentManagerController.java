package ca.uoguelph.frontend.admin;

import ca.uoguelph.backend.Student;
import ca.uoguelph.backend.StudentManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class StudentManagerController {

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

    @FXML
    private ComboBox<String> programFilter;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> sortOrderCombo;

    @FXML
    private ComboBox<String> yearFilter;

    @FXML
    private Button refreshButton;

    @FXML
    private Button printButton;

    @FXML
    private Button viewDetailsButton;

    @FXML
    private Button exportListButton;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        // Assuming your Student class has getProgram(), getYear(), getCoursesEnrolled(), and getStatus()
        programColumn.setCellValueFactory(new PropertyValueFactory<>("email")); //Adjust to your student class
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("currentSemester")); //Adjust to your student class
        coursesColumn.setCellValueFactory(new PropertyValueFactory<>("courses")); //Adjust to your student class
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("progress"));//Adjust to your student class

        // Load students into the table
        loadStudents();

        // Setup filter and search functionality (basic example)
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterStudents());

        // Setup refresh button
        refreshButton.setOnAction(event -> loadStudents());

        //TODO: add functionality for the other buttons and comboboxes.
    }

    private void loadStudents() {
        studentList.clear();
        studentList.addAll(StudentManager.getStudents());
        System.out.println("Loaded " + studentList.size() + " students."); // Add this line
        studentTable.setItems(studentList);
    }

    private void filterStudents() {
        String searchText = searchField.getText().toLowerCase();
        ObservableList<Student> filteredList = FXCollections.observableArrayList();

        for (Student student : StudentManager.getStudents()) {
            if (student.getID().toLowerCase().contains(searchText) || student.getName().toLowerCase().contains(searchText)) {
                filteredList.add(student);
            }
        }

        studentTable.setItems(filteredList);
    }

    @FXML
    private void handleRefreshStudents() {
        loadStudents();
    }

    @FXML
    private void handlePrintList() {
        // TODO: Implement print functionality
        System.out.println("Print button clicked");
    }

    @FXML
    private void handleStudentDetails() {
        // TODO: Implement student details view
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            System.out.println("View details for: " + selectedStudent.getName());
        } else {
            System.out.println("Please select a student.");
        }
    }

    @FXML
    private void handleExportList() {
        // TODO: Implement export functionality
        System.out.println("Export list button clicked");
    }
}