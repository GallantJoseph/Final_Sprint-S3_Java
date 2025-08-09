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

    public static void createMembershipType(String name, String description, double cost) {
        final String SQL = "INSERT INTO membership_types (mem_type_name, mem_type_description, mem_type_cost) VALUES (?, ?, ?)";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, cost);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while adding the new membership type.");
            LoggingManagement.log(e.getMessage(), true);
        }
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
            System.out.println("Error while retrieving the membership type.");
            LoggingManagement.log(e.getMessage(), true);
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
            System.out.println("Error while retrieving the membership types.");
            LoggingManagement.log(e.getMessage(), true);
        }

        return membershipTypes;
    }

    public static void updateMembershipType(int membershipTypeId, String name, String description, double cost) {
        final String SQL = "UPDATE membership_types SET mem_type_name = ?, mem_type_description = ?, mem_type_cost = ? WHERE mem_type_id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, cost);
            preparedStatement.setInt(4, membershipTypeId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while updating the membership type.");
            LoggingManagement.log(e.getMessage(), true);
        }
    }

    public static void deleteMembershipType(int membershipTypeId) {
        final String SQL = "DELETE FROM membership_types WHERE mem_type_id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setInt(1, membershipTypeId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while deleting the membership type. There might be memberships associated with this type.");
            LoggingManagement.log(e.getMessage(), true);
        }
    }
}
