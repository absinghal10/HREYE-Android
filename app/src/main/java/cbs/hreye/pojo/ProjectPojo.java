package cbs.hreye.pojo;

public class ProjectPojo{

    private String CUSTOMER_ID;
    private String CUSTOMER_NAME;
    private String PROJECT_ID;
    private String PROJECT_NAME;

    public ProjectPojo(String CUSTOMER_NAME, String CUSTOMER_ID, String PROJECT_NAME, String PROJECT_ID) {
        this.CUSTOMER_NAME = CUSTOMER_NAME;
        this.CUSTOMER_ID = CUSTOMER_ID;
        this.PROJECT_NAME = PROJECT_NAME;
        this.PROJECT_ID = PROJECT_ID;
    }

    public ProjectPojo() {
    }

    public String getCUSTOMER_ID() {
        return CUSTOMER_ID;
    }

    public void setCUSTOMER_ID(String CUSTOMER_ID) {
        this.CUSTOMER_ID = CUSTOMER_ID;
    }

    public String getCUSTOMER_NAME() {
        return CUSTOMER_NAME;
    }

    public void setCUSTOMER_NAME(String CUSTOMER_NAME) {
        this.CUSTOMER_NAME = CUSTOMER_NAME;
    }

    public String getPROJECT_ID() {
        return PROJECT_ID;
    }

    public void setPROJECT_ID(String PROJECT_ID) {
        this.PROJECT_ID = PROJECT_ID;
    }

    public String getPROJECT_NAME() {
        return PROJECT_NAME;
    }

    public void setPROJECT_NAME(String PROJECT_NAME) {
        this.PROJECT_NAME = PROJECT_NAME;
    }
}
