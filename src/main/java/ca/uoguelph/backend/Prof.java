package ca.uoguelph.backend;

import java.util.ArrayList;

public class Prof {
    public String id;
    public String password;
    public String name;
    public String profilePhoto; // file path to photo
    public String degree;
    public String researchInterest;

    public ArrayList<Course> courses;
    public String email;
    public String officeLocation;

    public ArrayList<Event> events;

    public Prof(String _id, String _name, String _degree, String _researchInterest, String _email,
            String _officeLocation, String _password) {
        this.id = _id;
        this.name = _name;
        this.password = _password;
        // this.profilePhoto = _profilePhoto; //TODO: add back after modifying excel sheet
        
        this.degree = _degree;
        this.researchInterest = _researchInterest;
        this.email = _email;
        this.officeLocation = _officeLocation;
        this.events = new ArrayList<Event>();

        this.courses = new ArrayList<Course>();
    }

    public void removeSelf() {
        for (Event event : events) {
            event.removeProf(this);
        }
        for (Course course : courses) {
            this.removeCourse(course);
        }
    }

    public void removeCourse(Course _course) {
        _course.prof = null;
        courses.remove(_course);
        ProfManager.removeCourse(this, _course);
    }

    public void addCourse(Course _course) {
        _course.prof = this;
        courses.add(_course);
        ProfManager.addCourse(this, _course);
    }

    public void addEvent(Event _event) {
        events.add(_event);
        ProfManager.addEvent(this, _event);
    }

    public void removeEvent(Event _event) {
        _event.removeProf(this);
        ProfManager.removeEvent(this, _event);
    }
}
