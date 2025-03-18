package ca.uoguelph.frontend.user;

import ca.uoguelph.frontend.objects.SubjectEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.List;

public class SubjectCatalogUserController {
    // list entry creates new GridPane containing subject name (left) and code (right)
    private class SbjCell<E> extends ListCell<E> {
        private final GridPane row = new GridPane();
        private final Label nameLabel = new Label(),
                codeLabel = new Label();

        @Override
        protected void updateItem(E param, boolean b) {
            super.updateItem(param, b);

            // assert that entry is of type Entry, etc.
            try {
                assert param.getClass() == SubjectEntry.class;
                assert ((SubjectEntry) param).name() != null;
                assert ((SubjectEntry) param).code() != null;
            } catch (Exception e) {e.printStackTrace();}
            SubjectEntry entry = (SubjectEntry) param;

            // repetitive actions may not need to occur multiple times
            if (row.getColumnConstraints().isEmpty()) {
                // set column constraints
                ColumnConstraints coConst = new ColumnConstraints();
                coConst.setPercentWidth(50);
                row.getColumnConstraints().add(coConst);
                row.setMaxWidth(Integer.MAX_VALUE);
            }
            if (row.getChildren().isEmpty()) {
                // add labels to constraint
                nameLabel.setPadding(new Insets(5));
                codeLabel.setPadding(new Insets(5));

                row.add(nameLabel, 0, 0);
                row.add(codeLabel, 1, 0);
            }


            if (entry != null) {
                nameLabel.setText(entry.name());
                codeLabel.setText(entry.code());
            } else {
                nameLabel.setText("");
                codeLabel.setText("");
            }

            setGraphic(row);
        }
    }

    
    // hardcoded values
    private final SubjectEntry[] entries = {
            new SubjectEntry("Mathematics", "MATH001"), new SubjectEntry("English", "ENG101"),
            new SubjectEntry("Computer Science", "CS201"), new SubjectEntry("Chemistry", "CHEM200"),
            new SubjectEntry("Biology", "BIO300")};

    // ObservableList for cell factories
    private ObservableList<SubjectEntry> entryObservableList;

    @FXML ListView<SubjectEntry> subjectList;
    @FXML TextField searchField;

    @FXML
    private void initialize() {
        // TODO: retrieve values from database
        SubjectEntry[] entries = this.entries;

        // change cell factory for ListView
        subjectList.setCellFactory(entryListView -> new SbjCell<>());

        // add observable list to ListView
        entryObservableList = FXCollections.observableArrayList(entries);

        subjectList.setItems(entryObservableList);
    }

    @FXML
    private void handleSearch(ActionEvent e) {
        String s = searchField.getText();

        // TODO: retrieve subjects based on search string
        List<SubjectEntry> sortEntries = entryObservableList.reversed();

        // update entryListView
        entryObservableList.clear();
        entryObservableList = FXCollections.observableArrayList(sortEntries);
    }
}
