package Model;

public class Booking_model {
    private String id;
    private String ownerName;
    private String img;
    private String shopName;
    private String status;
    private  String number;
    private String address;



    public Booking_model(String id,String ownerName, String shopName, String img, String status,String number,String address) {
        this.ownerName = ownerName;
        this.id = id;

        this.shopName = shopName;

        this.status = status;

        this.img = img;
        this.number = number;
        this.address =address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
