package Memberships;

/**
 * Represents a type of membership in the gym management system.
 * Contains details about the membership type such as ID, name, description, and cost.
 */
public class MembershipType {
    private int id;
    private String name;
    private String description;
    private double cost;

    /**
     * Constructs a MembershipType with the specified details.
     *
     * @param id          the unique identifier for the membership type
     * @param name        the name of the membership type
     * @param description a brief description of the membership type
     * @param cost        the cost associated with the membership type
     */
    public MembershipType(int id, String name, String description, double cost) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
    }

    /**
     * Gets the unique identifier of the membership type.
     * @return the unique identifier of the membership type
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the membership type.
     * @return the name of the membership type
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the membership type.
     * @return the description of the membership type
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the cost of the membership type.
     * @return the cost of the membership type
     */
    public double getCost() {
        return cost;
    }

    /**
     * Sets the unique identifier of the membership type.
     * @param id the unique identifier to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the name of the membership type.
     * @param name the name to set for the membership type
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the description of the membership type.
     * @param description the description to set for the membership type
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the cost of the membership type.
     * @param cost the cost to set for the membership type
     */
    public void setCost(double cost) {
        this.cost = cost;
    }
}
