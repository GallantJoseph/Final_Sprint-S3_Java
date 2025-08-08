package UserManagement;

import Users.Role;
import Users.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.ArrayList;

public class UserService {
    public static User login(String username, String password, ArrayList<Role> roles) {
        try {
            User user = UserDAO.getUserByUsername(username, roles);

            if (user != null) {
                if (BCrypt.checkpw(password, user.getPassword())) {
                    return user;
                } else {
                    //TODO Log the error
                    System.out.println("Invalid Credentials. Please try again.");
                }
            } else {
                //TODO Log the error
                System.out.println("Invalid Credentials. Please try again.");
            }
        //} catch (IOException e) {
        } catch (Exception e) {
            //TODO Log the error
            e.printStackTrace();
        }

        return null; // Return null if login fails
    }

    public static void register(User user) {
        try {
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

            int newUserId = UserDAO.createUser(user);
            System.out.println("User " + user.getUsername() + " registered successfully with ID: " + newUserId);

        } catch (Exception e) {
            //TODO Log the error
            System.out.println("Error while registering the new user: " + e.getMessage());
        }
    }
}
