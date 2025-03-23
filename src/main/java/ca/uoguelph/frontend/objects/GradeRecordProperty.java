package ca.uoguelph.frontend.objects;

import ca.uoguelph.backend.GradeRecord;
import javafx.beans.property.SimpleStringProperty;

import java.util.Arrays;

public final class GradeRecordProperty {
    private final SimpleStringProperty courseName, courseCode, sectionNo;
    private final SimpleStringProperty professors;
    private final SimpleStringProperty finalGrade;

    public GradeRecordProperty(String name, String code, String section, String[] profs, int grade) {
        courseName = new SimpleStringProperty(name);
        courseCode = new SimpleStringProperty(code);
        sectionNo = new SimpleStringProperty(section);
        professors = new SimpleStringProperty(Arrays.toString(profs));
        finalGrade = new SimpleStringProperty(String.valueOf(grade));
    }

    public GradeRecordProperty(GradeRecord r) {
        courseName = new SimpleStringProperty(r.getCourseName());
        courseCode = new SimpleStringProperty(r.getCourseCode());
        sectionNo = new SimpleStringProperty(r.getSectionNo());
        professors = new SimpleStringProperty(Arrays.toString(r.getProfessors()));
        finalGrade = new SimpleStringProperty(String.valueOf(r.getFinalGrade()));
    }

    public String getCourseName() {
        return courseName.get();
    }

    public SimpleStringProperty courseNameProperty() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode.get();
    }

    public SimpleStringProperty courseCodeProperty() {
        return courseCode;
    }

    public String getSectionNo() {
        return sectionNo.get();
    }

    public SimpleStringProperty sectionNoProperty() {
        return sectionNo;
    }

    public String getProfessors() {
        return professors.get();
    }

    public SimpleStringProperty professorsProperty() {
        return professors;
    }

    public String getFinalGrade() {
        return finalGrade.get();
    }

    public SimpleStringProperty finalGradeProperty() {
        return finalGrade;
    }
}
