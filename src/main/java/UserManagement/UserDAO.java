package UserManagement;

import Users.User;

public class UserDAO {
    public static void createUser(User user) {
        System.out.println("User " + user.getUsername() + " created successfully.");
    }

    public static void updateUser(User user) {
        System.out.println("User " + user.getUsername() + " updated successfully.");
    }

    public static void deleteUser(User user) {
        System.out.println("User " + user.getUsername() + " deleted successfully.");
    }

    public static User getUserById(int id) {
        // Get a user from their id
        return null;
    }

    public static User getUserByUsername(String username) {
        // Get a user from their username
        return null;
    }
}
