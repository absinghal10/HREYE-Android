package cbs.hreye.activities.travelRequest.AddTravelRequestFormData;

import java.util.ArrayList;
import java.util.List;

import cbs.hreye.pojo.ADetail;
import cbs.hreye.pojo.AssociateResponseDataModel;
import cbs.hreye.pojo.ProjectPojo;

public interface TravelRequestAddFormDataMvpView {
    void getdata();
    void getProjectAndCustomerData(List<ProjectPojo> customerAndProjectList);
    void getAssociateData(List<AssociateResponseDataModel> associateResponseDataModelList);
    void errorMessage(String msg);

}
