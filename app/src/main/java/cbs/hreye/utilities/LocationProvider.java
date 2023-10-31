package cbs.hreye.utilities;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Locale;

public class LocationProvider {
    public static final long  UPDATE_INTERVAL = 5 * 1000;  /* 5 secs */
    public static final long  FASTEST_INTERVAL  = 2000; /* 2 sec */
    private static final String TAG = "LocationProvider";

    public static void stopLocationUpdates(FusedLocationProviderClient mFusedLocationClient, LocationCallback mLocationCallback, final Context mContext) {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener((Activity) mContext, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        LogMsg.e(TAG, "Location updates stopped");
                    }
                });
    }

    public static String getLocation(double lat, double lang, Context context) {
        try {
            List<Address> addresses;
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(lat, lang, 1);
            String add = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            return add;
        } catch (Exception e) {
            //100 : error convert lat, lon to address
            Toast.makeText(context, "We are not able to find your location", Toast.LENGTH_SHORT).show();
        }
        return "";
    }


    public static String getCompleteAddressString(double latitude, double longitude, Context mContext) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i));
                }
                strAdd = strReturnedAddress.toString();

                if (strAdd.isEmpty()){
                    strAdd = "";
                }
               //Toast.makeText(mContext, strReturnedAddress.toString(), Toast.LENGTH_SHORT).show();
            } else {
                strAdd = "";
                Toast.makeText(mContext, "We are not able to find your location!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            strAdd = "";
            Toast.makeText(mContext, "Can not convert to address "+e.toString(), Toast.LENGTH_SHORT).show();
        }
        return strAdd;
    }


}
