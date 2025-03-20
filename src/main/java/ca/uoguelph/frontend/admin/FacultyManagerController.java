package ca.uoguelph.frontend.admin;

import ca.uoguelph.backend.Faculty;
import ca.uoguelph.backend.FacultyManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FacultyManagerController {
    private final List<Faculty> facultyList = new ArrayList<>();
    private final HashMap<Button, Faculty> buttonMap = new HashMap<>();
    private int page = 0;
    private int pageRowCount = 40;

    @FXML private GridPane tableGrid;
    @FXML private TextField searchField;
    @FXML private Button leftPageButton, rightPageButton;
    @FXML private TextField pageText, rowCountText;
    @FXML private ComboBox<String> departmentFilter;
    @FXML private ComboBox<String> yearFilter;

    @FXML
    public void initialize() {
        setupGridPane();
        facultyList.addAll(FacultyManager.getFaculties());
        updateTable("");
        searchField.textProperty().addListener((obs, old, newText) -> updateTable(newText.trim()));
    }

    private void setupGridPane() {
        tableGrid.setHgap(10);
        tableGrid.setVgap(5);
        tableGrid.setPadding(new Insets(10));

        // Add column constraints
        ColumnConstraints idCol = new ColumnConstraints();
        idCol.setPercentWidth(15);
        ColumnConstraints nameCol = new ColumnConstraints();
        nameCol.setPercentWidth(20);
        ColumnConstraints degreeCol = new ColumnConstraints();
        degreeCol.setPercentWidth(15);
        ColumnConstraints researchCol = new ColumnConstraints();
        researchCol.setPercentWidth(20);
        ColumnConstraints officeCol = new ColumnConstraints();
        officeCol.setPercentWidth(15);
        ColumnConstraints emailCol = new ColumnConstraints();
        emailCol.setPercentWidth(15);

        tableGrid.getColumnConstraints().addAll(idCol, nameCol, degreeCol, researchCol, officeCol, emailCol);
    }

    private void updateTable(String search) {
        facultyList.clear();
        page = 0;

        if (search.isEmpty()) {
            facultyList.addAll(FacultyManager.getFaculties());
        } else {
            facultyList.addAll(FacultyManager.getFacultiesByID(search));
            if (facultyList.isEmpty()) {
                facultyList.addAll(FacultyManager.getFacultiesByName(search));
            }
        }

        loadTable();
    }

    private void loadTable() {
        tableGrid.getChildren().clear();
        buttonMap.clear();

        addHeaderRow();

        Separator headerSeparator = new Separator();
        headerSeparator.setPadding(new Insets(5, 0, 5, 0));
        tableGrid.add(headerSeparator, 0, 1, 7, 1);

        int startIndex = page * pageRowCount;
        int endIndex = Math.min((page + 1) * pageRowCount, facultyList.size());

        for (int i = startIndex; i < endIndex; i++) {
            Faculty faculty = facultyList.get(i);
            int rowIndex = (i - startIndex) * 2 + 2;

            addFacultyRow(faculty, rowIndex);
        }

        updatePageButtons();
    }

    private void addHeaderRow() {
        Label[] headers = {
            new Label("Faculty ID"),
            new Label("Name"),
            new Label("Degree"),
            new Label("Research Interest"),
            new Label("Office"),
            new Label("Email")
        };

        for (int i = 0; i < headers.length; i++) {
            headers[i].getStyleClass().add("table-header");
            tableGrid.add(headers[i], i, 0);
        }
    }

    private void addFacultyRow(Faculty faculty, int rowIndex) {
        Label[] labels = {
            new Label(faculty.getID()),
            new Label(faculty.getName()),
            new Label(faculty.getDegree()),
            new Label(faculty.getResearchInterest()),
            new Label(faculty.getOfficeLocation()),
            new Label(faculty.getEmail())
        };

        String labelStyle = "-fx-padding: 5; -fx-font-size: 13px;";
        for (int i = 0; i < labels.length; i++) {
            labels[i].setStyle(labelStyle);
            tableGrid.add(labels[i], i, rowIndex);
        }

        Button editButton = createStyledButton("✎");
        tableGrid.add(editButton, 6, rowIndex);
        buttonMap.put(editButton, faculty);
        editButton.setOnAction(this::handleEdit);

        Separator rowSeparator = new Separator();
        rowSeparator.setPadding(new Insets(5, 0, 5, 0));
        tableGrid.add(rowSeparator, 0, rowIndex + 1, 7, 1);
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setMinWidth(60);
        button.setMinHeight(30);
        button.setStyle("-fx-background-color: #941B0C; -fx-text-fill: #F6AA1C; -fx-font-size: 14px; -fx-font-family: 'Graduate';");
        button.getStyleClass().add("action-button");
        
        // Add hover effect
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: #F6AA1C; -fx-text-fill: #941B0C; -fx-font-size: 14px; -fx-font-family: 'Graduate'; -fx-cursor: hand;");
            button.setEffect(new javafx.scene.effect.DropShadow(3, 0, 0, javafx.scene.paint.Color.rgb(0, 0, 0, 0.2)));
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: #941B0C; -fx-text-fill: #F6AA1C; -fx-font-size: 14px; -fx-font-family: 'Graduate';");
            button.setEffect(null);
        });
        
        button.setOnMousePressed(e -> 
            button.setStyle("-fx-background-color: #7B1609; -fx-text-fill: #F6AA1C; -fx-font-size: 14px; -fx-font-family: 'Graduate';"));
        
        button.setOnMouseReleased(e -> {
            if (button.isHover()) {
                button.setStyle("-fx-background-color: #F6AA1C; -fx-text-fill: #941B0C; -fx-font-size: 14px; -fx-font-family: 'Graduate';");
            } else {
                button.setStyle("-fx-background-color: #941B0C; -fx-text-fill: #F6AA1C; -fx-font-size: 14px; -fx-font-family: 'Graduate';");
            }
        });
        
        return button;
    }

    @FXML
    private void handleRefreshFaculty() {
        updateTable("");
    }

    @FXML
    private void handlePrintList() {
        System.out.println("Print button clicked");
    }

    @FXML
    private void handleFacultyDetails() {
        Faculty selectedFaculty = buttonMap.values().stream()
            .filter(faculty -> faculty.getID().equals(searchField.getText()))
            .findFirst()
            .orElse(null);
            
        if (selectedFaculty != null) {
            System.out.println("View details for: " + selectedFaculty.getName());
            // TODO: Implement faculty details view
        } else {
            System.out.println("Please select a faculty member");
        }
    }

    @FXML
    private void handleExportList() {
        System.out.println("Export list button clicked");
        // TODO: Implement export functionality
    }

    private void handleEdit(ActionEvent event) {
        Faculty faculty = buttonMap.get((Button) event.getSource());
        if (faculty != null) {
            System.out.println("Edit faculty: " + faculty.getName());
        }
    }

    @FXML
    private void handlePrevPage(ActionEvent event) {
        if (page > 0) {
            page--;
            loadTable();
        }
    }

    @FXML
    private void handleNextPage(ActionEvent event) {
        if ((page + 1) * pageRowCount < facultyList.size()) {
            page++;
            loadTable();
        }
    }

    @FXML
    private void handleKeyPage(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER) return;

        try {
            String text = pageText.getText();
            pageText.setText("");
            
            int newPage = Integer.parseInt(text) - 1;
            if (newPage >= 0 && newPage * pageRowCount < facultyList.size()) {
                page = newPage;
                loadTable();
            }
        } catch (NumberFormatException ignored) {}
    }

    @FXML
    private void handleKeyCount(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER) return;

        try {
            String text = rowCountText.getText();
            rowCountText.setText("");
            
            int newCount = Integer.parseInt(text);
            if (newCount > 5) {
                page = Math.min(page * pageRowCount / newCount, facultyList.size() / newCount);
                pageRowCount = newCount;
                loadTable();
            }
        } catch (NumberFormatException ignored) {}
    }

    private void updatePageButtons() {
        if (leftPageButton != null) {
            leftPageButton.setDisable(page <= 0);
        }
        if (rightPageButton != null) {
            rightPageButton.setDisable((page + 1) * pageRowCount >= facultyList.size());
        }
        if (pageText != null) {
            pageText.setPromptText(String.valueOf(page + 1));
        }
        if (rowCountText != null) {
            rowCountText.setPromptText(pageRowCount + " rows per page");
        }
    }
}