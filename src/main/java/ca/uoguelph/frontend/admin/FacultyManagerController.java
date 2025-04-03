package ca.uoguelph.frontend.admin;

import ca.uoguelph.backend.Faculty;
import ca.uoguelph.backend.FacultyManager;
import ca.uoguelph.frontend.objects.controller.AbstractAdminEditorController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.util.Pair;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FacultyManagerController extends AbstractAdminEditorController {
    private final List<Faculty> facultyList = new ArrayList<>();
    private final HashMap<Button, Faculty> buttonMap = new HashMap<>();
    private int page = 0;
    private int pageRowCount = 20;

    @FXML private Button addFacultyButton;
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

        if (addFacultyButton != null) {
            addFacultyButton.setOnAction(event -> {
                System.out.println("Add Faculty button clicked!");
                handleAdd(event);
            });
        }

        searchField.setPromptText("Search by ID, Name, or Email...");
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
            String normalizedSearch = search.toLowerCase().trim();

            facultyList.addAll(FacultyManager.getFaculties().stream()
                    .filter(f -> f.getID().toLowerCase().contains(normalizedSearch) ||
                            f.getName().toLowerCase().contains(normalizedSearch) ||
                            f.getEmail().toLowerCase().contains(normalizedSearch))
                    .toList());
        }

        loadTable();
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

    private void loadTable() {
        tableGrid.getChildren().clear();
        buttonMap.clear();
        addHeaderRow();

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
            headers[i].setStyle("-fx-font-weight: bold;");
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

        for (int i = 0; i < labels.length; i++) {
            tableGrid.add(labels[i], i, rowIndex);
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
    private void handleSearch(ActionEvent event) {
        updateTable(searchField.getText());
    }

    @FXML
    protected void handleAdd(ActionEvent event) {
        System.out.println("DEBUG: Add Faculty button clicked!");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/admin/faculty_editor.fxml"));
            Parent content = loader.load();

            FacultyEditorController controller = loader.getController();
            if (controller == null) {
                System.out.println("ERROR: FacultyEditorController is NULL.");
                return;
            }

            controller.loadFaculty("", "", "", "", "", "", "");
            System.out.println("DEBUG: FacultyEditorController successfully executed loadFaculty()!");

            // Verify that content is being added to the scene
            StackPane contentArea = (StackPane) ((Node) event.getSource()).getScene().lookup("#contentArea");
            if (contentArea == null) {
                System.out.println("ERROR: Could not find contentArea.");
                return;
            }

            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);
            System.out.println("DEBUG: Faculty Editor UI added to scene!");
        } catch (Exception e) {
            System.out.println("ERROR: Failed to load Faculty Editor.");
            e.printStackTrace();
        }
    }





    @Override
    protected void handleSave(ActionEvent event) {
        // Implementation here
    }

    @Override
    protected void handleDelete(ActionEvent event) {
        // Implementation here
    }

    @FXML
    private void handleKeyPage(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER) return;

        try {
            int newPage = Integer.parseInt(pageText.getText()) - 1;
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
            int newCount = Integer.parseInt(rowCountText.getText());
            if (newCount > 5) {
                page = Math.min(page * pageRowCount / newCount, facultyList.size() / newCount);
                pageRowCount = newCount;
                loadTable();
            }
        } catch (NumberFormatException ignored) {}
    }




}
