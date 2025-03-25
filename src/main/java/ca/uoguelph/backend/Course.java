package ca.uoguelph.backend;

import java.util.ArrayList;

public class Course {
    private String subjectCode;
    private String CourseCode;
    private String title;
    private float credits;
    private String description;
    private String requisites;
    private String locations;
    private String Offered;         //the semester the course is offered
    private String department;
    private ArrayList<Section> sections;

    protected Course(String subjectCode, String CourseCode, String title, float credits, String description, String requisites, String locations, String Offered, String department, ArrayList<Section> sections){
        this.sections = sections;
        this.department = department;
        this.Offered = Offered;
        this.locations = locations;
        this.requisites = requisites;
        this.description = description;
        this.credits = credits;
        this.title = title;
        this.CourseCode = CourseCode;
        this.subjectCode = subjectCode;
    }

    protected void addSections(Section section){this.sections.add(section);}
    protected void removeSection(Section section){this.sections.remove(section);}
    public String getSubjectCode() { return this.subjectCode; }
    protected void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    public String getCourseCode() { return this.CourseCode; }
    protected void setCourseCode(String courseCode) { this.CourseCode = courseCode; }
    public String getTitle() { return this.title; }
    protected void setTitle(String title) { this.title = title; }
    public float getCredits() { return this.credits; }
    protected void setCredits(float credits) { this.credits = credits; }
    public String getDescription() { return this.description; }
    protected void setDescription(String description) { this.description = description; }
    public String getRequisites() { return this.requisites; }
    protected void setRequisites(String requisites) { this.requisites = requisites; }
    public String getLocations() { return this.locations; }
    protected void setLocations(String locations) { this.locations = locations; }
    public String getOffered() { return this.Offered; }
    protected void setOffered(String offered) { this.Offered = offered; }
    public String getDepartment() { return this.department; }
    protected void setDepartment(String department) { this.department = department; }
    public ArrayList<Section> getSections() { return this.sections; }
}
