package Model;

public class Shop_Reg_service_model {
    private String serviceType;
    private String id;
    private String serviceSubType;
    private String imageResId;
    private String price;
    private String Title;

    public Shop_Reg_service_model(String id,String serviceType, String serviceSubType, String imageResId, String price, String title) {
        this.serviceType = serviceType;
        this.serviceSubType = serviceSubType;
        this.imageResId = imageResId;
        this.price = price;
        Title = title;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceSubType() {
        return serviceSubType;
    }

    public void setServiceSubType(String serviceSubType) {
        this.serviceSubType = serviceSubType;
    }

    public String getImageResId() {
        return imageResId;
    }

    public void setImageResId(String imageResId) {
        this.imageResId = imageResId;
    }
}
