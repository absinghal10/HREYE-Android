package cbs.hreye.pojo;

import com.google.gson.annotations.SerializedName;

public class PostTravelrequestauthorizedResult {

    @SerializedName("Message")
    PostTravelDataResponseMessageModel messageModel;

    public PostTravelDataResponseMessageModel getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(PostTravelDataResponseMessageModel messageModel) {
        this.messageModel = messageModel;
    }
}
