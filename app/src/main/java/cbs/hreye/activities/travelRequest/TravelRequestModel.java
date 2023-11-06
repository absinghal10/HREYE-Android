package cbs.hreye.activities.travelRequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TravelRequestModel implements Serializable {
    @SerializedName("srNo")
    String srNo;
    @SerializedName("transactionNo")
    String transactionNo;
    @SerializedName("typeOfEmpolyee")
    String typeOfEmpolyee;
    @SerializedName("associateCode")
    String associateCode;
    @SerializedName("associateName")
    String associateName;
    @SerializedName("nameAsPerGovtDoc")
    String nameAsPerGovtDoc;
    @SerializedName("age")
    String age;
    @SerializedName("customer")
    String customer;
    @SerializedName("trip")
    String trip;
    @SerializedName("travelData")
    String travelData;
    @SerializedName("returnDate")
    String returnDate;
    @SerializedName("travelMode")
    String travelMode;
    @SerializedName("reasonForTravel")
    String reasonForTravel;
    @SerializedName("hotelRequired")
    String hotelRequired;
    @SerializedName("hotelfrom")
    String 	hotelfrom;
    @SerializedName("hotelto")
    String hotelto;
    @SerializedName("fromLocation")
    String fromLocation;
    @SerializedName("ToLocation")
    String ToLocation;
    @SerializedName("passport")
    String passport;
    @SerializedName("validity")
    String validity;


        public TravelRequestModel(String srNo, String transactionNo, String typeOfEmpolyee, String associateCode, String associateName, String nameAsPerGovtDoc, String age, String customer, String trip, String travelData, String returnDate, String travelMode, String reasonForTravel, String hotelRequired, String hotelfrom, String hotelto, String fromLocation, String toLocation, String passport, String validity) {
            this.srNo = srNo;
            this.transactionNo = transactionNo;
            this.typeOfEmpolyee = typeOfEmpolyee;
            this.associateCode = associateCode;
            this.associateName = associateName;
            this.nameAsPerGovtDoc = nameAsPerGovtDoc;
            this.age = age;
            this.customer = customer;
            this.trip = trip;
            this.travelData = travelData;
            this.returnDate = returnDate;
            this.travelMode = travelMode;
            this.reasonForTravel = reasonForTravel;
            this.hotelRequired = hotelRequired;
            this.hotelfrom = hotelfrom;
            this.hotelto = hotelto;
            this.fromLocation = fromLocation;
            ToLocation = toLocation;
            this.passport = passport;
            this.validity = validity;
        }


    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getTypeOfEmpolyee() {
        return typeOfEmpolyee;
    }

    public void setTypeOfEmpolyee(String typeOfEmpolyee) {
        this.typeOfEmpolyee = typeOfEmpolyee;
    }

    public String getAssociateCode() {
        return associateCode;
    }

    public void setAssociateCode(String associateCode) {
        this.associateCode = associateCode;
    }

    public String getAssociateName() {
        return associateName;
    }

    public void setAssociateName(String associateName) {
        this.associateName = associateName;
    }

    public String getNameAsPerGovtDoc() {
        return nameAsPerGovtDoc;
    }

    public void setNameAsPerGovtDoc(String nameAsPerGovtDoc) {
        this.nameAsPerGovtDoc = nameAsPerGovtDoc;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    public String getTravelData() {
        return travelData;
    }

    public void setTravelData(String travelData) {
        this.travelData = travelData;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public String getReasonForTravel() {
        return reasonForTravel;
    }

    public void setReasonForTravel(String reasonForTravel) {
        this.reasonForTravel = reasonForTravel;
    }

    public String getHotelRequired() {
        return hotelRequired;
    }

    public void setHotelRequired(String hotelRequired) {
        this.hotelRequired = hotelRequired;
    }

    public String getHotelfrom() {
        return hotelfrom;
    }

    public void setHotelfrom(String hotelfrom) {
        this.hotelfrom = hotelfrom;
    }

    public String getHotelto() {
        return hotelto;
    }

    public void setHotelto(String hotelto) {
        this.hotelto = hotelto;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return ToLocation;
    }

    public void setToLocation(String toLocation) {
        ToLocation = toLocation;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }
}
