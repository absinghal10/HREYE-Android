package cbs.hreye.pojo;

import com.google.gson.annotations.SerializedName;

public class TravelPostAuthroziedResponseDataModel {

    @SerializedName("PostTravelrequestauthorizedResult")
    PostTravelrequestauthorizedResult PostTravelrequestauthorizedResult;


    public void setPostTravelrequestauthorizedResult(PostTravelrequestauthorizedResult PostTravelrequestauthorizedResult) {
        this.PostTravelrequestauthorizedResult = PostTravelrequestauthorizedResult;
    }
    public PostTravelrequestauthorizedResult getPostTravelrequestauthorizedResult() {
        return PostTravelrequestauthorizedResult;
    }

}



