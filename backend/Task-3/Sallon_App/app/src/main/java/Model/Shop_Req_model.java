package Model;

public class Shop_Req_model {
    private String id;

    private String shopname;
    private String ownername;
    private String contact;
    private String email;
    private String address;
    private String status;
    private String idProof;
    private String shopImg;
    private String ownerImg;

    public Shop_Req_model(String id,String shopname, String ownername, String contact, String email, String address, String status, String idProof, String shopImg, String ownerImg) {
        this.id = id;
        this.shopname = shopname;
        this.ownername = ownername;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.status = status;
        this.idProof = idProof;
        this.shopImg = shopImg;
        this.ownerImg = ownerImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProof() {
        return idProof;
    }

    public void setIdProof(String idProof) {
        this.idProof = idProof;
    }

    public String getShopImg() {
        return shopImg;
    }

    public void setShopImg(String shopImg) {
        this.shopImg = shopImg;
    }

    public String getOwnerImg() {
        return ownerImg;
    }

    public void setOwnerImg(String ownerImg) {
        this.ownerImg = ownerImg;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
