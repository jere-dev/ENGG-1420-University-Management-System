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
    @FXML private ScrollPane scrollPane;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        setupGridPane();
        facultyList.addAll(FacultyManager.getFaculties());
        updateTable("");

        // Setup scroll pane
        if (scrollPane != null) {
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            scrollPane.getStyleClass().add("edge-to-edge");
        }

        // Style the search field
        searchField.setPromptText("Search by ID, Name or Email...");
        searchField.setStyle("-fx-prompt-text-fill: #757575; -fx-background-radius: 3; " +
                           "-fx-border-radius: 3; -fx-border-color: #E0E0E0; " +
                           "-fx-border-width: 1; -fx-background-color: white;");

        // Enable dynamic search with small delay
        searchField.textProperty().addListener((obs, old, newText) -> {
            if (searchField.isFocused()) {
                javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(300));
                pause.setOnFinished(e -> updateTable(newText.trim()));
                pause.play();
            }
        });
    }

    private void setupGridPane() {
        tableGrid.setHgap(20);
        tableGrid.setVgap(5);
        tableGrid.setPadding(new Insets(5, 15, 15, 15));
        tableGrid.setStyle("-fx-background-color: white; -fx-background-radius: 3; " +
                          "-fx-border-radius: 3; -fx-border-color: #E0E0E0; " +
                          "-fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.05), 3, 0, 0, 0);");

        // Column constraints (match Student Manager's layout proportions)
        ColumnConstraints idCol = new ColumnConstraints();
        idCol.setPercentWidth(15);
        ColumnConstraints nameCol = new ColumnConstraints();
        nameCol.setPercentWidth(25);
        ColumnConstraints degreeCol = new ColumnConstraints();
        degreeCol.setPercentWidth(15);
        ColumnConstraints researchCol = new ColumnConstraints();
        researchCol.setPercentWidth(20);
        ColumnConstraints officeCol = new ColumnConstraints();
        officeCol.setPercentWidth(10);
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
            // Normalize search text
            String normalizedSearch = search.toLowerCase()
                .replaceAll("\\s+", " ")
                .replaceAll("\\.", " ")
                .trim();

            // Search with normalized comparison across multiple fields
            facultyList.addAll(FacultyManager.getFaculties().stream()
                .filter(f -> {
                    String normalizedID = f.getID().toLowerCase()
                        .replaceAll("\\s+", " ")
                        .trim();
                    String normalizedName = f.getName().toLowerCase()
                        .replaceAll("\\s+", " ")
                        .trim();
                    String normalizedEmail = f.getEmail().toLowerCase()
                        .replaceAll("\\s+", " ")
                        .trim();
                    
                    return normalizedID.contains(normalizedSearch) || 
                           normalizedName.contains(normalizedSearch) ||
                           normalizedEmail.contains(normalizedSearch);
                })
                .toList());
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

        String headerStyle = "-fx-font-weight: bold; -fx-font-size: 14px; " +
                           "-fx-text-fill: #941B0C; -fx-padding: 12 5 12 5; " +
                           "-fx-background-color: transparent;";
        
        for (int i = 0; i < headers.length; i++) {
            headers[i].setStyle(headerStyle);
            headers[i].setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(headers[i], Priority.ALWAYS);
            tableGrid.add(headers[i], i, 0);
        }
    }

    private void addFacultyRow(Faculty faculty, int rowIndex) {
        HBox rowContainer = new HBox();
        rowContainer.setMaxWidth(Double.MAX_VALUE);
        rowContainer.getStyleClass().add("table-row");
        rowContainer.setStyle("-fx-background-color: transparent;");

        Label[] labels = {
            new Label(faculty.getID()),
            new Label(faculty.getName()),
            new Label(faculty.getDegree()),
            new Label(faculty.getResearchInterest()),
            new Label(faculty.getOfficeLocation()),
            new Label(faculty.getEmail())
        };

        String labelStyle = "-fx-padding: 10 5 10 5; -fx-font-size: 13px; -fx-text-fill: #333333;";
        
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
        
        for (int i = 0; i < labels.length; i++) {
            labels[i].setStyle(labelStyle);
            labels[i].setMaxWidth(Double.MAX_VALUE);
            labels[i].setMaxHeight(Double.MAX_VALUE);
            GridPane.setHgrow(labels[i], Priority.ALWAYS);
            GridPane.setVgrow(labels[i], Priority.ALWAYS);
            tableGrid.add(labels[i], i, rowIndex);
        }

        Separator rowSeparator = new Separator();
        rowSeparator.setPadding(new Insets(0));
        rowSeparator.setStyle("-fx-background-color: #E0E0E0;");
        rowSeparator.setMaxWidth(Double.MAX_VALUE);
        tableGrid.add(rowSeparator, 0, rowIndex + 1, tableGrid.getColumnCount(), 1);
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
        } else {
            System.out.println("Please select a faculty member");
        }
    }

    @FXML
    private void handleExportList() {
        System.out.println("Export list button clicked");
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

    @FXML
    private void handleAdd(ActionEvent event) {
        System.out.println("Add Faculty clicked");
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()) {
            // TODO: load default settings
        }
        updateTable(searchText);
    }
}