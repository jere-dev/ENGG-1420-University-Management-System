package ca.uoguelph.backend;

import java.util.ArrayList;

public class Subject {
    // TODO: change modifiers to fit scenario
    public String name;
    public String code;

    // TODO: make better name
    ArrayList<Course> courses;

    public Subject(String _name, String _code) {
        this.name = _name;
        this.code = _code;
        this.courses = new ArrayList<Course>();
    }

    public void edit(String _name, String _code) {
        this.name = _name;
        this.code = _code;
    }

    public void addCourse(Course _course) {
        this.courses.add(_course);
    }

    public void removeCourse(Course _course){
        this.courses.remove(_course);
        _course.subject = null;
        //FIXME: does course simply not exist? then _course.removeself();
        SubjectManager.removeCourse(this, _course);
    }

    //TODO: make removeSelf function
    //do courses under function delete? 
}
