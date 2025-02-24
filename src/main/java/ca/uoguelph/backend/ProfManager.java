package ca.uoguelph.backend;

import java.util.HashMap;

public class ProfManager {
    private static HashMap<String, Prof> profs = new HashMap<String, Prof>();

    ProfManager() {
        LoadProfs();
    }

    public static void LoadProfs() {
        // TODO: load from excel
        // then use prof.addCourse(CourseManager.get("string from excel"));
        // then use prof.addEvent(EventManager.get("string from excel"));
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
