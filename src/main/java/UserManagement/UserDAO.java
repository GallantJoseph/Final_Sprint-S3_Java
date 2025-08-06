package UserManagement;

import DBManager.DatabaseConnection;
import Users.Role;
import Users.Trainer;
import Users.User;
import WorkoutClassManagement.WorkoutClassTypesDAO;
import WorkoutClasses.WorkoutClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

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

    public static User getUserById(int userId, ArrayList<Role> roles) {
        User user = null;
        final String SQL = "SELECT * FROM users WHERE user_id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Find the role object that matches the role_id from the result set
                int roleId = resultSet.getInt("role_id");
                Role foundRole = null;

                for (Role role : roles) {
                    if (role.getId() == roleId) {
                        foundRole = role;
                        break; // Element found, exit loop
                    }
                }

                user = new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("street_address"),
                        resultSet.getString("city"),
                        resultSet.getString("province"),
                        resultSet.getString("postal_code"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        foundRole
                );

            } else {
                System.out.println("No user found with ID: " + userId);
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving the user.");
            // TODO Log the error
            e.printStackTrace();
        }

        return user;
    }

    public static User getUserByUsername(String username) {
        // Get a user from their username
        return null;
    }

    public static ArrayList<Role> getRoles() {
        // Get all roles from the database
        ArrayList<Role> roles = new ArrayList<>();
        final String SQL = "SELECT * FROM roles";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Role role = new Role(
                        resultSet.getInt("role_id"),
                        resultSet.getString("name")
                );

                roles.add(role);
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving roles.");
            // TODO Log the error
            e.printStackTrace();
        }

        // Check if roles list is empty
        if (roles.isEmpty()) {
            System.out.println();
            throw new RuntimeException("No roles found in the database.");
        }

        return roles;
    }
}