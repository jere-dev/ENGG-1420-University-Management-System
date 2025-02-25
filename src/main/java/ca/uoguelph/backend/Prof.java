package ca.uoguelph.backend;

import java.util.ArrayList;

public class Prof {
    String name;
    String profilePhoto; // file path to photo
    String degree;
    String researchInterest;

    ArrayList<Course> courses;
    String email;
    String officeLocation;

    ArrayList<Event> events;

    public Prof(String _name, String _profilePhoto, String _degree, String _researchInterest, String _email,
            String _officeLocation) {
        this.name = _name;
        this.profilePhoto = _profilePhoto;
        this.degree = _degree;
        this.researchInterest = _researchInterest;
        this.email = _email;
        this.officeLocation = _officeLocation;
        this.events = new ArrayList<Event>();

    }

    public void removeSelf(){
        for(Event event: events){
            event.removeProf(this);
        }
        for(Course course : courses){
            this.removeCourse(course);
        }
    }

    public void removeCourse(Course _course){
        _course.prof = null;
        courses.remove(_course);
        ProfManager.removeCourse(this, _course);
    }

    public void addCourse(Course _course){
        _course.prof = this;
        courses.add(_course);
        ProfManager.addCourse(this, _course);
    }

    public void addEvent(Event _event){
        events.add(_event);
        ProfManager.addEvent(this, _event);
    }

    public void removeEvent(Event _event){
        _event.removeProf(this);
        ProfManager.removeEvent(this, _event);
    }
}
