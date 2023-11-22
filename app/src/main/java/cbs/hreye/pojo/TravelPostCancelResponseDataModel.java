package cbs.hreye.pojo;

import com.google.gson.annotations.SerializedName;

public class TravelPostCancelResponseDataModel {

    @SerializedName("PostTravelrequestcancelResult")
    PostTravelrequestcancelResult postTravelrequestcancelResult;

    public PostTravelrequestcancelResult getPostTravelrequestcancelResult() {
        return postTravelrequestcancelResult;
    }

    public void setPostTravelrequestcancelResult(PostTravelrequestcancelResult postTravelrequestcancelResult) {
        this.postTravelrequestcancelResult = postTravelrequestcancelResult;
    }
}
