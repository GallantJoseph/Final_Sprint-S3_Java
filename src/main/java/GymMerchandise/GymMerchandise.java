package GymMerchandise;

/**
 * GymMerchandise class.
 * This class is responsible for handling GymMerchandise objects in the system.
 */
public class GymMerchandise {
    private int id;
    private String merchandiseName;
    private double merchandisePrice;
    private int quantityInStock;
    private MerchandiseType merchandiseType;

    /**
     * Constructs a GymMerchandise with the specified details.
     * @param id the unique identifier of the merchandise
     * @param merchandiseName the name of the merchandise
     * @param merchandisePrice the price of the merchandise
     * @param quantityInStock the quantity of the merchandise in stock
     * @param merchandiseType the type of the merchandise
     */
    public GymMerchandise(int id, String merchandiseName, double merchandisePrice, int quantityInStock, MerchandiseType merchandiseType) {
        this.id = id;
        this.merchandiseName = merchandiseName;
        this.merchandisePrice = merchandisePrice;
        this.quantityInStock = quantityInStock;
        this.merchandiseType = merchandiseType;
    }

    /**
     * Gets the unique identifier of the merchandise.
     * @return the unique identifier of the merchandise
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the merchandise.
     * @param id the unique identifier to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the merchandise.
     * @return the name of the merchandise
     */
    public String getMerchandiseName() {
        return merchandiseName;
    }

    /**
     * Sets the name of the merchandise.
     * @param name the name to set
     */
    public void setMerchandiseName(String name) {
        this.merchandiseName = name;
    }

    /**
     * Gets the price of the merchandise.
     * @return the price of the merchandise
     */
    public double getMerchandisePrice() {
        return merchandisePrice;
    }

    /**
     * Sets the price of the merchandise.
     * @param merchandisePrice the price to set
     */
    public void setMerchandisePrice(double merchandisePrice) {
        this.merchandisePrice = merchandisePrice;
    }

    /**
     * Gets the quantity of the merchandise in stock.
     * @return the quantity of the merchandise in stock
     */
    public int getQuantityInStock() {
        return quantityInStock;
    }

    /**
     * Sets the quantity of the merchandise in stock.
     * @param quantityInStock the quantity to set
     */
    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    /**
     * Gets the type of the merchandise.
     * @return the type of the merchandise
     */
    public MerchandiseType getMerchandiseType() {
        return merchandiseType;
    }


    public void setMerchandiseType(MerchandiseType merchandiseType) {
        this.merchandiseType = merchandiseType;
    }
}
