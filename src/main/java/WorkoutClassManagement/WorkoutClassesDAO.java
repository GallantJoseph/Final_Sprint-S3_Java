package WorkoutClassManagement;

import DBManager.DatabaseConnection;
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

public class WorkoutClassesDAO {
    public static void createWorkoutClass(int workoutClassTypeId, String workoutClassDesc, int trainerId) {
        final String SQL = "INSERT INTO workout_classes (workout_class_type_id, workout_class_desc, trainer_id, workout_class_datetime) VALUES (?, ?, ?, ?)";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setInt(1, workoutClassTypeId);
            preparedStatement.setString(2, workoutClassDesc);
            preparedStatement.setInt(3, trainerId);
            preparedStatement.setObject(4, LocalDateTime.now());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while adding the new workout class.");
            // TODO Log the error
            e.printStackTrace();
        }
    }

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
                    WorkoutClassTypesDAO.getWorkoutClassType(resultSet.getInt("workout_class_type_id")),
                    resultSet.getString("workout_class_desc"),
                    (Trainer) UserDAO.getUserById(resultSet.getInt("trainer_id"), roles),
                     LocalDateTime.now()
                );

                // (LocalDateTime) resultSet.getString("workout_class_datetime")
            } else {
                System.out.println("No workout class found with ID: " + workoutClassId);
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving the workout class.");
            // TODO Log the error
            e.printStackTrace();
        }

        return workoutClass;
    }

    /**
     *
     * @param trainerId use -1 to get all the workout classes
     * @param roles
     * @return
     */
    public static ArrayList<WorkoutClass> getWorkoutClasses(int trainerId, ArrayList<Role> roles){
        ArrayList<WorkoutClass> workoutClasses = new ArrayList<>();
        String sql = "SELECT * FROM workout_classes";

        // Add the condition if the trainerId is not -1
        if (trainerId != -1){
            sql += " WHERE trainer_id = ?";
        }

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Only add the parameter if the trainerId is not -1
            if (trainerId != -1) {
                preparedStatement.setInt(1, trainerId);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // TODO Fix datetime format
                WorkoutClass workoutClass = new WorkoutClass(
                        resultSet.getInt("workout_class_id"),
                        WorkoutClassTypesDAO.getWorkoutClassType(resultSet.getInt("workout_class_type_id")),
                        resultSet.getString("workout_class_desc"),
                        new Trainer(UserDAO.getUserById(resultSet.getInt("trainer_id"), roles)),
                        LocalDateTime.now()
                );

                //(LocalDateTime) resultSet.getObject("workout_class_datetime")

                workoutClasses.add(workoutClass);
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving the workout classes.");
            // TODO Log the error
            e.printStackTrace();
        }

        return workoutClasses;
    }

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
            System.out.println("Error while updating the workout class.");
            // TODO Log the error
            e.printStackTrace();
        }
    }

    public static void deleteWorkoutClass(int workoutClassId) {
        final String SQL = "DELETE FROM workout_classes WHERE workout_class_id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setInt(1, workoutClassId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while deleting the workout class.");
            // TODO Log the error
            e.printStackTrace();
        }
    }
}
