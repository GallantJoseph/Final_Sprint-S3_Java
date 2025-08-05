package WorkoutClasses;

import Users.Trainer;

import java.time.LocalDateTime;

public class WorkoutClass {
    int id;
    private WorkoutClassType workoutClassType;
    private String description;
    private Trainer trainer;
    private LocalDateTime dateTime;

    public WorkoutClass(int id, WorkoutClassType workoutClassType, String description, Trainer trainer, LocalDateTime dateTime) {
        this.id = id;
        this.workoutClassType = workoutClassType;
        this.description = description;
        this.trainer = trainer;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WorkoutClassType getWorkoutClassType() {
        return workoutClassType;
    }

    public void setWorkoutClassType(WorkoutClassType workoutClassType) {
        this.workoutClassType = workoutClassType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}


