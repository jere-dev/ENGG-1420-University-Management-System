package ca.uoguelph.backend.login;

import ca.uoguelph.backend.Admin;
import ca.uoguelph.backend.Faculty;
import ca.uoguelph.backend.Student;
import ca.uoguelph.backend.User;

import java.util.ArrayList;

public class LoginManager {
    private static final ArrayList<User> users = new ArrayList<>();
    private static User loginUser;
    private static LoginState loginState;

    public static void addUser(User user) {
        users.add(user);
    }

    public static User login(String ID, String password) {
        // get user with ID (value null if user does not exist)
        loginUser = users.stream()
                .filter(u -> ID.equals(u.getID()))
                .findAny()
                .orElse(null);

        if (getCurrentUser() == null) {
            throw new IllegalArgumentException("Invalid User ID");
        } // throw exception if user does not exist
        else if (!getCurrentUser().getPassword().equals(password)) {
            throw new IllegalArgumentException("Password Incorrect");
        } // throw exception if password is wrong


        // set user role
        loginState = switch (getCurrentUser()) {
            case Admin ignored -> LoginState.ADMIN;
            case Faculty ignored1 -> LoginState.FACULTY;
            case Student ignored2 -> LoginState.STUDENT;
            default -> throw new IllegalStateException("Unexpected role: " + getCurrentUser());
        };

        return loginUser; // return user
    }

    // Removes the user role if exists
    public static boolean logout() {
        if (loginUser == null) return false;
        loginUser = null;
        loginState = null;
        return true;
    }

    public static User getCurrentUser() {
        return loginUser;
    }

    public static LoginState getRole() {
        return loginState;
    }
}
