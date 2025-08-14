package GymMerchandise;

/* * MerchandiseType class.
 * This class represents a type of GymMerchandise in the system.
 */
public class MerchandiseType {
    private int id;
    private String merchandiseTypeName;

    /**
     * Constructs a MerchandiseType with the specified id and type name.
     * @param id the unique identifier for the merchandise type
     * @param merchandiseTypeName the name of the merchandise type
     */
    public MerchandiseType(int id, String merchandiseTypeName) {
        this.id = id;
        this.merchandiseTypeName = merchandiseTypeName;
    }

    /** gets the unique identifier of the merchandise type.
     * @return the unique identifier of the merchandise type
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the merchandise type.
     * @param id the unique identifier to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the merchandise type.
     * @return the name of the merchandise type
     */
    public String getMerchandiseTypeName() {
        return merchandiseTypeName;
    }

    /**
     * Sets the name of the merchandise type.
     * @param typeName the name to set for the merchandise type
     */
    public void setMerchandiseTypeName(String typeName) {
        this.merchandiseTypeName = typeName;
    }
}
