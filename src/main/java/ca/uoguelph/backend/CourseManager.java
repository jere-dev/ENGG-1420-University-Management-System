package ca.uoguelph.backend;

import java.util.HashMap;

public class CourseManager {
    private static HashMap<String, Course> courses = new HashMap<String, Course>();

    public static void loadCourses() {
        // TODO: load courses
    }

    public static void removeCourse(Course _course) {
        courses.remove(_course.name);
        _course.removeSelf();

        // TODO: remove from excel
    }

    public static void addCourse(String _name, String _code, int _capacity, String _section, String _lecTime,
            String _examDate, String _location, Subject _subject) {
        courses.put(_name, new Course(_name, _code, _capacity, _section, _lecTime, _examDate, _location, _subject));
        //TODO: add to excel
    }

    public static Course getCourse(String _name){
        return courses.get(_name);
    }

    //TODO: add search function

}
