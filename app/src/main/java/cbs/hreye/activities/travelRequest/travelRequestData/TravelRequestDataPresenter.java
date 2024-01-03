package cbs.hreye.activities.travelRequest.travelRequestData;

import android.content.Context;

import java.util.List;

import cbs.hreye.activities.travelRequest.TravelRequestResponseData;
import cbs.hreye.network.RetrofitClient;
import cbs.hreye.pojo.TravelRequestGetResponseModel;
import cbs.hreye.pojo.TravelRequestGetRootModel;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.PrefrenceKey;
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

        String companyName= CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "");
        String locationNo=CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "");
        String userId=CommonMethods.getPrefsData(context, PrefrenceKey.USER_ID, "");


        RetrofitClient.getInstance(context).getMyApi().getTravelRequestData(companyName,locationNo,userId).enqueue(new Callback<TravelRequestGetRootModel>() {
            @Override
            public void onResponse(Call<TravelRequestGetRootModel> call, Response<TravelRequestGetRootModel> response) {
                if(response.code()==200&&response.body()!=null){
                    travelRequestDataMvpView.getTravelRequestData(response.body().getMobileTravelResult().getTravelRequestGetModel());
                }
            }

            @Override
            public void onFailure(Call<TravelRequestGetRootModel> call, Throwable t) {
                travelRequestDataMvpView.errorMessage(t.getMessage());
            }
        });

    }



}
