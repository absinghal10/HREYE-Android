package cbs.hreye.network;

import android.content.Context;

import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.PrefrenceKey;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    private ApiService myApi;
    private String baseUrl; // Add a baseUrl field

    private RetrofitClient(Context context) {
        // Get the base URL from the baseURL method
        baseUrl = CommonMethods.getPrefsDataURL(context, PrefrenceKey.SERVER_IP, "");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl) // Set the base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(ApiService.class);
    }

    public static synchronized RetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context);
        }
        return instance;
    }

    public ApiService getMyApi() {
        return myApi;
    }
}