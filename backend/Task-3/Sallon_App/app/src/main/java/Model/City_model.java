package Model;

public class City_model {
    private String statename;
    private String cityname;

    public City_model(String statename, String cityname) {
        this.statename = statename;
        this.cityname = cityname;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }
}
