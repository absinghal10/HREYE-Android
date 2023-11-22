package cbs.hreye.activities.travelRequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TravelRequestModel implements Serializable {



    @SerializedName("SR_NO")
    String srNo;
    // Same as Serial Number
    @SerializedName("MODE")
    String mode;
    @SerializedName("transactionNo")
    String transactionNo;
    @SerializedName("TYPE_OF_EMPLOYEE")
    String typeOfEmpolyee;
    @SerializedName("ASSO_CODE")
    String associateCode;
    @SerializedName("ASSO_NAME")
    String associateName;
    @SerializedName("DOCOMENTRY_NAME")
    String nameAsPerGovtDoc;
    @SerializedName("AGE")
    String age;
    @SerializedName("CUSTOMER_NAME")
    String customer;
    @SerializedName("TRIP")
    String trip;
    @SerializedName("DATE")
    String travelData;
    @SerializedName("To_date")
    String returnDate;
    @SerializedName("TRAVEL_MODE")
    String travelMode;
    @SerializedName("TRAVEL_REASON")
    String reasonForTravel;
    @SerializedName("HOTEL_REQUIRED")
    String hotelRequired;
    @SerializedName("HOTEL_FROM_DATE")
    String 	hotelfrom;
    @SerializedName("HOTEL_TO_DATE")
    String hotelto;
    @SerializedName("TRAVEL_FROM")
    String fromLocation;
    @SerializedName("TRAVEL_TO")
    String ToLocation;
    @SerializedName("PASSPORT")
    String passport;
    @SerializedName("VALIDITY")
    String validity;

    @SerializedName("STATUS")
    String status;

    public TravelRequestModel(String srNo, String mode, String transactionNo, String typeOfEmpolyee, String associateCode, String associateName, String nameAsPerGovtDoc, String age, String customer, String trip, String travelData, String returnDate, String travelMode, String reasonForTravel, String hotelRequired, String hotelfrom, String hotelto, String fromLocation, String toLocation, String passport, String validity,String status) {
        this.srNo = srNo;
        this.mode = mode;
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
        this.status=status;
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
