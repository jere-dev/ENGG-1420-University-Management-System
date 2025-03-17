package ca.uoguelph.backend;

import java.util.ArrayList;

public class Event {
    private String name;
    private String code;
    private String description;
    private String headerImage;
    private String location;
    private String dateAndTime;//TODO: use date class for this
    private int maxCapacity;
    private int registered; // used to tell how many seats are left
    private float cost;

    private ArrayList<String> students;//stored as ids
    private ArrayList<String> faculty;//stored as id

    protected Event(String name, String code, String description, String headerImage, String location, String dateAndTime, int maxCapacity, int registered, float cost){
        this.cost = cost;
        this.registered = registered;
        this.maxCapacity = maxCapacity;
        this.dateAndTime = dateAndTime;
        this.location = location;
        this.headerImage = headerImage;
        this.description = description;
        this.code = code;
        this.name = name;
        this.students = new ArrayList<String>();
        this.faculty = new ArrayList<String>();
    }

    protected void removeFaculty(String ID){
        faculty.remove(ID);
    }

    protected void addFaculty(String ID) {
        faculty.add(ID);
    }

    protected void removeStudent(String Id){
        students.remove(Id);
    }

    protected void addStudent(String ID) {
        students.add(ID);
    }

    public ArrayList<String> getStudents() {
        return students;
    }

    public ArrayList<String> getFaculty() {
        return faculty;
    }
    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    protected void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    protected void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public String getLocation() {
        return location;
    }

    protected void setLocation(String location) {
        this.location = location;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    protected void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    protected void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getRegistered() {
        return registered;
    }

    protected void setRegistered(int registered) {
        this.registered = registered;
    }

    public float getCost() {
        return cost;
    }

    protected void setCost(float cost) {
        this.cost = cost;
    }
}
