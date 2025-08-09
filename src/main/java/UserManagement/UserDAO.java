package UserManagement;

import DBManager.DatabaseConnection;
import Logging.LoggingManagement;
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
    public static int createUser(User user) {
        final String SQL = "INSERT INTO users (username, password, first_name, last_name, street_address, city, province, postal_code, email, phone, role_id) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        int generatedId = -1;

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getStreetAddress());
            preparedStatement.setString(6, user.getCity());
            preparedStatement.setString(7, user.getProvince());
            preparedStatement.setString(8, user.getPostalCode());
            preparedStatement.setString(9, user.getEmail());
            preparedStatement.setString(10, user.getPhone());
            preparedStatement.setInt(11, user.getRole().getId());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1); // Set the generated user ID
            }
        } catch (SQLException e) {
            System.out.println("Error while adding the new user.\n");
            LoggingManagement.log(e.getMessage(), true);
        }

        return generatedId;
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
            LoggingManagement.log(e.getMessage(), true);
        }

        return user;
    }

    public static User getUserByUsername(String username, ArrayList<Role> roles) {
        User user = null;
        final String SQL = "SELECT * FROM users WHERE username = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, username);

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
                System.out.println("No user found with username: " + username);
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving the user.");
            LoggingManagement.log(e.getMessage(), true);
        }

        return user;
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
            LoggingManagement.log(e.getMessage(), true);
        }

        // Check if roles list is empty
        if (roles.isEmpty()) {
            System.out.println();
            throw new RuntimeException("No roles found in the database.");
        }

        return roles;
    }
}