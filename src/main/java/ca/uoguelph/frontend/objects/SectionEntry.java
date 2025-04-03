package ca.uoguelph.frontend.objects;

import ca.uoguelph.backend.Section;
import javafx.beans.property.SimpleStringProperty;

/**
 * A class to wrap the fields of a {@code Section} with {@code SimpleStringProperty}.
 *
 * @author 180Sai
 */
public class SectionEntry {
    private final SimpleStringProperty code = new SimpleStringProperty(),
            term = new SimpleStringProperty(),
            instructors = new SimpleStringProperty(),
            seats = new SimpleStringProperty();

    private final Section section;

    public SectionEntry(Section sec) {
        codeProperty().setValue(sec.getCode());
        termProperty().setValue(sec.getTerm());
        instructorsProperty().setValue(String.join(", ", sec.getInstructors()));
        seatsProperty().setValue(sec.getSeats());

        section = sec;
    }

    public Section getSection() {
        return section;
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
