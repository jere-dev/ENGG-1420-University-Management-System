package ca.uoguelph.backend;

import java.util.ArrayList;
import java.util.stream.Collectors;

//modify excel Sheet
public class StudentManager {
    private static ArrayList<Student> students = new ArrayList<Student>();

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
    public static void addStudent(String Id, String password, String email, String name, String profilePhoto, String address, String telephone, String currentSemester, String academicLevel, String thesisTitle, float progres){
        //TODO: make sure ID is unique
        students.add(new Student(Id, password, email, name, profilePhoto, address, telephone, currentSemester, academicLevel, thesisTitle, progres));
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
