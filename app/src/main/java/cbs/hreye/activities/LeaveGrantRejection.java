package cbs.hreye.activities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.StringTokenizer;

import cbs.hreye.adapters.GrantRejectionAdapter;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PassCheckedListData;
import cbs.hreye.utilities.PrefrenceKey;
import cbs.hreye.pojo.GrantRejectPojo;
import cbs.hreye.R;

public class LeaveGrantRejection extends AppCompatActivity implements View.OnClickListener, PassCheckedListData {
    private Context mContext;
    private TextView tvHeader;
    private Dialog dialog;
    private ImageView ivBack, ivGrant, ivReject;
    private LinearLayout llGrantReject;
    private CheckBox cbSelectAll;
    private RecyclerView rvGrantReject;
    private RecyclerView.LayoutManager mLayoutManager;
    private GrantRejectionAdapter grantRejectionAdapter;
    private LinearLayout llRepunch;
   // private ProgressDialog mProgressDialog;
    private String error, success, grantRejectMsg,errorMsg;
    public static ArrayList<GrantRejectPojo> alGrantRejectList =new ArrayList<>();
    private String appNO, name, status, empCode, leaveType, reason, remarks,  fromDate, toDate, totalDays, applyDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grant_rejection_activity);
        mContext = LeaveGrantRejection.this;
        getID();
    }

    private void getID() {
        tvHeader = findViewById(R.id.header_text);
        ivBack = findViewById(R.id.header_back);
        ivGrant =  findViewById(R.id.header_grant);
        ivReject =  findViewById(R.id.header_reject);
        llGrantReject =  findViewById(R.id.ll_grant_reject);
        rvGrantReject =  findViewById(R.id.rv_grant_reject);
        cbSelectAll = findViewById(R.id.cb_select_all);
        llRepunch =  findViewById(R.id.ll_root);

        ivBack.setOnClickListener(this);
        cbSelectAll.setOnClickListener(this);
        ivGrant.setOnClickListener(this);
        ivReject.setOnClickListener(this);

        tvHeader.setText(R.string.grant_rejection);
        llGrantReject.setVisibility(View.VISIBLE);

        rvGrantReject.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        rvGrantReject.setLayoutManager(mLayoutManager);
        grantRejectionAdapter = new GrantRejectionAdapter(mContext, llGrantReject, this);

        if (CommonMethods.isOnline(mContext)) {
            String JSON_URL = ConsURL.baseURL(mContext) + "GetLeaveCalendar/Service1.svc/GrantRejectData?COMPANY_NO="
                    +CommonMethods.getPrefsData(mContext, PrefrenceKey.COMPANY_NO, "")
                    +"&LOCATION_NO="+CommonMethods.getPrefsData(mContext, PrefrenceKey.LOCATION_NO, "")
                    +"&USER_ID="+CommonMethods.getPrefsData(mContext, PrefrenceKey.USER_ID, "");

            LogMsg.d("GRANT_REJECT_URL", JSON_URL);
            grantRejectList(JSON_URL);
        } else {
            CommonMethods.setSnackBar(llRepunch, getString(R.string.net));
        }
    }

    private void itemListGrantRejectDialog(String grantRejectMsg) {
        final Dialog cDialog = new Dialog(mContext);
        cDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cDialog.setCancelable(true);
        cDialog.setContentView(R.layout.logout);
        TextView txtMsg = cDialog.findViewById(R.id.dlg_msg);
        txtMsg.setText("Sure to "+grantRejectMsg+" Leave?");
        Button cancel = cDialog.findViewById(R.id.cancel);
        cancel.setText(getString(R.string.no));
        Button YES = cDialog.findViewById(R.id.yes);
        YES.setText(R.string.yes);
        YES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cDialog.dismiss();
                for ( int  i = 0; i<alGrantRejectList.size(); i++){
                    appNO = alGrantRejectList.get(i).getAppNo();
                    name = alGrantRejectList.get(i).getName();
                    status = alGrantRejectList.get(i).getStatus();
                    empCode = alGrantRejectList.get(i).getEmpCode();
                    leaveType = alGrantRejectList.get(i).getLeaveType();
                    reason = alGrantRejectList.get(i).getReason();
                    totalDays = alGrantRejectList.get(i).getTotalDays();
                    applyDate = alGrantRejectList.get(i).getApplyDate();
                    StringTokenizer st = new StringTokenizer(alGrantRejectList.get(i).getDurationDate(), "-");
                    fromDate = st.nextToken();
                    toDate = st.nextToken();
                    remarks =alGrantRejectList.get(i).getRemarks();
                    postVolleyRequest(appNO, name, status, empCode, leaveType, reason, remarks,  fromDate, toDate, totalDays, applyDate, i);
                }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_back:
                onBackPressed();
                break;
            case R.id.cb_select_all:
                //this code is use for all items checked.
               /* if (cbSelectAll.isChecked()) {
                    grantRejectionAdapter.selectAll(true);
                } else {
                    grantRejectionAdapter.selectAll(false);
                }*/
                break;
            case R.id.header_grant:
                if (alGrantRejectList.size() > 0) {
                    grantRejectMsg = "Grant";
                    itemListGrantRejectDialog(grantRejectMsg);
                }else {
                    CommonMethods.setSnackBar(llRepunch,"Please select at least one item");
                }
                break;
            case R.id.header_reject:
                if (alGrantRejectList.size() > 0) {
                    grantRejectMsg = "Reject";
                    itemListGrantRejectDialog(grantRejectMsg);
                }else {
                    CommonMethods.setSnackBar(llRepunch,"Please select at least one item");
                }
                break;
            default:
                break;
        }
    }

    private void grantRejectList(String url) {
        CustomProgressbar.showProgressBar(mContext, false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CustomProgressbar.hideProgressBar();
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject grantRejectResult = jsono.getJSONObject("grantRejectResult");
                            JSONObject Msg = grantRejectResult.getJSONObject("Msg");
                            error = Msg.optString("ErrorMsg");
                            success = Msg.optString("Success");

                            if (success.equals("true")) {
                                JSONArray CSTDetails = grantRejectResult.getJSONArray("CSTDetails");
                          for (int i = 0; i < CSTDetails.length(); i++) {
                                    GrantRejectPojo grantRejectPojo = new GrantRejectPojo();
                                    grantRejectPojo.setAppNo(CSTDetails.getJSONObject(i).getString("APPLICATION_CODE"));
                                    grantRejectPojo.setApplyDate(CSTDetails.getJSONObject(i).getString("APPLY_DATE"));
                                    grantRejectPojo.setEmpCode(CSTDetails.getJSONObject(i).getString("ASSO_CODE"));
                                    grantRejectPojo.setName(CSTDetails.getJSONObject(i).getString("ASSO_NAME"));
                                    grantRejectPojo.setReason(CSTDetails.getJSONObject(i).getString("EMPLOYEE_REASON"));
                                    grantRejectPojo.setDurationDate(CSTDetails.getJSONObject(i).getString("FROM_DATE") + " - " + CSTDetails.getJSONObject(i).getString("TO_DATE"));
                                    grantRejectPojo.setLeaveType(CSTDetails.getJSONObject(i).getString("LEAVE_TYPE"));
                                    grantRejectPojo.setStatus(CSTDetails.getJSONObject(i).getString("STATUS"));
                                    grantRejectPojo.setStatusDesc(CSTDetails.getJSONObject(i).getString("STATUS_DESC"));
                                    grantRejectPojo.setTotalDays(CSTDetails.getJSONObject(i).getString("TOTAL_DAYS"));
                                    grantRejectionAdapter.addToArray(grantRejectPojo);
                                }
                                rvGrantReject.setAdapter(grantRejectionAdapter);

                            } else {
                                CommonMethods.setSnackBar(llRepunch, error);
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
                       // CommonMethods.setSnackBar(llRepunch, error.getMessage());
                        Toast.makeText(mContext, CommonMethods.volleyError(error), Toast.LENGTH_LONG).show();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onCheckedList(ArrayList<GrantRejectPojo> grantRejectPojoArrayList) {
        alGrantRejectList = grantRejectPojoArrayList;
    }

    private void postVolleyRequest(String appNO, String name, String status, String empCode, String leaveType, String reason, String remarks, String fromDate, String toDate, String totalDays, String applyDate, final int position){
        if (position == 0) {
            CustomProgressbar.showProgressBar(mContext, false);
        }
        try {
            String grantRejectURL;
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            if (grantRejectMsg.equalsIgnoreCase("Grant")){
                grantRejectURL = ConsURL.baseURL(mContext) + "CBSGrantRejectLeavePost/GrantRejectLeave.svc/SetGrantData";
            }else {
                grantRejectURL = ConsURL.baseURL(mContext)+ "CBSGrantRejectLeavePost/GrantRejectLeave.svc/SetRejectData";
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("COMPANY_NO", CommonMethods.getPrefsData(mContext, PrefrenceKey.COMPANY_NO, ""));
            jsonObject.put("LOCATION_NO", CommonMethods.getPrefsData(mContext, PrefrenceKey.LOCATION_NO, ""));
            jsonObject.put("AUTHORIZE_BY", CommonMethods.getPrefsData(mContext, PrefrenceKey.USER_NAME, ""));
            jsonObject.put("LEAVE_TYPE", leaveType);
            jsonObject.put("ASSO_CODE", empCode);
            jsonObject.put("REASON", reason);
            jsonObject.put("REMARKS", remarks);
            jsonObject.put("FROM_DATE", CommonMethods.changeDateFormateToMMDDYY(fromDate));
            jsonObject.put("TO_DATE", CommonMethods.changeDateFormateToMMDDYY(toDate));
            jsonObject.put("TOTAL_DAYS", totalDays);
            jsonObject.put("APPLICATION_NO",appNO);
            jsonObject.put("STATUS", status);
            jsonObject.put("ASSO_NAME", name);
            jsonObject.put("APPLY_DATE", CommonMethods.changeDateFormateToMMDDYY(applyDate));
            final String requestBody = jsonObject.toString();
            System.out.println("jsonString: " + requestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, grantRejectURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("response", response);
                    JSONObject jsono = null;
                    try {
                        jsono = new JSONObject(response);
                        JSONObject Messsage = jsono.getJSONObject("Message");
                        errorMsg = Messsage.optString("ErrorMsg");
                        success = Messsage.optString("Success");

                        if (position == (alGrantRejectList.size()-1)) {
                           // mProgressDialog.dismiss();
                            CustomProgressbar.hideProgressBar();

                            dialog = new Dialog(mContext);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.logout);

                            TextView txtMsg = dialog.findViewById(R.id.dlg_msg);
                            Button okay = dialog.findViewById(R.id.cancel);
                            Button yes = dialog.findViewById(R.id.yes);
                            yes.setVisibility(View.GONE);
                            okay.setText(getString(R.string.okay));

                            txtMsg.setText(errorMsg);

                            okay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    alGrantRejectList.clear();
                                    finish();
                                }
                            });
                            dialog.show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", error.toString());
                    if (position == (alGrantRejectList.size()-1)){
                        CustomProgressbar.hideProgressBar();
                        dialog = new Dialog(mContext);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.logout);

                        TextView txtMsg = dialog.findViewById(R.id.dlg_msg);
                        txtMsg.setText("Server Error "+error.toString());
                        Button okay = dialog.findViewById(R.id.cancel);
                        Button yes = dialog.findViewById(R.id.yes);
                        yes.setVisibility(View.GONE);
                        okay.setText(getString(R.string.okay));
                        okay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                alGrantRejectList.clear();
                                finish();
                            }
                        });
                        dialog.show();
                    }

                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "Content-Type/text/plain; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return requestBody == null ? null : requestBody.getBytes(StandardCharsets.UTF_8);
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString;
                    responseString = String.valueOf(response.statusCode);
                    LogMsg.e("response", responseString);
                    return super.parseNetworkResponse(response);
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        alGrantRejectList.clear();
    }
}