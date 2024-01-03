package cbs.hreye.pojo;

import com.google.gson.annotations.SerializedName;

public class TravelPostModifiedResponseDataModel {

    @SerializedName("PostTravelrequestmodifyResult")
    PostTravelrequestmodifyResult postTravelrequestmodifyResult;

    public PostTravelrequestmodifyResult getPostTravelrequestmodifyResult() {
        return postTravelrequestmodifyResult;
    }

    public void setPostTravelrequestmodifyResult(PostTravelrequestmodifyResult postTravelrequestmodifyResult) {
        this.postTravelrequestmodifyResult = postTravelrequestmodifyResult;
    }
}
