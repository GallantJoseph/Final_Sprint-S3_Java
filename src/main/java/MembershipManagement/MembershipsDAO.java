package MembershipManagement;

import DBManager.DatabaseConnection;
import Logging.LoggingManagement;
import Memberships.Membership;
import UserManagement.UserDAO;
import Users.Role;
import Users.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * MembershipsDAO class.
 * This class is responsible for handling Membership database operations in the system.
 */
public class MembershipsDAO {
    /**
     * Creates a new membership in the database.
     *
     * @param membershipTypeId The ID of the membership type.
     * @param memberId         The ID of the member.
     * @param startDate        The start date of the membership.
     * @param endDate          The end date of the membership (can be null).
     * @return The ID of the newly created membership, or -1 if the operation failed.
     */
    public static int createMembership(int membershipTypeId, int memberId, LocalDate startDate, LocalDate endDate) {
        final String SQL = "INSERT INTO memberships (membership_type_id, member_id, membership_start, membership_end) VALUES (?, ?, ?, ?)";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, membershipTypeId);
            preparedStatement.setInt(2, memberId);
            preparedStatement.setDate(3, Date.valueOf(startDate));
            preparedStatement.setDate(4, endDate != null ? Date.valueOf(endDate) : null);

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            // Check if the membership ID was generated
            if (generatedKeys.next()) {
                int newMembershipId = generatedKeys.getInt(1); // Get the generated membership ID

                // Log the successful addition of the new membership and return the ID
                LoggingManagement.log("New membership added for userID: " + memberId + ", MembershipID: " + newMembershipId, false);
                return newMembershipId; // Get the generated membership ID
            } else {
                // If no ID was generated, display an error message and log it
                String errorMessage = "No ID was generated for the new membership.";

                System.out.println(errorMessage);
                LoggingManagement.log(errorMessage, true);
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            String errorMessage = "Error while adding the new membership.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return -1; // Return -1 to indicate failure
    }

    /**
     * Retrieves a membership by its ID.
     *
     * @param membershipId The ID of the membership to retrieve.
     * @param roles        The list of roles for user retrieval.
     * @return The Membership object if found, otherwise null.
     */
    public static Membership getMembershipById(int membershipId, ArrayList<Role> roles) {
        Membership membership = null;
        final String SQL = "SELECT * FROM memberships WHERE membership_id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, membershipId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                membership = new Membership(
                        resultSet.getInt("membership_id"),
                        MembershipTypesDAO.getMembershipTypeById(resultSet.getInt("membership_type_id")),
                        (User) UserDAO.getUserById(resultSet.getInt("member_id"), roles),
                        resultSet.getDate("membership_start").toLocalDate(),
                        resultSet.getDate("membership_end") != null ? resultSet.getDate("membership_end").toLocalDate() : null
                );
            } else {
                System.out.println("No membership found with ID: " + membershipId);
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            String errorMessage = "Error while retrieving the membership with ID: " + membershipId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return membership;
    }

    /**
     * Retrieves all memberships from the database.
     *
     * @param roles The list of roles for user retrieval.
     * @return A list of all Membership objects.
     */
    public static ArrayList<Membership> getAllMemberships(ArrayList<Role> roles) {
        ArrayList<Membership> memberships = new ArrayList<>();
        final String SQL = "SELECT * FROM memberships";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Membership membership = new Membership(
                        resultSet.getInt("membership_id"),
                        MembershipTypesDAO.getMembershipTypeById(resultSet.getInt("membership_type_id")),
                        (User) UserDAO.getUserById(resultSet.getInt("member_id"), roles),
                        resultSet.getDate("membership_start").toLocalDate(),
                        resultSet.getDate("membership_end") != null ? resultSet.getDate("membership_end").toLocalDate() : null
                );

                memberships.add(membership);
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            String errorMessage = "Error while retrieving all memberships.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return memberships;
    }

    /**
     * Retrieves all memberships for a specific user.
     *
     * @param memberId The ID of the member whose memberships are to be retrieved.
     * @param roles    The list of roles for user retrieval.
     * @return A list of Membership objects associated with the specified user.
     */
    public static ArrayList<Membership> getUserMemberships(int memberId, ArrayList<Role> roles) {
        ArrayList<Membership> memberships = new ArrayList<>();
        final String SQL = "SELECT * FROM memberships WHERE member_id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, memberId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Membership membership = new Membership(
                        resultSet.getInt("membership_id"),
                        MembershipTypesDAO.getMembershipTypeById(resultSet.getInt("membership_type_id")),
                        (User) UserDAO.getUserById(resultSet.getInt("member_id"), roles),
                        resultSet.getDate("membership_start").toLocalDate(),
                        resultSet.getDate("membership_end") != null ? resultSet.getDate("membership_end").toLocalDate() : null
                );

                memberships.add(membership);
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            String errorMessage = "Error while retrieving memberships for user with ID: " + memberId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return memberships;
    }

    /**
     * Updates an existing membership in the database.
     *
     * @param membershipId     The ID of the membership to update.
     * @param membershipTypeId The new membership type ID.
     * @param memberId         The new member ID.
     * @param startDate        The new start date of the membership.
     * @param endDate          The new end date of the membership (can be null).
     * @return The number of affected rows (should be 1 if successful, 0 if no such membership exists).
     */
    public static int updateMembership(int membershipId, int membershipTypeId, int memberId, LocalDate startDate, LocalDate endDate) {
        final String SQL = "UPDATE memberships SET membership_type_id = ?, member_id = ?, membership_start = ?, membership_end = ? WHERE membership_id = ?";
        int affectedRows = 0;

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setInt(1, membershipTypeId);
            preparedStatement.setInt(2, memberId);
            preparedStatement.setDate(3, Date.valueOf(startDate));
            preparedStatement.setDate(4, endDate != null ? Date.valueOf(endDate) : null);
            preparedStatement.setInt(5, membershipId);

            affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                LoggingManagement.log("No membership found with ID: " + membershipId, true);
            } else {
                LoggingManagement.log("Membership with ID: " + membershipId + " updated successfully.", false);
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            String errorMessage = "Error while updating the membership with ID: " + membershipId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return affectedRows;
    }

    /**
     * Deletes a membership from the database.
     *
     * @param membershipId The ID of the membership to delete.
     * @return The number of affected rows (should be 1 if successful, 0 if no such membership exists).
     */
    public static int deleteMembership(int membershipId) {
        final String SQL = "DELETE FROM memberships WHERE membership_id = ?";
        int affectedRows = 0;

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, membershipId);
            affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                LoggingManagement.log("No membership found with ID: " + membershipId, true);
            } else {
                LoggingManagement.log("Membership with ID: " + membershipId + " deleted successfully.", false);
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            String errorMessage = "Error while deleting the membership with ID: " + membershipId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return affectedRows;
    }
}
