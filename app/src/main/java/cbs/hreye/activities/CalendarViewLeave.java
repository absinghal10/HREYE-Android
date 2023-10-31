package cbs.hreye.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import cbs.hreye.R;
import cbs.hreye.adapters.CalendarAdapter;
import cbs.hreye.pojo.CalenderPojo;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;

public class CalendarViewLeave extends Activity {

    Context context;
    public GregorianCalendar month, itemMonth;
    GridView gridview;
    TextView title;
    ImageView ivPrevious, ivNext, ivBack;
    public CalendarAdapter adapter;
    public Handler handler;
    public ArrayList<String> items;
    public ArrayList<String> calDates;
    public ArrayList<CalenderPojo> calList;
    LinearLayout rLayout;
    ArrayList<String> date;
    ArrayList<String> desc;
    String error, success;
    TextView tvView, tvHeader;
    String JSON_URL;
    LinearLayout llRoot;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_calnedar);

        getID();

        Locale.setDefault(Locale.US);
        tvHeader.setText(R.string.leave_cal);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        itemMonth = (GregorianCalendar) month.clone();

        Calendar c= Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH)+1;

        calendarURL(String.valueOf(cmonth), String.valueOf(cyear));

        items = new ArrayList<>();
        calDates = new ArrayList<>();
        calList = new ArrayList<>();

        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        ivPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });


        ivNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();
            }
        });


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if (rLayout.getChildCount() > 0) {
                    //((LinearLayout) rLayout).removeAllViews();
                }
                desc = new ArrayList<>();
                date = new ArrayList<>();
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalendarAdapter.dayString
                        .get(position);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*",
                        "");// taking last part of date. ie; 2 from 2012-12-02.
                int gridvalue = Integer.parseInt(gridvalueString);
                // navigate to next or previous month on clicking offdays.
                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);


                if (desc.size() > 0) {
                    for (int i = 0; i < desc.size(); i++) {
                        TextView rowTextView = new TextView(CalendarViewLeave.this);
                        // set some properties of rowTextView or something
                        rowTextView.setText("Event:" + desc.get(i));
                        rowTextView.setTextColor(Color.BLACK);
                        // add the textview to the linearlayout
                        rLayout.addView(rowTextView);
                    }
                }
                desc = null;

                if (calDates.contains(selectedGridDate)) {
                    int posi = calDates.indexOf(selectedGridDate);
                    tvView.setText(calList.get(posi).getOCCASION());
                } else {
                    tvView.setText("");
                }
            }
        });

    }

    private void getID(){
        context = CalendarViewLeave.this;
        llRoot = findViewById(R.id.ll_root);
        tvView = findViewById(R.id.tvView);
        title = findViewById(R.id.title);
        ivPrevious = findViewById(R.id.iv_previous);
        ivNext = findViewById(R.id.iv_next);
        gridview = findViewById(R.id.gridview);
        rLayout = findViewById(R.id.text);
        tvHeader = findViewById(R.id.header_text);
        ivBack = findViewById(R.id.header_back);
    }

    private void calendarURL(String cmonth, String cyear){
        tvView.setText("");
        JSON_URL = ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/GetGetLeaveCalDetail?AssoCode="+
                CommonMethods.getPrefsData(CalendarViewLeave.this, PrefrenceKey.ASS_CODE,"")+"&"+
                "Month="+ cmonth + "&"+
                "Year=" +cyear
                +"&LOCATION_NO="+CommonMethods.getPrefsData(CalendarViewLeave.this, PrefrenceKey.LOCATION_NO,"")
                +"&COMPANY_NO="+CommonMethods.getPrefsData(CalendarViewLeave.this, PrefrenceKey.COMPANY_NO,"");

        LogMsg.e("Calender_View",JSON_URL);
        loadCalService(JSON_URL);
    }
    @SuppressLint("SimpleDateFormat")
    private void loadCalService(String JSON_URL) {
        CustomProgressbar.showProgressBar(context, false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CustomProgressbar.hideProgressBar();
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject LeaveCalDetailResult = jsono.getJSONObject("LeaveCalDetailResult");
                            JSONObject GetLeaveCalMessage = LeaveCalDetailResult.getJSONObject("GetLeaveCalMessage");
                            error = GetLeaveCalMessage.optString("ErrorMsg");
                            success = GetLeaveCalMessage.optString("Success");

                            if (success.equals("true")) {
                                JSONArray LCDetails = LeaveCalDetailResult.getJSONArray("LCDetails");

                                for (int i = 0; i < LCDetails.length(); i++) {
                                    CalenderPojo pj = new CalenderPojo();

                                  SimpleDateFormat input = new SimpleDateFormat("dd/MM/yy");
                                    SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
                                    String sDate=LCDetails.getJSONObject(i).getString("HOL_DATE");
                                    try {
                                        if (sDate.equals("null")){
                                            Date leaveDate = input.parse(LCDetails.getJSONObject(i).getString("LEAVE_DATE"));                 // parse input
                                            String lDate = output.format(leaveDate);
                                            calDates.add(lDate);    // format output
                                            pj.setDATE(lDate);
                                        }else {
                                            Date convertDate = input.parse(LCDetails.getJSONObject(i).getString("HOL_DATE"));                 // parse input
                                            sDate = output.format(convertDate);
                                            calDates.add(sDate);    // format output
                                            pj.setDATE(sDate);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if(sDate.equals("null")){
                                        pj.setCOLOR_CODE(LCDetails.getJSONObject(i).getString("LEAVE_COLOR_CODE"));
                                        pj.setDATE(LCDetails.getJSONObject(i).getString("LEAVE_DATE"));
                                        pj.setOCCASION(LCDetails.getJSONObject(i).getString("LEAVE_TYPE"));
                                        calList.add(pj);
                                    }else {
                                        pj.setCOLOR_CODE(LCDetails.getJSONObject(i).getString("HOL_COLOR_CODE"));
                                        pj.setDATE(LCDetails.getJSONObject(i).getString("HOL_DATE"));
                                        pj.setOCCASION(LCDetails.getJSONObject(i).getString("OCCASION"));
                                        calList.add(pj);
                                    }
                                }
                            } else {
                                CommonMethods.setSnackBar(llRoot, error);
                            }
                            adapter = new CalendarAdapter(context, month);
                            gridview.setAdapter(adapter);

                            handler = new Handler();
                            handler.post(calendarUpdater);
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


    protected void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month.getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    protected void setPreviousMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
    }


    public void refreshCalendar() {
        TextView title = findViewById(R.id.title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        handler.post(calendarUpdater); // generate some calendar items

        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
        calendarURL(android.text.format.DateFormat.format("MM", month).toString(), android.text.format.DateFormat.format("yyyy", month).toString());
    }

    public Runnable calendarUpdater = new Runnable() {

        @Override
        public void run() {
            items.clear();
            // Print dates of the current week
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
          //  String itemvalue;
            items.addAll(calDates);
//			event = Utility.readCalendarEvent(CalendarView.this);
//			Log.d("=====Event====", event.toString());
//			Log.d("=====Date ARRAY====", Utility.startDates.toString());
//
//			for (int i = 0; i < Utility.startDates.size(); i++) {
//				itemvalue = df.format(itemmonth.getTime());
//				itemmonth.add(GregorianCalendar.DATE, 1);
//				items.add(Utility.startDates.get(i).toString());
//			}
            adapter.setItems(items);
            adapter.setItems2(calList);
            adapter.notifyDataSetChanged();
        }
    };
}