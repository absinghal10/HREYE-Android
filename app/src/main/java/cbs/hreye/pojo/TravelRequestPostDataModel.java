package cbs.hreye.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cbs.hreye.activities.travelRequest.TravelRequestModel;

public class TravelRequestPostDataModel {
    @SerializedName("COMPANY_NO")
    String companyNo;

    @SerializedName("LOCATION_NO")
    String locationNo;

    @SerializedName("USER_ID")
    String userID;

    @SerializedName("TRAN_NO")
    String tran_No;

    @SerializedName("TRAN_DATE")
    String transactionDate;

    @SerializedName("REMARKS")
    String remarks;

    @SerializedName("TRAVEL")
    String travel ;

    @SerializedName("STATUS")
    String status;


    @SerializedName("TRAN_MODE")
    String transMode;

    @SerializedName("TRD")
    List<TravelRequestModel> travelRequestList;

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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTran_No() {
        return tran_No;
    }


    public void setTran_No(String tran_No) {
        this.tran_No = tran_No;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTravel() {
        return travel;
    }

    public void setTravel(String travel) {
        this.travel = travel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public List<TravelRequestModel> getTravelRequestList() {
        return travelRequestList;
    }

    public void setTravelRequestList(List<TravelRequestModel> travelRequestList) {
        this.travelRequestList = travelRequestList;
    }
}
