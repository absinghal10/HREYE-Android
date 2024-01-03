package cbs.hreye.pojo;

import com.google.gson.annotations.SerializedName;

public class TravelRequestGetRootDetailDataModel {
    @SerializedName("GetTravelerRequestInfoResult")
    TravelGetRequestDetailResponseModel travelGetRequestDetailResponseModel;

    public TravelGetRequestDetailResponseModel getTravelGetRequestDetailResponseModel() {
        return travelGetRequestDetailResponseModel;
    }

    public void setTravelGetRequestDetailResponseModel(TravelGetRequestDetailResponseModel travelGetRequestDetailResponseModel) {
        this.travelGetRequestDetailResponseModel = travelGetRequestDetailResponseModel;
    }
}
