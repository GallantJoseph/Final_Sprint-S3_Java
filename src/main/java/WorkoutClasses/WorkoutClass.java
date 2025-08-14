package WorkoutClasses;

import Users.Trainer;

import java.time.LocalDateTime;

/**
 * Represents a workout class in the system.
 * This class contains details about the workout class such as type, description, trainer, and date/time.
 */
public class WorkoutClass {
    int id;
    private WorkoutClassType workoutClassType;
    private String description;
    private Trainer trainer;
    private LocalDateTime dateTime;

    /**
     * Constructs a WorkoutClass with the specified details.
     *
     * @param id                the unique identifier for the workout class
     * @param workoutClassType  the type of the workout class
     * @param description       a description of the workout class
     * @param trainer           the trainer conducting the workout class
     * @param dateTime          the date and time of the workout class
     */
    public WorkoutClass(int id, WorkoutClassType workoutClassType, String description, Trainer trainer, LocalDateTime dateTime) {
        this.id = id;
        this.workoutClassType = workoutClassType;
        this.description = description;
        this.trainer = trainer;
        this.dateTime = dateTime;
    }

    /**
     * Gets the unique identifier for the workout class.
     * @return the unique identifier of the workout class
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the workout class.
     * @param id the unique identifier to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the type of the workout class.
     * @return the type of the workout class
     */
    public WorkoutClassType getWorkoutClassType() {
        return workoutClassType;
    }

    /**
     * Sets the type of the workout class.
     * @param workoutClassType the type to set for the workout class
     */
    public void setWorkoutClassType(WorkoutClassType workoutClassType) {
        this.workoutClassType = workoutClassType;
    }

    /**
     * Gets the description of the workout class.
     * @return the description of the workout class
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the workout class.
     * @param description the description to set for the workout class
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the trainer conducting the workout class.
     * @return the trainer of the workout class
     */
    public Trainer getTrainer() {
        return trainer;
    }

    /**
     * Sets the trainer conducting the workout class.
     * @param trainer the trainer to set for the workout class
     */
    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    /**
     * Gets the date and time of the workout class.
     * @return the date and time of the workout class
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Sets the date and time of the workout class.
     * @param dateTime the date and time to set for the workout class
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}


