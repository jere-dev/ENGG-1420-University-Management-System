package ca.uoguelph.frontend.admin;

import ca.uoguelph.backend.Faculty;
import ca.uoguelph.backend.FacultyManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class FacultyManagerController {

    @FXML
    private TableView<Faculty> facultyTable;

    @FXML
    private TableColumn<Faculty, String> idColumn;

    @FXML
    private TableColumn<Faculty, String> nameColumn;

    @FXML
    private TableColumn<Faculty, String> departmentColumnTable; // Renamed to match FXML

    @FXML
    private TableColumn<Faculty, String> yearColumnTable; // Renamed to match FXML

    @FXML
    private TableColumn<Faculty, String> coursesColumnTable; // Renamed to match FXML

    @FXML
    private TableColumn<Faculty, String> statusColumnTable; // Renamed to match FXML

    @FXML
    private ComboBox<String> departmentFilter;

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
    private Button detailsButton;

    @FXML
    private Button exportButton;

    private ObservableList<Faculty> facultyList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        departmentColumnTable.setCellValueFactory(new PropertyValueFactory<>("degree"));
        yearColumnTable.setCellValueFactory(new PropertyValueFactory<>("researchInterest"));
        coursesColumnTable.setCellValueFactory(new PropertyValueFactory<>("officeLocation"));
        statusColumnTable.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Load faculties into the table
        loadFaculties();

        // Setup filter and search functionality (basic example)
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterFaculties());

        // Setup refresh button
        refreshButton.setOnAction(event -> loadFaculties());

        //TODO: add functionality for the other buttons and comboboxes.
    }

    private void loadFaculties() {
        facultyList.clear();
        facultyList.addAll(FacultyManager.getFaculties());
        facultyTable.setItems(facultyList);
    }

    private void filterFaculties() {
        String searchText = searchField.getText().toLowerCase();
        ObservableList<Faculty> filteredList = FXCollections.observableArrayList();

        for (Faculty faculty : FacultyManager.getFaculties()) {
            if (faculty.getID().toLowerCase().contains(searchText) || faculty.getName().toLowerCase().contains(searchText)) {
                filteredList.add(faculty);
            }
        }

        facultyTable.setItems(filteredList);
    }

    @FXML
    private void handleRefreshFaculty() {
        loadFaculties();
    }

    @FXML
    private void handlePrintList() {
        // TODO: Implement print functionality
        System.out.println("Print button clicked");
    }

    @FXML
    private void handleFacultyDetails() {
        // TODO: Implement faculty details view
        Faculty selectedFaculty = facultyTable.getSelectionModel().getSelectedItem();
        if (selectedFaculty != null) {
            System.out.println("View details for: " + selectedFaculty.getName());
        } else {
            System.out.println("Please select a faculty member.");
        }
    }

    @FXML
    private void handleExportList() {
        // TODO: Implement export functionality
        System.out.println("Export list button clicked");
    }
}