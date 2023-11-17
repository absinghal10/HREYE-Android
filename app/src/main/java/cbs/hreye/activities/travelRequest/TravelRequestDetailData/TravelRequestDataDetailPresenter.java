package cbs.hreye.activities.travelRequest.TravelRequestDetailData;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.List;

import cbs.hreye.R;
import cbs.hreye.activities.travelRequest.TravelRequestResponseData;
import cbs.hreye.network.RetrofitClient;
import cbs.hreye.pojo.TravelPostAuthroziedResponseDataModel;
import cbs.hreye.pojo.TravelPostRequestResponseDataModel;
import cbs.hreye.pojo.TravelRequestGetRootDetailDataModel;
import cbs.hreye.pojo.TravelRequestPostDataModel;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.PrefrenceKey;
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


    void fetchTravelRequestDetailData(String transactionNo) {

        String companyName= CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "");
        String locationNo=CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "");

        CustomProgressbar.showProgressBar(context,false);

        RetrofitClient.getInstance(context).getMyApi().getTravelRequestDetailData(companyName,locationNo,transactionNo,"").enqueue(new Callback<TravelRequestGetRootDetailDataModel>() {
            @Override
            public void onResponse(Call<TravelRequestGetRootDetailDataModel> call, Response<TravelRequestGetRootDetailDataModel> response) {
                 CustomProgressbar.hideProgressBar();
                if(response.code()==200){
                    if(response.body()!=null){
                        travelRequestDataMvpView.getTravelRequestDetailData(response.body().getTravelGetRequestDetailResponseModel().getTravelRequestGetDetailDataList());
                    }
                }
            }

            @Override
            public void onFailure(Call<TravelRequestGetRootDetailDataModel> call, Throwable t) {
                travelRequestDataMvpView.errorMessage(t.getMessage());
                CustomProgressbar.hideProgressBar();
            }
        });

    }

    public  void postTravelAuthrorizedRequestData(TravelRequestPostDataModel travelRequestPostDataModel){

        if (!CommonMethods.isOnline(context)) {
            Toast.makeText(context,context.getString(R.string.net),Toast.LENGTH_SHORT).show();
            return;
        }

        for(int i=0;i<travelRequestPostDataModel.getTravelRequestList().size();i++){
            travelRequestPostDataModel.getTravelRequestList().get(i).setSrNo(String.valueOf((i+1)));
            travelRequestPostDataModel.getTravelRequestList().get(i).setMode(String.valueOf((i+1)));
            if(TextUtils.isEmpty(travelRequestPostDataModel.getTravelRequestList().get(i).getTravelData())){

            }else{
                String dateStr=travelRequestPostDataModel.getTravelRequestList().get(i).getTravelData();
                String convertedDateString=CommonMethods.changeDateTOyyyyMMdd(dateStr);
                travelRequestPostDataModel.getTravelRequestList().get(i).setTravelData(convertedDateString);
            }

            if(TextUtils.isEmpty(travelRequestPostDataModel.getTravelRequestList().get(i).getReturnDate())){

            }else{
                travelRequestPostDataModel.getTravelRequestList().get(i).setReturnDate(CommonMethods.changeDateTOyyyyMMdd(travelRequestPostDataModel.getTravelRequestList().get(i).getReturnDate()));
            }


            if(TextUtils.isEmpty(travelRequestPostDataModel.getTravelRequestList().get(i).getHotelfrom())){

            }else{
                travelRequestPostDataModel.getTravelRequestList().get(i).setHotelfrom(CommonMethods.changeDateTOyyyyMMdd(travelRequestPostDataModel.getTravelRequestList().get(i).getHotelfrom()));
            }
            if(TextUtils.isEmpty(travelRequestPostDataModel.getTravelRequestList().get(i).getHotelto())){

            }else{
                travelRequestPostDataModel.getTravelRequestList().get(i).setHotelto(CommonMethods.changeDateTOyyyyMMdd(travelRequestPostDataModel.getTravelRequestList().get(i).getHotelto()));
            }

        }

        CustomProgressbar.showProgressBar(context,false);

        RetrofitClient.getInstance(context).getMyApi().postAuthroziedTravelRequestData(travelRequestPostDataModel).enqueue(new Callback<TravelPostAuthroziedResponseDataModel>() {
            @Override
            public void onResponse(Call<TravelPostAuthroziedResponseDataModel> call, Response<TravelPostAuthroziedResponseDataModel> response) {
                CustomProgressbar.hideProgressBar();

                if(response.code()==200&& response.body()!=null){

                    if(response.body().getPostTravelrequestauthorizedResult().getMessageModel()!=null){
                        if (response.body().getPostTravelrequestauthorizedResult().getMessageModel().getSuccess()) {
                            travelRequestDataMvpView.postTravelRequestDataStatus(response.body().getPostTravelrequestauthorizedResult().getMessageModel().getErrorMsg());
                        } else {
                            travelRequestDataMvpView.errorMessage(response.body().getPostTravelrequestauthorizedResult().getMessageModel().getErrorMsg());
                        }
                    }

                }
            }
            @Override
            public void onFailure(Call<TravelPostAuthroziedResponseDataModel> call, Throwable t) {
                CustomProgressbar.hideProgressBar();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }





}
