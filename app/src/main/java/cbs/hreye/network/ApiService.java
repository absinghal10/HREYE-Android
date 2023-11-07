package cbs.hreye.network;

import java.util.List;

import cbs.hreye.activities.travelRequest.TravelRequestResponseData;
import cbs.hreye.pojo.AssociateResponseDataModel;
import cbs.hreye.pojo.AssociateRootDataModel;
import cbs.hreye.pojo.CustomerResult;
import cbs.hreye.pojo.ProjectPojo;
import cbs.hreye.pojo.ProjectResponseRootDataModel;
import retrofit2.Call;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    String ENDPOINT_GET_TRAVEL_REQUEST_RESPONSE_ENDPOINT="json?";
    String ENDPOINT_GET_TRAVEL_REQUEST_DETAIL_RESPONSE_ENDPOINT="json?";

    String GET_CUSTOMER_DATA_END_POINT="/AttendanceService/AttendanceService.svc/Coustomer?";
    String GET_ASSOCIATE_DATA_END_POINT="/AttendanceService/AttendanceService.svc/Assosciate?";

    @GET(ENDPOINT_GET_TRAVEL_REQUEST_RESPONSE_ENDPOINT)
    Call<Response<List<TravelRequestResponseData>>> geTravelRequestResponseDataCall(
            @Query("userID") String userID,
            @Query("transID") String transNo);

    @GET(ENDPOINT_GET_TRAVEL_REQUEST_DETAIL_RESPONSE_ENDPOINT)
    Call<Response<List<TravelRequestResponseData>>> geTravelRequestDetailResponseDataCall(
            @Query("userID") String userID,
            @Query("transID") String transNo);


    @GET(GET_CUSTOMER_DATA_END_POINT)
    Call<ProjectResponseRootDataModel> getCustomerProjectList(
            @Query("COMPANY_NO") String companyNo,
            @Query("LOCATION_NO") String locationNO,
            @Query("CUSTOMER_ID") String customerId);

    @GET(GET_ASSOCIATE_DATA_END_POINT)
    Call<AssociateRootDataModel> getAssociateDataList(
            @Query("COMPANY_NO") String companyNo,
            @Query("LOCATION_NO") String locationNO,
            @Query("ASSO_CODE") String associateCode,
            @Query("ASSO_NAME") String associateName);


}
