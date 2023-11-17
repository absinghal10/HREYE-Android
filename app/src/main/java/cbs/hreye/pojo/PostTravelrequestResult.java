package cbs.hreye.pojo;

import com.google.gson.annotations.SerializedName;

public class PostTravelrequestResult {

    @SerializedName("COMPANY_NO")
    String COMPANYNO;

    @SerializedName("LOCATION_NO")
    String LOCATIONNO;

    @SerializedName("Message")
    PostTravelDataResponseMessageModel messageModel;

    @SerializedName("REMARKS")
    String REMARKS;

    @SerializedName("TRAN_DATE")
    String TRANDATE;

    @SerializedName("TRAN_NO")
    String TRANNO;

    @SerializedName("TRAVEL")
    String TRAVEL;

    @SerializedName("TRD")
    String TRD;

    @SerializedName("USER_ID")
    String USERID;


    public void setCOMPANYNO(String COMPANYNO) {
        this.COMPANYNO = COMPANYNO;
    }
    public String getCOMPANYNO() {
        return COMPANYNO;
    }

    public void setLOCATIONNO(String LOCATIONNO) {
        this.LOCATIONNO = LOCATIONNO;
    }
    public String getLOCATIONNO() {
        return LOCATIONNO;
    }

    public void setMessage(PostTravelDataResponseMessageModel messageModel) {
        this.messageModel = messageModel;
    }
    public PostTravelDataResponseMessageModel getMessage() {
        return messageModel;
    }

    public void setREMARKS(String REMARKS) {
        this.REMARKS = REMARKS;
    }
    public String getREMARKS() {
        return REMARKS;
    }

    public void setTRANDATE(String TRANDATE) {
        this.TRANDATE = TRANDATE;
    }
    public String getTRANDATE() {
        return TRANDATE;
    }

    public void setTRANNO(String TRANNO) {
        this.TRANNO = TRANNO;
    }
    public String getTRANNO() {
        return TRANNO;
    }

    public void setTRAVEL(String TRAVEL) {
        this.TRAVEL = TRAVEL;
    }
    public String getTRAVEL() {
        return TRAVEL;
    }

    public void setTRD(String TRD) {
        this.TRD = TRD;
    }
    public String getTRD() {
        return TRD;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }
    public String getUSERID() {
        return USERID;
    }


}
