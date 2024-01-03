package cbs.hreye.pojo.TravelGetModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Messsage {

    @SerializedName("ErrorMsg")
    @Expose
    private Object errorMsg;
    @SerializedName("Success")
    @Expose
    private Boolean success;
    @SerializedName("VisitCode")
    @Expose
    private Object visitCode;

    public Object getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(Object errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getVisitCode() {
        return visitCode;
    }

    public void setVisitCode(Object visitCode) {
        this.visitCode = visitCode;
    }

}
