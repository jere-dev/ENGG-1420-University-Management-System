package ca.uoguelph.backend;

public class SystemLogin {
    private static String currentUsername, userRole;

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
            "subject_catalog_user.fxml",
            "course_catalog_student.fxml",
            "student_profile_student.fxml",
            "faculty_list_admin.fxml",
            "event_manager_user.fxml"
    };
    private static final String[] facultyPaths = {
            "dashboard_content.fxml",
            "subject_catalog_user.fxml",
            "course_catalog_faculty.fxml",
            "student_list_admin.fxml",
            "faculty_profile_faculty.fxml",
            "event_manager_user.fxml"
    };
    private static final String[] adminPaths = {
            "dashboard_content.fxml",
            "subject_manager_admin.fxml",
            "course_manager.fxml",
            "student_list_admin.fxml",    // duplicate(s)?
            "faculty_list_admin.fxml",    // TODO: replace duplicate and/or fix missing files
            "event_manager.fxml",
    };

    public static String[] getFXMLPath() {
        return switch (userRole) {
            case "student" -> studentPaths;
            case "faculty" -> facultyPaths;
            default -> adminPaths;
        };
    }

    // Verifies if no login is set, if username is in database and sets role
    public static boolean login(String username) {
        if (userRole != null) return false;

        // TODO: find username in database and verifies
        String role = username;

        currentUsername = username;
        userRole = role;
        return true;
    }

    // Removes the user role if exists
    public static boolean logout(String userName) {
        if (userRole == null) return false;
        currentUsername = null;
        userRole = null;
        return true;
    }

    public static String getUsername() {
        return currentUsername;
    }

    public static String getRole() {
        return userRole;
    }
}
