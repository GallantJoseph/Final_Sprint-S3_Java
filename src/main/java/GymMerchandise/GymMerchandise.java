package GymMerchandise;

public class GymMerchandise {
    private int id;
    private String merchandiseName;
    private double merchandisePrice;
    private int quantityInStock;
    private MerchandiseTypes merchandiseType;

    public GymMerchandise(int id, String merchandiseName, double merchandisePrice, int quantityInStock, MerchandiseTypes merchandiseType) {
        this.id = id;
        this.merchandiseName = merchandiseName;
        this.merchandisePrice = merchandisePrice;
        this.quantityInStock = quantityInStock;
        this.merchandiseType = merchandiseType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMerchandiseName() {
        return merchandiseName;
    }

    public void setMerchandiseName(String name) {
        this.merchandiseName = name;
    }

    public double getMerchandisePrice() {
        return merchandisePrice;
    }

    public void setMerchandisePrice(double merchandisePrice) {
        this.merchandisePrice = merchandisePrice;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public MerchandiseTypes getMerchandiseType() {
        return merchandiseType;
    }

    public void setMerchandiseType(MerchandiseTypes merchandiseType) {
        this.merchandiseType = merchandiseType;
    }
}
