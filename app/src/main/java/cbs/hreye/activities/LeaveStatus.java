package cbs.hreye.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.AnimationEasing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cbs.hreye.pojo.LeaveStatusPojo;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;
import cbs.hreye.R;

public class LeaveStatus extends AppCompatActivity {

    Context context;
    LinearLayout llRoot;
    BarChart chart;
    String success, error;
    ArrayList<BarEntry> valueSet1;
    BarData data;
    String Availed, Balance, Entitled, Leave_type, Opening_bal;
    ArrayList<String> xAxis = new ArrayList<>();
    ArrayList<BarEntry> alBalance = new ArrayList<>();
    ArrayList<BarDataSet> dataSets = new ArrayList<>();
    int [] colors ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_status);
        context = LeaveStatus.this;
        getId();
    }

    private void getId() {
        TextView tvhear = findViewById(R.id.header_text);
        llRoot= findViewById(R.id.ll_root);
        chart =  findViewById(R.id.chart);
        tvhear.setText(R.string.leave_status);
        ImageView ivBack =  findViewById(R.id.header_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (CommonMethods.isOnline(context)) {
            valueSet1 = new ArrayList<>();
            String GRAPH_URL = ConsURL.baseURL(context)+ "HRLoginService/LoginService.svc/GetLeaveStatusDetail?AssoCode="+
                    CommonMethods.getPrefsData(context, PrefrenceKey.ASS_CODE,"")
                    +"&LOCATION_NO="+CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "")
                    +"&COMPANY_NO="+CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "");

            LogMsg.e("GRAPH_URL", GRAPH_URL);
            leaveStatusAPI(GRAPH_URL);
        }else{
            CommonMethods.setSnackBar(llRoot, getString(R.string.net));
        }
    }


    private void leaveStatusAPI(String JSON_URL) {
        CustomProgressbar.showProgressBar(context, false);
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
                        BarEntry entry;

                        for (int i = 0; i < TDetails.length(); i++) {
                            Availed = TDetails.getJSONObject(i).getString("Availed");
                            Balance = TDetails.getJSONObject(i).getString("Balance");
                            Entitled = TDetails.getJSONObject(i).getString("Entitled");
                            Leave_type = TDetails.getJSONObject(i).getString("Leave_type");
                            Opening_bal = TDetails.getJSONObject(i).getString("Opening_bal");
                            xAxis.add(Leave_type);

                            if (Availed.equals("0.0")){
                                entry = new BarEntry(new float[]{Float.parseFloat(Balance)}, i);
                                colors = new  int[]{ContextCompat.getColor(context, R.color.balance)};
                            } else if (Balance.equals("0.0")){
                                entry = new BarEntry(new float[]{Float.parseFloat(Availed)}, i);
                                colors = new  int[]{ContextCompat.getColor(context, R.color.availed)};
                            } else {
                                entry = new BarEntry(new float[]{Float.parseFloat(Availed), Float.parseFloat(Balance)}, i);
                                colors = new  int[]{ContextCompat.getColor(context, R.color.availed),ContextCompat.getColor(context, R.color.balance)};
                            }
                            alBalance.add(entry);
                        }
                        barchartGraph(xAxis);
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
                        Toast.makeText(context, CommonMethods.volleyError(error), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void barchartGraph(ArrayList<String> xAxis) {
        data = new BarData(xAxis, getDataSetBARCHART());
        chart.setData(data);
        chart.setDescription("");
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
       // chart.getXAxis().setDrawGridLines(false);
        chart.setDrawGridBackground(false);
        chart.setHighlightEnabled(false);

        YAxis y = chart.getAxisLeft();

        chart.getAxisRight().setEnabled(false);
      /* y.setAxisMaxValue(200);
        y.setAxisMinValue(20);
        y.setLabelCount(9);*/


        chart.getLegend().setEnabled(true);
        chart.setDoubleTapToZoomEnabled(true);
        chart.setPinchZoom(true);
        chart.setDragEnabled(true);
        chart.setVisibleXRange(10);
        chart.setFitsSystemWindows(true);

        y.setDrawLabels(true);

        YAxis leftAxis = chart.getAxisLeft();
        LimitLine ll = new LimitLine(140f, "");
        ll.setLineColor(Color.TRANSPARENT);
        ll.setLineWidth(4f);
        ll.setTextColor(Color.BLUE);
        ll.setTextSize(12f);
        leftAxis.addLimitLine(ll);

      //  chart.animateX(3000);
        //chart.animateY(3000);
        chart.animateXY(3000, 3000);
        chart.animateY(3000, AnimationEasing.EasingOption.EaseOutBack);
        chart.invalidate();
     // chart.setScaleMinima(1.2f, 1.2f);
        chart.fitScreen();
    }

    private ArrayList<BarDataSet> getDataSetBARCHART() {
        BarDataSet barDataSet = new BarDataSet(alBalance, "Double Tap to view all records");
        barDataSet.setStackLabels(new String[]{"Availed", "Balance"});
        barDataSet.setColors(colors);
        dataSets.add(barDataSet);
        return dataSets;
    }



}
