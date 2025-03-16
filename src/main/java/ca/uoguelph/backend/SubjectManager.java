package ca.uoguelph.backend;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SubjectManager {
    private static ArrayList<Subject> subjects = new ArrayList<Subject>();
    public static void addSubject(String code, String name) {subjects.add(new Subject(code, name));} //TODO: remove add to excel
    public static ArrayList<Subject> getSubjects(){return subjects;}

    public static void removeSubject(String code){
        Subject subject = subjects.stream().filter(s -> s.getCode().equals(code)).findFirst().orElse(null);
        if(subject == null){throw new IllegalArgumentException("invalid code");}
        subjects.remove(subject);
    }

    public static void removeSubject(Subject subject){
        subjects.remove(subject);
    }

    public static ArrayList<Subject> searchByName(String name){
        return subjects.stream().filter(s -> s.getName().contains(name)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<Subject> searchByCode(String code){
        return subjects.stream().filter(s -> s.getCode().contains(code)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static Subject getSubject(String code){
        Subject subject = subjects.stream().filter(s -> s.getCode().equals(code)).findFirst().orElse(null);
        if(subject == null){throw new IllegalArgumentException("invalid code");}
        return subject;
    }

    public static void editSubject(String code, String newCode, String newName){
        Subject subject = subjects.stream().filter(s -> s.getCode().equals(code)).findFirst().orElse(null);
        if(subject == null){throw new IllegalArgumentException("invalid code");}
        subject.setCode(newCode);
        subject.setName(newName);
    }

    public static void editSubject(Subject subject, String newCode, String newName){
        subject.setCode(newCode);
        subject.setName(newName);
    }
}
