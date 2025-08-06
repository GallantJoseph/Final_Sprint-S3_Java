package MembershipManagement;

import DBManager.DatabaseConnection;
import Memberships.Membership;
import UserManagement.UserDAO;
import Users.Role;
import Users.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class MembershipsDAO {

    public static void createMembership(int membershipTypeId, int memberId, LocalDate startDate, LocalDate endDate) {
        final String SQL = "INSERT INTO memberships (membership_type_id, member_id, membership_start, membership_end) VALUES (?, ?, ?, ?)";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setInt(1, membershipTypeId);
            preparedStatement.setInt(2, memberId);
            preparedStatement.setDate(3, Date.valueOf(startDate));
            preparedStatement.setDate(4, endDate != null ? Date.valueOf(endDate) : null);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while adding the new membership.");
            e.printStackTrace();
        }
    }

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
        } catch (SQLException e) {
            System.out.println("Error while retrieving the membership.");
            e.printStackTrace();
        }

        return membership;
    }

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
        } catch (SQLException e) {
            System.out.println("Error while retrieving memberships.");
            e.printStackTrace();
        }

        return memberships;
    }

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
        } catch (SQLException e) {
            System.out.println("Error while retrieving memberships for user.");
            e.printStackTrace();
        }

        return memberships;
    }

    public static void updateMembership(int membershipId, int membershipTypeId, int memberId, LocalDate startDate, LocalDate endDate) {
        final String SQL = "UPDATE memberships SET membership_type_id = ?, member_id = ?, membership_start = ?, membership_end = ? WHERE membership_id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setInt(1, membershipTypeId);
            preparedStatement.setInt(2, memberId);
            preparedStatement.setDate(3, Date.valueOf(startDate));
            preparedStatement.setDate(4, endDate != null ? Date.valueOf(endDate) : null);
            preparedStatement.setInt(5, membershipId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while updating membership.");
            e.printStackTrace();
        }
    }

    public static void deleteMembership(int membershipId) {
        final String SQL = "DELETE FROM memberships WHERE membership_id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, membershipId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while deleting membership.");
            e.printStackTrace();
        }
    }
}
