package ca.uoguelph.frontend.objects;

import ca.uoguelph.backend.Section;
import javafx.beans.property.SimpleStringProperty;

public class SectionEntry {
    private final SimpleStringProperty code = new SimpleStringProperty(),
            term = new SimpleStringProperty(),
            instructors = new SimpleStringProperty(),
            seats = new SimpleStringProperty();

    public SectionEntry(String code, String term, String instructors, String seats) {
        codeProperty().setValue(code);
        termProperty().setValue(term);
        instructorsProperty().setValue(instructors);
        seatsProperty().setValue(seats);
    }

    public SectionEntry(Section sec) {
        this(sec.getCode(), sec.getTerm(),
                String.join(", ", sec.getInstructors()),
                sec.getSeats());
    }

    public String getCode() {
        return code.get();
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public String getTerm() {
        return term.get();
    }

    public SimpleStringProperty termProperty() {
        return term;
    }

    public String getInstructors() {
        return instructors.get();
    }

    public SimpleStringProperty instructorsProperty() {
        return instructors;
    }

    public String getSeats() {
        return seats.get();
    }

    public SimpleStringProperty seatsProperty() {
        return seats;
    }
}
