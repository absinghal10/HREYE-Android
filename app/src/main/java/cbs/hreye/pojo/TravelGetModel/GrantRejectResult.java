package cbs.hreye.pojo.TravelGetModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GrantRejectResult {
    @SerializedName("GDetail")
    @Expose
    private List<GDetail> gDetail;
    @SerializedName("Messsage")
    @Expose
    private Messsage messsage;

    public List<GDetail> getGDetail() {
        return gDetail;
    }

    public void setGDetail(List<GDetail> gDetail) {
        this.gDetail = gDetail;
    }

    public Messsage getMesssage() {
        return messsage;
    }

    public void setMesssage(Messsage messsage) {
        this.messsage = messsage;
    }
}
