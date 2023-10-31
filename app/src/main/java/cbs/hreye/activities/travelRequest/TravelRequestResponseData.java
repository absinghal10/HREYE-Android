package cbs.hreye.activities.travelRequest;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class TravelRequestResponseData implements Parcelable {
    String SrNo;
    String associateCode;
    String name;
    String transactionNo;
    String transactionDate;
    String travelMode;
    String fromDate;
    String toDate;
    String hotelRequired;
    String hotelFromDate;
    String hotelToDate;
    String travelFrom;
    String travelTo;
    String grantOrRejectRemarks;
    String deskRemarks;
    String status;

    public TravelRequestResponseData(String srNo, String associateCode, String name, String transactionNo, String transactionDate, String travelMode, String fromDate, String toDate, String hotelRequired, String hotelFromDate, String hotelToDate, String travelFrom, String travelTo, String grantOrRejectRemarks, String deskRemarks, String status) {
        SrNo = srNo;
        this.associateCode = associateCode;
        this.name = name;
        this.transactionNo = transactionNo;
        this.transactionDate = transactionDate;
        this.travelMode = travelMode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.hotelRequired = hotelRequired;
        this.hotelFromDate = hotelFromDate;
        this.hotelToDate = hotelToDate;
        this.travelFrom = travelFrom;
        this.travelTo = travelTo;
        this.grantOrRejectRemarks = grantOrRejectRemarks;
        this.deskRemarks = deskRemarks;
        this.status = status;
    }

    protected TravelRequestResponseData(Parcel in) {
        SrNo = in.readString();
        associateCode = in.readString();
        name=in.readString();
        transactionNo = in.readString();
        transactionDate = in.readString();
        travelMode = in.readString();
        fromDate = in.readString();
        toDate = in.readString();
        hotelRequired = in.readString();
        hotelFromDate = in.readString();
        hotelToDate = in.readString();
        travelFrom = in.readString();
        travelTo = in.readString();
        grantOrRejectRemarks = in.readString();
        deskRemarks = in.readString();
        status = in.readString();
    }

    public static final Creator<TravelRequestResponseData> CREATOR = new Creator<TravelRequestResponseData>() {
        @Override
        public TravelRequestResponseData createFromParcel(Parcel in) {
            return new TravelRequestResponseData(in);
        }

        @Override
        public TravelRequestResponseData[] newArray(int size) {
            return new TravelRequestResponseData[size];
        }
    };

    public String getSrNo() {
        return SrNo;
    }

    public void setSrNo(String srNo) {
        SrNo = srNo;
    }

    public String getAssociateCode() {
        return associateCode;
    }

    public void setAssociateCode(String associateCode) {
        this.associateCode = associateCode;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getHotelRequired() {
        return hotelRequired;
    }

    public void setHotelRequired(String hotelRequired) {
        this.hotelRequired = hotelRequired;
    }

    public String getHotelFromDate() {
        return hotelFromDate;
    }

    public void setHotelFromDate(String hotelFromDate) {
        this.hotelFromDate = hotelFromDate;
    }

    public String getHotelToDate() {
        return hotelToDate;
    }

    public void setHotelToDate(String hotelToDate) {
        this.hotelToDate = hotelToDate;
    }

    public String getTravelFrom() {
        return travelFrom;
    }

    public void setTravelFrom(String travelFrom) {
        this.travelFrom = travelFrom;
    }

    public String getTravelTo() {
        return travelTo;
    }

    public void setTravelTo(String travelTo) {
        this.travelTo = travelTo;
    }

    public String getGrantOrRejectRemarks() {
        return grantOrRejectRemarks;
    }

    public void setGrantOrRejectRemarks(String grantOrRejectRemarks) {
        this.grantOrRejectRemarks = grantOrRejectRemarks;
    }

    public String getDeskRemarks() {
        return deskRemarks;
    }

    public void setDeskRemarks(String deskRemarks) {
        this.deskRemarks = deskRemarks;
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

    @Override
    public int describeContents() {
        return 0;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(SrNo);
        dest.writeString(associateCode);
        dest.writeString(name);
        dest.writeString(transactionNo);
        dest.writeString(transactionDate);
        dest.writeString(travelMode);
        dest.writeString(fromDate);
        dest.writeString(toDate);
        dest.writeString(hotelRequired);
        dest.writeString(hotelFromDate);
        dest.writeString(hotelToDate);
        dest.writeString(travelFrom);
        dest.writeString(travelTo);
        dest.writeString(grantOrRejectRemarks);
        dest.writeString(deskRemarks);
        dest.writeString(status);
    }
}
