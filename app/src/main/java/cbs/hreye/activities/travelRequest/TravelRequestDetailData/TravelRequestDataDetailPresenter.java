package cbs.hreye.activities.travelRequest.TravelRequestDetailData;

import android.content.Context;

import java.util.List;

import cbs.hreye.activities.travelRequest.TravelRequestResponseData;
import cbs.hreye.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravelRequestDataDetailPresenter {

    private Context context;

    private TravelRequestDataDetailMvpView travelRequestDataMvpView;

    public TravelRequestDataDetailPresenter(Context context, TravelRequestDataDetailMvpView travelRequestDataMvpView) {
        this.context = context;
        this.travelRequestDataMvpView = travelRequestDataMvpView;
    }


    void fetchTravelRequestDetailData() {
        RetrofitClient.getInstance().getMyApi().geTravelRequestDetailResponseDataCall("abc","djf").enqueue(new Callback<Response<List<TravelRequestResponseData>>>() {
            @Override
            public void onResponse(Call<Response<List<TravelRequestResponseData>>> call, Response<Response<List<TravelRequestResponseData>>> response) {
                if(response.code()==200){
                    if(response.body()!=null){
                     travelRequestDataMvpView.getTravelRequestDetailData();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<List<TravelRequestResponseData>>> call, Throwable t) {
                  travelRequestDataMvpView.errorMessage(t.getMessage());
            }
        });

    }



}
