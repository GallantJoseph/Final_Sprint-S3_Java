package UserManagement;

import DBManager.DatabaseConnection;
import Logging.LoggingManagement;
import Users.Role;
import Users.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * UserDAO class.
 * This class is responsible for handling User database operations in the system.
 */
public class UserDAO {
    /**
     * Create User
     * Inserts a new user into the database and returns the generated ID.
     *
     * @param user The User object containing user details.
     * @return The generated ID of the new user, or -1 if the operation failed.
     */
    public static int createUser(User user) {
        final String SQL = "INSERT INTO users (username, password, first_name, last_name, street_address, city, province, postal_code, email, phone, role_id) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

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
                int generatedId = generatedKeys.getInt(1); // Set the generated user ID

                LoggingManagement.log("New user added with ID: " + generatedId + " and username: " + user.getUsername(), false);
                return generatedId; // Return the ID of the newly created user
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error while adding the new user.\n");
            LoggingManagement.log("Error while adding the new user: " + e.getMessage(), true);
        }

        return -1;
    }

    /**
     * Update User
     * Updates an existing user in the database.
     *
     * @param user The User object containing updated user details.
     */
    public static void updateUser(User user) {
        final String SQL = "UPDATE users SET username = ?, password = ?, first_name = ?, last_name = ?, " +
                "street_address = ?, city = ?, province = ?, postal_code = ?, email = ?, phone = ?, role_id = ?" +
                " WHERE user_id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

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
            preparedStatement.setInt(12, user.getUserId());

            int rowsUpdated = preparedStatement.executeUpdate();

            String message;
            if (rowsUpdated > 0) {
                message = "User with ID: " + user.getUserId() + " updated successfully.";
            } else {
                message = "No user found with ID: " + user.getUserId();
            }

            System.out.println(message);
            LoggingManagement.log(message, false);

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            String errorMessage = "Error while updating the user with ID: " + user.getUserId();

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }
    }

    /**
     * Delete User and Memberships
     * Deletes a user and their associated memberships from the database by user ID.
     *
     * @param userId The ID of the user to delete.
     */
    public static void deleteUserAndMembershipsByUserId(int userId) {
        final String DELETE_MEMBERSHIPS_SQL = "DELETE FROM memberships WHERE member_id = ?";
        final String DELETE_USER_SQL = "DELETE FROM users WHERE user_id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();

            // Delete memberships first
            PreparedStatement deleteMembershipsStmt = connection.prepareStatement(DELETE_MEMBERSHIPS_SQL);
            deleteMembershipsStmt.setInt(1, userId);
            int membershipsDeleted = deleteMembershipsStmt.executeUpdate();

            // Delete user
            PreparedStatement deleteUserStmt = connection.prepareStatement(DELETE_USER_SQL);
            deleteUserStmt.setInt(1, userId);
            int usersDeleted = deleteUserStmt.executeUpdate();

            String message;
            if (usersDeleted > 0) {
                message = "User and their memberships deleted successfully.";
            } else {
                message = "No user found with ID: " + userId;
            }

            System.out.println(message);
            LoggingManagement.log(message, false);

            deleteMembershipsStmt.close();
            deleteUserStmt.close();
            connection.close();

        } catch (SQLException e) {
            String errorMessage = "Error while deleting user with ID: " + userId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }
    }

    /**
     * Get User by ID
     * Retrieves a user from the database by their user ID.
     *
     * @param userId The ID of the user to retrieve.
     * @param roles  The list of roles to match against the user's role ID.
     * @return The User object if found, or null if not found.
     */
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

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            String errorMessage = "Error while retrieving the user with ID: " + userId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return user;
    }

    /**
     * Get User by Username
     * Retrieves a user from the database by their username.
     *
     * @param username The username of the user to retrieve.
     * @param roles    The list of roles to match against the user's role ID.
     * @return The User object if found, or null if not found.
     */
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

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            String errorMessage = "Error while retrieving the user with username: " + username;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return user;
    }

    /**
     * Get Roles
     * Retrieves all roles from the database.
     *
     * @return An ArrayList of Role objects representing all roles.
     */
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

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            String errorMessage = "Error while retrieving roles from the database.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        // Check if roles list is empty
        if (roles.isEmpty()) {
            String errorMessage = "No roles found in the database.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage, true);
            throw new RuntimeException("No roles found in the database.");
        }

        return roles;
    }
}