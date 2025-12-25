package Model;

public class ServiceType_model {
    private String id;
    private String name;
    private String status;
    private String entry;

    public ServiceType_model(String id, String name, String status,String entry) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.entry = entry;
    }

    public String getEntry() {
        return entry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
