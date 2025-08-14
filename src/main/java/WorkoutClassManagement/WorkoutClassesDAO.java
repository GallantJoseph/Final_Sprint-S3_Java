package WorkoutClassManagement;

import DBManager.DatabaseConnection;
import Logging.LoggingManagement;
import UserManagement.UserDAO;
import Users.Role;
import Users.Trainer;
import WorkoutClasses.WorkoutClass;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * WorkoutClassesDAO class.
 * This class is responsible for handling Workout Class database operations in the system.
 */
public class WorkoutClassesDAO {
    /**
     * Creates a new workout class in the database.
     *
     * @param workoutClassTypeId the ID of the workout class type
     * @param workoutClassDesc   the description of the workout class
     * @param trainerId          the ID of the trainer conducting the class
     * @param workoutClassDatetime the date and time of the workout class
     * @return the ID of the newly created workout class, or -1 if an error occurs
     */
    public static int createWorkoutClass(int workoutClassTypeId, String workoutClassDesc, int trainerId, LocalDateTime workoutClassDatetime) {
        final String SQL = "INSERT INTO workout_classes (workout_class_type_id, workout_class_desc, trainer_id, workout_class_datetime) VALUES (?, ?, ?, ?)";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, workoutClassTypeId);
            preparedStatement.setString(2, workoutClassDesc);
            preparedStatement.setInt(3, trainerId);
            preparedStatement.setObject(4, workoutClassDatetime);

            preparedStatement.executeUpdate();

