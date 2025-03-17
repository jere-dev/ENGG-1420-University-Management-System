package ca.uoguelph.backend;

import java.util.ArrayList;
import java.util.stream.Collectors;

//TODO: modify excel sheet
public class SubjectManager {
    private static ArrayList<Subject> subjects = new ArrayList<Subject>();

//get Subjects
    public static Subject getSubject(String code){
        Subject subject = subjects.stream().filter(s -> s.getCode().equals(code)).findFirst().orElse(null);
        if(subject == null){throw new IllegalArgumentException("invalid code");}
        return subject;
    }
    public static ArrayList<Subject> getSubjects(){return subjects;}
    public static ArrayList<Subject> searchByName(String name){ return subjects.stream().filter(s -> s.getName().contains(name)).collect(Collectors.toCollection(ArrayList::new));}
    public static ArrayList<Subject> searchByCode(String code){ return subjects.stream().filter(s -> s.getCode().contains(code)).collect(Collectors.toCollection(ArrayList::new));}

//modify Subject
    //add Subject
    //TODO: make sure Code is unique
    public static void addSubject(String code, String name) {subjects.add(new Subject(code, name));} //TODO: remove add to excel
    //remove Subject
    public static void removeSubject(String code){ subjects.remove(getSubject(code));}
    public static void removeSubject(Subject subject){ subjects.remove(subject);}
    //edit Subject
    public static void editSubjectCode(Subject subject, String code){ subject.setCode(code);}
    public static void editSubjectName(Subject subject, String name){ subject.setName(name);}
}
