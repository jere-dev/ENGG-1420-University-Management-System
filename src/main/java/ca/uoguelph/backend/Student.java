package ca.uoguelph.backend;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.util.Pair;

public class Student extends User {
    private String address;
    private String telephone;
    // TODO: figure out how to repersent tuition
    private ArrayList<Pair<String, String>> courses; // TODO: store courses and sections together
    // TODO: figure out how to store grades
    private ArrayList<GradeRecord> gradeRecordList;
    private String currentSemester;
    private String academicLevel;
    private String thesisTitle;
    private float progress;
    private HashMap <String, String> Tuition;
    private String tuition;
    private String semester;

    public ArrayList<GradeRecord> getGradeRecordList() {
        return gradeRecordList;
    }

    public Student(String Id, String name, String address, String telephone, String email, String academicLevel, String currentSemester, String profilePhoto,
                    ArrayList<Pair<String, String>> courses, String thesisTitle, float progress, String password) {
        super(Id, password, email, name, profilePhoto);
        this.address = address;
        this.telephone = telephone;
        this.currentSemester = currentSemester;
        this.academicLevel = academicLevel;
        this.thesisTitle = thesisTitle;
        this.progress = (float)((int)(progress*100));
        this.courses =  courses;
    }
    //i made this so admin can whatever
    public void updateContactDetails(String address, String telephone) {
        this.address = address;
        this.telephone = telephone;
    }
    public String getAddress() {
        return address;
    }

    protected void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    protected void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public ArrayList<Pair<String, String>> getCourses() {
        return courses;
    }

    public void addCourse(Pair<String, String> course){
        this.courses.add(course);
    }

    public String getCurrentSemester() {
        return currentSemester;
    }

    protected void setCurrentSemester(String currentSemester) {
        this.currentSemester = currentSemester;
    }

    public String getAcademicLevel() {
        return academicLevel;
    }

    protected void setAcademicLevel(String academicLevel) {
        this.academicLevel = academicLevel;
    }

    public String getThesisTitle() {
        return thesisTitle;
    }

    protected void setThesisTitle(String thesisTitle) {
        this.thesisTitle = thesisTitle;
    }

    public float getProgress() {
        return progress;
    }

    protected void setProgress(float progress) {
        this.progress = progress;
    }

}
