package cbs.hreye.activities.travelRequest.travelRequestData;

import java.util.List;

import cbs.hreye.pojo.TravelRequestGetModel;
import cbs.hreye.pojo.TravelRequestGetResponseModel;

public interface TravelRequestDataMvpView {
    void getTravelRequestData(List<TravelRequestGetModel> travelRequestGetResponseModelList);
    void errorMessage(String msg);

}
