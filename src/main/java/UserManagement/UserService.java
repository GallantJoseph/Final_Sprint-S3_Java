package UserManagement;

import Logging.LoggingManagement;
import Users.Role;
import Users.User;
import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;

public class UserService {
    public static User login(String username, String password, ArrayList<Role> roles) {
        try {
            User user = UserDAO.getUserByUsername(username, roles);

            if (user != null) {
                if (BCrypt.checkpw(password, user.getPassword())) {
                    LoggingManagement.log("Login successful for user: " + username, false);
                    return user;
                } else {
                    System.out.println("Invalid Credentials. Please try again.");
                }
            } else {
                System.out.println("Invalid Credentials. Please try again.");
            }

            LoggingManagement.log("Login attempt failed for user: " + username, false);

        } catch (Exception e) {
            String errorMessage = "Error while logging in user: " + username;
            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return null; // Return null if login fails
    }

    public static void register(User user) {
        try {
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

            int newUserId = UserDAO.createUser(user);
            System.out.println("User " + user.getUsername() + " registered successfully with ID: " + newUserId);
            LoggingManagement.log("New user registered with ID: " + newUserId + " and username: " + user.getUsername(), false);

        } catch (Exception e) {
            String errorMessage = "Error while registering the new user.";
            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }
    }
}
