package Model;

public class ShopOwner_model {
    private String shopName;
    private String ownerName;
    private String mobile ;
    private String email;
    private String address ;
    private String password;
    private String state;
    private String city;
    private String area;
    private String idProof;
    private String shopPhoto;
    private String ownerPhoto;
    private String status;

    public ShopOwner_model(String shopName, String ownerName, String mobile, String email, String address, String password, String state, String city, String area, String idProof, String shopPhoto, String ownerPhoto, String status) {
        this.shopName = shopName;
        this.ownerName = ownerName;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.password = password;
        this.state = state;
        this.city = city;
        this.area = area;
        this.idProof = idProof;
        this.shopPhoto = shopPhoto;
        this.ownerPhoto = ownerPhoto;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getIdProof() {
        return idProof;
    }

    public void setIdProof(String idProof) {
        this.idProof = idProof;
    }

    public String getShopPhoto() {
        return shopPhoto;
    }

    public void setShopPhoto(String shopPhoto) {
        this.shopPhoto = shopPhoto;
    }

    public String getOwnerPhoto() {
        return ownerPhoto;
    }

    public void setOwnerPhoto(String ownerPhoto) {
        this.ownerPhoto = ownerPhoto;
    }
}
