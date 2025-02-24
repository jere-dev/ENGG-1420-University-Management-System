package ca.uoguelph.backend;

import java.util.HashMap;

public class EventManager {
    private static HashMap<String, Event> events = new HashMap<String, Event>();

    EventManager(){

    }

    public static void load(){
        //TODO: load from excel
        //dont try link to others, others will link to it
    }

    public static void removeEvent(Event _event){
        _event.removeSelf();
        events.remove(_event.name);

        //TODO: remove from excel
    }
}
