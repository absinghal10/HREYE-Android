package cbs.hreye.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cbs.hreye.R;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;

public class LeaveStatusDemo extends AppCompatActivity {
    private Context mContext;
    private LinearLayout llRoot;
    private TextView tvHeader;
    private ImageView ivBack;
    private BarChart barChart;
    private String leaveType;
    private String success, error;
    private ArrayList<String> leaveTypeArray = new ArrayList<>();
    private ArrayList<BarEntry> availedArrayEntry = new ArrayList<>();
    private ArrayList<BarEntry> balanceArrayEntry = new ArrayList<>();
    private BarEntry availedEntry, balanceEntry;
    private float availed, balance;
    private float totalAvailed, totalBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_status);
        mContext = LeaveStatusDemo.this;
        getId();
    }

    private void getId() {
        barChart = findViewById(R.id.chart);
        llRoot = findViewById(R.id.ll_root);
        tvHeader = findViewById(R.id.header_text);
        tvHeader.setText(R.string.leave_status);
        ivBack = findViewById(R.id.header_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (CommonMethods.isOnline(mContext)) {
            String GRAPH_URL = ConsURL.baseURL(mContext) + "HRLoginService/LoginService.svc/GetLeaveStatusDetail?AssoCode=" +
                    CommonMethods.getPrefsData(mContext, PrefrenceKey.ASS_CODE, "")
                    + "&LOCATION_NO=" + CommonMethods.getPrefsData(mContext, PrefrenceKey.LOCATION_NO, "")
                    + "&COMPANY_NO=" + CommonMethods.getPrefsData(mContext, PrefrenceKey.COMPANY_NO, "");

            LogMsg.e("GRAPH_URL", GRAPH_URL);
            leaveStatusAPI(GRAPH_URL);
        } else {
            CommonMethods.setSnackBar(llRoot, getString(R.string.net));
        }

    }

    private void leaveStatusAPI(String JSON_URL) {
        CustomProgressbar.showProgressBar(mContext, false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressbar.hideProgressBar();
             try {
                 JSONObject jsono = new JSONObject(response);
                 JSONObject LeaveStatusDetailResult = jsono.getJSONObject("LeaveStatusDetailResult");
                 JSONObject GetLeaveStatusMessage = LeaveStatusDetailResult.getJSONObject("GetLeaveStatusMessage");
                 error = GetLeaveStatusMessage.optString("ErrorMsg");
                 success = GetLeaveStatusMessage.optString("Success");
                 if (success.equals("true")) {
                     JSONArray TDetails = LeaveStatusDetailResult.getJSONArray("LSDetails");
                     for (int i = 0; i < TDetails.length(); i++) {
                         availed = Float.parseFloat(TDetails.getJSONObject(i).getString("Availed"));
                         balance = Float.parseFloat(TDetails.getJSONObject(i).getString("Balance"));
                         leaveType = TDetails.getJSONObject(i).getString("Leave_type");

                         balanceEntry = new BarEntry(new float[]{balance}, i);
                         availedEntry = new BarEntry(new float[]{availed}, i);

                         totalAvailed = totalAvailed + availed;
                         totalBalance = totalBalance + balance;

                         leaveTypeArray.add(leaveType);
                         balanceArrayEntry.add(balanceEntry);
                         availedArrayEntry.add(availedEntry);
                     }
                     setBarData(leaveTypeArray);

                 } else {
                     CommonMethods.setSnackBar(llRoot, error);
                 }
             } catch (JSONException e){
                 e.printStackTrace();
             }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomProgressbar.hideProgressBar();
                        Toast.makeText(mContext, CommonMethods.volleyError(error), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void  setBarData(ArrayList<String> leaveTypeArray) {
        BarData data = new BarData(leaveTypeArray, getDataSet());
        barChart.setData(data);
        barChart.setDescription("");
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.setDrawGridBackground(false);
        barChart.setHighlightEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(true);
        barChart.setDoubleTapToZoomEnabled(true);
        barChart.setPinchZoom(true);
        barChart.setDragEnabled(true);
        barChart.setVisibleXRange(10);
        barChart.setFitsSystemWindows(true);
        barChart.animateXY(2000, 2000);
        barChart.invalidate();
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets;
        BarDataSet barDataSet1 = new BarDataSet(balanceArrayEntry, "Total Balance " + totalBalance);
        barDataSet1.setColor(ContextCompat.getColor(mContext, R.color.balance));
        barDataSet1.setBarSpacePercent(-2f);

        BarDataSet barDataSet2 = new BarDataSet(availedArrayEntry, "Total Availed  " + totalAvailed);
        barDataSet2.setColor(ContextCompat.getColor(mContext, R.color.availed));
        barDataSet2.setBarSpacePercent(-2f);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
