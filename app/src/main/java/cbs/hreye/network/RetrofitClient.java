package cbs.hreye.network;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.PrefrenceKey;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    private ApiService myApi;
    private String baseUrl; // Add a baseUrl field

    private RetrofitClient(Context context) {
        // Get the base URL from the baseURL method
        baseUrl = CommonMethods.getPrefsDataURL(context, PrefrenceKey.SERVER_IP, "");


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(60, TimeUnit.SECONDS); // Set connection timeout
        httpClient.readTimeout(60, TimeUnit.SECONDS); // Set read timeout
        httpClient.writeTimeout(60, TimeUnit.SECONDS); // Set write timeout


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl) // Set the base URL
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build()) // Set the custom OkHttpClient
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