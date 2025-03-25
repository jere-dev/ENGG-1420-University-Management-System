package ca.uoguelph.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import javafx.util.Pair;

//TODO: modify excel Sheet
public class StudentManager {
    private static ArrayList<Student> students = new ArrayList<Student>();
//load students
    public static void loadStudents(){
        var arar = Database.loadStrings(2);
        for(var ar : arar){
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
            students.add(new Student(ar.get(0), ar.get(1), ar.get(2), ar.get(3), ar.get(4), ar.get(5), ar.get(6), ar.get(7), courses, ar.get(9), Float.parseFloat(ar.get(10)), ar.get(11)));
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
    public static void editStudentPassword(Student student, String password){student.setPassword(password);}
    public static void editStudentEmail(Student student, String email){student.setEmail(email);}
    public static void editStudentName(Student student, String name){student.setName(name);}
    public static void editStudentProfilePhoto(Student student, String profilePhoto){student.setProfilePhoto(profilePhoto);}
    public static void editStudentAddress(Student student, String telephone){student.setTelephone(telephone);}
    public static void editStudentCurrentSemester(Student student, String CurrentSemester){student.setCurrentSemester(CurrentSemester);}
    public static void editStudentAcademicLevel(Student student, String  academicLevel){student.setAcademicLevel(academicLevel);}
    public static void editStudentThesisTitle(Student student, String ThesisTitle){student.setThesisTitle(ThesisTitle);}
    public static void editStudentProgress(Student student, float progress){student.setProgress(progress);}
    //remove student
    public static void removeStudent(Student student){students.remove(student);}
    public static void removeStudent(String ID){students.remove(getStudent(ID));}
}
