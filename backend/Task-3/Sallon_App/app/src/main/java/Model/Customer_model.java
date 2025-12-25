package Model;

public class Customer_model {
    private String customerName;
    private String phone;
    private String email;
    private String address;
    private String img;

    public Customer_model(String customerName, String phone, String email, String address, String img) {
        this.customerName = customerName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.img = img;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
