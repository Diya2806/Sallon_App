package Model;

public class Shop_Booking_model {
    int profile;
    String name;
    String number;
    String time;
    String status;

    public Shop_Booking_model(int profile, String name, String number, String time, String status) {
        this.profile = profile;
        this.name = name;
        this.number = number;
        this.time = time;
        this.status = status;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
