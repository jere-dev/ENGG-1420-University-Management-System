package ca.uoguelph.frontend.objects.table.row;

import ca.uoguelph.backend.*;
import ca.uoguelph.frontend.objects.section.DateConverter;
import ca.uoguelph.frontend.objects.section.DateFilter;
import ca.uoguelph.frontend.objects.section.TimeConverter;
import ca.uoguelph.frontend.objects.section.TimeFilter;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * A utility class providing preset TableRow configurations for various section editor components.
 * <p>
 * This class contains factory methods for creating standardized table rows for faculty, meetings,
 * and enrollment data in a consistent format.
 *
 * @author 180Sai
 */
public class SectionEditorRowPresets {
    /** The number of columns in a faculty row (Name, Degree, Research Interest, Email, Action) */
    public static final int facultyRowCount = 2;

    /**
     * Creates and returns a header row for faculty information table.
     * <p>
     * The row contains columns for Name and an action column.
     *
     * @return a styled TableRow configured as a faculty header
     */
    public static TableRow getFacultyHeader() {
        return new TableRow(
                new Label("Name")
        );
    }

    /**
     * Creates and returns a data row populated with faculty information.
     * <p>
     * The row contains: Name (non-editable) and a Delete button in the action column.
     *
     * @param fShortName the name of the instructor to display
     * @return a styled TableRow populated with the faculty member's information
     */
    public static TableRow getFacultyRow(String fShortName) {
        return new TableRow(
                new Label(fShortName), new Button("Delete")
        );
    }

    /** The number of columns in a meeting row (Days, Date, Time, Location, Type, Action) */
    public static final int meetingRowCount = 6;

    /**
     * Creates and returns a header row for meeting information table.
     * <p>
     * The row contains columns for: Days, Date, Time, Location, Type, and an empty action column.
     *
     * @return a styled TableRow configured as a meeting header with 6 columns
     */
    public static TableRow getMeetingHeader() {
        return new TableRow(
                new Label("Days"), new Label("Date"), new Label("Time"),
                new Label("Location"), new Label("Type"), new Label()
        );
    }

    /**
     * Creates and returns a data row populated with meeting information.
     * <p>
     * The row contains editable fields for: Days, Date range (with validation), Time range (with validation),
     * Location, Type, and a Delete button in the action column.
     *
     * @param m the Meeting object containing the data to display and edit
     * @return a styled TableRow populated with the meeting's information and appropriate editors
     */
    public static TableRow getMeetingRow(Meeting m) {
        String[] dates = m.getDates().split(" - "),
                times = m.getTime().split(" - ");
        TextInputControl[] dateBinds = new TextInputControl[2], timeBinds = new TextInputControl[2];

        VBox dateBox = new VBox(dateBinds[0] = new TextField(dates[0]), new Label("-"), dateBinds[1] = new TextField(dates[1])),
                timeBox = new VBox(timeBinds[0] = new TextField(times[0]), new Label("-"), timeBinds[1] = new TextField(dates[1]));

        for (TextInputControl date : dateBinds)
            date.setTextFormatter(new TextFormatter<>(new DateConverter(), "9/5/2024", new DateFilter()));

        for (TextInputControl time : timeBinds)
            time.setTextFormatter(new TextFormatter<>(new TimeConverter(), "10:30 AM", new TimeFilter()));

        return new TableRow(
                new TextField(m.getDays()), dateBox, timeBox, new TextField(m.getLocation()),
                new TextField(m.getType()), new Button("Delete")
        );
    }

    /** The number of columns in an enrollment row (Name, Email, Academic Level, Action) */
    public static final int enrollmentRowCount = 4;

    /**
     * Creates and returns a header row for enrollment information table.
     * <p>
     * The row contains columns for: Name, Email, Academic Level, and an empty action column.
     *
     * @return a styled TableRow configured as an enrollment header with 4 columns
     */
    public static TableRow getEnrollmentHeader() {
        return new TableRow("Name", "Email", "Academic Level", new Label());
    }

    /**
     * Creates and returns a data row populated with student enrollment information.
     * <p>
     * The row contains: Name (non-editable), Email (non-editable), Academic Level (non-editable),
     * and a Remove button in the action column.
     *
     * @param st the Student object containing the data to display
     * @return a styled TableRow populated with the student's information
     */
    public static TableRow getEnrollmentRow(Student st) {
//        List<Student> enrolledStudentsList = StudentManager.getStudents().stream().filter(s -> {
//            for (Pair<String, String> course : s.getCourses()) {
//                if (course.getKey().equals(c.getSubjectCode()) && course.getValue().equals(c.getCourseCode()))
//                    return true;
//            }
//            return false;
//        }).toList();

        return new TableRow(st.getName(), st.getEmail(), st.getAcademicLevel(), new Button("Remove"));
    }
}
