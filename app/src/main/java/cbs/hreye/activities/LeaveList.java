package cbs.hreye.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import cbs.hreye.R;
import cbs.hreye.adapters.LeaveListAdapter;
import cbs.hreye.pojo.LeaveListPojo;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;

public class LeaveList extends AppCompatActivity {
    Context context;
    String JSON_URL;
    ListView lstLeave;
    ArrayList<LeaveListPojo> mArray;
    LeaveListAdapter mAdap;
    public static String operation;
    public static String eName = "";
    String errorMsg, success, responseText;
    public static String appCode, lvtype, dtFrm, dtTo, frmSess, toSess, rsn, notDate;
    CancelAsyncTask mCancelAsyncTask;
    ImageView mView, ivUpScroll;
    String status, JSON_URL_MAIL;
    String lvTypeFull;
    LinearLayout llRoot;
    TextView tvHeader;
    ShimmerFrameLayout shimmerFrameLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_list);
        context = LeaveList.this;
        getID();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CommonMethods.isOnline(context)) {
            JSON_URL = ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/GetLeaveAppDetail" + "?"
                    + "AppCode=" + "" + "&"
                    + "EmpCode=" + CommonMethods.getPrefsData(context, PrefrenceKey.ASS_CODE, "").replaceAll(" ", "%20")
                    +"&LOCATION_NO="+CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "")
                    +"&COMPANY_NO="+CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "");

            LogMsg.d("LEAVE_LIST", JSON_URL);

            mArray = new ArrayList<>();
            loadActivitesService();
        } else {
            CommonMethods.setSnackBar(llRoot, getString(R.string.net));
        }
    }

    public void getID() {
        llRoot = findViewById(R.id.ll_root);
        mView = findViewById(R.id.add_lv);
        lstLeave =  findViewById(R.id.lst_lv_activities);
        tvHeader = findViewById(R.id.header_text);
        ivUpScroll = findViewById(R.id.iv_scroll_up);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        tvHeader.setText(R.string.leaves);
        ImageView ivBack = findViewById(R.id.header_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LeaveList.this, Leave.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

       lstLeave.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem < 10) {
                    ivUpScroll.setVisibility(View.INVISIBLE);
                }else {
                    ivUpScroll.setVisibility(View.VISIBLE);
                }
            }
        });

        ivUpScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lstLeave.smoothScrollToPosition(0);
            }
        });

        lstLeave.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                appCode = mArray.get(position).getAppl_No();
                lvtype = mArray.get(position).getLeave_type();
                dtFrm = mArray.get(position).getFROM_DATE();
                dtTo = mArray.get(position).getTO_DATE();
                frmSess = mArray.get(position).getFrom_session();
                toSess = mArray.get(position).getTo_session();
                rsn = mArray.get(position).getEmployee_reason();
                notDate = mArray.get(position).getNotified_date();
                status= mArray.get(position).getStatus();
                lvTypeFull= mArray.get(position).getLeave_Decs();

                switch (mArray.get(position).getStatus().trim()) {
                    case "1":
                        break;
                    case "3":
                        cancelLeaveDialog();
                        break;
                    case "4":
                        break;
                    case "2": {
                        cancelLeaveDialog();
                        break;
                    }
                    default: {
                        final Dialog dialog = new Dialog(LeaveList.this);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.leave_dialog);

                        final TextView txtMsg = dialog.findViewById(R.id.dlg_msg);
                        txtMsg.setText(R.string.choose_action);
                        Button lv_mod = dialog.findViewById(R.id.lv_mod);
                        Button lv_auth = dialog.findViewById(R.id.lv_auth);
                        Button lv_can = dialog.findViewById(R.id.lv_can);

                        if (mArray.get(position).getStatus().trim().equals("2")) {
                            lv_mod.setVisibility(View.GONE);
                        } else {
                            lv_mod.setVisibility(View.VISIBLE);
                        }
                        lv_mod.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                operation = "m";

                                Intent i = new Intent(LeaveList.this, AuthorizeLeave.class);
                                 startActivity(i);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                            }
                        });

                        lv_auth.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                operation = "a";

                                Intent i = new Intent(LeaveList.this, AuthorizeLeave.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                            }
                        });

                        lv_can.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                final Dialog cDialog = new Dialog(LeaveList.this);
                                cDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                cDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                cDialog.setCancelable(false);
                                cDialog.setContentView(R.layout.logout);

                                TextView txtMsg = cDialog.findViewById(R.id.dlg_msg);
                                txtMsg.setText(R.string.sure_cancel_leave);
                                Button cancel = cDialog.findViewById(R.id.cancel);
                                cancel.setText(getString(R.string.no));
                                Button yes = cDialog.findViewById(R.id.yes);
                                yes.setText(getString(R.string.yes));
                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cDialog.dismiss();
                                        mCancelAsyncTask = new CancelAsyncTask();
                                        mCancelAsyncTask.execute();
                                    }
                                });
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cDialog.dismiss();
                                    }
                                });
                                cDialog.show();
                            }
                        });
                        dialog.show();
                        break;
                    }
                }
            }
        });

    }

    private void cancelLeaveDialog(){
        final Dialog cDialog = new Dialog(LeaveList.this);
        cDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cDialog.setCancelable(false);
        cDialog.setContentView(R.layout.logout);

        TextView txtMsg = cDialog.findViewById(R.id.dlg_msg);
        txtMsg.setText(R.string.sure_cancel_leave);
        Button cancel = cDialog.findViewById(R.id.cancel);
        cancel.setText(getString(R.string.no));
        Button yes = cDialog.findViewById(R.id.yes);
        yes.setText(getString(R.string.yes));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cDialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cDialog.dismiss();
                if (CommonMethods.isOnline(context)) {
                    mCancelAsyncTask = new CancelAsyncTask();
                    mCancelAsyncTask.execute();
                } else {
                    CommonMethods.setSnackBar(llRoot, getString(R.string.net));
                }
            }
        });
        cDialog.show();
    }

    private void loadActivitesService() {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject LocationDataResult = jsonObject.getJSONObject("LeaveAppDetailResult");
                            JSONObject CustomerMessage = LocationDataResult.getJSONObject("LeaveAppMessage");

                            if (CustomerMessage.optString("Success").equals("true")) {
                                mView.setVisibility(View.VISIBLE);
                                JSONArray LADetails = LocationDataResult.getJSONArray("LADetails");

                                for (int i = 0; i < LADetails.length(); i++) {
                                    LeaveListPojo pj = new LeaveListPojo();

                                    eName = LADetails.getJSONObject(i).getString("ReportingPerson");
                                    pj.setAppl_No(LADetails.getJSONObject(i).getString("Appl_No"));
                                    pj.setFROM_DATE(LADetails.getJSONObject(i).getString("FROM_DATE"));
                                    pj.setTO_DATE(LADetails.getJSONObject(i).getString("TO_DATE"));
                                    pj.setLeave_type(LADetails.getJSONObject(i).getString("Leave_type"));
                                    pj.setLeave_Decs(LADetails.getJSONObject(i).getString("Leave_Decs"));
                                    pj.setFrom_session(LADetails.getJSONObject(i).getString("from_session"));
                                    pj.setTo_session(LADetails.getJSONObject(i).getString("To_session"));
                                    pj.setEmployee_reason(LADetails.getJSONObject(i).getString("employee_reason"));
                                    pj.setStatus(LADetails.getJSONObject(i).getString("status"));
                                    pj.setNotified_date(LADetails.getJSONObject(i).getString("notified_date"));
                                    mArray.add(pj);
                                }
                                Collections.reverse(mArray);
                                mAdap = new LeaveListAdapter(context, mArray);
                                lstLeave.setAdapter(mAdap);
                                mAdap.notifyDataSetChanged();
                            } else {
                                CommonMethods.setSnackBar(llRoot,CustomerMessage.optString("ErrorMsg"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
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

    @SuppressLint("StaticFieldLeak")
    private class CancelAsyncTask extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CustomProgressbar.showProgressBar(context, false);
        }

        @Override
        protected String doInBackground(String... params) {
            postLeaveActivity();
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            CustomProgressbar.hideProgressBar();
            if (!success.equals("true")) {
                CommonMethods.showAlert(errorMsg, LeaveList.this);
            } else {
                final Dialog dialog = new Dialog(LeaveList.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.logout);

                TextView txtMsg = dialog.findViewById(R.id.dlg_msg);
                txtMsg.setText(R.string.leave_canceled_suc);
                Button cancel = dialog.findViewById(R.id.cancel);
                cancel.setVisibility(View.GONE);
                Button yes = dialog.findViewById(R.id.yes);
                yes.setText(getString(R.string.okay));
                yes.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (status.contains("2")){
                            Date frmDt = null, toDt = null,  notDt = null;
                            try {
                                frmDt = new SimpleDateFormat("dd/MM/yyyy").parse(dtFrm);
                                toDt = new SimpleDateFormat("dd/MM/yyyy").parse(dtTo);
                                notDt = new SimpleDateFormat("dd/MM/yyyy").parse(notDate);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String dateFRom = new SimpleDateFormat("yyyy-MM-dd").format(frmDt);
                            String dateTo = new SimpleDateFormat("yyyy-MM-dd").format(toDt);
                            String strNot = new SimpleDateFormat("dd/MM/yyyy").format(notDt);

                            double requestNoLeaves;
                            if (frmSess.equals("W") && toSess.equals("W")){
                                long date=  CommonMethods.calculateDates(context, dateFRom, dateTo);
                                requestNoLeaves=date+1.0;
                            }else {
                                requestNoLeaves=0.5;
                            }

                            JSON_URL_MAIL = ConsURL.baseURL(context) + "DailyActivityMailService/AutoMailService.svc/SendLeaveEmail?" +
                                    "COMPANY_NO=" +CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "")+ "&" +
                                    "LOCATION_NO=" +CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "") + "&" +
                                    "USER_ID=" + CommonMethods.getPrefsData(LeaveList.this, PrefrenceKey.USER_ID, "") + "&" +
                                    "CONDITION_FLAG=C" + "&" +
                                    "ASSO_CODE=" + CommonMethods.getPrefsData(LeaveList.this, PrefrenceKey.ASS_CODE, "") + "&" +
                                    "DESCRIPTION=" + "&" +
                                    "EMP_NAME=" + CommonMethods.getPrefsData(LeaveList.this, PrefrenceKey.USER_NAME, "").replaceAll(" ","%20") + "&" +
                                    "EMP_CODE=" + CommonMethods.getPrefsData(LeaveList.this, PrefrenceKey.ASS_CODE, "") + "&" +
                                    "LEAVE_TYPE=" + lvTypeFull.replaceAll(" ","%20") + "&" +
                                    "DATE_FROM=" + CommonMethods.changeDateTOddMMMyyy(dtFrm) + "&" +
                                    "DATE_TO=" + CommonMethods.changeDateTOddMMMyyy(dtTo)+ "&" +
                                    "REQUEST_NO_LEAVE="+requestNoLeaves+"&" +
                                    "EMP_REASON=" +rsn.replaceAll(" ","%20")+ "&" +
                                    "STATUS=C"+
                                    "&APPLIED_ON="+CommonMethods.changeDateFromMMddyyyy(notDate);
                            LogMsg.d("CANCEL_LEAVE_MAIL", JSON_URL_MAIL);
                            loadEmailService();
                            }
                        finish();

                    }
                });
                dialog.show();
            }
        }
    }

    private void loadEmailService() {
        CustomProgressbar.showProgressBar(context, false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_MAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CustomProgressbar.hideProgressBar();
                        try {
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomProgressbar.hideProgressBar();
                        Toast.makeText(context, CommonMethods.volleyError(error), Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @SuppressLint("SimpleDateFormat")
    public void postLeaveActivity() {
        Date startDate = null, endDate = null;
        String strStart, endStart;
        String URL_DETIAL;
        try {
            startDate = new SimpleDateFormat("dd/MM/yyyy").parse(dtFrm);
            endDate = new SimpleDateFormat("dd/MM/yyyy").parse(dtTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        strStart = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
        endStart = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("COMPANY_NO", CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, ""));
                jsonObject.put("LOCATION_NO", CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, ""));
                jsonObject.put("emp_code", CommonMethods.getPrefsData(LeaveList.this, PrefrenceKey.ASS_CODE, ""));
                jsonObject.put("App_code", appCode);
                jsonObject.put("Leave_type", lvtype);
                jsonObject.put("Notified_date", notDate);
                jsonObject.put("From_date", strStart);
                jsonObject.put("To_date", endStart);
                jsonObject.put("Calender_Code", CommonMethods.getPrefsData(LeaveList.this, PrefrenceKey.CAL_CODE, ""));
                jsonObject.put("From_session", frmSess);
                jsonObject.put("To_session", toSess);
                jsonObject.put("employee_reason", rsn);
                jsonObject.put("employer_reason", "");
                if (status.equals("3")) {
                    jsonObject.put("Status", "5");
                } else {
                    jsonObject.put("Status", "1");
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String jsonStr = jsonObject.toString();
            System.out.println("jsonString : " + jsonStr);

            URL_DETIAL = ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/SetLeaveDataCan";
            getResponse(jsonStr, URL_DETIAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResponse(String data, String url) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost post = new HttpPost(url);
            StringEntity params = new StringEntity(data, "UTF-8");

            post.setHeader(ConsURL.CONTENT_TYPE, ConsURL.APPLICATION_JSON);
            post.setEntity(params);
            HttpResponse response;
            response = httpClient.execute(post);

            StringBuffer stringBuffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), ConsURL.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseText = stringBuffer.toString();
            System.out.println("responseText :" + responseText);
            JSONObject jsono = new JSONObject(responseText);
            JSONObject Messsage = jsono.getJSONObject("LeaveResult");
            errorMsg = Messsage.optString("ErrorMsg");
            success = Messsage.optString("Success");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
