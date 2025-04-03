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
        searchField.setStyle("-fx-prompt-text-fill: #757575; -fx-background-radius: 3; " +
                           "-fx-border-radius: 3; -fx-border-color: #E0E0E0; " +
                           "-fx-border-width: 1; -fx-background-color: white;");

        searchField.textProperty().addListener((obs, old, newText) -> {
            if (searchField.isFocused()) {
                javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(300));
                pause.setOnFinished(e -> updateTable(newText.trim()));
                pause.play();
            }
        });

        if (scrollPane != null) {
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            scrollPane.getStyleClass().add("edge-to-edge");
        }
    }


    private void setupGridPane() {
        tableGrid.setHgap(20);
        tableGrid.setVgap(5);
        tableGrid.setPadding(new Insets(5, 15, 15, 15));
        tableGrid.setStyle("-fx-background-color: white; -fx-background-radius: 3; " +
                          "-fx-border-radius: 3; -fx-border-color: #E0E0E0; " +
                          "-fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.05), 3, 0, 0, 0);");

        tableGrid.setMaxWidth(Double.MAX_VALUE);
        tableGrid.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(tableGrid, Priority.ALWAYS);
        HBox.setHgrow(tableGrid, Priority.ALWAYS);

        ColumnConstraints idCol = new ColumnConstraints();
        idCol.setPercentWidth(12);
        ColumnConstraints nameCol = new ColumnConstraints();
        nameCol.setPercentWidth(23);
        ColumnConstraints degreeCol = new ColumnConstraints();
        degreeCol.setPercentWidth(15);
        ColumnConstraints researchCol = new ColumnConstraints();
        researchCol.setPercentWidth(20);
        ColumnConstraints officeCol = new ColumnConstraints();
        officeCol.setPercentWidth(10);
        ColumnConstraints emailCol = new ColumnConstraints();
        emailCol.setPercentWidth(15);
        ColumnConstraints actionCol = new ColumnConstraints();
        actionCol.setPercentWidth(5);

        tableGrid.getColumnConstraints().clear();
        tableGrid.getColumnConstraints().addAll(idCol, nameCol, degreeCol, researchCol, officeCol, emailCol, actionCol);
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

        Separator headerSeparator = new Separator();
        headerSeparator.setPadding(new Insets(5, 0, 5, 0));
        tableGrid.add(headerSeparator, 0, 1, tableGrid.getColumnConstraints().size(), 1);

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
                new Label("Email"),
                new Label("Actions")
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

        for (int i = 0; i < labels.length; i++) {
            labels[i].setStyle(labelStyle);
            labels[i].setMaxWidth(Double.MAX_VALUE);
            labels[i].setMaxHeight(Double.MAX_VALUE);
            GridPane.setHgrow(labels[i], Priority.ALWAYS);
            GridPane.setVgrow(labels[i], Priority.SOMETIMES);
            tableGrid.add(labels[i], i, rowIndex);
        }

        Button editButton = createStyledButton("Edit");
        editButton.setOnAction(event -> handleEdit(faculty, event));
        buttonMap.put(editButton, faculty);

        HBox actionBox = new HBox(editButton);
        actionBox.setPadding(new Insets(5, 5, 5, 5));
        actionBox.setAlignment(javafx.geometry.Pos.CENTER);
        GridPane.setHalignment(actionBox, javafx.geometry.HPos.CENTER);
        GridPane.setValignment(actionBox, javafx.geometry.VPos.CENTER);
        tableGrid.add(actionBox, labels.length, rowIndex);

        Separator rowSeparator = new Separator();
        rowSeparator.setPadding(new Insets(0));
        rowSeparator.setStyle("-fx-background-color: #E0E0E0;");
        rowSeparator.setMaxWidth(Double.MAX_VALUE);
        tableGrid.add(rowSeparator, 0, rowIndex + 1, tableGrid.getColumnConstraints().size(), 1);
    }

    private void handleEdit(Faculty faculty, ActionEvent event) {
        System.out.println("DEBUG: Edit Faculty button clicked for: " + faculty.getName());
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/admin/faculty_editor.fxml"));
            Parent content = loader.load();

            FacultyEditorController controller = loader.getController();
            if (controller == null) {
                System.err.println("ERROR: FacultyEditorController is NULL.");
                 if (errorLabel != null) errorLabel.setText("Error: Could not load faculty editor.");
                return;
            }

            controller.loadFaculty(
                faculty.getID(),
                faculty.getName(),
                faculty.getDegree(),
                faculty.getResearchInterest(),
                faculty.getOfficeLocation(),
                faculty.getEmail(),
                ""
            );
            System.out.println("DEBUG: FacultyEditorController successfully executed loadFaculty()!");

            StackPane contentArea = (StackPane) ((Node) event.getSource()).getScene().lookup("#contentArea");
            if (contentArea == null) {
                 System.err.println("ERROR: Could not find contentArea.");
                 if (errorLabel != null) errorLabel.setText("Error: Could not find main content area.");
                return;
            }

            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);
            System.out.println("DEBUG: Faculty Editor UI loaded for editing!");

        } catch (Exception e) {
            System.err.println("ERROR: Failed to load Faculty Editor for editing.");
            if (errorLabel != null) errorLabel.setText("Error loading editor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #941B0C; -fx-text-fill: white; -fx-font-size: 12px;");
        button.setMinWidth(40);
        button.setMinHeight(25);

        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #B73A2C; -fx-text-fill: white; -fx-font-size: 12px; -fx-cursor: hand;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #941B0C; -fx-text-fill: white; -fx-font-size: 12px;"));
        button.setOnMousePressed(e -> button.setStyle("-fx-background-color: #7B1609; -fx-text-fill: white; -fx-font-size: 12px;"));
        button.setOnMouseReleased(e -> {
            if (button.isHover()) {
                button.setStyle("-fx-background-color: #B73A2C; -fx-text-fill: white; -fx-font-size: 12px;");
            } else {
                button.setStyle("-fx-background-color: #941B0C; -fx-text-fill: white; -fx-font-size: 12px;");
            }
        });

        return button;
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
                pageText.clear();
                errorLabel.setText("");
            } else {
                if (errorLabel != null) errorLabel.setText("Invalid page number.");
            }
        } catch (NumberFormatException ignored) {
            if (errorLabel != null) errorLabel.setText("Please enter a valid number for page.");
            pageText.clear();
        }
    }
    @FXML
    private void handleKeyCount(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER) return;

        try {
            int newCount = Integer.parseInt(rowCountText.getText());
            if (newCount > 0) {
                page = Math.min(page * pageRowCount / newCount, Math.max(0, facultyList.size() - 1) / newCount);
                pageRowCount = newCount;
                loadTable();
                rowCountText.clear();
                errorLabel.setText("");
            } else {
                if (errorLabel != null) errorLabel.setText("Row count must be positive.");
            }
        } catch (NumberFormatException ignored) {
            if (errorLabel != null) errorLabel.setText("Please enter a valid number for row count.");
            rowCountText.clear();
        }
    }
}
