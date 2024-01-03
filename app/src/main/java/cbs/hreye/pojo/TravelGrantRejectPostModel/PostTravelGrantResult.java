package cbs.hreye.pojo.TravelGrantRejectPostModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostTravelGrantResult {
    @SerializedName("Message")
    @Expose
    private Message message;
    @SerializedName("TGR")
    @Expose
    private Object tgr;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Object getTgr() {
        return tgr;
    }

    public void setTgr(Object tgr) {
        this.tgr = tgr;
    }
}
