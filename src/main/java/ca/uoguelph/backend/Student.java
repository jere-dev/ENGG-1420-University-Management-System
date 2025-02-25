package ca.uoguelph.backend;

import java.util.ArrayList;

public class Student {
    String name;
    String ID;

    // represent this as a path
    String profilePhoto;
    String address;
    String telephone; // should this be an int?

    // TODO: Tuition fee/state
    ArrayList<Course> courses;
    String emailAddress;

    // TODO: GRADES
    String currentSemester;
    ArrayList<Subject> subjects;
    String academicLevel;
    String thesisTitle; // for phd students

    float progress; // is a percentage repersented as a decimal ie 50% is .5

    //TODO: Prof list
    ArrayList<Prof> profs;
    ArrayList<Event> events;

    public Student(String _name, String _ID, String _profilePhoto, String _address, String _telephone, String _emailAddress,
            String _currentSemester, String _academicLevel, String _thesisTitle, float _progress) {
        this.name = _name;
        this.ID = _ID;
        this.profilePhoto = _profilePhoto;
        this.address = _address;
        this.telephone = _telephone;
        this.emailAddress = _emailAddress;
        this.currentSemester = _currentSemester;
        this.academicLevel = _academicLevel;
        this.thesisTitle = _thesisTitle;
        this.progress = _progress;
        this.profs = new ArrayList<Prof>();
        this.events = new ArrayList<Event>();
    }

    public void addCourse(Course _course) {
        _course.students.add(this);
        this.courses.add(_course);
        this.subjects.add(_course.subject);
        this.profs.add(_course.prof);
    }

    public void removeCourse(Course _course) {
        _course.removeStudent(this);
    }

    public void addEvent(Event _event){
        _event.addStudent(this);
    }

    public void removeEvent(Event _event){
        _event.removeStudent(this);
    }

    //should be call by student manager only
    public void removeSelf(){
        for(Event _event: events){
            _event.removeStudent(this);
        }
        for(Course _course : courses){
            _course.removeStudent(this);
        }
    }

}
