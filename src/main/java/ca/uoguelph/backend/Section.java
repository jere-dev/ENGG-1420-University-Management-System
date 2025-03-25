package ca.uoguelph.backend;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(
    getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
    setterVisibility = JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC
)
public class Section {
    private String term;
    private ArrayList<Meeting> meetings;
    private ArrayList<String> instructors;
    private String code;
    private String title;
    private String seats; // TODO: change "seats" to capacity

    public String getTerm() { return term; }
    protected void setTerm(String term) { this.term = term; }
    public ArrayList<Meeting> getMeetings() { return meetings; }
    protected void setMeetings(ArrayList<Meeting> meetings) { this.meetings = meetings; }
    public ArrayList<String> getInstructors() { return instructors; }
    protected void setInstructors(ArrayList<String> instructors) { this.instructors = instructors; }
    public String getCode() { return code; }
    protected void setCode(String code) { this.code = code; }
    public String getTitle() { return title; }
    protected void setTitle(String title) { this.title = title; }
    public String getSeats() { return seats; }
    protected void setSeats(String seats) { this.seats = seats; }
}