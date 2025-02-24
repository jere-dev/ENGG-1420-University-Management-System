package ca.uoguelph.backend;

import java.util.HashMap;

public class StudentManager {
    public static HashMap<String, Student> students;

    StudentManager(){

    }

    public static void loadStudents(){
        //TODO: load from excel
    }

    public static void addStudent(String _name, String _ID, String _profilePhoto, String _address, String _telephone, String _emailAddress,
        String _currentSemester, String _academicLevel, String _thesisTitle, float _progress){
        students.put(_name, new Student(_name, _ID, _profilePhoto, _address, _telephone, _emailAddress, _currentSemester, _academicLevel, _thesisTitle, _progress));
        //TODO: add to excel
    }

    public static void removeStudent(Student _student){
        _student.removeSelf();
        //TODO: remove from excel
    }
}
