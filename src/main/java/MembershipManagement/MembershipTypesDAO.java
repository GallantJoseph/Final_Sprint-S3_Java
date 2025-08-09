package MembershipManagement;

import DBManager.DatabaseConnection;
import Logging.LoggingManagement;
import Memberships.MembershipType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MembershipTypesDAO {

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

        } catch (SQLException e) {
            String errorMessage = "Error while adding the new membership type.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return -1; // Return -1 to indicate failure
    }

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
        } catch (SQLException e) {
            String errorMessage = "Error while retrieving the membership type with ID: " + membershipTypeId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return membershipType;
    }

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
        } catch (SQLException e) {
            String errorMessage = "Error while retrieving all membership types.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return membershipTypes;
    }

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

        } catch (SQLException e) {
            String errorMessage = "Error while updating the membership type with ID: " + membershipTypeId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return affectedRows;
    }

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

        } catch (SQLException e) {
            String errorMessage = "Error while deleting the membership type with ID: " + membershipTypeId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return affectedRows;
    }
}
