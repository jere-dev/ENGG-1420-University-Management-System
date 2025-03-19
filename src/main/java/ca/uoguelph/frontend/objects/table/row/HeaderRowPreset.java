package ca.uoguelph.frontend.objects.table.row;

import javafx.scene.control.Label;

public enum HeaderRowPreset {
    COURSE, SUBJECT, STUDENT, FACULTY, EVENT;

    private static final TableRow courseRow = new TableRow(
            new Label("Course Title"),
            new Label("Course Code"),
            new Label("Requirements"),
            new Label("Instructors"),
            new Label("Course Profile")
    );

    // TODO: add remaining presets

    public TableRow getPreset() {
        TableRow preset = switch (this) {
            case COURSE -> courseRow;
            case SUBJECT -> new TableRow();
            case STUDENT -> new TableRow();
            case FACULTY -> new TableRow();
            case EVENT -> new TableRow();
        };

        for (int i = 0; i < preset.size(); i++) {
            preset.get(i).setStyle("-fx-font-family: Graduate;");
        }

        return preset;
    }

    public int presetRowCount() {
        return switch (this) {
            case COURSE -> courseRow.size();
            case SUBJECT -> 0;
            case STUDENT -> 0;
            case FACULTY -> 0;
            case EVENT -> 0;
        };
    }
}
