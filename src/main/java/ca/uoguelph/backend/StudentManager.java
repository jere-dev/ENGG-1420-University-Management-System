package ca.uoguelph.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import javafx.util.Pair;

public class StudentManager {
    private static ArrayList<Student> students = new ArrayList<Student>();
//load students
    public static void loadStudents(){
        var arar = Database.loadStrings(2);
        for(var pair : arar){
            var ar = pair.getKey();
            ArrayList<Pair<String, String>> courses = Arrays.stream(ar.get(8).split(","))
                .map(sc -> {
                    String[] parts = sc.split("\\*", 2); // Split at first asterisk only
                    if (parts.length < 2) {
                        return new Pair<>("invalid-key", "invalid-value");//TODO: throw error instead
                    }
                    // Get the full course code (e.g., "ENGL") and number (e.g., "2740")
                    String courseCode = (parts[0] + "*" + parts[1].substring(0, 4)).trim();
                    String courseName = parts[1].substring(4).trim();
                    return new Pair<>(courseCode, courseName);
                })
                .collect(Collectors.toCollection(() -> new ArrayList<>()));
                var changelater = new Student(ar.get(0), ar.get(1), ar.get(2), ar.get(3), ar.get(4), ar.get(5), ar.get(6), ar.get(7), courses, ar.get(9), Float.parseFloat(ar.get(10)), ar.get(11));
                changelater.setRowNum(pair.getValue());
            students.add(changelater);
        }
    }

//get students
    public static Student getStudent(String ID){ //only search by Id as people can have the same name
        Student student = students.stream().filter(s -> s.getID().equals(ID)).findFirst().orElse(null);
        if(student == null){throw new IllegalArgumentException("invalid ID");}
        return student;
    }
    public static ArrayList<Student> getStudents(){return students;}
    public static ArrayList<Student> searchByID(String ID){return students.stream().filter(s -> s.getID().contains(ID)).collect(Collectors.toCollection(ArrayList::new));}
    public static ArrayList<Student> searchByCode(String name){return students.stream().filter(s -> s.getID().contains(name)).collect(Collectors.toCollection(ArrayList::new));}

//modify student
    //add student
    public static void addStudent(String Id, String name, String address, String telephone, String email, String academicLevel, String currentSemester, String profilePhoto,
                    ArrayList<Pair<String, String>> courses, String thesisTitle, float progress, String password){
        //TODO: make sure ID is unique
        students.add(new Student(Id, name, address, telephone, email, academicLevel, currentSemester, profilePhoto, courses, thesisTitle, progress, password));
    }
    //edit student
    private static void updateSheet(Student student){
        var t = new ArrayList<String>();
        t.add(student.getID());
        t.add(student.getName());
        t.add(student.getAddress());
        t.add(student.getTelephone());
        t.add(student.getEmail());
        t.add(student.getAcademicLevel());
        t.add(student.getCurrentSemester());
        t.add(student.getProfilePhoto());
        t.add(student.getCourses().stream().map(c -> c.getKey()+"*"+c.getValue()).collect(Collectors.joining(",")));
        t.add(student.getThesisTitle());
        t.add(String.valueOf(student.getProgress()));
        t.add(student.getPassword());
        Database.editRow(2, student.getRowNum(), t);
    }
    public static void editStudentPassword(Student student, String password){student.setPassword(password);updateSheet(student);}
    public static void editStudentEmail(Student student, String email){student.setEmail(email);updateSheet(student);}
    public static void editStudentName(Student student, String name){student.setName(name);updateSheet(student);}
    public static void editStudentProfilePhoto(Student student, String profilePhoto){student.setProfilePhoto(profilePhoto);updateSheet(student);}
    public static void editStudentAddress(Student student, String telephone){student.setTelephone(telephone);updateSheet(student);}
    public static void editStudentCurrentSemester(Student student, String CurrentSemester){student.setCurrentSemester(CurrentSemester);updateSheet(student);}
    public static void editStudentAcademicLevel(Student student, String  academicLevel){student.setAcademicLevel(academicLevel);updateSheet(student);}
    public static void editStudentThesisTitle(Student student, String ThesisTitle){student.setThesisTitle(ThesisTitle);updateSheet(student);}
    public static void editStudentProgress(Student student, float progress){student.setProgress(progress);updateSheet(student);}
    //remove student
    //TODO: remove from excel
    public static void removeStudent(Student student){students.remove(student);}
    public static void removeStudent(String ID){students.remove(getStudent(ID));}
}
