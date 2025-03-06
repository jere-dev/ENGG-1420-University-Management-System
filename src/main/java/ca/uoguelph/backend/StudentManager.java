package ca.uoguelph.backend;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentManager {
    private static HashMap<String, Student> students = new HashMap<String, Student>();

    public static void loadStudents() {
        ArrayList<ArrayList<String>> list = Database.loadStrings(2);
        for (ArrayList<String> student : list) {
            //TODO: only subjects are listed in excel so prof and courses are not initialized
            Student s = new Student(student.get(0), student.get(1), student.get(2), student.get(3), student.get(4), student.get(5), student.get(6), student.get(7), student.get(9), Double.parseDouble(student.get(10)), student.get(11));
            students.put(student.get(1), s);
            for(String subject : student.get(8).split(", "))
            {
                s.subjects.add(SubjectManager.getSubject(subject));
            }
        }
    }

    public static void addStudent(String _name, String _ID, String _profilePhoto, String _address, String _telephone,
            String _emailAddress,
            String _currentSemester, String _academicLevel, String _thesisTitle, float _progress) {
        // students.put(_name, new Student(_name, _ID, _profilePhoto, _address, _telephone, _emailAddress,
                // _currentSemester, _academicLevel, _thesisTitle, _progress));
        // TODO: add to excel
    }

    public static void removeStudent(Student _student) {
        _student.removeSelf();
        // TODO: remove from excel
    }

    public static Student getStudent(String _name) {
        return students.get(_name);
    }

    // TODO: search function

}
