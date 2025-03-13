package ca.uoguelph.backend;

import java.util.ArrayList;

import javafx.util.Pair;

public class Student extends User {
    private String address;
    private String telephone;
    // TODO: figure out how to repersent tutition/payment status
    private ArrayList<Pair<String, String>> courses; // subjects and courses in stored in same list
    // TODO: figure out how to store grades
    private String currentSemester;
    private String academicLevel;
    private String thesisTitle;
    private float progress;

    Student(String Id, String password, String email, String name, String profilePhoto, String address,
            String telephone, String currentSemester, String academicLevel,
            String thesisTitle, float progress) {
        super(Id, password, email, name, profilePhoto);
        this.address = address;
        this.telephone = telephone;
        this.currentSemester = currentSemester;
        this.academicLevel = academicLevel;
        this.thesisTitle = thesisTitle;
        this.progress = progress;
        this.courses =  new ArrayList<Pair<String, String>>();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
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

    public void setCurrentSemester(String currentSemester) {
        this.currentSemester = currentSemester;
    }

    public String getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(String academicLevel) {
        this.academicLevel = academicLevel;
    }

    public String getThesisTitle() {
        return thesisTitle;
    }

    public void setThesisTitle(String thesisTitle) {
        this.thesisTitle = thesisTitle;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

}
