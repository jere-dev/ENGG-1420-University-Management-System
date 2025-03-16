package ca.uoguelph.backend;

import java.util.ArrayList;

public class LoginManager {
    private static ArrayList<User> users = new ArrayList<User>();

    public static void addUser(User user) {
        users.add(user);
    }

    public static User login(String ID, String password) {
        // get user with ID (value null if user does not exist)
        User user = users.stream()
                .filter(u -> ID.equals(u.getID()))
                .findAny()
                .orElse(null);

        if (user == null) {
            throw new IllegalArgumentException("Invalid User ID");
        } // throw exception if user does not exist
        else if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Password Incorrect");
        } // throw exception if password is wrong

        return user; // return user
    }
}
