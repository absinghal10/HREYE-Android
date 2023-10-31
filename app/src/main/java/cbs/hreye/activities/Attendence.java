package cbs.hreye.activities;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import cbs.hreye.R;
import cbs.hreye.databinding.ActivityAttendenceBinding;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.LocationProvider;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;

public class  Attendence extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private static final String TAG = Attendence.class.getSimpleName();
    String time_in, time_out, inFlag = "", outFlag = "";
    String ErrorMsg = "", SuccessDetailMsg = "", MyLocation = "";
    String error = "", success = "", currentTime = "", currentDate = "", time_mode = "", PunchResponseCode = "";
    //  TelephonyManager telephonyManager;
    SettingsClient mSettingsClient;
    LocationRequest mLocationRequest;
    LocationSettingsRequest mLocationSettingsRequest;
    Location mCurrentLocation;

    LocationCallback mLocationCallback;
    LocationManager locationManager;
    private Context context;
    private FusedLocationProviderClient mFusedLocationClient;
    private double latitude, longitude;
    private boolean clickableIN = true, clickableOUT = true, callLoadTimeURL = true;
    private ActivityAttendenceBinding attBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attBinding = DataBindingUtil.setContentView(this, R.layout.activity_attendence);
        context = Attendence.this;
        setClickListener();
    }

    @SuppressLint({"MissingPermission", "HardwareIds", "SimpleDateFormat"})
    private void setClickListener() {
        attBinding.toolbarLayout.headerText.setText(R.string.attendance);
        attBinding.toolbarLayout.headerBack.setOnClickListener(this);
        attBinding.progressUpdate.setOnClickListener(this);
        attBinding.btnIn.setOnClickListener(this);
        attBinding.btnOut.setOnClickListener(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

     /*
      // comment getLine number due to not getting number in android 11.
        deviceID = telephonyManager.getDeviceId()+"-"+telephonyManager.getLine1Number();
        If your app targets Android 11 or higher and needs to access the phone number APIs shown in the following list,
        you must request the READ_PHONE_NUMBERS permission, instead of the READ_PHONE_STATE permission.
        https://developer.android.com/about/versions/11/privacy/permissions#auto-reset
        */
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogMsg.d(TAG, "onResume() called");

        getLatLon();
        if (callLoadTimeURL)
            loadTimeURL();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.progress_update:
                if (mFusedLocationClient != null) {
                    startLocationUpdates();
                }
                break;
            case R.id.header_back:
                onBackPressed();
                break;
            case R.id.btn_in:
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (attBinding.etInTime.getText().toString().trim().length() == 0) {
                        CommonMethods.setSnackBar(attBinding.llRoot, "Time is not available");

                    } else {
                        if (inFlag.equals("0")) {
                            if (CommonMethods.isOnline(context)) {
                                getLastLocation();
                                time_mode = "0";  // in time mode
                                if (!MyLocation.isEmpty()) {
                                    if (clickableIN) {
                                        attendancePunchAPI();
                                    }

                                    clickableIN = true;


                                } else {
                                    Toast.makeText(context, "Please wait, Your location is updating", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                CommonMethods.setSnackBar(attBinding.llRoot, getString(R.string.net));
                            }
                        } else {

                           //CommonMethods.showAlert("You already punched in time for today", Attendence.this);
                            // multiple time repunch
                            attendancePunchAPI();
                        }
                    }
                } else {
                    getLatLon();

                }

                break;


            case R.id.btn_out:
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (outFlag.equals("0")) {
                        if (attBinding.btnIn.getText().toString().equalsIgnoreCase("Punched")) {
                            if (CommonMethods.isOnline(context)) {
                                getLastLocation();
                                time_mode = "0";  // out time mode
                                if (!MyLocation.isEmpty()) {
                                    if (clickableOUT) {
                                        attendancePunchAPI();
                                    }
                                    clickableOUT = false;
                                } else {
                                    Toast.makeText(context, "Please wait, Your location is updating", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                CommonMethods.setSnackBar(attBinding.llRoot, getString(R.string.net));
                            }
                        } else {
                            CommonMethods.showAlert("First punch in today", Attendence.this);
                        }

                    } else {
                        CommonMethods.showAlert("You already punched out time for today", Attendence.this);
                    }
                } else {
                    getLatLon();
                }
                break;
            default:
                break;
        }
    }

    private void loadTimeURL() {
        if (CommonMethods.isOnline(context)) {
            String Time_URL = "http://hbmas.cogniscient.in/HBItemService/ItemServices.svc/GetDateTime";
            LogMsg.e("Time_URL", Time_URL);
            loadTimeService(Time_URL);
        } else {
            CommonMethods.setSnackBar(attBinding.llRoot, getString(R.string.net));
        }
    }

    private void loadTimeService(String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject GetDateTimeResult = jsono.getJSONObject("GetDateTimeResult");
                            JSONObject ItemMessage = GetDateTimeResult.getJSONObject("ItemMessage");
                            success = ItemMessage.optString("Success");
                            error = ItemMessage.optString("ErrorMsg");

                            if (success.equals("true")) {
                                JSONArray DatetimeDetails = GetDateTimeResult.getJSONArray("DatetimeDetails");
                                for (int i = 0; i < DatetimeDetails.length(); i++) {
                                    currentTime = DatetimeDetails.getJSONObject(i).getString("Time");
                                    currentDate = DatetimeDetails.getJSONObject(i).getString("Date");
                                }

                                String URL = ConsURL.baseURL(context) + "AttendanceService/AttendanceService.svc/GetAttendance?" +
                                        "COMPANY_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "") + "&" +
                                        "LOCATION_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "") + "&" +
                                        "ASS_CODE=" + CommonMethods.getPrefsData(Attendence.this, PrefrenceKey.ASS_CODE, "0") + "&" +
                                        "DATE=" + currentDate;

                                LogMsg.e("URL_ATTENDANCE", URL);
                                attendanceTimeService(URL);
                            } else {
                                CommonMethods.setSnackBar(attBinding.llRoot, error);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, CommonMethods.volleyError(error), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,//Socket time out in milies
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    private void attendanceTimeService(String url) {
        if (!isFinishing()) {
            CustomProgressbar.showProgressBar(context, false);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CustomProgressbar.hideProgressBar();
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject AttendanceDataResult = jsono.getJSONObject("AttendanceDataResult");
                            JSONObject Messsage = AttendanceDataResult.getJSONObject("Messsage");
                            error = Messsage.optString("ErrorMsg");
                            success = Messsage.optString("Success");

                            JSONObject Result = AttendanceDataResult.getJSONObject("Result");
                            time_in = Result.optString("IN_TIME");
                            time_out = Result.optString("OUT_TIME");
                            inFlag = Result.optString("INFLAG");
                            outFlag = Result.optString("OUTFLAG");

                            if (success.equals("true")) {
                                getLastLocation();
                                time_mode= "0";
//                                if (inFlag.equals("1") && outFlag.equals("1")) {
                                if (inFlag.equals("1")) {
                                    String second = "", second1 = "";
                                    if (!time_in.isEmpty()) {
                                        StringTokenizer tokens = new StringTokenizer(time_in, " ");
                                        String first = tokens.nextToken();
                                        second = tokens.nextToken();
                                    }
                                    if (!time_out.isEmpty()) {
                                        StringTokenizer tokens1 = new StringTokenizer(time_out, " ");
                                        String first1 = tokens1.nextToken();
                                        second1 = tokens1.nextToken();
                                    }
                                    attBinding.etInTime.setText(CommonMethods.timeFormate1(currentTime));
                                    attBinding.etOutTime.setText(CommonMethods.timeFormate1(currentTime));
                                    attBinding.btnIn.setText(getString(R.string.re_punch));
                                    attBinding.btnOut.setText(getString(R.string.re_punch));
                                } else {
                                    if (inFlag.equals("1")) {
                                        String second = "";
                                        if (!time_in.isEmpty()) {
                                            StringTokenizer tokens = new StringTokenizer(time_in, " ");
                                            String first = tokens.nextToken();
                                            second = tokens.nextToken();
                                        }
                                        attBinding.etInTime.setText(CommonMethods.timeFormate1(currentTime));
                                        attBinding.btnIn.setText(getString(R.string.re_punch));
                                        attBinding.etOutTime.setText(CommonMethods.timeFormate1(currentTime));
                                    } else {
                                        attBinding.etInTime.setText(CommonMethods.timeFormate1(currentTime));
                                        attBinding.etOutTime.setText(CommonMethods.timeFormate1(currentTime));
                                        attBinding.btnIn.setText(getString(R.string.save));
                                    }
                                    attBinding.btnOut.setText(getString(R.string.save));
                                }

                            } else {
                                CommonMethods.setSnackBar(attBinding.llRoot, error);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonMethods.setSnackBar(attBinding.llRoot, e.toString());
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomProgressbar.hideProgressBar();
                        Toast.makeText(context, CommonMethods.volleyError(error), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,//Socket time out in milies
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void getLatLon() {
        // Create the location request to start receiving updates
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(LocationProvider.UPDATE_INTERVAL)
                .setFastestInterval(LocationProvider.FASTEST_INTERVAL)
                .setMaxWaitTime(LocationProvider.FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        mSettingsClient = LocationServices.getSettingsClient(this);
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                onLocationChanged(mCurrentLocation);
            }
        };

        //location is updating here..
        startLocationUpdates();

    }

    private void startLocationUpdates() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                        //   Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
                        showLocationUpdateIcon();
                        onLocationChanged(mCurrentLocation);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        hideLocationUpdateIcon();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                String errorMessage1 = "Location settings are not satisfied. Attempting to upgrade location setting";
                                Log.i(TAG, errorMessage1);
                                //   CommonMethods.setSnackBar(attBinding.llRoot, errorMessage1);
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(Attendence.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                LogMsg.e(TAG, errorMessage);
                        }

                    }
                });
    }


    public void onLocationChanged(Location location) {
        // New location has now been determined
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            String locationAdd = getLocation(latitude, longitude);
            if (!locationAdd.equals("")) {
                MyLocation = locationAdd + " - " + CommonMethods.getDeviceID(context);
            } else {
                MyLocation = "";
            }
            //  Toast.makeText(context, locationAdd, Toast.LENGTH_SHORT).show();
            attBinding.tvShowLocation.setText(locationAdd);

        }
    }


    private String getLocation(double lat, double lang) {
        try {
            List<Address> addresses;
            String add = "";
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(lat, lang, 1);
            if (addresses != null && addresses.size() > 0) {
                add = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
            } else {
                Toast.makeText(context, "We are not able to find your location!", Toast.LENGTH_SHORT).show();
            }
            return add;
        } catch (Exception e) {
            //100 : error convert lat, lon to address
            Toast.makeText(context, "Can not convert to address, Please wait!", Toast.LENGTH_SHORT).show();
        }
        return "";
    }

    @SuppressLint("MissingPermission")
    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        mFusedLocationClient = getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        LogMsg.d("MapDemoActivity", "Error trying to get last GPS location");
                        CommonMethods.setSnackBar(attBinding.llRoot, "Error trying to get GPS location");
                        e.printStackTrace();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check for the integer request code originally supplied to startResolutionForResult().
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    showLocationUpdateIcon();
                    LogMsg.e(TAG, "User agreed to make required location settings changes.");
                    callLoadTimeURL = false;
                    startLocationUpdates();
                    CommonMethods.setSnackBar(attBinding.llRoot, "Now you are ready to punch attendance.");
                    break;
                case Activity.RESULT_CANCELED:
                    hideLocationUpdateIcon();
                    LogMsg.e(TAG, "User chose not to make required location settings changes.");
                    LocationProvider.stopLocationUpdates(mFusedLocationClient, mLocationCallback, context);
                    CommonMethods.setSnackBar(attBinding.llRoot, "You are not able to punch your attendance");
                    break;
            }
        }
    }

    private void attendancePunchAPI() {
        if (!isFinishing()) {
            CustomProgressbar.showProgressBar(context, false);
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("COMPANY_NO", CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, ""));
            jsonObject.put("LOCATION_NO", CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, ""));
            jsonObject.put("ASSO_CODE", CommonMethods.getPrefsData(context, PrefrenceKey.ASS_CODE, "0"));
            jsonObject.put("MODE", time_mode);
            jsonObject.put("DATE", currentDate);
            if (inFlag.equals("0")) {  //in time
                jsonObject.put("IN_TIME", currentDate + " " + attBinding.etInTime.getText().toString());
                jsonObject.put("INFLAG", "1");
                jsonObject.put("OUTFLAG", "0");
                jsonObject.put("IN_TIME_LOCATION", MyLocation);
                jsonObject.put("OUT_TIME_LOCATION", "");
                jsonObject.put("IN_LAT", String.valueOf(latitude));
                jsonObject.put("IN_LONG", String.valueOf(longitude));
                jsonObject.put("OUT_LAT", "0.00");
                jsonObject.put("OUT_LONG", "0.00");
                jsonObject.put("OUT_TIME", "01/01/1912 00:00");
            }
            if (inFlag.equals("1")) {  //out time
                jsonObject.put("OUT_TIME", currentDate + " " + attBinding.etOutTime.getText().toString());
                jsonObject.put("OUTFLAG", "1");
                jsonObject.put("INFLAG", "1");
                jsonObject.put("IN_TIME_LOCATION", MyLocation);
                jsonObject.put("OUT_TIME_LOCATION", MyLocation);
                jsonObject.put("OUT_LAT", String.valueOf(latitude));
                jsonObject.put("OUT_LONG", String.valueOf(longitude));
                jsonObject.put("IN_LAT", "0.00");
                jsonObject.put("IN_LONG", "0.00");
                jsonObject.put("IN_TIME", currentDate + " " + attBinding.etInTime.getText().toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = jsonObject.toString();
        LogMsg.i("mRequestBody", mRequestBody);
        String URL = ConsURL.baseURL(context) + "AttendanceService/AttendanceService.svc/ModifyAttendance";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogMsg.i("Response", response);
                        CustomProgressbar.hideProgressBar();
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject BPSetResult = jsono.getJSONObject("Messsage");
                            ErrorMsg = BPSetResult.optString("ErrorMsg");
                            SuccessDetailMsg = BPSetResult.optString("Success");

                            if (SuccessDetailMsg.equals("true")) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setMessage(ErrorMsg);
                                builder1.setCancelable(false);

                                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        if (time_mode.equals("0")) {
                                            //attBinding.btnIn.setText(getString(R.string.punched));
                                        } else {
                                            attBinding.btnOut.setText(getString(R.string.punched));
                                        }
                                        loadTimeURL();
                                    }
                                });
                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            } else {
                                CommonMethods.showAlert(ErrorMsg, Attendence.this);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonMethods.setSnackBar(attBinding.llRoot, "JSON Exception " + PunchResponseCode + e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressbar.hideProgressBar();
                Toast.makeText(context, CommonMethods.volleyError(error), Toast.LENGTH_LONG).show();
            }

        }) {
            @Override
            public String getBodyContentType() {
                return "Content-Type/text/plain; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return mRequestBody == null ? null : mRequestBody.getBytes(StandardCharsets.UTF_8);
            }

            @
                    Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                PunchResponseCode = String.valueOf(response.statusCode);
                return super.parseNetworkResponse(response);
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }


    @Override
    protected void onPause() {
        super.onPause();
        LogMsg.d(TAG, "onPause() called");
        CustomProgressbar.hideProgressBar();
        hideLocationUpdateIcon();
        LocationProvider.stopLocationUpdates(mFusedLocationClient, mLocationCallback, context);
    }

    private void showLocationUpdateIcon() {
        attBinding.progressUpdate.setVisibility(View.VISIBLE);
        attBinding.ivUpdate.setVisibility(View.INVISIBLE);

    }

    private void hideLocationUpdateIcon() {
        attBinding.progressUpdate.setVisibility(View.INVISIBLE);
        attBinding.ivUpdate.setVisibility(View.VISIBLE);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}