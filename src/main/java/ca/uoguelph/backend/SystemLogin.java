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

    public static String[] getFXMLPaths() {
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
//        // check students
//        if (StudentManager.getStudent(username) != null && StudentManager.getStudent(username).checkPassword(password)) {
//            currentUsername = username;
//            userRole = "student";
//            return true;
//        }
//
//        // check professors
//        if (ProfManager.getProf(username) != null && ProfManager.getProf(username).checkPassword(password)) {
//            currentUsername = username;
//            userRole = "faculty";
//            return true;
//        }
//
//        // check admin
//        if (AdminManager.getAdmin(username) != null && AdminManager.getAdmin(username).checkPassword(password)) {
//            currentUsername = username;
//            userRole = "admin";
//            return true;
//        }

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
