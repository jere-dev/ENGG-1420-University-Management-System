package ca.uoguelph.backend;

public class GradeRecord {
    private final String courseName, courseCode, sectionNo;
    private final String[] professors;
    private final int finalGrade;

    public GradeRecord(String name, String code, String section, String[] profs, int grade) {
        courseName = name;
        courseCode = code;
        sectionNo = section;
        professors = profs;
        finalGrade = grade;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getSectionNo() {
        return sectionNo;
    }

    public String[] getProfessors() {
        return professors;
    }

    public int getFinalGrade() {
        return finalGrade;
    }
}
