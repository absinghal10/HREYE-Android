package cbs.hreye.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssociateResult {

    @SerializedName("ADetail")
    List<AssociateResponseDataModel> responseDataModelList;

    public List<AssociateResponseDataModel> getResponseDataModelList() {
        return responseDataModelList;
    }

    public void setResponseDataModelList(List<AssociateResponseDataModel> responseDataModelList) {
        this.responseDataModelList = responseDataModelList;
    }
}
