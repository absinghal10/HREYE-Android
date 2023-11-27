package cbs.hreye.activities.travelRequest.TravelRequestDetailData;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import cbs.hreye.R;
import cbs.hreye.network.RetrofitClient;
import cbs.hreye.pojo.TravelPostAuthroziedResponseDataModel;
import cbs.hreye.pojo.TravelPostCancelResponseDataModel;
import cbs.hreye.pojo.TravelPostModifiedResponseDataModel;
import cbs.hreye.pojo.TravelRequestGetRootDetailDataModel;
import cbs.hreye.pojo.TravelRequestPostDataModel;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.PrefrenceKey;
import okhttp3.ResponseBody;
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

    public  void postTravelAuthrorizedRequestData(TravelRequestPostDataModel travelRequestPostDataModel,String requestType){

        if (!CommonMethods.isOnline(context)) {
            Toast.makeText(context,context.getString(R.string.net),Toast.LENGTH_SHORT).show();
            return;
        }

        for(int i=0;i<travelRequestPostDataModel.getTravelRequestList().size();i++){
            travelRequestPostDataModel.getTravelRequestList().get(i).setSrNo(String.valueOf((i+1)));
            travelRequestPostDataModel.getTravelRequestList().get(i).setMode(String.valueOf((i+1)));


            if(!TextUtils.isEmpty(travelRequestPostDataModel.getTravelRequestList().get(i).getTravelData())){
                String dateStr=travelRequestPostDataModel.getTravelRequestList().get(i).getTravelData();
                String convertedDateString=CommonMethods.convertVariousDateFormatsToYYYYMMDD(dateStr);
                travelRequestPostDataModel.getTravelRequestList().get(i).setTravelData(convertedDateString);
            }

            if(!TextUtils.isEmpty(travelRequestPostDataModel.getTravelRequestList().get(i).getReturnDate())){
                String dateStr=travelRequestPostDataModel.getTravelRequestList().get(i).getReturnDate();
                String convertedDateString=CommonMethods.convertVariousDateFormatsToYYYYMMDD(dateStr);
                travelRequestPostDataModel.getTravelRequestList().get(i).setReturnDate(convertedDateString);
            }

            if(!TextUtils.isEmpty(travelRequestPostDataModel.getTravelRequestList().get(i).getHotelfrom())){
                String dateStr=travelRequestPostDataModel.getTravelRequestList().get(i).getHotelfrom();
                String convertedDateString=CommonMethods.convertVariousDateFormatsToYYYYMMDD(dateStr);
                travelRequestPostDataModel.getTravelRequestList().get(i).setHotelfrom(convertedDateString);
            }


            if(!TextUtils.isEmpty(travelRequestPostDataModel.getTravelRequestList().get(i).getHotelto())){
                String dateStr=travelRequestPostDataModel.getTravelRequestList().get(i).getHotelto();
                String convertedDateString=CommonMethods.convertVariousDateFormatsToYYYYMMDD(dateStr);
                travelRequestPostDataModel.getTravelRequestList().get(i).setHotelto(convertedDateString);
            }

            if(!TextUtils.isEmpty(travelRequestPostDataModel.getTravelRequestList().get(i).getValidity())){
                String dateStr=travelRequestPostDataModel.getTravelRequestList().get(i).getValidity();
                String convertedDateString=CommonMethods.convertVariousDateFormatsToYYYYMMDD(dateStr);
                travelRequestPostDataModel.getTravelRequestList().get(i).setValidity(convertedDateString);
            }

            if(travelRequestPostDataModel.getTravelRequestList().get(i).getTrip().equalsIgnoreCase("Single trip")){
                travelRequestPostDataModel.getTravelRequestList().get(i).setTrip("S");
            }else if(travelRequestPostDataModel.getTravelRequestList().get(i).getTrip().equalsIgnoreCase("Round trip")){
                travelRequestPostDataModel.getTravelRequestList().get(i).setTrip("R");
            }

            String travelModeType= String.valueOf(travelRequestPostDataModel.getTravelRequestList().get(i).getTravelMode().charAt(0));
            travelRequestPostDataModel.getTravelRequestList().get(i).setTravelMode(travelModeType);

        }

        // Authorized Case
        if(requestType.equalsIgnoreCase("A")){
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

        }else if(requestType.equalsIgnoreCase("M")){
            // Modify Case Call

            CustomProgressbar.showProgressBar(context,false);

            RetrofitClient.getInstance(context).getMyApi().postModifyTravelRequestData(travelRequestPostDataModel).enqueue(new Callback<TravelPostModifiedResponseDataModel>() {
                @Override
                public void onResponse(Call<TravelPostModifiedResponseDataModel> call, Response<TravelPostModifiedResponseDataModel> response) {
                    CustomProgressbar.hideProgressBar();

                    if(response.code()==200&& response.body()!=null){

                        if(response.body().getPostTravelrequestmodifyResult().getMessageModel()!=null){
                            if (response.body().getPostTravelrequestmodifyResult().getMessageModel().getSuccess()) {
                                travelRequestDataMvpView.postTravelRequestDataStatus(response.body().getPostTravelrequestmodifyResult().getMessageModel().getErrorMsg());
                            } else {
                                travelRequestDataMvpView.errorMessage(response.body().getPostTravelrequestmodifyResult().getMessageModel().getErrorMsg());
                            }
                        }

                    }
                }
                @Override
                public void onFailure(Call<TravelPostModifiedResponseDataModel> call, Throwable t) {
                    CustomProgressbar.hideProgressBar();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }else if(requestType.equalsIgnoreCase("C")){
            // Cancel Case Call
            CustomProgressbar.showProgressBar(context,false);
            RetrofitClient.getInstance(context).getMyApi().postCancelTravelRequestData(travelRequestPostDataModel).enqueue(new Callback<TravelPostCancelResponseDataModel>() {
                @Override
                public void onResponse(Call<TravelPostCancelResponseDataModel> call, Response<TravelPostCancelResponseDataModel> response) {
                    CustomProgressbar.hideProgressBar();

                    if(response.code()==200&& response.body()!=null){

                        if(response.body().getPostTravelrequestcancelResult().getMessageModel()!=null){
                            if (response.body().getPostTravelrequestcancelResult().getMessageModel().getSuccess()) {
                                travelRequestDataMvpView.postTravelRequestDataStatus(response.body().getPostTravelrequestcancelResult().getMessageModel().getErrorMsg());
                            } else {
                                travelRequestDataMvpView.errorMessage(response.body().getPostTravelrequestcancelResult().getMessageModel().getErrorMsg());
                            }
                        }

                    }
                }
                @Override
                public void onFailure(Call<TravelPostCancelResponseDataModel> call, Throwable t) {
                    CustomProgressbar.hideProgressBar();
                    Toast.makeText(context, t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }

    }


}
