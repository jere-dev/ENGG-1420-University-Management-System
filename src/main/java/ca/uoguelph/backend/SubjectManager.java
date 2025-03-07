package ca.uoguelph.backend;

import java.util.ArrayList;
import java.util.HashMap;

public class SubjectManager {
    private static HashMap<String, Subject> subjects = new HashMap<String, Subject>();

    public static void loadSubjects(){
        ArrayList<ArrayList<String>> lists = Database.loadStrings(0);
        for (ArrayList<String> subject : lists) {
            subjects.put(subject.get(0), new Subject(subject.get(0), subject.get(1)));
        }
    }


    public static void deleteSubject(Subject _subject){
        //TODO: make _subject.removeSelf();
        subjects.remove(_subject.name);
        //TODO: delete from excel
    }

    public static void addSubject(String _name, String _code){
        subjects.put(_name, new Subject(_name, _code));
        //TODO: add to excel
    }

    public static Subject getSubject(String _code){
        return subjects.get(_code);
    }

    public static void removeCourse(Subject _subject, Course _course){
        //TODO: remove course from excel
    }

    //TODO: implement search function

}
