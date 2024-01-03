package cbs.hreye.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cbs.hreye.activities.travelRequest.TravelRequestModel;

public class TravelGetRequestDetailResponseModel {
    @SerializedName("GDetail")
    List<TravelRequestModel> travelRequestGetDetailDataList;

    public List<TravelRequestModel> getTravelRequestGetDetailDataList() {
        return travelRequestGetDetailDataList;
    }

    public void setTravelRequestGetDetailDataList(List<TravelRequestModel> travelRequestGetDetailDataList) {
        this.travelRequestGetDetailDataList = travelRequestGetDetailDataList;
    }

}
