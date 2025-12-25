package Model;

public class Report_model {

    private String shopId;
    private String shopName;
    private String ownerName;
    private String totalCustomers;
    private String totalServices;
    private String totalRevenue;
    private String monthYear;
    private String img;

    public Report_model(String shopId, String shopName, String ownerName, String totalCustomers, String totalServices, String totalRevenue, String monthYear,String img) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.ownerName = ownerName;
        this.totalCustomers = totalCustomers;
        this.totalServices = totalServices;
        this.totalRevenue = totalRevenue;
        this.monthYear = monthYear;
        this.img =img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(String totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public String getTotalServices() {
        return totalServices;
    }

    public void setTotalServices(String totalServices) {
        this.totalServices = totalServices;
    }

    public String getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(String totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }
}
