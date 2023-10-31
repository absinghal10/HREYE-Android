package cbs.hreye.activities;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import cbs.hreye.adapters.AttendanceAdapter;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.MonthYearPickerDialog;
import cbs.hreye.utilities.PrefrenceKey;
import cbs.hreye.pojo.AttendancelistPojo;
import cbs.hreye.R;

public class AttendanceList extends AppCompatActivity {

    Context context;
    ListView lstRecords;
    String JSON_URL;
    String error, success;
    TextView rec_date;
    TextView rec_srch;
    ArrayList<AttendancelistPojo> mArray;
    AttendanceAdapter mAttendanceAdapter;
    LinearLayout llRoot, noRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);
        context = AttendanceList.this;
        getID();
    }


    private void getID() {
        llRoot = findViewById(R.id.ll_root);
        TextView tvhear = findViewById(R.id.header_text);
        rec_date = findViewById(R.id.rec_date);
        lstRecords = findViewById(R.id.lst_attendac);
        rec_srch = findViewById(R.id.rec_srch);
        noRecord = findViewById(R.id.ll_no_record);
        tvhear.setText(R.string.atten_records);
        ImageView ivBack = findViewById(R.id.header_back);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        attendanceRecordsAPI(year, month);

        rec_date.setText(CommonMethods.getMonth(month) + " - " + year);

        rec_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MonthYearPickerDialog pd = new MonthYearPickerDialog();
                pd.show(getFragmentManager(), "MonthYearPickerDialog");
            }
        });
    }

    private void attendanceRecordsAPI(int year, int month) {
        if (CommonMethods.isOnline(context)) {
            JSON_URL = ConsURL.baseURL(context) + "AttendanceService/AttendanceService.svc/AttendanceDetail?"
                    + "COMPANY_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "") + "&"
                    + "LOCATION_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "") + "&"
                    + "ASS_CODE=" + CommonMethods.getPrefsData(context, PrefrenceKey.ASS_CODE, "") + "&"
                    + "Year=" + year + "&"
                    + "Month=" + CommonMethods.pad(month);

            LogMsg.d("Load_Attendance_Records", JSON_URL);
            mArray = new ArrayList<>();
            loadAttendance();
        } else {
            CommonMethods.setSnackBar(llRoot, getString(R.string.net));
        }
    }

    public void getResult(int month, int year) {
        rec_date.setText(CommonMethods.getMonth(month) + " - " + year);
        attendanceRecordsAPI(year, month);
    }

    private void loadAttendance() {
        CustomProgressbar.showProgressBar(context, false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CustomProgressbar.hideProgressBar();
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject AttendanceDetailResult = jsono.getJSONObject("AttendanceDetailResult");
                            JSONObject Messsage = AttendanceDetailResult.getJSONObject("Messsage");
                            error = Messsage.optString("ErrorMsg");
                            success = Messsage.optString("Success");

                            if (success.equals("true")) {
                                noRecord.setVisibility(View.GONE);
                                JSONArray ADetail = AttendanceDetailResult.getJSONArray("ADetail");

                                for (int i = 0; i < ADetail.length(); i++) {
                                    AttendancelistPojo pj = new AttendancelistPojo();
                                    pj.setIN_TIME(ADetail.getJSONObject(i).getString("IN_TIME"));
                                    pj.setIN_TIME_LOCATION(ADetail.getJSONObject(i).getString("IN_TIME_LOCATION"));
                                    pj.setOUT_TIME(ADetail.getJSONObject(i).getString("OUT_TIME"));
                                    pj.setOUT_TIME_LOCATION(ADetail.getJSONObject(i).getString("OUT_TIME_LOCATION"));
                                    mArray.add(pj);

                                }

//
//                                for(int i=0; i<mArray.size();i++){
//
//                                    for (int j=i; j<mArray.size();j++){
//                                        String timeI,timeJ;
//                                        String[] timeIArr= mArray.get(i).getIN_TIME().split(" ");
//                                        timeI= timeIArr[0];
//                                        String[] timeJArr= mArray.get(j).getIN_TIME().split(" ");
//                                        timeJ = timeJArr[0];
//                                        if (timeI.equalsIgnoreCase(timeJ)){
//                                            mArray.remove(j);
//                                        }
//                                    }
//                                }

                             //   Collections.reverse(mArray);
                                mAttendanceAdapter = new AttendanceAdapter(context, mArray);
                                lstRecords.setAdapter(mAttendanceAdapter);
                                mAttendanceAdapter.notifyDataSetChanged();
                            } else {
                                lstRecords.setAdapter(null);
                                noRecord.setVisibility(View.VISIBLE);
                                CommonMethods.setSnackBar(llRoot, error);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}