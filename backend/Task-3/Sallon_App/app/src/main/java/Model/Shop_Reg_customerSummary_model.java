package Model;

public class Shop_Reg_customerSummary_model {
    private String id;
    private String profileImg;
    private String name;
    private String phone;
    private String address;
    private int totalServices;


    public Shop_Reg_customerSummary_model(String id,String profileImg, String name, String phone, String address, int totalServices) {
        this.id =id;
        this.profileImg = profileImg;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.totalServices = totalServices;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalServices() {
        return totalServices;
    }

    public void setTotalServices(int totalServices) {
        this.totalServices = totalServices;
    }
}
