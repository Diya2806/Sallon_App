package Model;

public class Customer_Shop_Details_model {

    private String shopName;
    private String shopDetails;
    private float rating;
    private String shopImg;
    private String OwnerName;
    private String shopId;

    public Customer_Shop_Details_model(String shopId,String OwnerName,String shopName, String shopDetails, float rating, String shopImg) {
        this.shopName = shopName;
        this.shopDetails = shopDetails;
        this.rating = rating;
        this.shopImg = shopImg;
        this.OwnerName = OwnerName;
        this.shopId = shopId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopDetails() {
        return shopDetails;
    }

    public void setShopDetails(String shopDetails) {
        this.shopDetails = shopDetails;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getShopImg() {
        return shopImg;
    }

    public void setShopImg(String shopImg) {
        this.shopImg = shopImg;
    }
}
