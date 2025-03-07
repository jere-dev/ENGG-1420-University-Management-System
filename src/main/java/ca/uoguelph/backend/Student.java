package ca.uoguelph.backend;

import java.util.ArrayList;

public class Student {
    public String name;
    public String ID;
    public String password;

    // represent this as a path
    public String profilePhoto;
    public String address;
    public String telephone; // should this be an int?

    // TODO: Tuition fee/state
    public ArrayList<Course> courses;
    public String emailAddress;

    // TODO: GRADES
    public String currentSemester;
    public ArrayList<Subject> subjects;
    public String academicLevel;
    public String thesisTitle; // for phd students

    public double progress; // is a percentage repersented as a decimal ie 50% is .5

    // TODO: Prof list
    public ArrayList<Prof> profs;
    public ArrayList<Event> events;

    public Student(String _ID, String _name, String _address, String _telephone, String _emailAddress,
            String _academicLevel, String _currentSemester, String _profilePhoto, String _thesisTitle, double _progress, String _password) {
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
        this.password = _password;
        this.profs = new ArrayList<Prof>();
        this.events = new ArrayList<Event>();
        this.subjects = new ArrayList<Subject>();
        this.courses = new ArrayList<Course>();
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

    public void addEvent(Event _event) {
        _event.addStudent(this);
    }

    public void removeEvent(Event _event) {
        _event.removeStudent(this);
    }

    // should be call by student manager only
    public void removeSelf() {
        for (Event _event : events) {
            _event.removeStudent(this);
        }
        for (Course _course : courses) {
            _course.removeStudent(this);
        }
    }

}
