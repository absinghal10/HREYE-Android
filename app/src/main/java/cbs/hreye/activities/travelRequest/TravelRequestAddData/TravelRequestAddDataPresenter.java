package cbs.hreye.activities.travelRequest.TravelRequestAddData;

import android.content.Context;

import java.util.List;

import cbs.hreye.activities.travelRequest.TravelRequestResponseData;
import cbs.hreye.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravelRequestAddDataPresenter {

    private Context context;

    private TravelRequestAddDataMvpView travelRequestDataMvpView;

    public TravelRequestAddDataPresenter(Context context, TravelRequestAddDataMvpView travelRequestDataMvpView) {
        this.context = context;
        this.travelRequestDataMvpView = travelRequestDataMvpView;
    }


    public void getData(){


    }

}
