package ca.uoguelph.backend;

import java.util.HashMap;

public class SubjectManager {
    private static HashMap<String, Subject> subjects = new HashMap<String, Subject>();

    public SubjectManager(){
        loadSubjects();
    }

    public static void loadSubjects(){
        //TODO: load from excel
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

    public static Subject getCourse(String _name){
        return subjects.get(_name);
    }

    public static void removeCourse(Subject _subject, Course _course){
        //TODO: remove course from excel
    }

    //TODO: implement search function 

}
