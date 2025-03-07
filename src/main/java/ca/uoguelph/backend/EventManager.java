package ca.uoguelph.backend;

import java.util.ArrayList;
import java.util.HashMap;

public class EventManager {
    private static HashMap<String, Event> events = new HashMap<String, Event>();

    public static void loadEvents(){
        ArrayList<ArrayList<String>> list = Database.loadStrings(4);
        for (ArrayList<String> event : list) {
            Event e = new Event(event.get(0), event.get(1), event.get(2), event.get(3), event.get(4), Integer.parseInt(event.get(5)), event.get(6), event.get(7));
            for (String student : event.get(8).split("\\s*,\\s*")) {
                Student s = StudentManager.getStudent(student);
                s.events.add(e);
                e.addStudent(s);
            }
        }
    }

    public static void removeEvent(Event _event){
        _event.removeSelf();
        events.remove(_event.name);

        //TODO: remove from excel
    }

    public static Event getEvent(String _name){
        return events.get(_name);
    }

    //TODO: search function
}
