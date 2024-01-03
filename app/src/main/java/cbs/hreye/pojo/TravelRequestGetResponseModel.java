package cbs.hreye.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TravelRequestGetResponseModel {
    @SerializedName("ADetail")
    List<TravelRequestGetModel> travelRequestGetModel;


    public List<TravelRequestGetModel> getTravelRequestGetModel() {
        return travelRequestGetModel;
    }

    public TravelRequestGetResponseModel(List<TravelRequestGetModel> travelRequestGetModel) {
        this.travelRequestGetModel = travelRequestGetModel;
    }
}

