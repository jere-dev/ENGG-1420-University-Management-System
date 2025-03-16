package ca.uoguelph.backend;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseManager {
    private static HashMap<String, Course> courses = new HashMap<String, Course>();

    public static void loadCourses() {
        // TODO: load courses
        ArrayList<ArrayList<String>> lists = Database.loadStrings(1);
//        for (ArrayList<String> course : lists) {
//            System.out.println(course.get(0) + "\n+" + course.get(1) + "\n+" + course.get(2) + "\n" + course.get(3) + "\n+" + course.get(4) + "\n+" + course.get(5) + "\n+" + course.get(6) + "\n+" + course.get(7));
//
//            int isThisTheFuckingError = (int)Double.parseDouble(course.get(4));
//            Course c = new Course(course.get(0), course.get(1), course.get(3), isThisTheFuckingError, course.get(5), course.get(6), course.get(7));
//            c.subject = SubjectManager.getSubject(course.get(2));
//            c.subject.courses.add(c);
//            courses.put(course.get(1), c);
//        }
    }

    public static void removeCourse(Course _course) {
        courses.remove(_course.name);
        _course.removeSelf();

        // TODO: remove from excel
    }

    public static void addCourse(String _name, String _code, int _capacity, String _section, String _lecTime,
            String _examDate, String _location, Subject _subject) {
        // courses.put(_name, new Course(_name, _code, _capacity, _section, _lecTime, _examDate, _location, _subject));
        //TODO: add to excel
    }

    public static Course getCourse(String _name){
        return courses.get(_name);
    }

    //TODO: add search function

}
