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

import java.util.ArrayList;
import java.util.HashMap;

public final class SubjectManagerController extends AbstractAdminListController {
    /*------------------- internal functions -------------------*/

    // maps button to entry values
    private final HashMap<Button, Subject> buttonMap = new HashMap<>();

    @Override
    protected void updateTable(String search) {
        // clear grid values
        tableGrid.getChildren().clear();
        tableGrid.getRowConstraints().clear();
        buttonMap.clear();

        // add entries
        ArrayList<Subject> subjects = SubjectManager.searchByName(search);
        for (int i = 0; i < subjects.size(); i++) {
            tableGrid.addRow(1);
            tableGrid.getRowConstraints().add(new RowConstraints(30, 60, 60));

            TextArea newName = new TextArea(subjects.get(i).getName()),
                    newCode = new TextArea(subjects.get(i).getCode());
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
            buttonMap.put(newButton, subjects.get(i));
            newButton.setOnAction(this::handleEdit);
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
    
    /* ------------------- \internal functions -------------------*/

    @FXML private GridPane tableGrid;
    @FXML private TextField searchField;

    @FXML
    public void initialize() {
        // clear grid contents first
        tableGrid.getChildren().clear();

        updateTable("");
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