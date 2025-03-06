package ca.uoguelph.frontend;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.security.interfaces.EdECKey;

public class SubjectCatalogUserController {
    private class Entry {
        private final SimpleStringProperty name, code;

        private String getName() {return name.get();}
        private String getCode() {return code.get();}

        private Entry(String name, String code) {
            this.name = new SimpleStringProperty(name);
            this.code = new SimpleStringProperty(code);
        }
    }
    
    // hardcoded values
    private final Entry[] entries = {
            new Entry("Mathematics", "MATH001"), new Entry("English", "ENG101"),
            new Entry("Computer Science", "CS201"), new Entry("Chemistry", "CHEM200"),
            new Entry("Biology", "BIO300")};
    
    // ObservableList for cell factories
    private ObservableList<Entry> entryObservableList;

    @FXML ListView<Entry> entryListView;
    @FXML TextField searchField;

    @FXML
    private void initialize() {
        // TODO: retrieve values from database
        Entry[] entries = this.entries;

        // add observable list to ListView
        entryObservableList = FXCollections.observableArrayList(entries);
        entryListView.setItems(entryObservableList);
    }



    @FXML
    private void handleSearch(ActionEvent e) {
        String s = searchField.getText();

        // TODO: retrieve subjects based on search string
        Entry[] sortEntries = this.entries;

        // update entryListView
        entryObservableList = FXCollections.observableArrayList(sortEntries);
    }
}
