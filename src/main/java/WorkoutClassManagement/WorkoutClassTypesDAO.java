package WorkoutClassManagement;

import DBManager.DatabaseConnection;
import Logging.LoggingManagement;
import WorkoutClasses.WorkoutClassType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WorkoutClassTypesDAO {
    public static int createWorkoutClassType(String name, String description){
        final String SQL = "INSERT INTO workout_class_types (name, description) VALUES (?,?)";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);

            preparedStatement.executeUpdate();

            // Retrieve the generated ID of the new workout class type
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                // Get the generated ID of the new workout class type
                int newWorkoutClassTypeId = generatedKeys.getInt(1);

                // Log the successful addition of the new workout class type
                LoggingManagement.log("New workout class type added with ID: " + newWorkoutClassTypeId + " and name: " + name, false);

                // Return the ID of the newly created workout class type
                return newWorkoutClassTypeId;
            } else {
                String errorMessage = "No ID was generated for the new workout class type.";

                System.out.println(errorMessage);
                LoggingManagement.log(errorMessage, true);
            }
        } catch (SQLException e) {
            String errorMessage = "Error while adding the new workout class type.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return -1; // Return -1 to indicate failure
    }

    public static WorkoutClassType getWorkoutClassType(int workoutClassTypeId) {
        WorkoutClassType workoutClassType = null;
        final String SQL = "SELECT * FROM workout_class_types WHERE workout_class_type_id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setInt(1, workoutClassTypeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                workoutClassType = new WorkoutClassType(
                        resultSet.getInt("workout_class_type_id"),
                        resultSet.getString("name"),
                        resultSet.getString("description")
                );
            } else {
                System.out.println("No workout class type found with ID: " + workoutClassTypeId);
            }
        } catch (SQLException e) {
            String errorMessage = "Error while retrieving the workout class type with ID: " + workoutClassTypeId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return workoutClassType;
    }

    public static ArrayList<WorkoutClassType> getAllWorkoutClassTypes() {
        ArrayList<WorkoutClassType> workoutClassTypes = new ArrayList<>();
        final String SQL = "SELECT * FROM workout_class_types";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                WorkoutClassType workoutClassType = new WorkoutClassType(
                    resultSet.getInt("workout_class_type_id"),
                    resultSet.getString("name"),
                    resultSet.getString("description")
                );

                workoutClassTypes.add(workoutClassType);
            }
        } catch (SQLException e) {
            String errorMessage = "Error while retrieving all workout class types.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return workoutClassTypes;
    }

    public static void updateWorkoutClassType(int workoutClassTypeId, String name, String description){
        final String SQL = "UPDATE workout_class_types SET name = ?, description = ? WHERE workout_class_type_id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setInt(1, workoutClassTypeId);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, description);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            String errorMessage = "Error while updating the workout class type with ID: " + workoutClassTypeId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }
    }

    public static int deleteWorkoutClassType(int workoutClassTypeId){
        final String SQL = "DELETE FROM workout_class_types WHERE workout_class_type_id = ?";
        int affectedRows = 0;

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setInt(1, workoutClassTypeId);

            affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                String errorMessage = "No workout class type found with ID: " + workoutClassTypeId;
                System.out.println(errorMessage);
                LoggingManagement.log(errorMessage, true);
            } else {
                LoggingManagement.log("Workout class type with ID: " + workoutClassTypeId + " deleted successfully.", false);
            }
        } catch (SQLException e) {
            String errorMessage = "Error while deleting the workout class type with ID: " + workoutClassTypeId + ". There might be workout classes associated with this type.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return affectedRows;
    }
}
