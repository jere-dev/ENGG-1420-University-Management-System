package ca.uoguelph.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class EventManager {
    private static ArrayList<Event> events = new ArrayList<Event>();
//load course
    public static void loadCourses(){
        var arar = Database.loadStrings(4);
        for(var pair : arar){
            var ar = pair.getKey();
            ArrayList<String> faculty = new ArrayList<String>();//TODO: add faculty to excel to load
            ArrayList<String> students = new ArrayList<String>(Arrays.asList(ar.get(8).split("\\s*,\\s*")));
            var changelater = new Event(ar.get(0), ar.get(1), ar.get(2), ar.get(3), ar.get(4), Integer.parseInt(ar.get(5)), Float.parseFloat(ar.get(6)), ar.get(7), students, faculty);
                changelater.setRowNum(pair.getValue());
            events.add(changelater);
        }
    }

//get Events
    public static Event getEvent(String code){
        Event event = events.stream().filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
        if(event == null){throw new IllegalArgumentException("Invalid event code");}
        return event;
    }
    public static ArrayList<Event> getEvents(){return events;}
    public static ArrayList<Event> searchEventsByCode(String code){return events.stream().filter(e -> e.getCode().contains(code)).collect(Collectors.toCollection(ArrayList::new));}
    public static ArrayList<Event> searchEventsByName(String name){return events.stream().filter(e -> e.getCode().contains(name)).collect(Collectors.toCollection(ArrayList::new));}

//modify Event
    //addEvent
    public static void addEvent(String name, String code, String description, String headerImage, String location, String dateAndTime, int maxCapacity, int registered, float cost, ArrayList<String> students, ArrayList<String> faculty){
        //TODO: make sure code is unique
        events.add(new Event(code, name, description, location, dateAndTime, maxCapacity, cost, headerImage, students, faculty));
    }
    //edit event
    //TODO: fix minor conflict with registered
    public static void updateSheet(Event event){
        ArrayList<String> t = new ArrayList<>();
        t.add(event.getCode());
        t.add(event.getName());
        t.add(event.getDescription());
        t.add(event.getLocation());
        t.add(event.getDateAndTime());
        t.add(String.valueOf(event.getMaxCapacity()));
        t.add(String.valueOf(event.getCost()));
        t.add(event.getHeaderImage());
        t.add(event.getStudents().stream().collect(Collectors.joining(",")));
        Database.editRow(4, event.getRowNum(), t);
    }
    public static void editEventCost(Event event, float cost){event.setCost(cost);updateSheet(event);}
    public static void editEventRegistered(Event event, int registered){event.setRegistered(registered);updateSheet(event);}
    public static void editEventMaxCapacity(Event event, int maxCapacity){event.setMaxCapacity(maxCapacity);updateSheet(event);}
    public static void editEventDateAndTime(Event event, String dateAndTime){event.setDateAndTime(dateAndTime);updateSheet(event);}
    public static void editEventLocation(Event event, String location){event.setLocation(location);updateSheet(event);}
    public static void editEventHeaderImage(Event event, String headerImage){event.setHeaderImage(headerImage);updateSheet(event);}
    public static void editEventDescription(Event event, String description){event.setDescription(description);updateSheet(event);}
    public static void editEventCode(Event event, String code){event.setCode(code);updateSheet(event);}
    public static void editEventName(Event event, String name){event.setName(name);updateSheet(event);}
    //remove event
    //TODO: remove from excel
    public static void removeEvent(Event event){events.remove(event);}
    public static void removeEvent(String code){events.remove(getEvent(code));}
}
