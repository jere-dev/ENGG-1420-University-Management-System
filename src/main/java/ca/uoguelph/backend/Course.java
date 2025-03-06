package ca.uoguelph.backend;

import java.util.ArrayList;

public class Course {
    public String name;
    public String code;
    public int capacity;
    public String section;

    // TODO: use object to store dates/time
    public String lecTime;
    public String examDate;
    public String location;

    public Subject subject;
    public Prof prof;
    public ArrayList<Student> students;

    public Course(String _code, String _name, String _section, int _capacity, String _lecTime, String _examDate,
            String _location) {
        this.name = _name;
        this.code = _code;
        this.capacity = _capacity;
        this.section = _section;

        this.lecTime = _lecTime;
        this.examDate = _examDate;
        this.location = _location;

        this.students = new ArrayList<Student>();
    }

    public void removeStudent(Student _student) {
        _student.subjects.remove(this.subject);
        _student.courses.remove(this);
        students.remove(_student);
        // TODO: Student manager remove course
    }

    public void removeSelf() {
        // students also have a subject list they remove based on this class
        // order matters
        for (Student student : students) {
            student.removeCourse(this);
        }
        this.prof.removeCourse(this);
        this.subject.removeCourse(this);
    }

}
