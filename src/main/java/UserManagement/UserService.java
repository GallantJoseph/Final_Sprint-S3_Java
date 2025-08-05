package UserManagement;

import Users.User;

public class UserService {
    public static void login(String username, String password) {
        // Menu for user login
        System.out.println("User " + username + " logged in successfully.");
    }

    public static void register(User user) {
        // Menu for user registration
        UserDAO.createUser(user);
    }
}
