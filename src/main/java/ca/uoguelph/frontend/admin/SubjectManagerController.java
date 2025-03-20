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
    private int page = 0;
    private int pageRowCount = 40;

    @FXML private Button leftPageButton, rightPageButton;
    @FXML private TextField pageText, rowCountText, searchField;
    @FXML private GridPane tableGrid;
    @FXML private ScrollPane scrollPane;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        // Set up grid properties
        tableGrid.setHgap(10);
        tableGrid.setVgap(5);
        tableGrid.setPadding(new Insets(10));

        // Add column constraints
        ColumnConstraints nameCol = new ColumnConstraints();
        nameCol.setPercentWidth(60);
        ColumnConstraints codeCol = new ColumnConstraints();
        codeCol.setPercentWidth(30);
        ColumnConstraints editCol = new ColumnConstraints();
        editCol.setPercentWidth(10);
        tableGrid.getColumnConstraints().addAll(nameCol, codeCol, editCol);

        // Initialize list and table
        subjectList.addAll(SubjectManager.getSubjects());
        updateTable("");

        // Enable dynamic search
        searchField.textProperty().addListener((obs, old, newText) -> 
            updateTable(newText.trim()));
    }

    @Override
    protected void updateTable(String search) {
        subjectList.clear();
        page = 0;

        if (search.isEmpty()) {
            subjectList.addAll(SubjectManager.getSubjects());
        } else {
            // Search by both name and code
            subjectList.addAll(SubjectManager.searchByName(search));
            if (subjectList.isEmpty()) {
                subjectList.addAll(SubjectManager.searchByCode(search));
            }
        }

        loadTable();
    }

    private void loadTable() {
        tableGrid.getChildren().clear();
        buttonMap.clear();
        
        // Add header
        addHeaderRow();
        
        // Update pagination
        updatePageButtons();

        // Calculate range for current page
        int startIndex = page * pageRowCount;
        int endIndex = Math.min((page + 1) * pageRowCount, subjectList.size());

        // Add subjects for current page
        for (int i = startIndex; i < endIndex; i++) {
            Subject subject = subjectList.get(i);
            int rowIndex = (i - startIndex) + 1;

            Label nameLabel = new Label(subject.getName());
            Label codeLabel = new Label(subject.getCode());
            Button editButton = createStyledButton("✎");

            nameLabel.setStyle("-fx-padding: 5; -fx-font-size: 13px;");
            codeLabel.setStyle("-fx-padding: 5; -fx-font-size: 13px;");

            tableGrid.addRow(rowIndex, nameLabel, codeLabel, editButton);
            buttonMap.put(editButton, subject);
            editButton.setOnAction(this::handleEdit);
        }
        
        
    }

    private void addHeaderRow() {
        Label nameHeader = new Label("Subject Name");
        Label codeHeader = new Label("Subject Code");
        Label editHeader = new Label("Actions");
        
        nameHeader.getStyleClass().add("table-header");
        codeHeader.getStyleClass().add("table-header");
        editHeader.getStyleClass().add("table-header");
        
        tableGrid.addRow(0, nameHeader, codeHeader, editHeader);
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
        
        // Add pressed effect
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
        pageText.setPromptText(String.valueOf(page + 1));
        rowCountText.setPromptText(pageRowCount + " subjects per page");
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
}
