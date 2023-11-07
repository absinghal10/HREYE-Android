package cbs.hreye.activities.travelRequest.travelRequestData;

import android.content.Context;

import java.util.List;

import cbs.hreye.activities.travelRequest.TravelRequestResponseData;
import cbs.hreye.network.RetrofitClient;
import cbs.hreye.utilities.CustomProgressbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravelRequestDataPresenter {

    private Context context;

    private TravelRequestDataMvpView travelRequestDataMvpView;

    public TravelRequestDataPresenter(Context context, TravelRequestDataMvpView travelRequestDataMvpView) {
        this.context = context;
        this.travelRequestDataMvpView = travelRequestDataMvpView;
    }


    void fetchTravelRequestData() {
        RetrofitClient.getInstance(context).getMyApi().geTravelRequestResponseDataCall("abc","djf").enqueue(new Callback<Response<List<TravelRequestResponseData>>>() {
            @Override
            public void onResponse(Call<Response<List<TravelRequestResponseData>>> call, Response<Response<List<TravelRequestResponseData>>> response) {
                if(response.code()==200){
                    if(response.body()!=null){
                     travelRequestDataMvpView.getTravelRequestData();
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
