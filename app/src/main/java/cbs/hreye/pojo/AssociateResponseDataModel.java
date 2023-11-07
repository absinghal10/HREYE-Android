package cbs.hreye.pojo;

import com.google.gson.annotations.SerializedName;

public class AssociateResponseDataModel {

    @SerializedName("ASSO_CODE")
    String AssoCode;
    @SerializedName("Name")
    String Name;
    @SerializedName("Department")
    String  department;
    @SerializedName("Designation")
    String designation;
    @SerializedName("JOINNING")
    String JoiningDate;
    @SerializedName("LEAVING_DATE")
    String LeavingDate;


    public AssociateResponseDataModel(String assoCode, String name, String department, String designation, String joiningDate, String leavingDate) {
        AssoCode = assoCode;
        Name = name;
        this.department = department;
        this.designation = designation;
        JoiningDate = joiningDate;
        LeavingDate = leavingDate;
    }

    public String getAssoCode() {
        return AssoCode;
    }

    public void setAssoCode(String assoCode) {
        AssoCode = assoCode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getJoiningDate() {
        return JoiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        JoiningDate = joiningDate;
    }

    public String getLeavingDate() {
        return LeavingDate;
    }

    public void setLeavingDate(String leavingDate) {
        LeavingDate = leavingDate;
    }


}
