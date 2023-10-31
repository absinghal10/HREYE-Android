package cbs.hreye.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import cbs.hreye.R;
import cbs.hreye.adapters.AuthModifyAdapter;
import cbs.hreye.adapters.CustomerDetailsAdapter;
import cbs.hreye.pojo.DailyActivityPojo;
import cbs.hreye.pojo.ProjectPojo;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;
import cbs.hreye.utilities.RemoveClickListener;

public class AuthorizedActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private TextView tvHeader;
    private ImageView ivBack, ivRight, ivAdd;
    private LinearLayout  llRepunch, llRightAddRow;
    private RecyclerView rvModAuth;
    private RecyclerView.LayoutManager mLayoutManager;
    private AuthModifyAdapter authModifyAdapter;
    private String URL_AUTH_MOD;
    private String[] arrayHours, arrayStatus;
    private ArrayAdapter<String> adapterHours, adapterStatus, adapterAct;
    private ArrayList<ProjectPojo> alCustList = new ArrayList<>();
    private ArrayList<String> alActCode = new ArrayList<>();
    private ArrayList<String> alActDes = new ArrayList<>();
    private ImageView ivClose;
    private Spinner spnActivityID, spnHours, spnStatus ;
    private EditText etCustID,etProjectID,  etDetails, etRemarks ;
    private Button btnAddTOList;
    private int iLength = 0, editListPos;
    private String activityID, authPostJsonArray, authMod, JSON_PROJECT_URL;
    private RemoveClickListener removeClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorized_activity);
        mContext = AuthorizedActivity.this;
        getID();

        if (CommonMethods.isOnline(mContext)) {
            URL_AUTH_MOD = ConsURL.baseURL(mContext) + "HRLoginService/LoginService.svc/GetDailyReportDatail?"
                    + "UserId=" + CommonMethods.getPrefsData(mContext, PrefrenceKey.USER_ID, "") + "&"
                    + "AssociateCode=" +CommonMethods.getPrefsData(mContext, PrefrenceKey.ASS_CODE, "").replaceAll(" ", "%20") + "&"
                    + "TransactionNo=" +ActivityList.trnNo
                    + "&ServerDate=" +CommonMethods.mobileCurrentDate()
                    + "&ReportingDate=" +CommonMethods.changeDateTOyyyyMMdd(ActivityList.rptDate)
                    + "&SpinFlag=V"
                    +"&CompanyNo="+CommonMethods.getPrefsData(mContext, PrefrenceKey.COMPANY_NO, "")
                    +"&LocationNo="+CommonMethods.getPrefsData(mContext, PrefrenceKey.LOCATION_NO, "");

            loadDetailService(URL_AUTH_MOD);
            LogMsg.d("URL_AUTH_MOD", URL_AUTH_MOD);
        } else {
            CommonMethods.setSnackBar(llRepunch, getString(R.string.net));
        }
    }

    private void getID() {
        tvHeader =  findViewById(R.id.header_text);
        ivBack =  findViewById(R.id.header_back);
        llRightAddRow = findViewById(R.id.ll_grant_reject);
        ivRight =findViewById(R.id.header_grant);
        ivAdd = findViewById(R.id.header_reject);
        llRepunch = findViewById(R.id.ll_root);
        rvModAuth = findViewById(R.id.rv_auth_modify);

        ivBack.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        ivAdd.setOnClickListener(this);


        if (ActivityList.flag.equals("A")){
            llRightAddRow.setVisibility(View.GONE);
            tvHeader.setText(R.string.auth_activity);
        }else {
            llRightAddRow.setVisibility(View.VISIBLE);
            ivAdd.setImageResource(R.drawable.add_row);
            tvHeader.setText(R.string.fresh_activity);
        }

        removeClickListener= new RemoveClickListener() {
            @Override
            public void onRemove(View view, int position, String removeEDit) {
                addToListDialog(removeEDit, position);
            }
        };

        rvModAuth.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        rvModAuth.setLayoutManager(mLayoutManager);
        authModifyAdapter = new AuthModifyAdapter(mContext, removeClickListener);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_back:
                onBackPressed();
                break;
            case R.id.header_grant:  // authorize/modify click event
                authorizeModifyDialog();
                break;
            case R.id.header_reject:  // add row click event
                addToListDialog("add", 0);
                break;
                default:
                    break;
        }
    }


    private void loadDetailService(String url) {
        CustomProgressbar.showProgressBar(mContext, false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CustomProgressbar.hideProgressBar();
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject DailyReportDatailResult = jsono.getJSONObject("DailyReportDatailResult");
                            JSONObject DailyReportMessage = DailyReportDatailResult.getJSONObject("DailyReportMessage");
                            String error = DailyReportMessage.optString("ErrorMsg");
                            String success = DailyReportMessage.optString("Success");

                            if (success.equals("true")) {
                                JSONArray DRDetails = DailyReportDatailResult.getJSONArray("DRDetails");
                                for (int i = 0; i < DRDetails.length(); i++) {
                                    DailyActivityPojo dailyActivityPojo = new DailyActivityPojo();
                                    JSONObject jsonObject = DRDetails.getJSONObject(i);
                                    dailyActivityPojo.setActivity_id(jsonObject.getString("Activity_Id"));
                                    dailyActivityPojo.setActivity(jsonObject.getString("activity"));
                                    dailyActivityPojo.setCustom_id(jsonObject.getString("customer_id"));
                                    dailyActivityPojo.setProject_id(jsonObject.getString("project_id"));
                                    dailyActivityPojo.setTransactionNo(jsonObject.getString("TransactionNo"));
                                    dailyActivityPojo.setActivity_det(jsonObject.getString("activity_details"));
                                    dailyActivityPojo.setHours(jsonObject.getString("total_hours"));
                                    dailyActivityPojo.setStatus(jsonObject.getString("status"));
                                    dailyActivityPojo.setRemarks_one(jsonObject.getString("remarks1"));
                                    authModifyAdapter.addToArray(dailyActivityPojo);
                            }
                                rvModAuth.setAdapter(authModifyAdapter);
                            } else {
                                CommonMethods.setSnackBar(llRepunch, error);
                            }

                            String JSON_URL = ConsURL.baseURL(mContext)+ "HRLoginService/LoginService.svc/GetCustomerDetails?" +
                                    "COMPANY_NO="+CommonMethods.getPrefsData(mContext, PrefrenceKey.COMPANY_NO, "")+
                                    "&LOCATION_NO="+CommonMethods.getPrefsData(mContext, PrefrenceKey.LOCATION_NO, "");

                            LogMsg.e("LOAD_CUSTOMERS", JSON_URL);
                            loadCustomersService(JSON_URL);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomProgressbar.hideProgressBar();
                        Toast.makeText(mContext, CommonMethods.volleyError(error), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,//Socket time out in milies
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void addToListDialog(final String addEdit, final int position){
        final Dialog conDialog = new Dialog(mContext, R.style.MyCustomTheme);
        conDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        conDialog.setCancelable(false);
        conDialog.setContentView(R.layout.dialog_daily_activity);

        //find id here...
         ivClose = conDialog.findViewById(R.id.dlg_cancel);
         etCustID = conDialog.findViewById(R.id.et_customer);
         etProjectID = conDialog.findViewById(R.id.et_project_id);
         spnActivityID = conDialog.findViewById(R.id.spn_act_id);
         spnHours = conDialog.findViewById(R.id.spn_hours);
         spnStatus = conDialog.findViewById(R.id.spn_status);
         etDetails = conDialog.findViewById(R.id.et_details);
         etRemarks = conDialog.findViewById(R.id.et_daily_act_remarks);
         btnAddTOList = conDialog.findViewById(R.id.btn_add);

        etCustID.setFocusable(false);
        etProjectID.setFocusable(false);

        this.arrayHours = new String[]{
                "Hours","00.30", "01.00", "01.30", "02.00", "02.30", "03.00", "03.30", "04.00", "04.30", "05.00",
                "05.30", "06.00", "06.30", "07.00", "07.30", "08.00", "08.30", "09.00", "09.30", "10.00"
        };

        this.arrayStatus = new String[]{
                "Status",
                "Complete",
                "Progress",
                "Pending",
                "Assigned",
                "Not Assigned"
        };

        adapterHours = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_dropdown_item, arrayHours);
         spnHours.setAdapter(adapterHours);

        adapterStatus = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_dropdown_item, arrayStatus);
          spnStatus.setAdapter(adapterStatus);

        if (addEdit.equals("edit")){
            btnAddTOList.setText(getString(R.string.edit_list));

            //Activity and its Id services after edit call.
            editListPos = position;
            JSON_PROJECT_URL = ConsURL.baseURL(mContext) + "HRLoginService/LoginService.svc/ActivityDetails?"
                    + "ProjId=" + authModifyAdapter.getAlAuthModify().get(position).getProject_id() + "&"
                    + "CustId=" + authModifyAdapter.getAlAuthModify().get(position).getCustom_id() + "&"
                    + "ActivityId="
                    +"&LOCATION_NO="+CommonMethods.getPrefsData(mContext, PrefrenceKey.LOCATION_NO, "")
                    +"&COMPANY_NO="+CommonMethods.getPrefsData(mContext, PrefrenceKey.COMPANY_NO, "");

            LogMsg.e("JSON_PROJECT", JSON_PROJECT_URL);
            loadProjectId(addEdit);


            etCustID.setText(authModifyAdapter.getAlAuthModify().get(position).getCustom_id());
            etProjectID.setText(authModifyAdapter.getAlAuthModify().get(position).getProject_id());
            etDetails.setText(authModifyAdapter.getAlAuthModify().get(position).getActivity_det());
            etRemarks.setText(authModifyAdapter.getAlAuthModify().get(position).getRemarks_one());

            arrayStatus = new String[arrayStatus.length];
            arrayHours[position] = authModifyAdapter.getAlAuthModify().get(position).getHours();
            arrayStatus[position] = authModifyAdapter.getAlAuthModify().get(position).getStatus();

            spnHours.setSelection(adapterHours.getPosition(authModifyAdapter.getAlAuthModify().get(position).getHours()));


            switch (arrayStatus[position]) {
                case "0" :
                    spnStatus.setSelection(adapterStatus.getPosition("Complete"));
                    break;
                case "1":
                    spnStatus.setSelection(adapterStatus.getPosition("Progress"));
                    break;
                case "2":
                    spnStatus.setSelection(adapterStatus.getPosition("Pending"));
                    break;
                case "3":
                    spnStatus.setSelection(adapterStatus.getPosition("Assigned"));
                    break;
                case "4":
                    spnStatus.setSelection(adapterStatus.getPosition("Not Assigned"));
                    break;
                default:
                    spnStatus.setSelection(adapterStatus.getPosition(authModifyAdapter.getAlAuthModify().get(position).getStatus()));
                    break;
            }


        }

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conDialog.dismiss();
            }
        });

        etCustID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomerList();
            }
        });

        spnActivityID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activityID = alActCode.get(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        btnAddTOList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidate()) {
                    DailyActivityPojo dailyActivityPojo = new DailyActivityPojo();
                    dailyActivityPojo.setTransactionNo(ActivityList.trnNo);
                    dailyActivityPojo.setCustom_id(etCustID.getText().toString().trim());
                    dailyActivityPojo.setProject_id(etProjectID.getText().toString().trim());
                    dailyActivityPojo.setActivity(spnActivityID.getSelectedItem().toString().trim());
                    dailyActivityPojo.setActivity_id(activityID);
                    dailyActivityPojo.setStatusCheck("1");
                    dailyActivityPojo.setHours(spnHours.getSelectedItem().toString());
                    dailyActivityPojo.setStatus(spnStatus.getSelectedItem().toString());
                    dailyActivityPojo.setActivity_det(etDetails.getText().toString().trim());
                    dailyActivityPojo.setRemarks_one(etRemarks.getText().toString().trim());
                    if (addEdit.equals("edit")){
                        authModifyAdapter.getAlAuthModify().set(position, dailyActivityPojo);
                        rvModAuth.smoothScrollToPosition(position);
                    }else {
                        authModifyAdapter.addToArray(dailyActivityPojo);
                        rvModAuth.smoothScrollToPosition(authModifyAdapter.getItemCount());
                    }

                    rvModAuth.setAdapter(authModifyAdapter);
                    conDialog.dismiss();
                }

            }
        });

        conDialog.show();
    }

    private void loadCustomersService(String url) {
        CustomProgressbar.showProgressBar(mContext, false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CustomProgressbar.hideProgressBar();
                        alCustList.clear();
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject LocationDataResult = jsono.getJSONObject("CustomerDetailsResult");
                            JSONObject CustomerMessage = LocationDataResult.getJSONObject("CustomerMessage");
                            String Error = CustomerMessage.optString("ErrorMsg");
                            String Success = CustomerMessage.optString("Success");

                            if (Success.equals("true")) {
                                JSONArray CSTDetails = LocationDataResult.getJSONArray("CSTDetails");
                                for (int i = 0; i < CSTDetails.length(); i++) {
                                    ProjectPojo pj = new ProjectPojo();
                                    pj.setCUSTOMER_ID(CSTDetails.getJSONObject(i).getString("CUSTOMER_ID").replaceAll("\\s+$", ""));
                                    pj.setCUSTOMER_NAME(CSTDetails.getJSONObject(i).getString("CUSTOMER_NAME").replaceAll("\\s+$", ""));
                                    pj.setPROJECT_ID(CSTDetails.getJSONObject(i).getString("PROJECT_ID").replaceAll("\\s+$", ""));
                                    pj.setPROJECT_NAME(CSTDetails.getJSONObject(i).getString("PROJECT_NAME").replaceAll("\\s+$", ""));
                                    alCustList.add(pj);
                                }
                            } else {
                                Toast.makeText(mContext, Error, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(mContext, CommonMethods.volleyError(error), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    //company list with search.
    private void showCustomerList() {
        try {
            final Dialog custDialog = new Dialog(mContext);
            custDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            custDialog.setContentView(R.layout.customer_details);

            SearchView mSearch = custDialog.findViewById(R.id.search_client);

            ListView custList = custDialog.findViewById(R.id.customer_list);
            final CustomerDetailsAdapter mDet = new CustomerDetailsAdapter(mContext, alCustList);
            custList.setAdapter(mDet);
            mDet.notifyDataSetChanged();

            custList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {



                    if (iLength != 0) {
                        etCustID.setText(CustomerDetailsAdapter.filters.get(position).getCUSTOMER_ID().replaceAll("\\s+", ""));
                        etProjectID.setText(CustomerDetailsAdapter.filters.get(position).getPROJECT_ID().replaceAll("\\s+", ""));

                        JSON_PROJECT_URL = ConsURL.baseURL(mContext) + "HRLoginService/LoginService.svc/ActivityDetails?"
                                + "ProjId=" + CustomerDetailsAdapter.filters.get(position).getPROJECT_ID().replaceAll("\\s+", "") + "&"
                                + "CustId=" + CustomerDetailsAdapter.filters.get(position).getCUSTOMER_ID().replaceAll("\\s+", "") + "&"
                                + "ActivityId="
                                +"&LOCATION_NO="+CommonMethods.getPrefsData(mContext, PrefrenceKey.LOCATION_NO, "")
                                +"&COMPANY_NO="+CommonMethods.getPrefsData(mContext, PrefrenceKey.COMPANY_NO, "");

                    } else {
                        etCustID.setText(alCustList.get(position).getCUSTOMER_ID().replaceAll("\\s+", ""));
                        etProjectID.setText(alCustList.get(position).getPROJECT_ID().replaceAll("\\s+", ""));

                        JSON_PROJECT_URL = ConsURL.baseURL(mContext) + "HRLoginService/LoginService.svc/ActivityDetails?"
                                + "ProjId=" + alCustList.get(position).getPROJECT_ID().replaceAll("\\s+", "") + "&"
                                + "CustId=" + alCustList.get(position).getCUSTOMER_ID().replaceAll("\\s+", "") + "&"
                                + "ActivityId="
                                +"&LOCATION_NO="+CommonMethods.getPrefsData(mContext, PrefrenceKey.LOCATION_NO, "")
                                +"&COMPANY_NO="+CommonMethods.getPrefsData(mContext, PrefrenceKey.COMPANY_NO, "");
                    }
                    LogMsg.e("JSON_PROJECT", JSON_PROJECT_URL);
                    loadProjectId("fresh");
                    iLength = 0;
                    custDialog.dismiss();
                }
            });
            mSearch.setQueryHint("Search Client");
            mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String arg0) {
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    iLength = query.length();
                    mDet.getFilter().filter(query);
                    return false;
                }
            });

            custDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProjectId(final String editValue) {
        CustomProgressbar.showProgressBar(mContext, false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_PROJECT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CustomProgressbar.hideProgressBar();
                        alActCode.clear();
                        alActDes.clear();
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject ActivityDetailsResult = jsono.getJSONObject("ActivityDetailsResult");
                            JSONObject ActivityMessage = ActivityDetailsResult.getJSONObject("ActivityMessage");
                            String Error = ActivityMessage.optString("ErrorMsg");
                            String Success = ActivityMessage.optString("Success");

                            if (Success.equals("true")) {
                                JSONArray ACDetails = ActivityDetailsResult.getJSONArray("ACDetails");
                                for (int i = 0; i < ACDetails.length(); i++) {
                                    alActCode.add(ACDetails.getJSONObject(i).getString("Activity_Id"));
                                    alActDes.add(ACDetails.getJSONObject(i).getString("Activity_Description"));
                                }
                                alActCode.add(0, "Select");
                                alActDes.add(0, "Select");
                                adapterAct = new ArrayAdapter<>(mContext,
                                        android.R.layout.simple_spinner_dropdown_item, alActDes);
                                spnActivityID.setAdapter(adapterAct);
                                adapterAct.notifyDataSetChanged();

                                if (editValue.equals("edit")) {
                                    spnActivityID.setSelection(adapterAct.getPosition(authModifyAdapter.getAlAuthModify().get(editListPos).getActivity()));
                                }

                            } else {
                                Toast.makeText(mContext, Error, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(mContext, CommonMethods.volleyError(error), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,//Socket time out in milies
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    private boolean isValidate() {
        if (etCustID.getText().toString().length() == 0) {
            Toast.makeText(mContext, "Customer ID required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spnActivityID.getSelectedItem().equals("Select")) {
            Toast.makeText(mContext, "Please select Activity ID", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spnHours.getSelectedItem().equals("Hours")) {
            Toast.makeText(mContext, "Please select hours", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spnStatus.getSelectedItem().equals("Status")) {
            Toast.makeText(mContext, "Please select status", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etDetails.getText().toString().length() == 0) {
            etDetails.requestFocus();
            etDetails.setError("Activity Details required");
            return false;
        }
        return true;
    }


    private void authorizeModifyDialog() {
        final Dialog cDialog = new Dialog(mContext);
        cDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cDialog.setCancelable(true);
        cDialog.setContentView(R.layout.logout);
        TextView txtMsg = cDialog.findViewById(R.id.dlg_msg);
        txtMsg.setText(R.string.want_auth_modify);
        Button modify = cDialog.findViewById(R.id.cancel);
        modify.setText(getString(R.string.modify));
        Button Authorize = cDialog.findViewById(R.id.yes);
        Authorize.setText(getString(R.string.authorize));
        Authorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (authModifyAdapter.getItemCount() == 0){
                    CommonMethods.setSnackBar(llRepunch,getString(R.string.enter_minimum_record));
                }else {
                    if (CommonMethods.isOnline(mContext)) {
                        authMod = "A";
                        postAddActivity();
                    } else {
                        CommonMethods.setSnackBar(llRepunch, getString(R.string.net));
                    }
                }
                cDialog.dismiss();
            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (authModifyAdapter.getItemCount() == 0){
                    CommonMethods.setSnackBar(llRepunch,getString(R.string.enter_minimum_record));
                }else {
                    if (CommonMethods.isOnline(mContext)) {
                        authMod = "F";
                        postAddActivity();
                    } else {
                        CommonMethods.setSnackBar(llRepunch, getString(R.string.net));
                    }
                }
                cDialog.dismiss();
            }
        });

        cDialog.show();
    }

    public void postAddActivity() {
        CustomProgressbar.showProgressBar(mContext, false);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject;
        try {
            try {
                int modeLINO = 1;
                jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                if (authModifyAdapter.getItemCount() > 0) {
                    for (int i = 0; i < authModifyAdapter.getItemCount(); i++) {
                        JSONObject jsonob2 = new JSONObject();
                        try {
                            jsonob2.put("LI_NO", String.valueOf(modeLINO));
                            jsonob2.put("CUSTOMER_ID", authModifyAdapter.getAlAuthModify().get(i).getCustom_id());
                            jsonob2.put("PROJECT_ID", authModifyAdapter.getAlAuthModify().get(i).getProject_id());
                            jsonob2.put("Activity_Id", authModifyAdapter.getAlAuthModify().get(i).getActivity_id());
                            jsonob2.put("ACTIVITY", authModifyAdapter.getAlAuthModify().get(i).getActivity());
                            jsonob2.put("ACTIVITY_DETAILS", authModifyAdapter.getAlAuthModify().get(i).getActivity_det());
                            jsonob2.put("TOTAL_HOURS", authModifyAdapter.getAlAuthModify().get(i).getHours());
                            double hours = Double.parseDouble(authModifyAdapter.getAlAuthModify().get(i).getHours()) * 100 / 8.00;
                            jsonob2.put("PERCENTAGE", String.valueOf(hours));
                            String getStatus = authModifyAdapter.getAlAuthModify().get(i).getStatus();

                            switch (getStatus) {
                                case "Complete":
                                    jsonob2.put("STATUS", "0");
                                    break;
                                case "Progress":
                                    jsonob2.put("STATUS", "1");
                                    break;
                                case "Pending":
                                    jsonob2.put("STATUS", "2");
                                    break;
                                case "Assigned":
                                    jsonob2.put("STATUS", "3");
                                    break;
                                case "Not Assigned":
                                    jsonob2.put("STATUS", "4");
                                    break;
                                default:
                                    jsonob2.put("STATUS", getStatus);
                                    break;
                            }

                            jsonob2.put("REMARKS1", authModifyAdapter.getAlAuthModify().get(i).getRemarks_one());
                            jsonob2.put("REMARKS2", "");
                            jsonob2.put("REMARKS3", "");
                            jsonob2.put("user_id", CommonMethods.getPrefsData(mContext, PrefrenceKey.USER_ID, ""));
                            jsonob2.put("Doc_Status", authMod); // F, A
                           // jsonob2.put("Doc_Status", "F");
                            jsonob2.put("ConvertedTime", authModifyAdapter.getAlAuthModify().get(i).getHours());
                            jsonob2.put("CaseId", "0");

                            modeLINO = modeLINO + 1;
                            jsonArray.put(jsonob2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                jsonObject.put("Associate_Name", CommonMethods.getPrefsData(mContext, PrefrenceKey.USER_NAME, ""));
                jsonObject.put("Associate_Code", CommonMethods.getPrefsData(mContext, PrefrenceKey.ASS_CODE, ""));
                jsonObject.put("Transaction_No", ActivityList.trnNo);
                if (authMod.equals("A")) {
                    jsonObject.put("TRANSACTION_MODE", "2");
                } else {
                    jsonObject.put("TRANSACTION_MODE", "1");
                }
                jsonObject.put("COMPANY_NO", CommonMethods.getPrefsData(mContext, PrefrenceKey.COMPANY_NO, ""));
                jsonObject.put("LOCATION_NO", CommonMethods.getPrefsData(mContext, PrefrenceKey.LOCATION_NO, ""));
                jsonObject.put("REPORTING_DATE", CommonMethods.changeDateTOyyyyMMdd(ActivityList.rptDate));
                jsonObject.put("SERVER_DATE", CommonMethods.mobileCurrentDate());

                jsonObject.put("ItemDtail", jsonArray);
                authPostJsonArray = jsonObject.toString(2);
                LogMsg.d("AUTH_MOD_ARRAY", authPostJsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    ConsURL.baseURL(mContext) + "HRLoginService/LoginService.svc/SetAllActivity",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            LogMsg.d("authModResponse", response);
                            CustomProgressbar.hideProgressBar();
                            try {
                                JSONObject jsono = new JSONObject(response);
                                JSONObject Messsage = jsono.getJSONObject("Messsage");
                                String Success = Messsage.optString("Success");
                                String Error = Messsage.optString("ErrorMsg");

                                if (!Success.equals("true")) {
                                    CommonMethods.setSnackBar(llRepunch, Error);
                                } else {
                                   if (authMod.equals("A")) {
                                       String MAIL_URL = ConsURL.baseURL(mContext) + "DailyActivityMailService/AutoMailService.svc/SendEmail?" +
                                               "COMPANY_NO=" + CommonMethods.getPrefsData(mContext, PrefrenceKey.COMPANY_NO, "") +
                                               "&LOCATION_NO=" + CommonMethods.getPrefsData(mContext, PrefrenceKey.LOCATION_NO, "") +
                                               "&tran_no=" + ActivityList.trnNo +
                                               "&USER_ID=" + CommonMethods.getPrefsData(mContext, PrefrenceKey.USER_ID, "") +
                                               "&SERVER_DATE=" + CommonMethods.mobileCurrentDate() +
                                               "&REPORTING_DATE=" + CommonMethods.changeDateTOyyyyMMdd(ActivityList.rptDate) +
                                               "&SPIN_FLAG=V" +
                                               "&ASSOCIATE_CODE=" + CommonMethods.getPrefsData(mContext, PrefrenceKey.ASS_CODE, "") +
                                               "&ASSOCIATE_NAME=" + CommonMethods.getPrefsData(mContext, PrefrenceKey.USER_NAME, "").replaceAll(" ", "%20");

                                       LogMsg.e("MAIL_URL", MAIL_URL);
                                       sendActivityMailService(MAIL_URL);

                                   } else{
                                       alertSucceessMsg(authMod);
                                   }

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressbar.hideProgressBar();
                    Toast.makeText(mContext, CommonMethods.volleyError(error), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "Content-Type/text/plain; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    return authPostJsonArray == null ? null : authPostJsonArray.getBytes(StandardCharsets.UTF_8);
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    return super.parseNetworkResponse(response);
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,-1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendActivityMailService(String mailURL) {
        CustomProgressbar.showProgressBar(mContext, false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, mailURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CustomProgressbar.hideProgressBar();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(mContext, jsonObject.optString("SendEmailResult"), Toast.LENGTH_SHORT).show();
                            alertSucceessMsg(authMod);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomProgressbar.hideProgressBar();
                        Toast.makeText(mContext, CommonMethods.volleyError(error), Toast.LENGTH_SHORT).show();
                        AuthorizedActivity.this.finish();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void alertSucceessMsg(String authMod){
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.logout);
        TextView txtMsg = dialog.findViewById(R.id.dlg_msg);

        if (authMod.equals("A")) {
            txtMsg.setText(getString(R.string.activity_authorized));
        } else {
            txtMsg.setText(getString(R.string.activity_modified));
        }

        Button cancel = dialog.findViewById(R.id.cancel);
        Button yes = dialog.findViewById(R.id.yes);

        cancel.setText(getString(R.string.okay));
        yes.setVisibility(View.GONE);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                AuthorizedActivity.this.finish();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onPause() {
        super.onPause();
        CustomProgressbar.hideProgressBar();
    }

}
