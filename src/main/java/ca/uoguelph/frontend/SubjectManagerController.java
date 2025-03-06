package ca.uoguelph.frontend;

import com.sun.jdi.event.ExceptionEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class SubjectManagerController {
    private static final Logger log = LogManager.getLogger(SubjectManagerController.class);

    private class Entry {
        private final String name, code;

        private String getName() {return name;}

        private String getCode() {return code;}

        private Entry(String name, String code) {
            this.name = name;
            this.code = code;
        }
    }

    private final Entry[] entries = {
            new Entry("Mathematics", "MATH001"), new Entry("English", "ENG101"),
            new Entry("Computer Science", "CS201"), new Entry("Chemistry", "CHEM200"),
            new Entry("Biology", "BIO300")};

    @FXML private GridPane tableGrid;
    @FXML private TextField searchField;

    @FXML
    public void initialize() {
        // clear grid contents first
        tableGrid.getChildren().clear();

        // TODO: grab subjects from database
        Entry[] entries = this.entries;

        // add subjects to grid
        for (int i = 0; i < entries.length; i++) {
            tableGrid.addRow(1);

            // set row constraint
            tableGrid.getRowConstraints().add(new RowConstraints(30, 60, 60));

            TextArea newName = new TextArea(entries[i].getName());
            TextArea newCode = new TextArea(entries[i].getCode());
            Button newButton = new Button("✎");

            tableGrid.add(newName, 0, i);
            tableGrid.add(newCode, 1, i);
            tableGrid.add(newButton, 2, i);

            GridPane.setMargin(newName, new Insets(5));
            GridPane.setMargin(newCode, new Insets(5));
            newButton.setPadding(new Insets(5, 10, 5, 10));

            // attach method to on-action
            newButton.setOnAction(this::handleEditSubject);
        }
    }

    // TODO: remove searchObjects and create a button-tracking system instead to optimize code
    private Node searchObjects(int r, int c) {
        for (Node node : tableGrid.getChildren()) {
            if (GridPane.getRowIndex(node) == null) continue;
            if (GridPane.getRowIndex(node) == r && GridPane.getColumnIndex(node) == c) {
                System.out.println("Found child " + node.getClass() + " at (" + r + ", " + c + ")");
                return node;
            }
        }

        System.out.println("Child not found");
        return null;
    }

    private boolean temp = false;
    @FXML
    private void handleSearch(ActionEvent event) {
        String searchText = searchField.getText();
        if (searchText.equals("")) {
            // TODO: load default settings
        }

        // TODO: search for subjects
        Entry[] searchEntries = new Entry[this.entries.length];
        if (temp) for (int i = 0; i < this.entries.length; i++) searchEntries[i] = this.entries[this.entries.length - i - 1];
        else searchEntries = this.entries;
        temp = !temp;

        // clear grid
        ObservableList<Node> children = tableGrid.getChildren();
        tableGrid.getChildren().clear();
        tableGrid.getRowConstraints().clear();

        // add entries to grid
        // TODO: create independent function to add entries to grid
        for (int i = 0; i < searchEntries.length; i++) {
            tableGrid.addRow(1);
            tableGrid.getRowConstraints().add(new RowConstraints(30, 60, 60));

            TextArea newName = new TextArea(searchEntries[i].getName());
            TextArea newCode = new TextArea(searchEntries[i].getCode());
            Button newButton = new Button("✎");

            tableGrid.add(newName, 0, i);
            tableGrid.add(newCode, 1, i);
            tableGrid.add(newButton, 2, i);

            GridPane.setMargin(newName, new Insets(5));
            GridPane.setMargin(newCode, new Insets(5));
            newButton.setPadding(new Insets(5, 10, 5, 10));

            // attach method to on-action
            newButton.setOnAction(this::handleEditSubject);
        }
    }

    @FXML
    private void handleAddSubject(ActionEvent event) {
        System.out.println("Add Subject clicked");

        handleLoadEditor(event);
    }

    @FXML
    private void handleEditSubject(ActionEvent event) {
        System.out.println("Edit Subject clicked");

        try {
            // Retrieve TextAreas from GridPane on row where Button resides
            int sourceRow = GridPane.getRowIndex((Button) event.getSource());
            TextArea nameArea = (TextArea) searchObjects(sourceRow, 0);
            TextArea codeArea = (TextArea) searchObjects(sourceRow, 1);

            if (nameArea == null || codeArea == null) {
                handleLoadEditor(event);
            } else {
                handleLoadEditor(nameArea.getText(), codeArea.getText(), event);
            }

        } catch (Exception e) {
            log.error("Catch: 122", e);
        }
    }

    private void handleLoadEditor(ActionEvent event) { handleLoadEditor("", "", event); }

    private void handleLoadEditor(String name, String code, ActionEvent event) {
        try {
            // Load the login screen FXML from the assets folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/subject_editor.fxml"));
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
            log.error("Catch: 164", e);
        }
    }
}