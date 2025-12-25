package Model;

public class Shop_Reg_Revenue_model {
    private String profile;
    private String customerName;
    private String dateTime;
    private String amount;
    private String status;

    public Shop_Reg_Revenue_model(String profile, String customerName, String dateTime, String amount,String status) {
        this.profile = profile;
        this.customerName = customerName;
        this.dateTime = dateTime;
        this.status =status;
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
