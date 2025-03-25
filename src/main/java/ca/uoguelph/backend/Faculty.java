package ca.uoguelph.backend;

import java.util.ArrayList;

import javafx.util.Pair;

public class Faculty extends User {
    private String degree;
    private String researchInterest;
    private ArrayList<Pair<String, String>> courses;
    private String officeLocation;

    public Faculty(String id, String name, String degree, String researchInterest, String email, String officeLocation, ArrayList<Pair<String, String>> courses, String password, String profilePhoto){
        super(id, password, email, name, profilePhoto);
        this.degree = degree;
        this.researchInterest = researchInterest;
        this.officeLocation = officeLocation;
        this.courses = courses;
    }

    public String getDegree() {
        return degree;
    }

    protected void setDegree(String degree) {
        this.degree = degree;
    }

    public String getResearchInterest() {
        return researchInterest;
    }

    protected void setResearchInterest(String researchInterest) {
        this.researchInterest = researchInterest;
    }

    public ArrayList<Pair<String, String>> getCourses() {
        return courses;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    protected void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

}
