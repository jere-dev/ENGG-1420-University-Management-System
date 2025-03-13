package ca.uoguelph.backend;

import java.util.ArrayList;

import javafx.util.Pair;

public class Faculty extends User {
    private String degree;
    private String researchInterest;
    private ArrayList<Pair<String, String>> courses;
    private String officeLocation;

    Faculty(String id, String password, String email, String name, String profilePhoto, String degree, String researchInterest, String officeLocation){
        super(id, password, email, name, profilePhoto);
        this.degree = degree;
        this.researchInterest = researchInterest;
        this.officeLocation = officeLocation;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getResearchInterest() {
        return researchInterest;
    }

    public void setResearchInterest(String researchInterest) {
        this.researchInterest = researchInterest;
    }

    public ArrayList<Pair<String, String>> getCourses() {
        return courses;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

}