            if (preparedStatement.getGeneratedKeys().next()) {
                // Retrieve the generated ID of the new workout class
                int workoutClassId = preparedStatement.getGeneratedKeys().getInt(1);

                // Log the successful addition of the new workout class
                LoggingManagement.log("New workout class added with ID: " + workoutClassId + ", Type ID: " + workoutClassTypeId + ", Description: " + workoutClassDesc, false);

                // Return the ID of the newly created workout class
                return workoutClassId;
            } else {
                String errorMessage = "No ID was generated for the new workout class.";
                System.out.println(errorMessage);
                LoggingManagement.log(errorMessage, true);
            }
        } catch (SQLException e) {
            String errorMessage = "Error while adding the new workout class.";
            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return -1; // Return -1 to indicate failure
    }

    /**
     * Retrieves a workout class by its ID.
     *
     * @param workoutClassId the ID of the workout class to retrieve
     * @param roles          the list of roles for user permissions
     * @return the WorkoutClass object if found, or null if not found or an error occurs
     */
    public static WorkoutClass getWorkoutClassById(int workoutClassId, ArrayList<Role> roles) {
        WorkoutClass workoutClass = null;
        final String SQL = "SELECT * FROM workout_classes WHERE workout_class_id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, workoutClassId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                workoutClass = new WorkoutClass(
                    resultSet.getInt("workout_class_id"),
                    WorkoutClassTypeDAO.getWorkoutClassType(resultSet.getInt("workout_class_type_id")),
                    resultSet.getString("workout_class_desc"),
                    new Trainer(UserDAO.getUserById(resultSet.getInt("trainer_id"), roles)),
                    LocalDateTime.now()
                );

                // (LocalDateTime) resultSet.getString("workout_class_datetime")
            }
        } catch (SQLException e) {
            String errorMessage = "Error while retrieving the workout class with ID: " + workoutClassId;
            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return workoutClass;
    }

    /**
     * Retrieves all workout classes, optionally filtered by trainer ID.
     *
     * @param trainerId the ID of the trainer to filter by, or -1 to retrieve all classes
     * @param roles     the list of roles for user permissions
     * @return an ArrayList of WorkoutClass objects
     */
    public static ArrayList<WorkoutClass> getWorkoutClasses(int trainerId, ArrayList<Role> roles){
        ArrayList<WorkoutClass> workoutClasses = new ArrayList<>();
        String sql = "SELECT * FROM workout_classes";

        // Add the condition if the trainerId is not -1
        if (trainerId != -1){
            sql += " WHERE trainer_id = ?";
        }

        sql += " ORDER BY workout_class_datetime"; // Order by datetime

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Only add the parameter if the trainerId is not -1
            if (trainerId != -1) {
                preparedStatement.setInt(1, trainerId);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                WorkoutClass workoutClass = new WorkoutClass(
                        resultSet.getInt("workout_class_id"),
                        WorkoutClassTypeDAO.getWorkoutClassType(resultSet.getInt("workout_class_type_id")),
                        resultSet.getString("workout_class_desc"),
                        new Trainer(UserDAO.getUserById(resultSet.getInt("trainer_id"), roles)),
                        resultSet.getTimestamp("workout_class_datetime").toLocalDateTime()
                );

                workoutClasses.add(workoutClass);
            }
        } catch (SQLException e) {
            String errorMessage = "Error while retrieving the workout classes.";

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return workoutClasses;
    }

    /**
     * Updates an existing workout class in the database.
     *
     * @param workoutClassId      the ID of the workout class to update
     * @param workoutClassTypeId  the new ID of the workout class type
     * @param workoutClassDesc    the new description of the workout class
     * @param trainerId           the new ID of the trainer conducting the class
     * @param workoutClassDatetime the new date and time of the workout class
     */
    public static void updateWorkoutClass(int workoutClassId, int workoutClassTypeId, String workoutClassDesc, int trainerId, LocalDateTime workoutClassDatetime) {
        final String SQL = "UPDATE workout_classes SET workout_class_type_id = ?, workout_class_desc = ?, trainer_id = ?, workout_class_datetime = ? WHERE workout_class_id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setInt(1, workoutClassTypeId);
            preparedStatement.setString(2, workoutClassDesc);
            preparedStatement.setInt(3, trainerId);
            preparedStatement.setObject(4, workoutClassDatetime);
            preparedStatement.setInt(5, workoutClassId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            String errorMessage = "Error while updating the workout class with ID: " + workoutClassId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }
    }

    /**
     * Deletes a workout class from the database by its ID.
     *
     * @param workoutClassId the ID of the workout class to delete
     * @return the number of affected rows, or 0 if no class was found with the given ID
     */
    public static int deleteWorkoutClass(int workoutClassId) {
        final String SQL = "DELETE FROM workout_classes WHERE workout_class_id = ?";
        int affectedRows = 0;

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setInt(1, workoutClassId);

            affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                String errorMessage = "No workout class found with ID: " + workoutClassId;
                System.out.println(errorMessage);
                LoggingManagement.log(errorMessage, true);
            } else {
                LoggingManagement.log("Workout class with ID: " + workoutClassId + " deleted successfully.", false);
            }
        } catch (SQLException e) {
            String errorMessage = "Error while deleting the workout class with ID: " + workoutClassId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return affectedRows;
    }

    /**
     * Deletes all workout classes associated with a specific trainer ID.
     *
     * @param trainerId the ID of the trainer whose workout classes should be deleted
     * @return the number of affected rows, or 0 if no classes were found for the given trainer ID
     */
    public static int deleteWorkoutClassByTrainerId(int trainerId) {
        final String SQL = "DELETE FROM workout_classes WHERE trainer_id = ?";
        int affectedRows = 0;

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setInt(1, trainerId);

            affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                String errorMessage = "No workout classes found for trainer with ID: " + trainerId;
                System.out.println(errorMessage);
                LoggingManagement.log(errorMessage, true);
            } else {
                LoggingManagement.log("Workout classes for trainer with ID: " + trainerId + " deleted successfully.", false);
            }
        } catch (SQLException e) {
            String errorMessage = "Error while deleting workout classes for trainer with ID: " + trainerId;

            System.out.println(errorMessage);
            LoggingManagement.log(errorMessage + ": " + e.getMessage(), true);
        }

        return affectedRows;
    }
}
