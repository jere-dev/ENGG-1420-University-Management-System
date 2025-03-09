package ca.uoguelph.backend;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfManager {
    private static HashMap<String, Prof> profs = new HashMap<String, Prof>();

    public static void LoadProfs() {
        ArrayList<ArrayList<String>> list = Database.loadStrings(3);
        for (ArrayList<String> prof : list) {
            Prof p = new Prof(prof.get(0),prof.get(1),prof.get(2),prof.get(3),prof.get(4),prof.get(5), prof.get(7));
            Course c = CourseManager.getCourse(prof.get(6));
            c.prof = p;
            p.courses.add(c); //TODO: change after reformating xlsx to use corse code instead of name to allow more course
            profs.put(prof.get(1), p);
        }
    }

    public static Prof getProf(String _name){
        return profs.get(_name);
    }

    public static void addCourse(Prof _prof, Course _course){
        //TODO: add to excel
    }

    public static void addEvent(Prof _prof, Event _event){
        //TODO: add to excel
        //how tf are events stored should this be a job of event manager???
    }

    public static void removeProf(Prof _prof) {
        _prof.removeSelf();
        profs.remove(_prof.name);

        // TODO: remove from excel
    }

    public static void removeEvent(Prof _prof, Event _event) {
        // TODO: remove event from prof in excel
        // how tf are events stored
    }

    public static void removeCourse(Prof _prof, Course _course) {
        // TODO: remove Course from prof in excel
    }
}
