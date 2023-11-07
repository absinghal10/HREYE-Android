package cbs.hreye.activities.travelRequest.AddTravelRequestFormData;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import cbs.hreye.R;
import cbs.hreye.activities.travelRequest.TravelRequestResponseData;
import cbs.hreye.network.RetrofitClient;
import cbs.hreye.pojo.AssociateRootDataModel;
import cbs.hreye.pojo.CustomerResult;
import cbs.hreye.pojo.ProjectPojo;
import cbs.hreye.pojo.ProjectResponseRootDataModel;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.PrefrenceKey;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravelRequestAddFormDataPresenter {

    private Context context;

    private TravelRequestAddFormDataMvpView travelRequestDataMvpView;

    public TravelRequestAddFormDataPresenter(Context context, TravelRequestAddFormDataMvpView travelRequestDataMvpView) {
        this.context = context;
        this.travelRequestDataMvpView = travelRequestDataMvpView;
    }


    void fetchTravelRequestData() {
        RetrofitClient.getInstance(context).getMyApi().geTravelRequestResponseDataCall("abc","djf").enqueue(new Callback<Response<List<TravelRequestResponseData>>>() {
            @Override
            public void onResponse(Call<Response<List<TravelRequestResponseData>>> call, Response<Response<List<TravelRequestResponseData>>> response) {
                if(response.code()==200){
                    if(response.body()!=null){
                     travelRequestDataMvpView.getdata();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<List<TravelRequestResponseData>>> call, Throwable t) {
                  travelRequestDataMvpView.errorMessage(t.getMessage());
            }
        });

    }


    public void fetchCustomerAndProjectList(){

        if (!CommonMethods.isOnline(context)) {
            Toast.makeText(context,context.getString(R.string.net),Toast.LENGTH_SHORT).show();
            return;
        }

        CustomProgressbar.showProgressBar(context,false);

        String companyName=CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "");
        String locationNo=CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "");

        RetrofitClient.getInstance(context).getMyApi().getCustomerProjectList(companyName,locationNo,"").enqueue(new Callback<ProjectResponseRootDataModel>() {
            @Override
            public void onResponse(Call<ProjectResponseRootDataModel> call, Response<ProjectResponseRootDataModel> response) {
                CustomProgressbar.hideProgressBar();
                if(response.code()==200 && response.body()!=null){
                    travelRequestDataMvpView.getProjectAndCustomerData(response.body().getCoustomerResult().getProjectList());
                }
            }

            @Override
            public void onFailure(Call<ProjectResponseRootDataModel> call, Throwable t) {
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                CustomProgressbar.hideProgressBar();
            }
        });

    }


    public void fetchAssociateData(){
        if (!CommonMethods.isOnline(context)) {
            Toast.makeText(context,context.getString(R.string.net),Toast.LENGTH_SHORT).show();
            return;
        }

        CustomProgressbar.showProgressBar(context,false);

        String companyName=CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "");
        String locationNo=CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "");



        RetrofitClient.getInstance(context).getMyApi().getAssociateDataList(companyName,locationNo,"","").enqueue(new Callback<AssociateRootDataModel>() {
            @Override
            public void onResponse(Call<AssociateRootDataModel> call, Response<AssociateRootDataModel> response) {
                CustomProgressbar.hideProgressBar();
                if(response.code()==200 && response.body()!=null){
                    travelRequestDataMvpView.getAssociateData(response.body().getAssociateResult().getResponseDataModelList());
                }
            }

            @Override
            public void onFailure(Call<AssociateRootDataModel> call, Throwable t) {
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                CustomProgressbar.hideProgressBar();
            }
        });

    }

}
