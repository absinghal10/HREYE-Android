package cbs.hreye.utilities;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

public class MyApplication extends Application {
    private static MyApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
