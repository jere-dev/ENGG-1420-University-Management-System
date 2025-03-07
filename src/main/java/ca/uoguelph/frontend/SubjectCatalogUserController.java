package ca.uoguelph.frontend;

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
    // object wrapper for subject name and code
    private class Entry {
        private final String name, code;

        public String getName() {return name;}
        public String getCode() {return code;}

        public Entry(String name, String code) {
            this.name = name;
            this.code = code;
        }
    }

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
                assert param.getClass() == Entry.class;
                assert ((Entry) param).getName() != null;
                assert ((Entry) param).getCode() != null;
            } catch (Exception e) {e.printStackTrace();}
            Entry entry = (Entry) param;

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
                nameLabel.setText(entry.getName());
                codeLabel.setText(entry.getCode());
            } else {
                nameLabel.setText("");
                codeLabel.setText("");
            }

            setGraphic(row);
        }
    }

    
    // hardcoded values
    private final Entry[] entries = {
            new Entry("Mathematics", "MATH001"), new Entry("English", "ENG101"),
            new Entry("Computer Science", "CS201"), new Entry("Chemistry", "CHEM200"),
            new Entry("Biology", "BIO300")};

    // ObservableList for cell factories
    private ObservableList<Entry> entryObservableList;

    @FXML ListView<Entry> subjectList;
    @FXML TextField searchField;

    @FXML
    private void initialize() {
        // TODO: retrieve values from database
        Entry[] entries = this.entries;

        // change cell factory for ListView
        subjectList.setCellFactory(entryListView -> new SbjCell<>());

        // add observable list to ListView
        entryObservableList = FXCollections.observableArrayList(entries);

        subjectList.setItems(entryObservableList);
    }


    boolean temp = false;
    @FXML
    private void handleSearch(ActionEvent e) {
        String s = searchField.getText();

        // TODO: retrieve subjects based on search string
        List<Entry> sortEntries = entryObservableList.reversed();

        // update entryListView
        entryObservableList.clear();
        entryObservableList = FXCollections.observableArrayList(sortEntries);
    }
}
