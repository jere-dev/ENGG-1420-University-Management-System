package ca.uoguelph.backend;

import java.util.ArrayList;

public class Course {
    String name;
    String code;
    int capacity;
    String section;

    // TODO: use object to store dates/time
    String lecTime;
    String examDate;
    String location;

    Subject subject;
    Prof prof;
    ArrayList<Student> students;

    Course(String _name, String _code, int _capacity, String _section, String _lecTime, String _examDate,
            String _location, Subject _subject) {
        this.name = _name;
        this.code = _code;
        this.capacity = _capacity;
        this.section = _section;

        this.lecTime = _lecTime;
        this.examDate = _examDate;
        this.location = _location;

        this.subject = _subject;
        this.subject.addCourse(this);
        
        this.students = new ArrayList<Student>();
    }

    public void removeStudent(Student _student) {
        _student.subjects.remove(this.subject);
        _student.courses.remove(this);
        students.remove(_student);
        //TODO: Student manager remove course
    }

    public void removeSelf(){
        //students also have a subject list they remove based on this class
        //order matters
        for(Student student : students){
            student.removeCourse(this);
        }
        this.prof.removeCourse(this);
        this.subject.removeCourse(this);
    }

}
