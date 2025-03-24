package ca.uoguelph.frontend.objects.table.row;

import ca.uoguelph.backend.Course;
import ca.uoguelph.backend.Student;
import ca.uoguelph.backend.Subject;
import ca.uoguelph.backend.login.LoginManager;
import ca.uoguelph.backend.login.UserRole;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

import java.util.HashSet;
import java.util.List;

public final class TableRowPreset extends TableRow {
    /**
     * Converts a {@code Subject} object to a table row. Adds an edit button if the {@code LoginState} is an administrator.
     */
    public TableRowPreset(Subject sbj) {
        super(subjectRow(sbj));
    }

    private static List<Node> subjectRow(Subject sbj) {
        Label nameLabel = new Label(sbj.getName()),
                codeLabel = new Label(sbj.getCode());

        // TODO: customize look

        // return right away if faculty or student
        if (LoginManager.getRole() == UserRole.FACULTY || LoginManager.getRole() == UserRole.STUDENT)
            return List.of(nameLabel, codeLabel);

        Button editButton = new Button("✎");

        // TODO: customize

        return List.of(nameLabel, codeLabel, editButton);
    }

    /**
     * Converts a {@code Course} object to a table row. Specific to when the {@code LoginState} is an administrator.
     * @param c the course
     */
    public TableRowPreset(Course c) {
        super(courseRow(c));
    }

    public static List<Node> courseRow(Course c) {
        if (LoginManager.getRole() != UserRole.ADMIN)
            throw new IllegalStateException("Attempt to create course list for " + LoginManager.getRole());

        Label titleLabel = new Label(c.getTitle()),
                codeLabel = new Label(c.getSubjectCode() + c.getCourseCode()),
                requisitesLabel = new Label(c.getRequisites());
        Hyperlink editorLink = new Hyperlink("See profile");

        requisitesLabel.setWrapText(true);

        // manage name of instructors
        Label instructorLabel = new Label();
        instructorLabel.setWrapText(true);

        HashSet<String> nameSet = new HashSet<>();
        c.getSections().forEach(s -> nameSet.addAll(s.getInstructors()));
        String[] names = nameSet.toArray(new String[1]);

        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < Math.min(names.length, 4); i++)
            if (names[i] != null) nameBuilder.append(names[i]).append("; ");

        if (!nameBuilder.isEmpty()) nameBuilder.delete(nameBuilder.length() - 2, nameBuilder.length() - 1);
        else nameBuilder.append("N/A");
        if (names.length > 4) nameBuilder.append("...");

        instructorLabel.setText(nameBuilder.toString());
        // end instructors

        return List.of(titleLabel, codeLabel, requisitesLabel, instructorLabel, editorLink);
    }

    /**
     * Converts a {@code Student} object to a table row. Specific to only when the {@code LoginState} is an administrator.
     */
    public TableRowPreset(Student st) {
        super(studentRowAdmin(st));
    }

    private static List<Node> studentRowAdmin(Student st) {
        if (LoginManager.getRole() != UserRole.ADMIN)
            throw new IllegalStateException("Attempt to create administrator's list of students for " + LoginManager.getRole());

        Node[] labels = {
                new Label(st.getName()),
                new Label(st.getID()),
                new Label(st.getAcademicLevel()),
                new Label(st.getEmail()),
                new Button("✎")
        };

        // TODO: customize

        return List.of(labels);
    }

    /**
     * Converts a {@code Student} object to a table row for a specified course type. Specific to when the {@code LoginState} is a faculty member.
     * @param st    student
     * @param c     course name of student
     */
    public TableRowPreset(Student st, Course c) {
        super(studentRowFaculty(st, c));
    }

    private static List<Node> studentRowFaculty(Student st, Course c) {
        if (LoginManager.getRole() != UserRole.FACULTY)
            throw new IllegalStateException("Attempt to create faculty's list of students for " + LoginManager.getRole());

        Node[] labels = {
                new Label(st.getName()),
                new Label(st.getID()),
                new Label(st.getAcademicLevel()),
                new Label(st.getEmail()),
                new Label("TODO"), // TODO: add courses to label
                new Hyperlink("See profile")
        };

        // TODO: customize looks

        return List.of(labels);
    }
}
