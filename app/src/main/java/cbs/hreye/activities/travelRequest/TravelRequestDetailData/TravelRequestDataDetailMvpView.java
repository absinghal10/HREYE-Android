package cbs.hreye.activities.travelRequest.TravelRequestDetailData;

import java.util.List;

import cbs.hreye.activities.travelRequest.TravelRequestModel;
import cbs.hreye.pojo.TravelRequestGetDetailData;

public interface TravelRequestDataDetailMvpView {
    void getTravelRequestDetailData(List<TravelRequestModel> travelRequestGetDetailDataList);
    void errorMessage(String msg);
    void postTravelRequestDataStatus(String status);



}
