package cbs.hreye.pojo;

import com.google.gson.annotations.SerializedName;

public class PostTravelDataResponseMessageModel {

    @SerializedName("ErrorMsg")
    String ErrorMsg;

    @SerializedName("Success")
    boolean Success;

    @SerializedName("TravelCode")
    String TravelCode;


    public void setErrorMsg(String ErrorMsg) {
        this.ErrorMsg = ErrorMsg;
    }
    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }
    public boolean getSuccess() {
        return Success;
    }

    public void setTravelCode(String TravelCode) {
        this.TravelCode = TravelCode;
    }
    public String getTravelCode() {
        return TravelCode;
    }
}
