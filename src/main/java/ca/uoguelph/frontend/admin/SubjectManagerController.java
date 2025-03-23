package ca.uoguelph.frontend.admin;

import ca.uoguelph.frontend.objects.controller.AbstractAdminListController;

import ca.uoguelph.backend.Subject;
import ca.uoguelph.backend.SubjectManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class SubjectManagerController extends AbstractAdminListController {
    private final List<Subject> subjectList = new ArrayList<>();
    private final HashMap<Button, Subject> buttonMap = new HashMap<>();
    public VBox screenBox;
    private int page = 0;
    private int pageRowCount = 20;

    @FXML private Button leftPageButton, rightPageButton;
    @FXML private TextField pageText, rowCountText, searchField;
    @FXML private GridPane tableGrid;
    @FXML private ScrollPane scrollPane;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        setupGridPane();
        subjectList.addAll(SubjectManager.getSubjects());
        updateTable("");

        // Setup scroll pane
        if (scrollPane != null) {
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            scrollPane.getStyleClass().add("edge-to-edge");
        }

        // Style the search field
        searchField.setPromptText("Search by Name or Code...");
        searchField.setStyle("-fx-prompt-text-fill: #757575; -fx-background-radius: 3; " +
                           "-fx-border-radius: 3; -fx-border-color: #E0E0E0; " +
                           "-fx-border-width: 1; -fx-background-color: white; " +
                           "-fx-font-family: System; -fx-font-size: 13px;");

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

        // Make grid fill parent
        tableGrid.setMaxWidth(Double.MAX_VALUE);
        tableGrid.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(tableGrid, Priority.ALWAYS);
        HBox.setHgrow(tableGrid, Priority.ALWAYS);

        // Column constraints
        ColumnConstraints codeCol = new ColumnConstraints();
        codeCol.setPercentWidth(30);
        ColumnConstraints nameCol = new ColumnConstraints();
        nameCol.setPercentWidth(50);
        ColumnConstraints actionsCol = new ColumnConstraints();
        actionsCol.setPercentWidth(20);

        tableGrid.getColumnConstraints().addAll(codeCol, nameCol, actionsCol);
    }

    @Override
    protected void updateTable(String search) {
        subjectList.clear();
        page = 0;

        if (search.isEmpty()) {
            subjectList.addAll(SubjectManager.getSubjects());
        } else {
            // Normalize search text
            String normalizedSearch = search.toLowerCase()
                .replaceAll("\\s+", " ")
                .replaceAll("\\.", " ")
                .trim();

            // Search with normalized comparison
            subjectList.addAll(SubjectManager.getSubjects().stream()
                .filter(s -> {
                    String normalizedName = s.getName().toLowerCase()
                        .replaceAll("\\s+", " ")
                        .replaceAll("\\.", " ")
                        .trim();
                    String normalizedCode = s.getCode().toLowerCase()
                        .replaceAll("\\s+", " ")
                        .replaceAll("\\.", " ")
                        .trim();

                    return normalizedName.contains(normalizedSearch) ||
                           normalizedCode.contains(normalizedSearch);
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
        tableGrid.add(headerSeparator, 0, 1, tableGrid.getColumnCount(), 1);

        int startIndex = page * pageRowCount;
        int endIndex = Math.min((page + 1) * pageRowCount, subjectList.size());

        for (int i = startIndex; i < endIndex; i++) {
            Subject subject = subjectList.get(i);
            int rowIndex = (i - startIndex) * 2 + 2;
            addSubjectRow(subject, rowIndex);
        }

        updatePageButtons();
    }

    private void addHeaderRow() {
        Label[] headers = {
            new Label("Subject Code"),
            new Label("Subject Name"),
            new Label("Actions")
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
    }

    private void addSubjectRow(Subject subject, int rowIndex) {
        HBox rowContainer = new HBox();
        rowContainer.setMaxWidth(Double.MAX_VALUE);
        rowContainer.getStyleClass().add("table-row");
        rowContainer.setStyle("-fx-background-color: transparent;");

        Label[] labels = {
            new Label(subject.getCode()),
            new Label(subject.getName())
        };

        // Create edit button
        Button editButton = createStyledButton("Edit");
        buttonMap.put(editButton, subject);
        editButton.setOnAction(this::handleEdit);

        String labelStyle = "-fx-padding: 10 5 10 5; -fx-font-size: 13px; " +
                          "-fx-text-fill: #333333; -fx-font-family: System;";

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

        // Add labels and edit button
        for (int i = 0; i < labels.length; i++) {
            labels[i].setStyle(labelStyle);
            labels[i].setMaxWidth(Double.MAX_VALUE);
            labels[i].setMaxHeight(Double.MAX_VALUE);
            GridPane.setHgrow(labels[i], Priority.ALWAYS);
            GridPane.setVgrow(labels[i], Priority.ALWAYS);
            tableGrid.add(labels[i], i, rowIndex);
        }
        tableGrid.add(editButton, 2, rowIndex);

        Separator rowSeparator = new Separator();
        rowSeparator.setPadding(new Insets(0));
        rowSeparator.setStyle("-fx-background-color: #E0E0E0;");
        rowSeparator.setMaxWidth(Double.MAX_VALUE);
        tableGrid.add(rowSeparator, 0, rowIndex + 1, tableGrid.getColumnCount(), 1);
    }

    @FXML
    private void handleNextPage(ActionEvent event) {
        page++;
        loadTable();
    }

    @FXML
    private void handlePrevPage(ActionEvent event) {
        page--;
        loadTable();
    }

    @FXML
    private void handleKeyPage(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER) return;

        String text = pageText.getText();
        pageText.setText("");

        try {
            int newPage = Integer.parseInt(text) - 1;
            if (newPage >= 0 && newPage * pageRowCount < subjectList.size()) {
                page = newPage;
                loadTable();
            }
        } catch (NumberFormatException ignored) {}
    }

    @FXML
    private void handleKeyCount(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER) return;

        String text = rowCountText.getText();
        rowCountText.setText("");

        try {
            int newCount = Integer.parseInt(text);
            if (newCount > 5) {
                page = Math.min(page * pageRowCount / newCount, subjectList.size() / newCount);
                pageRowCount = newCount;
                loadTable();
            }
        } catch (NumberFormatException ignored) {}
    }

    private void updatePageButtons() {
        boolean leftDisable = page <= 0;
        boolean rightDisable = (page + 1) * pageRowCount >= subjectList.size();

        leftPageButton.setDisable(leftDisable);
        rightPageButton.setDisable(rightDisable);
        if (pageText != null) {
            pageText.setPromptText(String.valueOf(page + 1));
            pageText.setStyle("-fx-font-family: System; -fx-font-size: 13px;");
        }
        if (rowCountText != null) {
            rowCountText.setPromptText(pageRowCount + " subjects per page");
            rowCountText.setStyle("-fx-font-family: System; -fx-font-size: 13px;");
        }
    }

    @Override
    protected void handleLoadEditor(ActionEvent event) { handleLoadEditor("", "", event); }

    private void handleLoadEditor(String name, String code, ActionEvent event) {
        try {
            // Load the login screen FXML from the assets folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/admin/subject_editor.fxml"));
            Parent content = loader.load();

            // Find contentArea that houses dashboard content
            StackPane contentArea = null;
            Parent parent = ((Node) event.getSource()).getParent();
            while (parent != null) {
                if (parent.getId() != null && parent.getId().equals("contentArea")) {
                    contentArea = (StackPane) parent;
                    break;
                }
                parent = parent.getParent();
            }
            if (contentArea == null) throw new RuntimeException("Could not find parent StackPane");

            // Send name, code to TextFields under FXML controller
            SubjectEditorController controller = loader.getController();
            controller.loadSubject(name, code);

            // Load FXML to contentArea
            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML @Override
    protected void handleSearch(ActionEvent event) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()) {
            // TODO: load default settings
        }

        updateTable(searchText);
    }

    @FXML @Override
    protected void handleAdd(ActionEvent event) {
        System.out.println("Add Subject clicked");

        handleLoadEditor(event);
    }

    @FXML @Override
    protected void handleEdit(ActionEvent event) {
        System.out.println("Edit Subject clicked");

        try {
            // Retrieve TextAreas from GridPane on row where Button resides
            Subject sourceEntry = buttonMap.get((Button) event.getSource());

            if (sourceEntry.getName().isEmpty()) {
                handleLoadEditor(event);
            } else {
                handleLoadEditor(sourceEntry.getName(), sourceEntry.getCode(), event);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #941B0C; -fx-text-fill: #F6AA1C; " +
                       "-fx-background-radius: 3; -fx-font-family: System; -fx-font-size: 13px;");

        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: #F6AA1C; -fx-text-fill: #941B0C; " +
                          "-fx-background-radius: 3; -fx-font-family: System; -fx-font-size: 13px; -fx-cursor: hand;");
            button.setEffect(new javafx.scene.effect.DropShadow(3, 0, 0, javafx.scene.paint.Color.rgb(0, 0, 0, 0.2)));
        });

        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: #941B0C; -fx-text-fill: #F6AA1C; " +
                          "-fx-background-radius: 3; -fx-font-family: System; -fx-font-size: 13px;");
            button.setEffect(null);
        });

        button.setOnMousePressed(e ->
            button.setStyle("-fx-background-color: #7B1609; -fx-text-fill: #F6AA1C; " +
                          "-fx-background-radius: 3; -fx-font-family: System; -fx-font-size: 13px;"));

        button.setOnMouseReleased(e -> {
            if (button.isHover()) {
                button.setStyle("-fx-background-color: #F6AA1C; -fx-text-fill: #941B0C; " +
                              "-fx-background-radius: 3; -fx-font-family: System; -fx-font-size: 13px;");
            } else {
                button.setStyle("-fx-background-color: #941B0C; -fx-text-fill: #F6AA1C; " +
                              "-fx-background-radius: 3; -fx-font-family: System; -fx-font-size: 13px;");
            }
        });

        return button;
    }
}
