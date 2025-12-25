package Model;

public class Revenue_model {
    private String ownerName;
    private String shopName;
    private String todayRevenue;
    private String monthlyRevenue;
    private String img;

    public Revenue_model(String ownerName, String shopName, String todayRevenue, String monthlyRevenue,String img) {
        this.ownerName = ownerName;
        this.shopName = shopName;
        this.todayRevenue = todayRevenue;
        this.monthlyRevenue = monthlyRevenue;
        this.img = img;
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

    public String getTodayRevenue() {
        return todayRevenue;
    }

    public void setTodayRevenue(String todayRevenue) {
        this.todayRevenue = todayRevenue;
    }

    public String getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(String monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }
}
