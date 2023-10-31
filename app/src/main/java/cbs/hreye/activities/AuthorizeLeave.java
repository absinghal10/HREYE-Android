package cbs.hreye.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;
import cbs.hreye.pojo.LeaveStatusPojo;
import cbs.hreye.R;

public class AuthorizeLeave extends AppCompatActivity {

    Context context;
    TextView txtDateFrom, txtDateTo;
    Spinner spnType, spnFrom, spnTo;
    EditText etReason, etEmp, etRep;
    Button btnApply;
    LinearLayout llRoot;
    SimpleDateFormat sdf;
    public int mYear, mMonth, mDay;
    private String[] arraySessionId;
    private String[] arraySession;
    String lvType, lvTypeFull, JSON_URL_MAIL, snFrom, snTo;
    PostDataAsyncTask mPostDataAsyncTask;
    EditText et_bal;
    String success = "", errorMsg = "", responseText;
    String strFrm = "", strTo = "", strNot = "";
    TextView tvHeader;
    ImageView ivBack;
    Date frmDt = null, toDt = null, notDt = null;
    ArrayList<String> alLeaveType = new ArrayList<>();
    ArrayList<LeaveStatusPojo> alLeaveTypeFull = new ArrayList<>();
    ArrayAdapter<LeaveStatusPojo> adapLeaveStatusDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize_leave);
        context = AuthorizeLeave.this;
        getId();
    }

    public void getId() {
        et_bal = findViewById(R.id.et_bal);
        spnType = findViewById(R.id.spn_auth_type);
        txtDateFrom = findViewById(R.id.txt_auth_from);
        txtDateTo = findViewById(R.id.txt_auth_to);

        spnFrom = findViewById(R.id.spn_auth_one);
        spnTo = findViewById(R.id.spn_auth_two);
        etReason = findViewById(R.id.et_auth_rsn);
        btnApply = findViewById(R.id.btn_lv_auth);
        etEmp = findViewById(R.id.txt_emp);
        etRep = findViewById(R.id.txt_rep);
        llRoot = findViewById(R.id.ll_root);
        tvHeader = findViewById(R.id.header_text);
        ivBack = findViewById(R.id.header_back);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtDateFrom.setText(LeaveList.dtFrm);
        txtDateTo.setText(LeaveList.dtTo);
        etRep.setText(LeaveList.eName);
        etEmp.setText(CommonMethods.getPrefsData(this, PrefrenceKey.USER_NAME, ""));

        if (LeaveList.operation.equals("m")) {
            tvHeader.setText(R.string.modify_leave);
            btnApply.setText(R.string.modify_leave);
        } else {
            tvHeader.setText(R.string.submit_leave);
            btnApply.setText(R.string.submit);
        }

        if (LeaveList.rsn != null) {
            etReason.setText(LeaveList.rsn.trim());
        }

        txtDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker = new DatePickerDialog(AuthorizeLeave.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        Calendar c = Calendar.getInstance();
                        c.set(selectedyear, selectedmonth, selectedday);
                        sdf = new SimpleDateFormat("dd/MM/yyyy");
                        txtDateFrom.setText(CommonMethods.pad(selectedday) + "/" + CommonMethods.pad(selectedmonth + 1) +
                                "/" + CommonMethods.pad(selectedyear));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setCalendarViewShown(false);
                mDatePicker.show();
            }
        });

        txtDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker = new DatePickerDialog(AuthorizeLeave.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        Calendar c = Calendar.getInstance();
                        c.set(selectedyear, selectedmonth, selectedday);
                        sdf = new SimpleDateFormat("dd/MM/yyyy");
                        txtDateTo.setText(CommonMethods.pad(selectedday) + "/" + CommonMethods.pad(selectedmonth + 1) +
                                "/" + CommonMethods.pad(selectedyear));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setCalendarViewShown(false);
                mDatePicker.show();
            }
        });

   String BAL_URL = ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/GetLeaveStatusDetail" + "?"
                + "AssoCode=" + CommonMethods.getPrefsData(context, PrefrenceKey.ASS_CODE, "")
                +"&LOCATION_NO="+CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "")
                +"&COMPANY_NO="+CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "");

        LogMsg.e("BALANCE_URL", BAL_URL);
        new BalAsyncTask().execute(BAL_URL);

        this.arraySession = new String[]{
                "Whole Day", "First Session", "Second Session"
        };

        this.arraySessionId = new String[]{
                "W", "F", "A"
        };

        ArrayAdapter<String> adaptSess = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, arraySession);
        spnFrom.setAdapter(adaptSess);
        spnTo.setAdapter(adaptSess);

        ArrayAdapter<String> arrSessId = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, arraySessionId);

        if (LeaveList.frmSess != null) {
            int spnFrmSess = arrSessId.getPosition(LeaveList.frmSess.trim());
            spnFrom.setSelection(spnFrmSess);
        }

        if (LeaveList.toSess != null) {
            int spnToSess = arrSessId.getPosition(LeaveList.toSess.trim());
            spnTo.setSelection(spnToSess);
        }


        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    et_bal.setText(alLeaveTypeFull.get(position).getBalance());
                    lvType = alLeaveTypeFull.get(position).getLeave_type();
                    lvTypeFull = alLeaveTypeFull.get(position).getLeave_Decs();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                snFrom = arraySessionId[position];
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                snTo = arraySessionId[position];
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {
                    if (CommonMethods.isOnline(context)) {
                        mPostDataAsyncTask = new PostDataAsyncTask();
                        mPostDataAsyncTask.execute();
                    } else {
                        CommonMethods.setSnackBar(llRoot, getString(R.string.net));
                    }
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class BalAsyncTask extends AsyncTask<String, Void, Boolean> {
        JSONArray LSDetails;
        JSONObject joLSDetails;
        String Success, error;
        String leaveType;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CustomProgressbar.showProgressBar(context, false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject jsono = new JSONObject(data);
                    JSONObject LeaveStatusDetailResult = jsono.getJSONObject("LeaveStatusDetailResult");
                    JSONObject GetLeaveStatusMessage = LeaveStatusDetailResult.getJSONObject("GetLeaveStatusMessage");
                    Success = GetLeaveStatusMessage.optString("Success");
                    error = GetLeaveStatusMessage.optString("ErrorMsg");
                    if (Success.equals("true")) {
                        LSDetails = LeaveStatusDetailResult.getJSONArray("LSDetails");
                        LeaveStatusPojo leaveStatusPojo;
                        for (int i = 0; i < LSDetails.length(); i++) {
                            joLSDetails = LSDetails.getJSONObject(i);
                            leaveStatusPojo = new LeaveStatusPojo();
                            leaveType = joLSDetails.getString("Leave_type");
                            leaveStatusPojo.setLeave_type(joLSDetails.getString("Leave_type"));
                            leaveStatusPojo.setBalance(joLSDetails.getString("Balance"));
                            leaveStatusPojo.setLeave_Decs(joLSDetails.getString("Leave_Decs"));
                            alLeaveType.add(leaveType);
                            alLeaveTypeFull.add(leaveStatusPojo);
                        }
                    } else {
                        CommonMethods.setSnackBar(llRoot, error);
                    }
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            CustomProgressbar.hideProgressBar();
            if (Success.equals("true")) {
                adapLeaveStatusDetails = new ArrayAdapter<>(context,
                        android.R.layout.simple_spinner_dropdown_item, alLeaveTypeFull);
                spnType.setAdapter(adapLeaveStatusDetails);

                ArrayAdapter<String> adapLeaveID = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, alLeaveType);

                if (LeaveList.lvtype != null) {
                    int spinnerPosition = adapLeaveID.getPosition(LeaveList.lvtype);
                    spnType.setSelection(spinnerPosition);
                }
            } else {
                CommonMethods.setSnackBar(llRoot, error);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class PostDataAsyncTask extends AsyncTask<String, String, String> {

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
                CommonMethods.showAlert(errorMsg, AuthorizeLeave.this);
            } else {
                final Dialog dialog = new Dialog(AuthorizeLeave.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.logout);

                TextView txtMsg = dialog.findViewById(R.id.dlg_msg);
                if (LeaveList.operation.equals("m")) {
                    txtMsg.setText(R.string.leave_modified);
                } else {
                    txtMsg.setText(R.string.leave_submitted);
                }

                Button cancel = dialog.findViewById(R.id.cancel);
                cancel.setVisibility(View.GONE);
                Button yes = dialog.findViewById(R.id.yes);
                yes.setText(R.string.okay);
                yes.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public void onClick(View v) {
                        if (LeaveList.operation.equals("m")) {
                            dialog.dismiss();
                            finish();
                        } else {
                            dialog.dismiss();
                            strFrm = new SimpleDateFormat("dd-MMM-yyyy").format(frmDt);
                            strTo = new SimpleDateFormat("dd-MMM-yyyy").format(toDt);
                            strNot = new SimpleDateFormat("dd-MMM-yyyy").format(notDt);

                            double requestNoLeaves;
                            String fromDate = txtDateFrom.getText().toString();
                            String toDate = txtDateTo.getText().toString();

                            if (snFrom.equals("W") && snTo.equals("W")) {
                                long date = CommonMethods.calculateDates(context, fromDate, toDate);
                                requestNoLeaves = date + 1.0;
                            } else {
                                requestNoLeaves = 0.5;
                            }

                            String empReason = etReason.getText().toString().trim();
                            JSON_URL_MAIL = ConsURL.baseURL(context) + "DailyActivityMailService/AutoMailService.svc/SendLeaveEmail?" +
                                    "COMPANY_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "") + "&" +
                                    "LOCATION_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "") + "&" +
                                    "USER_ID=" + CommonMethods.getPrefsData(AuthorizeLeave.this, PrefrenceKey.USER_ID, "") + "&" +
                                    "CONDITION_FLAG=A" + "&" +
                                    "ASSO_CODE=" + CommonMethods.getPrefsData(AuthorizeLeave.this, PrefrenceKey.ASS_CODE, "") + "&" +
                                    "DESCRIPTION=" + "&" +
                                    "EMP_NAME=" + CommonMethods.getPrefsData(AuthorizeLeave.this, PrefrenceKey.USER_NAME, "").replaceAll(" ", "%20") + "&" +
                                    "EMP_CODE=" + CommonMethods.getPrefsData(AuthorizeLeave.this, PrefrenceKey.ASS_CODE, "") + "&" +
                                    "LEAVE_TYPE=" + lvTypeFull.replaceAll(" ", "%20") + "&" +
                                    "DATE_FROM=" + strFrm + "&" +
                                    "DATE_TO=" + strTo + "&" +
                                    "REQUEST_NO_LEAVE=" + requestNoLeaves + "&" +
                                    "EMP_REASON=" + empReason.replaceAll(" ", "%20") + "&" +
                                    "STATUS=A" + "&APPLIED_ON=" + strNot;
                            LogMsg.d("LEAVE_AUTH_MAIL", JSON_URL_MAIL);
                            loadEmailService();
                        }
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
                        try {
                            CustomProgressbar.hideProgressBar();
                            finish();

                        } catch (Exception e) {
                            e.printStackTrace();
                            CustomProgressbar.hideProgressBar();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomProgressbar.hideProgressBar();
                        finish();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,//Socket time out in milies
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @SuppressLint("SimpleDateFormat")
    public void postLeaveActivity() {
        String URL_DETIAL;
        try {
            frmDt = new SimpleDateFormat("dd/MM/yyyy").parse(txtDateFrom.getText().toString().trim());
            toDt = new SimpleDateFormat("dd/MM/yyyy").parse(txtDateTo.getText().toString().trim());
            notDt = new SimpleDateFormat("MM/dd/yyyy").parse(LeaveList.notDate.trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        strFrm = new SimpleDateFormat("yyyy-MM-dd").format(frmDt);
        strTo = new SimpleDateFormat("yyyy-MM-dd").format(toDt);
        strNot = new SimpleDateFormat("yyyy-MM-dd").format(notDt);

        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("COMPANY_NO",  CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, ""));
                jsonObject.put("LOCATION_NO",  CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, ""));
                jsonObject.put("emp_code", CommonMethods.getPrefsData(AuthorizeLeave.this, PrefrenceKey.ASS_CODE, ""));
                jsonObject.put("App_code", LeaveList.appCode);
                jsonObject.put("Leave_type", lvType);
                jsonObject.put("Notified_date", strNot);
                jsonObject.put("From_date", strFrm);
                jsonObject.put("To_date", strTo);
                jsonObject.put("Calender_Code", CommonMethods.getPrefsData(AuthorizeLeave.this, PrefrenceKey.CAL_CODE, ""));
                jsonObject.put("From_session", snFrom);
                jsonObject.put("To_session", snTo);
                jsonObject.put("employee_reason", etReason.getText().toString().trim());
                jsonObject.put("employer_reason", "");
                if (LeaveList.operation.equals("m")) {
                    jsonObject.put("Status", "0");
                } else {
                    jsonObject.put("Status", "2");
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String jsonStr = jsonObject.toString();
            System.out.println("jsonString: " + jsonStr);

            if (LeaveList.operation.equals("m")) {
                URL_DETIAL = ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/SetLeaveDataModify";
                getResponse(jsonStr, URL_DETIAL);
            } else {
                URL_DETIAL = ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/SetLeaveDataAuth";
                getResponse(jsonStr, URL_DETIAL);
            }
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

            String line = null;
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

    private boolean isValidate() {

        if (etReason.getText().toString().length() == 0) {
            CommonMethods.setSnackBar(llRoot, "Reason required");
            return false;
        }
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        CustomProgressbar.hideProgressBar();
    }
}
