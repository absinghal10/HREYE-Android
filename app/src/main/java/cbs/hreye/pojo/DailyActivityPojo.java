package cbs.hreye.pojo;

public class DailyActivityPojo {

    private String custom_name;
    private String custom_id;
    private String activity;
    private String activity_id;
    private String project_id;
    private String activity_det;
    private String hours;
    private String status;
    private String statusCheck = "";
    private String remarks_one;
    private String remarks_two;
    private String TransactionNo;


    public String getCustom_name() {
        return custom_name;
    }

    public void setCustom_name(String custom_name) {
        this.custom_name = custom_name;
    }

    public String getCustom_id() {
        return custom_id;
    }

    public void setCustom_id(String custom_id) {
        this.custom_id = custom_id;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_det() {
        return activity_det;
    }

    public void setActivity_det(String activity_det) {
        this.activity_det = activity_det;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks_one() {
        return remarks_one;
    }

    public void setRemarks_one(String remarks_one) {
        this.remarks_one = remarks_one;
    }

    public String getRemarks_two() {
        return remarks_two;
    }

    public void setRemarks_two(String remarks_two) {
        this.remarks_two = remarks_two;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getTransactionNo() {
        return TransactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        TransactionNo = transactionNo;
    }

    public String getStatusCheck() {
        return statusCheck;
    }

    public void setStatusCheck(String statusCheck) {
        this.statusCheck = statusCheck;
    }
}
