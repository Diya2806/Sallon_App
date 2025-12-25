package Model;

public class Shop_model {
    private String ownerName;
    private String shopName;
    private String phone;
    private String email;
    private String address;
    private String status;
    private String img;

    public Shop_model(String ownerName, String shopName, String phone, String email, String address, String status,String img) {
        this.ownerName = ownerName;
        this.shopName = shopName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.status = status;
        this.img =img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
