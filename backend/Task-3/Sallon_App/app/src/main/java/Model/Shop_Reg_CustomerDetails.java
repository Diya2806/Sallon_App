package Model;

public class Shop_Reg_CustomerDetails {

    private String date;
    private String serviceName;
    private double price;

    public Shop_Reg_CustomerDetails(String date, String serviceName, double price) {
        this.date = date;
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
