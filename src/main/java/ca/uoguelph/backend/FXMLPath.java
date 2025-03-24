package ca.uoguelph.backend;

import ca.uoguelph.backend.login.LoginManager;
import ca.uoguelph.backend.login.UserRole;

public enum FXMLPath {
    LOGIN, DASHBOARD, DASHBOARD_CONTENT, SUBJECTS, COURSES, STUDENTS, FACULTY, EVENTS;

    private static final String base = "/assets/fxml/";

    private static final String calendarPath = "event_calendar.fxml",
            dashboardPath = "dashboard.fxml", loginPath = "login.fxml",
            sectionPath = "admin/section_editor.fxml";

    /* file paths
    *
    * 1. dashboard content
    * 2. subjects
    * 3. courses
    * 4. students
    * 5. faculty
    * 6. events
    * */
    private static final String[] sPaths = {
            "dashboard_content.fxml",
            "user/subject_catalog_user.fxml",
            "user/course_catalog_student.fxml",
            "user/student_profile_student.fxml",
            null,   // option should not exist for this one (project description pg. 10)
            "user/event_manager_user.fxml"
    };
    private static final String[] sProfiles = {
            null,
            null,
            "user/course_profile_student",
            "user/student_profile_student",
            "user/faculty_profile_student",
            "user/event_profile.fxml"
    };

    private static final String[] fPaths = {
            "dashboard_content.fxml",
            "user/subject_catalog_user.fxml",
            "user/course_catalog_faculty.fxml",
            null,
            "user/faculty_profile_faculty.fxml",
            "user/event_manager_user.fxml"
    };
    private static final String[] fProfiles = {
            null,
            null,
            "user/course_profile_faculty.fxml",
            "user/student_profile_faculty.fxml",
            "user/faculty_profile_faculty.fxml",
            "user/event_profile.fxml"
    };

    private static final String[] aPaths = {
            "dashboard_content.fxml",
            "admin/subject_manager.fxml",
            "admin/course_manager.fxml",
            "admin/student_manager.fxml",
            "admin/faculty_manager.fxml",
            "admin/event_manager.fxml",
    };
    private static final String[] editorPaths = {
            null,
            "admin/subject_editor.fxml",
            "admin/course_editor.fxml",
            "admin/student_profile_admin.fxml",
            "admin/faculty_profile_admin.fxml",
            "admin/event_editor.fxml"
    };

    public static String basic(FXMLPath path) {
        String[] paths = switch (LoginManager.getRole()) {
            case ADMIN -> aPaths;
            case STUDENT -> sPaths;
            case FACULTY -> fPaths;
        };

        return base + switch (path) {
            case DASHBOARD_CONTENT -> paths[0];
            case SUBJECTS -> paths[1];
            case COURSES -> paths[2];
            case STUDENTS -> paths[3];
            case FACULTY -> paths[4];
            case EVENTS -> paths[5];
            default -> throw new IllegalArgumentException("Basic path of " + path + " does not exist");
        };
    }

    public static String profile(FXMLPath path) {
        if (LoginManager.getRole() == UserRole.ADMIN)
            throw new IllegalStateException("Attempt to access view-only profile as admin");

        String[] paths = switch (LoginManager.getRole()) {
            case FACULTY -> fProfiles;
            case STUDENT -> sProfiles;
            default -> null;
        };

        return base + switch (path) {
            case COURSES -> paths[2];
            case STUDENTS -> paths[3];
            case FACULTY -> paths[4];
            case EVENTS -> paths[5];
            default -> throw new IllegalArgumentException("Profile for " + path + " does not exist");
        };
    }

    public static String editor(FXMLPath path) {
        if (LoginManager.getRole() != UserRole.ADMIN) 
            throw new IllegalStateException("Attempt to access editors as " + LoginManager.getRole());

        return base + switch (path) {
            case SUBJECTS -> editorPaths[1];
            case COURSES -> editorPaths[2];
            case STUDENTS -> editorPaths[3];
            case FACULTY -> editorPaths[4];
            case EVENTS -> editorPaths[5];
            default -> throw new IllegalArgumentException("Editor for " + path + " does not exist");
        };
    }
    public static String sectionEditor() {
        if (LoginManager.getRole() != UserRole.ADMIN)
            throw new IllegalStateException("Attempt to access section editor as " + LoginManager.getRole());

        return base + sectionPath;
    }

    public static String eventCalendar() {
        return base + calendarPath;
    }
    public static String login() {
        return base + loginPath;
    }
    public static String dashboard() {
        return base + dashboardPath;
    }
}
