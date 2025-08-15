package MembershipManagement;

import DBManager.DatabaseConnection;
import Logging.LoggingManagement;
import Memberships.MembershipType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * MembershipTypesDAO class.
 * This class is responsible for handling Membership Type operations in the system.
 */
public class MembershipTypesDAO {


    /**
     * Create Membership Type
     * Inserts a new membership type into the database and returns the generated ID.
     *
     * @param name The name of the membership type.
     * @param description The description of the membership type.
     * @param cost The cost of the membership type.
     * @return The generated ID of the new membership type, or -1 if the operation failed.
     */
    public static int createMembershipType(String name, String description, double cost) {
        final String SQL = "INSERT INTO membership_types (mem_type_name, mem_type_description, mem_type_cost) VALUES (?, ?, ?)";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, cost);

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            // Check if the membership type ID was generated
            if (generatedKeys.next()) {
                int newMembershipTypeId = generatedKeys.getInt(1); // Get the generated membership type ID
                LoggingManagement.log("New membership type added with ID: " + newMembershipTypeId + " and name: " + name, false);

                return newMembershipTypeId; // Return the generated membership type ID
            } else {
                String errorMessage = "No ID was generated for the new membership type.";
                System.out.println(errorMessage);
                LoggingManagement.log(errorMessage, true);
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            String errorMessage = "Error while adding the new membership type.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return -1; // Return -1 to indicate failure
    }

    /**
     * Get Membership Type by ID
     * Retrieves a membership type by its ID from the database.
     *
     * @param membershipTypeId The ID of the membership type to retrieve.
     * @return The MembershipType object if found, or null if not found or an error occurred.
     */
    public static MembershipType getMembershipTypeById(int membershipTypeId) {
        MembershipType membershipType = null;
        final String SQL = "SELECT * FROM membership_types WHERE mem_type_id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setInt(1, membershipTypeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                membershipType = new MembershipType(
                        resultSet.getInt("mem_type_id"),
                        resultSet.getString("mem_type_name"),
                        resultSet.getString("mem_type_description"),
                        resultSet.getDouble("mem_type_cost")
                );
            } else {
                System.out.println("No membership type found with ID: " + membershipTypeId);
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            String errorMessage = "Error while retrieving the membership type with ID: " + membershipTypeId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return membershipType;
    }

    /**
     * Get All Membership Types
     * Retrieves all membership types from the database.
     *
     * @return An ArrayList of MembershipType objects representing all membership types.
     */
    public static ArrayList<MembershipType> getAllMembershipTypes() {
        ArrayList<MembershipType> membershipTypes = new ArrayList<>();
        final String SQL = "SELECT * FROM membership_types";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                MembershipType membershipType = new MembershipType(
                        resultSet.getInt("mem_type_id"),
                        resultSet.getString("mem_type_name"),
                        resultSet.getString("mem_type_description"),
                        resultSet.getDouble("mem_type_cost")
                );

                membershipTypes.add(membershipType);
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            String errorMessage = "Error while retrieving all membership types.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return membershipTypes;
    }

    /**
     * Update Membership Type
     * Updates an existing membership type in the database.
     *
     * @param membershipTypeId The ID of the membership type to update.
     * @param name The new name of the membership type.
     * @param description The new description of the membership type.
     * @param cost The new cost of the membership type.
     * @return The number of affected rows, or 0 if no rows were updated.
     */
    public static int updateMembershipType(int membershipTypeId, String name, String description, double cost) {
        final String SQL = "UPDATE membership_types SET mem_type_name = ?, mem_type_description = ?, mem_type_cost = ? WHERE mem_type_id = ?";
        int affectedRows = 0;

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, cost);
            preparedStatement.setInt(4, membershipTypeId);

            affectedRows = preparedStatement.executeUpdate();

            if(affectedRows == 0) {
                LoggingManagement.log("No membership type found with ID: " + membershipTypeId, true);
            } else {
                LoggingManagement.log("Membership type with ID: " + membershipTypeId + " updated successfully.", false);
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            String errorMessage = "Error while updating the membership type with ID: " + membershipTypeId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return affectedRows;
    }

    /**
     * Delete Membership Type
     * Deletes a membership type from the database by its ID.
     *
     * @param membershipTypeId The ID of the membership type to delete.
     * @return The number of affected rows, or 0 if no rows were deleted.
     */
    public static int deleteMembershipType(int membershipTypeId) {
        final String SQL = "DELETE FROM membership_types WHERE mem_type_id = ?";
        int affectedRows = 0;

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setInt(1, membershipTypeId);

            affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                LoggingManagement.log("No membership type found with ID: " + membershipTypeId, true);
            } else {
                LoggingManagement.log("Membership type with ID: " + membershipTypeId + " deleted successfully.", false);
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            String errorMessage = "Error while deleting the membership type with ID: " + membershipTypeId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return affectedRows;
    }
}
