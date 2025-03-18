package ca.uoguelph.frontend.objects.table.row;

import ca.uoguelph.backend.Course;
import ca.uoguelph.backend.Section;
import ca.uoguelph.backend.Student;
import ca.uoguelph.backend.Subject;
import ca.uoguelph.backend.login.LoginManager;
import ca.uoguelph.backend.login.LoginState;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class TableRowPreset extends TableRow {
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
        if (LoginManager.getRole() == LoginState.FACULTY || LoginManager.getRole() == LoginState.STUDENT)
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
        if (LoginManager.getRole() != LoginState.ADMIN)
            throw new IllegalStateException("Attempt to create course list for " + LoginManager.getRole());

        Label titleLabel = new Label(c.getTitle()),
                codeLabel = new Label(c.getCourseCode());
        Hyperlink courseLink = new Hyperlink("See profile");

        Label instructorLabel = new Label();
        instructorLabel.setWrapText(true);

        String[] instructorNames = (String[]) c.getSections().stream()
                .map(s -> s.getInstructors().stream()).distinct().limit(5).toArray();
        System.out.println(instructorNames);

//        ArrayList<String> nameList = new ArrayList<>();
//        HashSet<String> nameSet = new HashSet<>();
//        c.getSections().stream().forEach(s -> nameSet.addAll(s.getInstructors()));

        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < Math.max(instructorNames.length, 4); i++) nameBuilder.append(instructorNames[i]).append(",");
        nameBuilder.deleteCharAt(nameBuilder.length() - 1);
        if (instructorNames.length > 4) nameBuilder.append("...");

        instructorLabel.setText(nameBuilder.toString());

        return List.of(titleLabel, codeLabel, instructorLabel, courseLink);
    }

    /**
     * Converts a {@code Student} object to a table row. Specific to only when the {@code LoginState} is an administrator.
     */
    public TableRowPreset(Student st) {
        super(studentRowAdmin(st));
    }

    private static List<Node> studentRowAdmin(Student st) {
        if (LoginManager.getRole() != LoginState.ADMIN)
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

//    /**
//     * Converts a {@code Student} object to a table row for a specified course type. Specific to when the {@LoginState} is a faculty member.
//     * @param st    student
//     * @param c     course name of student
//     */
//    public TableRow(Student st, Course c) {
//        super(studentRowFaculty(st, c));
//    }
//
//    private static List<Node> studentRowFaculty(Student st, Course c) {
//        if (LoginManager.getRole() != LoginState.FACULTY)
//            throw new IllegalStateException("Attempt to create faculty's list of students for " + LoginManager.getRole());
//
//        Node[] labels = {
//                new Label(st.getName()),
//                new Label(st.getID()),
//                new Label(st.getAcademicLevel()),
//                new Label(st.getEmail()),
//                new Label(st.getCourses().get(st.getCourses().get(c)).getValue()),
//                new Hyperlink("See profile")
//        };
//
//        // TODO: customize looks
//
//        return List.of(labels);
//    }
}
