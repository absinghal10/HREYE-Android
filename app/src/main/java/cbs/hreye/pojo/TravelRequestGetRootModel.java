package cbs.hreye.pojo;

import com.google.gson.annotations.SerializedName;

public class TravelRequestGetRootModel {

    @SerializedName("MobileTravelResult")
    TravelRequestGetResponseModel MobileTravelResult;

    public TravelRequestGetResponseModel getMobileTravelResult() {
        return MobileTravelResult;
    }

    public void setMobileTravelResult(TravelRequestGetResponseModel mobileTravelResult) {
        MobileTravelResult = mobileTravelResult;
    }
}


