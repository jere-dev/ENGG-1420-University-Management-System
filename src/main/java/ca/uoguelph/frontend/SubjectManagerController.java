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
import org.apache.poi.hssf.record.chart.SeriesChartGroupIndexRecord;

import java.io.IOException;
import java.util.Arrays;

public class SubjectManagerController {
    private static final Logger log = LogManager.getLogger(SubjectManagerController.class);

    private static class Entry {
        private final SimpleStringProperty name, code;

        private String getName() {return name.get();}

        private String getCode() {return code.get();}

        private Entry(String name, String code) {
            this.name = new SimpleStringProperty(name);
            this.code = new SimpleStringProperty(code);
        }
    }

    private final Entry[] entries = {
            new Entry("Mathematics", "MATH001"), new Entry("English", "ENG101"),
            new Entry("Computer Science", "CS201"), new Entry("Chemistry", "CHEM200"),
            new Entry("Biology", "BIO300")};

    @FXML private GridPane tableGrid;

    @FXML private TextArea replicableName, replicableCode;
    @FXML private Button replicableButton;

    @FXML
    public void initialize() {
        // TODO: grab subjects from database

        Entry[] entries = this.entries;

        for (int i = 0; i < entries.length; i++) {
            tableGrid.addRow(1);

            // set row constraint
            tableGrid.getRowConstraints().add(new RowConstraints(30, 60, 60));

            TextArea newName = new TextArea(entries[i].getName());
            TextArea newCode = new TextArea(entries[i].getCode());
            Button newButton = new Button("✎");

            tableGrid.add(newName, 0, i + 1);
            tableGrid.add(newCode, 1, i + 1);
            tableGrid.add(newButton, 2, i + 1);

            GridPane.setMargin(newName, new Insets(5));
            GridPane.setMargin(newCode, new Insets(5));
            newButton.setPadding(new Insets(5, 10, 5, 10));

            // attach method to on-action
            newButton.setOnAction(this::handleEditSubject);

            for (Node node : tableGrid.getChildren())
                System.out.printf("\nFound child %s", node.getClass());
        }
    }

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

            /*
            // Set up the scene and stage
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Subject Editor");
            stage.setScene(scene);

            stage.show();
            */
        } catch (Exception e) {
            log.error("Catch: 164", e);
        }
    }
}