package ca.uoguelph.backend;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(
    getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
    setterVisibility = JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC
)
public class Meeting {
    private String days;
    private String time;
    private String dates;
    private String location;
    private String type;

    protected void setDays(String days){this.days=days;}
    public String getDays(){return this.days;}
    protected void setTime(String time){this.time=time;}
    public String getTime(){return this.time;}
    protected void setDates(String dates){this.dates=dates;}
    public String getDates(){return this.dates;}
    protected void setLocation(String location){this.location=location;}
    public String getLocation(){return this.location;}
    protected void setType(String type){this.type=type;}
    public String getType(){return this.type;}
}
