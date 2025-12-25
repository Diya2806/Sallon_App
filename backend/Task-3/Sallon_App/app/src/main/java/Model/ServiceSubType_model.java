package Model;

public class ServiceSubType_model {
    private String id;
    private String mainService;
    private String subServiceName;
    private String status;
    private String entry;



    public ServiceSubType_model(String id, String mainService, String subServiceName, String status, String entry) {
        this.id = id;
        this.mainService = mainService;
        this.subServiceName = subServiceName;
        this.status = status;
        this.entry = entry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getMainService() {
        return mainService;
    }

    public void setMainService(String mainService) {
        this.mainService = mainService;
    }

    public String getSubServiceName() {
        return subServiceName;
    }

    public void setSubServiceName(String subServiceName) {
        this.subServiceName = subServiceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
