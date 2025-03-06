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
import java.util.HashMap;

public class SubjectManagerController {
    /*------------------- internal functions -------------------*/
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

    // hardcoded values
    private final Entry[] entries = {
            new Entry("Mathematics", "MATH001"), new Entry("English", "ENG101"),
            new Entry("Computer Science", "CS201"), new Entry("Chemistry", "CHEM200"),
            new Entry("Biology", "BIO300")
    };

    // maps button to entry values
    private final HashMap<Button, Entry> buttonMap = new HashMap<>();

    private void updateGrid(Entry[] entries) {
        // clear grid values
        tableGrid.getChildren().clear();
        tableGrid.getRowConstraints().clear();
        buttonMap.clear();

        // add entries
        for (int i = 0; i < entries.length; i++) {
            tableGrid.addRow(1);
            tableGrid.getRowConstraints().add(new RowConstraints(30, 60, 60));

            TextArea newName = new TextArea(entries[i].getName()),
                    newCode = new TextArea(entries[i].getCode());
            Button newButton = new Button("✎");

            // make TextAreas un-editable
            newName.setEditable(false); newCode.setEditable(false);

            tableGrid.add(newName, 0, i);
            tableGrid.add(newCode, 1, i);
            tableGrid.add(newButton, 2, i);

            GridPane.setMargin(newName, new Insets(5));
            GridPane.setMargin(newCode, new Insets(5));
            newButton.setPadding(new Insets(5, 10, 5, 10));

            // attach method to on-action and map
            buttonMap.put(newButton, entries[i]);
            newButton.setOnAction(this::handleEditSubject);
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
    /* ------------------- \internal functions -------------------*/

    @FXML private GridPane tableGrid;
    @FXML private TextField searchField;

    @FXML
    public void initialize() {
        // clear grid contents first
        tableGrid.getChildren().clear();

        // TODO: grab subjects from database
        Entry[] entries = this.entries;

        updateGrid(entries);
    }

    private boolean temp = false;
    @FXML
    private void handleSearch(ActionEvent event) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()) {
            // TODO: load default settings
        }

        // TODO: search for subjects
        Entry[] searchEntries = new Entry[this.entries.length];
        if (temp) for (int i = 0; i < this.entries.length; i++) searchEntries[i] = this.entries[this.entries.length - i - 1];
        else searchEntries = this.entries;
        temp = !temp;

        updateGrid(searchEntries);
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
            Entry sourceEntry = buttonMap.get((Button) event.getSource());

            if (sourceEntry.getName().isEmpty()) {
                handleLoadEditor(event);
            } else {
                handleLoadEditor(sourceEntry.getName(), sourceEntry.getCode(), event);
            }

        } catch (Exception e) {
            log.error("Catch: 122", e);
        }
    }
}