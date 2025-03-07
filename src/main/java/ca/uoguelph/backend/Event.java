package ca.uoguelph.backend;

import java.util.ArrayList;

public class Event {
    public String name;
    public String code;
    public String description;
    public String image; // file path
    public String location;

    // TODO: use object to store dates/time
    public String dateAndTime;
    public int capacity;

    public String cost;
    public ArrayList<Student> students;
    public ArrayList<Prof> profs;

    public Event(String _code, String _name, String _description, String _location, String _dateAndTime,
            int _capacity, String _cost, String _image) {
        this.name = _name;
        this.code = _code;
        this.description = _description;
        this.image = _image; // file path
        this.location = _location;
        this.dateAndTime = _dateAndTime;
        this.capacity = _capacity;
        this.cost = _cost;
        this.students = new ArrayList<Student>();
        this.profs = new ArrayList<Prof>();
    }

    //this should only be called by event manager
    public void removeSelf(){
        for(Student student : students){
            student.removeEvent(this);
        }

        for(Prof prof : profs){
            prof.removeEvent(null);
        }
    }

    public void removeStudent(Student _student){
        _student.events.remove(this);
        this.students.remove(_student);
        //TODO: Event manager remove student event from excel
    }

    public void addStudent(Student _student){
        this.students.add(_student);
        _student.events.add(this);
        //TODO: event manager add to excel
    }

    //should be called only by prof
    public void removeProf(Prof _prof){
        _prof.events.remove(this);
        this.profs.remove(_prof);
    }

    //should be called only by prof
    public void addProf(Prof _prof){
        this.profs.add(_prof);
        _prof.events.add(this);
    }
}
