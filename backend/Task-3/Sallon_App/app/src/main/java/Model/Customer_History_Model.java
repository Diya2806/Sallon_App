package Model;

public class Customer_History_Model {
    private String shopName;
    private String service;
    private String date;
    private String time;
    private String status;
    private String shopId;

    public Customer_History_Model(String shopName, String service, String date, String time, String status, String shopId) {
        this.shopName = shopName;
        this.service = service;
        this.date = date;
        this.time = time;
        this.status = status;
        this.shopId =shopId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
