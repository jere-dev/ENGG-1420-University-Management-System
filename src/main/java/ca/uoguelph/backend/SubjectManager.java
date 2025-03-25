package ca.uoguelph.backend;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SubjectManager {
    private static ArrayList<Subject> subjects = new ArrayList<Subject>();

//load subjects
    public static void loadSubjects(){
        var arar = Database.loadStrings(0);
        for(var pair : arar){
            var ar = pair.getKey();
            Subject subject = new Subject(ar.get(1), ar.get(0));
            subject.setRowNum(pair.getValue());
            subjects.add(subject);
        }
    }

    // Get Subjects
    public static Subject getSubject(String code) {
        Subject subject = subjects.stream()
                .filter(s -> s.getCode().equals(code))
                .findFirst()
                .orElse(null);
        if (subject == null) {
            throw new IllegalArgumentException("Invalid code");
        }
        return subject;
    }

    // Get Subject by Name
    public static Subject getSubjectByName(String name) {
        Subject subject = subjects.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
        if (subject == null) {
            throw new IllegalArgumentException("Invalid name");
        }
        return subject;
    }

    public static ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public static ArrayList<Subject> searchByName(String name) {
        return subjects.stream()
                .filter(s -> s.getName().contains(name))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<Subject> searchByCode(String code) {
        return subjects.stream()
                .filter(s -> s.getCode().contains(code))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // Modify Subject
    // Add Subject
    // TODO: make sure Code is unique
    public static void addSubject(String code, String name) {
        // Check for duplicate code
        if (subjects.stream().anyMatch(s -> s.getCode().equals(code))) {
            throw new IllegalArgumentException("Subject code already exists");
        }
        // Check for duplicate name
        if (subjects.stream().anyMatch(s -> s.getName().equalsIgnoreCase(name))) {
            throw new IllegalArgumentException("Subject name already exists");
        }
        subjects.add(new Subject(code, name));
    }

    // Remove Subject
    public static void removeSubject(String code) {
        subjects.remove(getSubject(code));
    }

    public static void removeSubject(Subject subject) {
        subjects.remove(subject);
    }

    // Edit Subject
    public static void updateSheet(Subject subject){
        ArrayList<String> t = new ArrayList<String>();
        t.add(subject.getCode());
        t.add(subject.getName());
        Database.editRow(0, subject.getRowNum(), t);
    }
    public static void editSubjectCode(Subject subject, String code) {
        // Check for duplicate code
        if (subjects.stream().anyMatch(s -> s.getCode().equals(code) && !s.equals(subject))) {
            throw new IllegalArgumentException("Subject code already exists");
        }
        subject.setCode(code);
        updateSheet(subject);
    }

    public static void editSubjectName(Subject subject, String name) {
        // Check for duplicate name
        if (subjects.stream().anyMatch(s -> s.getName().equalsIgnoreCase(name) && !s.equals(subject))) {
            throw new IllegalArgumentException("Subject name already exists");
        }
        subject.setName(name);
        updateSheet(subject);
    }
}