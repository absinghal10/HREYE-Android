package cbs.hreye.pojo.TravelGrantRejectPostModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tgr {
    @SerializedName("COMPANY_NO")
    @Expose
    private String companyNo;
    @SerializedName("LOCATION_NO")
    @Expose
    private String locationNo;
    @SerializedName("li_no")
    @Expose
    private String liNo;
    @SerializedName("TRIP")
    @Expose
    private String trip;
    @SerializedName("TRANSACTION_MODE")
    @Expose
    private String transactionMode;
    @SerializedName("ASSO_CODE")
    @Expose
    private String assoCode;
    @SerializedName("ASSO_NAME")
    @Expose
    private String assoName;
    @SerializedName("TRAVEL_MODE")
    @Expose
    private String travelMode;
    @SerializedName("TRAVEL_FROM_DATE")
    @Expose
    private String travelFromDate;
    @SerializedName("TRAVEL_TO_DATE")
    @Expose
    private String travelToDate;
    @SerializedName("TRAN_NO")
    @Expose
    private String tranNo;
    @SerializedName("TRAN_DATE")
    @Expose
    private String tranDate;
    @SerializedName("TRAVEL_FROM")
    @Expose
    private String travelFrom;
    @SerializedName("TO_TRAVEL")
    @Expose
    private String toTravel;
    @SerializedName("HOTEL_REQUIRED")
    @Expose
    private String hotelRequired;
    @SerializedName("REASON")
    @Expose
    private String reason;
    @SerializedName("REMARKS")
    @Expose
    private String remarks;
    @SerializedName("STATUS")
    @Expose
    private String status;

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

    public String getLiNo() {
        return liNo;
    }

    public void setLiNo(String liNo) {
        this.liNo = liNo;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    public String getTransactionMode() {
        return transactionMode;
    }

    public void setTransactionMode(String transactionMode) {
        this.transactionMode = transactionMode;
    }

    public String getAssoCode() {
        return assoCode;
    }

    public void setAssoCode(String assoCode) {
        this.assoCode = assoCode;
    }

    public String getAssoName() {
        return assoName;
    }

    public void setAssoName(String assoName) {
        this.assoName = assoName;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public String getTravelFromDate() {
        return travelFromDate;
    }

    public void setTravelFromDate(String travelFromDate) {
        this.travelFromDate = travelFromDate;
    }

    public String getTravelToDate() {
        return travelToDate;
    }

    public void setTravelToDate(String travelToDate) {
        this.travelToDate = travelToDate;
    }

    public String getTranNo() {
        return tranNo;
    }

    public void setTranNo(String tranNo) {
        this.tranNo = tranNo;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getTravelFrom() {
        return travelFrom;
    }

    public void setTravelFrom(String travelFrom) {
        this.travelFrom = travelFrom;
    }

    public String getToTravel() {
        return toTravel;
    }

    public void setToTravel(String toTravel) {
        this.toTravel = toTravel;
    }

    public String getHotelRequired() {
        return hotelRequired;
    }

    public void setHotelRequired(String hotelRequired) {
        this.hotelRequired = hotelRequired;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
