package cbs.hreye.pojo.TravelGrantRejectPostModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TravelRejectResponse {
    @SerializedName("PostTravelRejectResult")
    @Expose
    private PostTravelRejectResult postTravelRejectResult;

    public PostTravelRejectResult getPostTravelRejectResult() {
        return postTravelRejectResult;
    }

    public void setPostTravelRejectResult(PostTravelRejectResult postTravelRejectResult) {
        this.postTravelRejectResult = postTravelRejectResult;
    }

}
