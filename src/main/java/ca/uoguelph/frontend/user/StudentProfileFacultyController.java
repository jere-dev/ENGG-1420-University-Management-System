package ca.uoguelph.frontend.user;

import ca.uoguelph.backend.Student;
import ca.uoguelph.backend.StudentManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Pair;

import java.util.ArrayList;
//this controller lets the FACTY see the students id and name. USER Features (for faculty):
//- View Student Information: View name & ID of students enrolled in their cours

public class StudentProfileFacultyController {
    @FXML
    private TableView<Pair<String, String>> studentTable;

    @FXML
    private TableColumn<Pair<String, String>, String> nameColumn;

    @FXML
    private TableColumn<Pair<String, String>, String> idColumn;

    @FXML
    public void initialize(){

        ArrayList<Student> students = StudentManager.getStudents();//populated array
        ArrayList<Pair<String, String>> studentIDAndNameList = new ArrayList<>();

        for(Student student: students){//
        studentIDAndNameList.add(new Pair<>(student.getName(), student.getID()));//have to add new pair cuz weird
        }

        ObservableList<Pair<String, String>> observableList = FXCollections.observableArrayList(studentIDAndNameList);

        studentTable.setItems(observableList);//observable version of stuidandnamelist

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));//key stu name bound
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue()));//value bound tostu id
    }

}
