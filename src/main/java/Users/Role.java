package Users;

/**
 * Represents a Role in the system.
 * This class is responsible for handling role-related operations in the system.
 */
public class Role {
    private int id;
    private String name;

    /**
     * Constructs a Role with the specified id and name.
     * @param id the unique identifier for the role
     * @param name the name of the role
     */
    public Role(int id, String name){
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the unique identifier for the role.
     * @return the id of the role
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the role.
     * @param id the id to set for the role
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the role.
     * @return the name of the role
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the role.
     * @param name the name to set for the role
     */
    public void setName(String name) {
        this.name = name;
    }
}
