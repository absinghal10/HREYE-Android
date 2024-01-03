package cbs.hreye.pojo.TravelGrantRejectPostModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("ErrorMsg")
    @Expose
    private String errorMsg;
    @SerializedName("Success")
    @Expose
    private Boolean success;
    @SerializedName("TravelCode")
    @Expose
    private Object travelCode;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getTravelCode() {
        return travelCode;
    }

    public void setTravelCode(Object travelCode) {
        this.travelCode = travelCode;
    }
}
