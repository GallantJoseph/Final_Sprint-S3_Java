package WorkoutClasses;

/**
 * Represents a type of workout class in the system.
 * This class contains details about the workout class type such as id, name, and description.
 */
public class WorkoutClassType {
    private int id;
    private String name;
    private String description;

    /**
     * Constructs a WorkoutClassType with the specified id, name, and description.
     *
     * @param id          the unique identifier for the workout class type
     * @param name        the name of the workout class type
     * @param description a description of the workout class type
     */
    public WorkoutClassType(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the unique identifier for the workout class type.
     * @return the unique identifier of the workout class type
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the workout class type.
     * @param id the unique identifier to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the workout class type.
     * @return the name of the workout class type
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the workout class type.
     * @param name the name to set for the workout class type
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the workout class type.
     * @return the description of the workout class type
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the workout class type.
     * @param description the description to set for the workout class type
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
