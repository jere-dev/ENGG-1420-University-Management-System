package ca.uoguelph.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

//TODO: modify excel sheet
public class EventManager {
    private static ArrayList<Event> events = new ArrayList<Event>();
//load course
    public static void loadCourses(){
        var arar = Database.loadStrings(4);
        for(var ar : arar){
            ArrayList<String> faculty = new ArrayList<String>();//TODO: add faculty to excel to load
            ArrayList<String> students = new ArrayList<String>(Arrays.asList(ar.get(8).split("\\s*,\\s*")));
            events.add(new Event(ar.get(0), ar.get(1), ar.get(2), ar.get(3), ar.get(4), Integer.parseInt(ar.get(5)), Float.parseFloat(ar.get(6)), ar.get(7), students, faculty));
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
    public void editEventCoast(Event event, float cost){event.setCost(cost);}
    public void editEventRegistered(Event event, int registered){event.setRegistered(registered);}
    public void editEventMaxCapacity(Event event, int maxCapacity){event.setMaxCapacity(maxCapacity);}
    public void editEventDateAndTime(Event event, String dateAndTime){event.setDateAndTime(dateAndTime);}
    public void editEventLocation(Event event, String location){event.setLocation(location);}
    public void editEventHeaderImage(Event event, String headerImage){event.setHeaderImage(headerImage);}
    public void editEventDescription(Event event, String description){event.setDescription(description);}
    public void editEventCode(Event event, String code){event.setCode(code);}
    public void editEventName(Event event, String name){event.setName(name);}
    //remove event
    public static void removeEvent(Event event){events.remove(event);}
    public static void removeEvent(String code){events.remove(getEvent(code));}
}
