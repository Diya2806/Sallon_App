package Model;

public class Appointment_model {
    private String id;
    private String customerName;
    private String serviceName;
    private String servicesub;
    private String time;
    private String imageUrl;
    private String status;
    private String number;
    private String title;

    public Appointment_model(String id,String customerName, String serviceName,String servicesub, String time, String imageUrl, String status,String number,String title) {
        this.customerName = customerName;
        this.serviceName = serviceName;
        this.time = time;
        this.imageUrl = imageUrl;
        this.status = status;
        this.number = number;
        this.id = id;
        this.servicesub = servicesub;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServicesub() {
        return servicesub;
    }

    public void setServicesub(String servicesub) {
        this.servicesub = servicesub;
    }
}
