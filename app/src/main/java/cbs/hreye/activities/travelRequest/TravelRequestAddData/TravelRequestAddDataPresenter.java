package cbs.hreye.activities.travelRequest.TravelRequestAddData;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cbs.hreye.R;
import cbs.hreye.activities.travelRequest.TravelRequestResponseData;
import cbs.hreye.network.RetrofitClient;
import cbs.hreye.pojo.TravelPostRequestResponseDataModel;
import cbs.hreye.pojo.TravelRequestPostDataModel;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.CustomProgressbar;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class TravelRequestAddDataPresenter {

    private Context context;

    private TravelRequestAddDataMvpView travelRequestDataMvpView;

    public TravelRequestAddDataPresenter(Context context, TravelRequestAddDataMvpView travelRequestDataMvpView) {
        this.context = context;
        this.travelRequestDataMvpView = travelRequestDataMvpView;
    }



    public  void postTravelRequest(TravelRequestPostDataModel travelRequestPostDataModel){

        if (!CommonMethods.isOnline(context)) {
            Toast.makeText(context,context.getString(R.string.net),Toast.LENGTH_SHORT).show();
            return;
        }


        for(int i=0;i<travelRequestPostDataModel.getTravelRequestList().size();i++){
            String travelModeType= String.valueOf(travelRequestPostDataModel.getTravelRequestList().get(i).getTravelMode().charAt(0));
            travelRequestPostDataModel.getTravelRequestList().get(i).setTravelMode(travelModeType);
        }

        CustomProgressbar.showProgressBar(context,false);

        RetrofitClient.getInstance(context).getMyApi().postTravelRequestData(travelRequestPostDataModel).enqueue(new Callback<TravelPostRequestResponseDataModel>() {
            @Override
            public void onResponse(Call<TravelPostRequestResponseDataModel> call, Response<TravelPostRequestResponseDataModel> response) {
                CustomProgressbar.hideProgressBar();

                if(response.code()==200&& response.body()!=null){
                    if(response.body().getPostTravelrequestResult().getMessage().getSuccess()){
                        travelRequestDataMvpView.postTravelRequestDataStatus(response.body().getPostTravelrequestResult().getMessage().getErrorMsg());
                    }else{
                        travelRequestDataMvpView.errorMessage(response.body().getPostTravelrequestResult().getMessage().getErrorMsg());
                    }
                }
            }
            @Override
            public void onFailure(Call<TravelPostRequestResponseDataModel> call, Throwable t) {
                CustomProgressbar.hideProgressBar();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
