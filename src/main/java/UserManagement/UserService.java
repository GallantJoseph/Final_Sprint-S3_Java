package UserManagement;

import Logging.LoggingManagement;
import Users.Role;
import Users.User;
import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;

/**
 * UserService class provides methods for user login and registration.
 * It interacts with UserDAO to perform database operations.
 * It uses BCrypt for password hashing and verification.
 */
public class UserService {
    /**
     * Logs in a user with the provided username and password.
     * Validates credentials and returns the user object if successful.
     * Logs success or failure messages.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param roles    the list of roles available in the system
     * @return User object if login is successful, null otherwise
     */
    public static User login(String username, String password, ArrayList<Role> roles) {
        try {
            User user = UserDAO.getUserByUsername(username, roles);

            if (user != null) {
                if (BCrypt.checkpw(password, user.getPassword())) {
                    System.out.println("\nLogin successful!\n");
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

    /**
     * Registers a new user in the system.
     * Hashes the password using BCrypt before saving.
     * Logs success or error messages.
     *
     * @param user the user to register
     */
    public static void register(User user) {
        try {
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

            int newUserId = UserDAO.createUser(user);

            if (newUserId == -1) {
                String errorMessage = "Error: the username :" + user.getUsername() + " already exists.";

                System.out.println(errorMessage + " Please choose a different username.");
                LoggingManagement.log(errorMessage, true);
                return;
            }

            System.out.println("\nUser " + user.getUsername() + " registered successfully with ID: " + newUserId);
            LoggingManagement.log("New user registered with ID: " + newUserId + " and username: " + user.getUsername(), false);

        } catch (Exception e) {
            String errorMessage = "Error while registering the new user.";
            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }
    }
}
