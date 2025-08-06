package GymMerchandise;

public class MerchandiseTypes {
    private int id;
    private String merchandiseTypeName;

    public MerchandiseTypes(int id, String typeName) {
        this.id = id;
        this.merchandiseTypeName = typeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMerchandiseTypeName() {
        return merchandiseTypeName;
    }

    public void setMerchandiseTypeName(String typeName) {
        this.merchandiseTypeName = typeName;
    }
}
