package ca.uoguelph.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import javafx.util.Pair;

//TODO: modify excel sheet
public class FacultyManager {
    private static ArrayList<Faculty> faculties = new ArrayList<Faculty>();

//load faculty
    public static void loadFaculty()
    {
        var arar = Database.loadStrings(3);
        for(var ar : arar){
            ArrayList<Pair<String, String>> courses = Arrays.stream(ar.get(6).replace(" ", "").split(","))
                .map(sc -> {
                    String[] parts = sc.split("\\*");
                    if (parts.length < 2) {
                        return new Pair<>("invalid-key", "invalid-value");//TODO: throw error instead
                    }
                    return new Pair<>(parts[0], parts[1]);
                })
                .collect(Collectors.toCollection(() -> new ArrayList<>()));
            faculties.add(new Faculty(ar.get(0), ar.get(1), ar.get(2), ar.get(3), ar.get(4), ar.get(5), courses, ar.get(7), ar.get(8)));
        }
    }

//getFaculty
    public static Faculty getFaculty(String ID){
        Faculty faculty = faculties.stream().filter(f -> f.getID().equals(ID)).findFirst().orElse(null);
        if(faculty == null){throw new IllegalArgumentException("invalid ID");}
        return faculty;
    }
    public static ArrayList<Faculty> getFaculties(){ return faculties;}
    public static ArrayList<Faculty> getFacultiesByID(String ID) {return faculties.stream().filter(f -> f.getID().contains(ID)).collect(Collectors.toCollection(ArrayList::new));}
    public static ArrayList<Faculty> getFacultiesByName(String name) {return faculties.stream().filter(f -> f.getName().contains(name)).collect(Collectors.toCollection(ArrayList::new));}

//modify Faculty
    //add faculty
    public static void addFaculty(String ID, String name, String degree, String researchInterest, String email, String officeLocation, ArrayList<Pair<String, String>> courses, String password, String profilePhoto){
        //TODO: make sure ID is unique
        faculties.add(new Faculty(ID, name, degree, researchInterest, email, officeLocation, courses, password, profilePhoto));
    }
    //edit faculty
    public static void editFacultyOfficeLocation(Faculty faculty, String  OfficeLocation){faculty.setOfficeLocation(OfficeLocation);}
    public static void editFacultyResearchInterest(Faculty faculty, String  ResearchInterest){faculty.setResearchInterest(ResearchInterest);}
    public static void editFacultyDegree(Faculty faculty, String  Degree){faculty.setDegree(Degree);}
    public static void editFacultyProfilePhoto(Faculty faculty, String  ProfilePhoto){faculty.setProfilePhoto(ProfilePhoto);}
    public static void editFacultyName(Faculty faculty, String  Name){faculty.setName(Name);}
    public static void editFacultyEmail(Faculty faculty, String  Email){faculty.setEmail(Email);}
    public static void editFacultyPassword(Faculty faculty, String  Password){faculty.setPassword(Password);}
    public static void editFacultyID(Faculty faculty, String  ID){faculty.setID(ID);}
    //remove faculty
    public static void removeFaculty(Faculty faculty){faculties.remove(faculty);}
    public static void removeFaculty(String ID){faculties.remove(getFaculty(ID));}
}
