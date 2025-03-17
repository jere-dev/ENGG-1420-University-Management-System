package ca.uoguelph.backend;

import ca.uoguelph.backend.login.LoginManager;

public enum FXMLPath {
    DASHBOARD_CONTENT, SUBJECTS, COURSES, STUDENTS, FACULTY, EVENTS;

    /* file paths
    *
    * 1. dashboard content
    * 2. subjects
    * 3. courses
    * 4. students
    * 5. faculty
    * 6. events
    * */
    private static final String[] studentPaths = {
            "dashboard_content.fxml",
            "user/subject_catalog_user.fxml",
            "user/course_catalog_student.fxml",
            "user/student_profile_student.fxml",
            null,   // option should not exist for this one (project description pg. 10)
            "user/event_manager_user.fxml"
    };
    private static final String[] facultyPaths = {
            "dashboard_content.fxml",
            "user/subject_catalog_user.fxml",
            "user/course_catalog_faculty.fxml",
            null,
            "user/faculty_profile_faculty.fxml",
            "user/event_manager_user.fxml"
    };
    private static final String[] adminPaths = {
            "dashboard_content.fxml",
            "admin/subject_manager.fxml",
            "admin/course_manager.fxml",
            "admin/student_manager.fxml",
            "admin/faculty_manager.fxml",
            "admin/event_manager.fxml",
    };

    public static String getFXMLPath(FXMLPath path) {
        String[] paths = switch (LoginManager.getRole()) {
            case ADMIN -> adminPaths;
            case STUDENT -> facultyPaths;
            case FACULTY -> studentPaths;
        };

        return switch (path) {
            case DASHBOARD_CONTENT -> paths[0];
            case SUBJECTS -> paths[1];
            case COURSES -> paths[2];
            case STUDENTS -> paths[3];
            case FACULTY -> paths[4];
            case EVENTS -> paths[5];
        };
    }
}
