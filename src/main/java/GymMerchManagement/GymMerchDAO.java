package GymMerchManagement;

import DBManager.DatabaseConnection;
import GymMerchandise.GymMerchandise;
import GymMerchandise.MerchandiseType;
import Logging.LoggingManagement;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class is responsible for handling Gym Merchandise operations from the database.
 * It provides methods to create, retrieve, update, and delete GymMerchandise and MerchandiseTypes.
 */
public class GymMerchDAO {

    /**
     * Creates a new merchandise type in the database.
     * @param typeName the name of the merchandise type to be added
     */
    public static void createMerchandiseType(String typeName) {
        final String query = "INSERT INTO merchandise_types (merchandise_type_name) VALUES (?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, typeName);
            statement.executeUpdate();

            LoggingManagement.log("Merchandise type: " + typeName + " added successfully.", false);

        } catch (SQLException exception) {
            String errorMessage = "Something went wrong while adding a new merchandise type.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage+ ": " + exception.getMessage(), true);
        }
    }

    /**
     * Creates a new gym merchandise item in the database. (ex: gear, drinks, or food)
     * @param typeId the ID of the merchandise type
     * @param name the name of the merchandise
     * @param price the price of the merchandise
     * @param quantity the quantity of the merchandise in stock
     */
    public static void createGymMerchandise(int typeId, String name, double price, int quantity) {
        final String query = "INSERT INTO gym_merchandise (merchandise_type_id, merchandise_name, merchandise_price, quantity_in_stock) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, typeId);
            statement.setString(2, name);
            statement.setDouble(3, price);
            statement.setInt(4, quantity);
            statement.executeUpdate();

            LoggingManagement.log("Gym merchandise: " + name + " added successfully.", false);

        } catch (SQLException exception) {
            String errorMessage = "Something went wrong while adding a new gym merchandise item.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + exception.getMessage(), true);
        }
    }

    /**
     * Retrieves a merchandise type by its ID.
     * @param typeId the ID of the merchandise type to retrieve
     * @return MerchandiseType object if found, otherwise null
     */
    public static MerchandiseType getMerchandiseTypeById(int typeId) {
        MerchandiseType type = null;
        final String query = "SELECT * FROM merchandise_types WHERE merchandise_type_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, typeId);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                type = new MerchandiseType(
                        result.getInt("merchandise_type_id"),
                        result.getString("merchandise_type_name")
                );
            }

        } catch (SQLException exception) {
            String errorMessage = "Something went wrong while retrieving merchandise type with ID: " + typeId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + exception.getMessage(), true);
        }

        return type;
    }

    /**
     * Retrieves all gym merchandise items from the database.
     * @return an ArrayList of GymMerchandise objects
     */
    public static ArrayList<GymMerchandise> getAllGymMerchandise() {
        ArrayList<GymMerchandise> merchandiseList = new ArrayList<>();

        final String query =
                "SELECT g.merchandise_id, g.merchandise_name, g.merchandise_price, g.quantity_in_stock, " +
                        "t.merchandise_type_id, t.merchandise_type_name " +
                        "FROM gym_merchandise g " +
                        "JOIN merchandise_types t ON g.merchandise_type_id = t.merchandise_type_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                MerchandiseType type = new MerchandiseType(
                        result.getInt("merchandise_type_id"),
                        result.getString("merchandise_type_name")
                );

                GymMerchandise merchandise = new GymMerchandise(
                        result.getInt("merchandise_id"),
                        result.getString("merchandise_name"),
                        result.getDouble("merchandise_price"),
                        result.getInt("quantity_in_stock"),
                        type
                );

                merchandiseList.add(merchandise);
            }

        } catch (SQLException exception) {
            String errorMessage = "Something went wrong while getting all gym merchandise.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + exception.getMessage(), true);
        }

        return merchandiseList;
    }

    /**
     * Retrieves a gym merchandise item by its ID.
     * @param merchId the ID of the merchandise to retrieve
     * @return GymMerchandise object if found, otherwise null
     */
    public static GymMerchandise getGymMerchandiseById(int merchId) {
        GymMerchandise merchandise = null;

        final String query =
                "SELECT g.merchandise_id, g.merchandise_name, g.merchandise_price, g.quantity_in_stock, " +
                        "t.merchandise_type_id, t.merchandise_type_name " +
                        "FROM gym_merchandise g " +
                        "JOIN merchandise_types t ON g.merchandise_type_id = t.merchandise_type_id " +
                        "WHERE g.merchandise_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, merchId);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                MerchandiseType type = new MerchandiseType(
                        result.getInt("merchandise_type_id"),
                        result.getString("merchandise_type_name")
                );

                merchandise = new GymMerchandise(
                        result.getInt("merchandise_id"),
                        result.getString("merchandise_name"),
                        result.getDouble("merchandise_price"),
                        result.getInt("quantity_in_stock"),
                        type
                );
            }

        } catch (SQLException exception) {
            String errorMessage = "Something went wrong while getting gym merchandise with ID: " + merchId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + exception.getMessage(), true);
        }

        return merchandise;
    }

    /**
     * Updates an existing gym merchandise item in the database.
     * @param merch the GymMerchandise object containing updated details
     */
    public static void updateGymMerchandise(GymMerchandise merch) {
        final String query =
                "UPDATE gym_merchandise " +
                        "SET merchandise_name = ?, merchandise_price = ?, quantity_in_stock = ? " +
                        "WHERE merchandise_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, merch.getMerchandiseName());
            statement.setDouble(2, merch.getMerchandisePrice());
            statement.setInt(3, merch.getQuantityInStock());
            statement.setInt(4, merch.getId());

            int rowsChanged = statement.executeUpdate();

            if (rowsChanged == 0) {
                System.out.println("No merchandise was found with identification number " + merch.getId());
            } else {
                LoggingManagement.log("Gym merchandise with ID: " + merch.getId() + " updated successfully.", false);
            }

        } catch (SQLException exception) {
            String errorMessage = "Something went wrong while updating the gym merchandise with ID: " + merch.getId();

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + exception.getMessage(), true);
        }
    }

    /**
     * Deletes a gym merchandise item from the database by its ID.
     * @param merchId the ID of the merchandise to delete
     */
    public static void deleteGymMerchandise(int merchId) {
        final String query = "DELETE FROM gym_merchandise WHERE merchandise_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, merchId);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted == 0) {
                System.out.println("No merchandise was found with identification number " + merchId);
            } else {
                LoggingManagement.log("Gym merchandise with ID: " + merchId + " deleted successfully.", false);
            }

        } catch (SQLException exception) {
            String errorMessage = "Something went wrong while deleting gym merchandise with ID: " + merchId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + exception.getMessage(), true);
        }
    }

    /**
     * Calculates the total value of all merchandise in stock.
     * @return the total stock value as a double
     */
    public static double calculateTotalStockValue() {
        final String query = "SELECT SUM(merchandise_price * quantity_in_stock) AS total_value FROM gym_merchandise";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {

            if (result.next()) {
                return result.getDouble("total_value");
            }

        } catch (SQLException exception) {
            String errorMessage = "Something went wrong while calculating the total stock value.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + exception.getMessage(), true);
        }

        return 0.0;
    }

    /**
     * Retrieves all merchandise types from the database.
     * @return an ArrayList of MerchandiseType objects
     */
    public static ArrayList<MerchandiseType> getAllMerchandiseType() {
        ArrayList<MerchandiseType> types = new ArrayList<>();
        final String query = "SELECT * FROM merchandise_types";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                MerchandiseType type = new MerchandiseType(
                        result.getInt("merchandise_type_id"),
                        result.getString("merchandise_type_name")
                );
                types.add(type);
            }

        } catch (SQLException exception) {
            String errorMessage = "Something went wrong while getting all merchandise types.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + exception.getMessage(), true);
        }

        return types;
    }

    /**
     * Deletes a merchandise type from the database by its ID.
     * @param typeId the ID of the merchandise type to delete
     */
    public static void deleteMerchandiseType(int typeId) {
        final String query = "DELETE FROM merchandise_types WHERE merchandise_type_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, typeId);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted == 0) {
                System.out.println("No merchandise type found with identification number " + typeId);
            } else {
                LoggingManagement.log("Merchandise type with ID: " + typeId + " deleted successfully.", false);
            }

        } catch (SQLException exception) {
            String errorMessage = "Something went wrong while deleting merchandise type with ID: " + typeId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + exception.getMessage(), true);
        }
    }
}
