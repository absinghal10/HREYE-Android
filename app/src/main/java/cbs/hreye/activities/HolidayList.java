package cbs.hreye.activities;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import cbs.hreye.adapters.HolidayAdapter;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;
import cbs.hreye.pojo.HolidayPojo;
import cbs.hreye.R;

public class HolidayList extends AppCompatActivity {

    Context context;
    ListView lstHoli;
    String JSON_URL;
    String error, success;
    ArrayList<HolidayPojo> mArray;
    HolidayAdapter holidayAdapter;
    LinearLayout llRepunch;
    int mStatusCode;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_list);

        context = HolidayList.this;
        getID();
    }

    private void getID() {
        llRepunch=  findViewById(R.id.ll_root);
        TextView tvhear = findViewById(R.id.header_text);
        ImageView ivBack = findViewById(R.id.header_back);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);

        tvhear.setText(R.string.holiday_list);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

        lstHoli = findViewById(R.id.lst_holi);

        if (CommonMethods.isOnline(context)) {
            JSON_URL =ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/GetHolidayDatail?" +
                    "YEAR=" + Calendar.getInstance().get(Calendar.YEAR) +"&"+
                    "CALENDER_CODE=" +CommonMethods.getPrefsData(HolidayList.this, PrefrenceKey.CAL_CODE, "")
                    +"&LOCATION_NO="+CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "")
                    +"&COMPANY_NO="+CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "")
                    +"&ASSO_CODE="+CommonMethods.getPrefsData(context,PrefrenceKey.ASS_CODE,"")
            ;

            LogMsg.d("Load_Holidays", JSON_URL);

            mArray = new ArrayList<>();
            loadHoliService();
        } else {
            CommonMethods.setSnackBar(llRepunch, getString(R.string.net));
        }
    }

    private void loadHoliService() {
       // CustomProgressbar.showProgressBar(context, false);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject HolidayDatailResult = jsono.getJSONObject("HolidayDatailResult");
                            JSONObject HolidayMessage = HolidayDatailResult.getJSONObject("HolidayMessage");
                            error = HolidayMessage.optString("ErrorMsg");
                            success = HolidayMessage.optString("Success");

                            if (success.equals("true")) {
                                JSONArray HDDetails = HolidayDatailResult.getJSONArray("HDDetails");

                                for (int i = 0; i < HDDetails.length(); i++) {
                                    HolidayPojo pj = new HolidayPojo();

                                    if (HDDetails.getJSONObject(i).getString("Holiday_Occasion").equals("Fourth Saturday") ||
                                            HDDetails.getJSONObject(i).getString("Holiday_Occasion").equals("Second Saturday")) {
                                    } else {
                                        pj.setHoliday_Date(HDDetails.getJSONObject(i).getString("Holiday_Date").replaceAll("\\s+$", ""));
                                        pj.setHoliday_Day(HDDetails.getJSONObject(i).getString("Holiday_Day"));
                                        pj.setHoliday_Occasion(HDDetails.getJSONObject(i).getString("Holiday_Occasion"));
                                        mArray.add(pj);
                                    }
                                }

                                holidayAdapter = new HolidayAdapter(HolidayList.this, mArray);
                                lstHoli.setAdapter(holidayAdapter);
                                holidayAdapter.notifyDataSetChanged();
                           } else {
                                CommonMethods.setSnackBar(llRepunch, error);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  CustomProgressbar.hideProgressBar();
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        Toast.makeText(context, CommonMethods.volleyError(error), Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
