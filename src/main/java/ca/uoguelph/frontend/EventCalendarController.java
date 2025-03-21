package ca.uoguelph.frontend;

import ca.uoguelph.backend.Event;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;
import javafx.fxml.FXML;
import javafx.scene.layout.*;

import java.time.*;

public class EventCalendarController {
    @FXML private AnchorPane rootPane;

    public void initialize() {
        CalendarView calendarView = new CalendarView(); // (1)

        Calendar birthdays = new Calendar("Birthdays"); // (2)
        Calendar holidays = new Calendar("Holidays");

        CalendarSource myCalendarSource = new CalendarSource("My Calendars"); // (4)
        myCalendarSource.getCalendars().addAll(birthdays, holidays);

        calendarView.getCalendarSources().addAll(myCalendarSource); // (5)

        calendarView.setRequestedTime(LocalTime.now());

        calendarView.setToday(LocalDate.now());
        calendarView.setTime(LocalTime.now());

        rootPane.getChildren().add(calendarView);
        AnchorPane.setBottomAnchor(calendarView, 0.0);
        AnchorPane.setLeftAnchor(calendarView, 0.0);
        AnchorPane.setRightAnchor(calendarView, 0.0);
        AnchorPane.setTopAnchor(calendarView, 0.0);
    }
}