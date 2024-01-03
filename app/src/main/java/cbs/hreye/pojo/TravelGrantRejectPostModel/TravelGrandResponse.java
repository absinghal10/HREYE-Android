package cbs.hreye.pojo.TravelGrantRejectPostModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TravelGrandResponse {
    @SerializedName("PostTravelGrantResult")
    @Expose
    private PostTravelGrantResult postTravelGrantResult;

    public PostTravelGrantResult getPostTravelGrantResult() {
        return postTravelGrantResult;
    }

    public void setPostTravelGrantResult(PostTravelGrantResult postTravelGrantResult) {
        this.postTravelGrantResult = postTravelGrantResult;
    }

}
