package cbs.hreye.pojo.TravelGrantRejectPostModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TravelGrandPostDetails {
    @SerializedName("TGR")
    @Expose
    private List<Tgr> tgr;

    public List<Tgr> getTgr() {
        return tgr;
    }

    public void setTgr(List<Tgr> tgr) {
        this.tgr = tgr;
    }
}
