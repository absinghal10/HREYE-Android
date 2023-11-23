package cbs.hreye.network;

import java.util.List;

import cbs.hreye.activities.travelRequest.TravelRequestResponseData;
import cbs.hreye.pojo.AssociateResponseDataModel;
import cbs.hreye.pojo.AssociateRootDataModel;
import cbs.hreye.pojo.CustomerResult;
import cbs.hreye.pojo.ProjectPojo;
import cbs.hreye.pojo.ProjectResponseRootDataModel;
import cbs.hreye.pojo.TravelGetModel.TravelGrandRejectDetails;
import cbs.hreye.pojo.TravelGrantRejectPostModel.TravelGrandPostDetails;
import cbs.hreye.pojo.TravelGrantRejectPostModel.TravelGrandResponse;
import cbs.hreye.pojo.TravelGrantRejectPostModel.TravelRejectResponse;
import cbs.hreye.pojo.TravelPostAuthroziedResponseDataModel;
import cbs.hreye.pojo.TravelPostCancelResponseDataModel;
import cbs.hreye.pojo.TravelPostModifiedResponseDataModel;
import cbs.hreye.pojo.TravelPostRequestResponseDataModel;
import cbs.hreye.pojo.TravelRequestGetResponseModel;
import cbs.hreye.pojo.TravelRequestGetRootDetailDataModel;
import cbs.hreye.pojo.TravelRequestGetRootModel;
import cbs.hreye.pojo.TravelRequestPostDataModel;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    String ENDPOINT_GET_TRAVEL_REQUEST_RESPONSE_ENDPOINT="json?";
    String ENDPOINT_GET_TRAVEL_REQUEST_DETAIL_RESPONSE_ENDPOINT="json?";

    String GET_CUSTOMER_DATA_END_POINT="/AttendanceService/AttendanceService.svc/Coustomer?";
    String GET_ASSOCIATE_DATA_END_POINT="/AttendanceService/AttendanceService.svc/Assosciate?";
    String GET_TRAVEL_REQUEST_DATA_END_POINT="/AttendanceService/AttendanceService.svc/MobileTravel?";
    String GET_TRAVEL_REQUEST_DETAIL_DATA_END_POINT="/AttendanceService/AttendanceService.svc/GetTravelerRequestInfo?";


    // Post Data - Add Data
    String POST_DATA_TRAVEL_REQUEST_DATA="/AttendanceService/AttendanceService.svc/PostTravelrequest";

    // Authorized Data
    String POST_AUTHROZIED_TRAVEL_REQUEST_DATA="/AttendanceService/AttendanceService.svc/PostTravelrequestauthorized";
    String POST_MODIFY_TRAVEL_REQUEST_DATA="/AttendanceService/AttendanceService.svc/PostTravelrequestmodify";
    String POST_CANCEL_TRAVEL_REQUEST_DATA="/AttendanceService/AttendanceService.svc/PostTravelrequestcancel";

    String GET_TRAVEL_GRAND_DATA = "AttendanceService/AttendanceService.svc/GrantReject?";
    String POST_TRAVEL_GRAND_DATA = "AttendanceService/AttendanceService.svc/PostTravelGrant";
    String POST_TRAVEL_REJECT_DATA = "AttendanceService/AttendanceService.svc/PostTravelReject";




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



    // Add Travel Request Data Call
    @POST(POST_DATA_TRAVEL_REQUEST_DATA)
    Call<TravelPostRequestResponseDataModel> postTravelRequestData(@Body TravelRequestPostDataModel travelRequestPostDataModel);

    // Authrorized Travel Request Data Call
    @POST(POST_AUTHROZIED_TRAVEL_REQUEST_DATA)
    Call<TravelPostAuthroziedResponseDataModel> postAuthroziedTravelRequestData(@Body TravelRequestPostDataModel travelRequestPostDataModel);

    // Modify Travel Request Data Call
    @POST(POST_MODIFY_TRAVEL_REQUEST_DATA)
    Call<TravelPostModifiedResponseDataModel> postModifyTravelRequestData(@Body TravelRequestPostDataModel travelRequestPostDataModel);

    // Cancel Request
    @POST(POST_CANCEL_TRAVEL_REQUEST_DATA)
    Call<TravelPostCancelResponseDataModel> postCancelTravelRequestData(@Body TravelRequestPostDataModel travelRequestPostDataModel);


    // Get Travel Request Data Call
    @GET(GET_TRAVEL_REQUEST_DATA_END_POINT)
    Call<TravelRequestGetRootModel>  getTravelRequestData(@Query("COMPANY_NO") String companyNo, @Query("LOCATION_NO") String locationNO);


    // Get Travel Request Detail Data Call
    @GET(GET_TRAVEL_REQUEST_DETAIL_DATA_END_POINT)
    Call<TravelRequestGetRootDetailDataModel>  getTravelRequestDetailData(@Query("COMPANY_NO") String companyNo, @Query("LOCATION_NO") String locationNO, @Query("TRAN_NO") String transactionNo, @Query("ASSO_CODE") String associateCode);


    @GET(GET_TRAVEL_GRAND_DATA)
    Call<TravelGrandRejectDetails> getTravelGrantRejectList(
            @Query("COMPANY_NO") String companyNo,
            @Query("LOCATION_NO") String locationNO);

    @POST(POST_TRAVEL_GRAND_DATA)
    Call<TravelGrandResponse> getPostTravelGrand(@Body TravelGrandPostDetails travelGrandPostDetails);

    @POST(POST_TRAVEL_REJECT_DATA)
    Call<TravelRejectResponse> getPostTravelReject(@Body TravelGrandPostDetails travelGrandPostDetails);

}
