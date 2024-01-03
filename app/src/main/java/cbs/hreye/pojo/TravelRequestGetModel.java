package cbs.hreye.pojo;

import com.google.gson.annotations.SerializedName;

public class TravelRequestGetModel {

    @SerializedName("COMPANY_NO")
    String companyNo;

    @SerializedName("LOCATION_NO")
    String locationNo;

    @SerializedName("STATUS")
    String status;

    @SerializedName("TRAN_DATE")
    String transactionDate;

    @SerializedName("TRAN_NO")
    String transactionNo;

    @SerializedName("TRAVEL_REQ_REMARKS")
    String travelRequestRemark;

    @SerializedName("TRAVEL_TYPE")
    String travelType;

    @SerializedName("TRAVEL_TYPE_VALUE")
    String travelTypeValue;


    public TravelRequestGetModel(String companyNo, String locationNo, String status, String transactionDate, String transactionNo, String travelRequestRemark, String travelType, String travelTypeValue) {
        this.companyNo = companyNo;
        this.locationNo = locationNo;
        this.status = status;
        this.transactionDate = transactionDate;
        this.transactionNo = transactionNo;
        this.travelRequestRemark = travelRequestRemark;
        this.travelType = travelType;
        this.travelTypeValue = travelTypeValue;
    }


    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getTravelRequestRemark() {
        return travelRequestRemark;
    }

    public void setTravelRequestRemark(String travelRequestRemark) {
        this.travelRequestRemark = travelRequestRemark;
    }

    public String getTravelType() {
        return travelType;
    }

    public void setTravelType(String travelType) {
        this.travelType = travelType;
    }

    public String getTravelTypeValue() {
        return travelTypeValue;
    }

    public void setTravelTypeValue(String travelTypeValue) {
        this.travelTypeValue = travelTypeValue;
    }

}
