package cbs.hreye.pojo;

import com.google.gson.annotations.SerializedName;

public class TravelPostRequestResponseDataModel {

    @SerializedName("PostTravelrequestResult")
    PostTravelrequestResult postTravelrequestResult;

    public TravelPostRequestResponseDataModel(PostTravelrequestResult postTravelrequestResult) {
        this.postTravelrequestResult = postTravelrequestResult;
    }

    public void setPostTravelrequestResult(PostTravelrequestResult postTravelrequestResult) {
        this.postTravelrequestResult = postTravelrequestResult;
    }
    public PostTravelrequestResult getPostTravelrequestResult() {
        return postTravelrequestResult;
    }
}
