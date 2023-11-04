package cbs.hreye.network;

import java.util.List;

import cbs.hreye.activities.travelRequest.TravelRequestResponseData;
import retrofit2.Call;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    String ENDPOINT_GET_TRAVEL_REQUEST_RESPONSE_ENDPOINT="json?";
    String ENDPOINT_GET_TRAVEL_REQUEST_DETAIL_RESPONSE_ENDPOINT="json?";

    @GET(ENDPOINT_GET_TRAVEL_REQUEST_RESPONSE_ENDPOINT)
    Call<Response<List<TravelRequestResponseData>>> geTravelRequestResponseDataCall(
            @Query("userID") String userID,
            @Query("transID") String transNo);

    @GET(ENDPOINT_GET_TRAVEL_REQUEST_DETAIL_RESPONSE_ENDPOINT)
    Call<Response<List<TravelRequestResponseData>>> geTravelRequestDetailResponseDataCall(
            @Query("userID") String userID,
            @Query("transID") String transNo);



}
