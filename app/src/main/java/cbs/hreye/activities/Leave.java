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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cbs.hreye.R;
import cbs.hreye.pojo.LeaveStatusPojo;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;

public class Leave extends AppCompatActivity {
    Context context;
    Spinner spnLeaveType;
    TextView txtDateFrom;
    TextView txtDateTo;
    TextView tvHeader;
    ImageView ivBack, ivGrant, ivReject;
    Spinner spnFrom;
    Spinner spnTo;
    EditText etReason, etEmp, etRep, etBal;
    Button btnApply;
    SimpleDateFormat sdf;
    public int mYear, mMonth, mDay;
    private String[] arraySessionId;
    private String[] arraySession;
    String lvType, snFrom, snTo;
    String responseText, errorMsg, success;
    SaveDataAsyncTask mSaveDataAsyncTask;
    LinearLayout llRoot, llGrantReject;
    String repPerson;
    ArrayList<LeaveStatusPojo> alLeaveType = new ArrayList<>();
    ArrayAdapter<LeaveStatusPojo> leaveTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        context = Leave.this;
        getID();
    }

    private void getID() {
        etBal = findViewById(R.id.et_bal);
        spnLeaveType = findViewById(R.id.spn_type);
        txtDateFrom = findViewById(R.id.txt_from);
        txtDateTo = findViewById(R.id.txt_to);
        spnFrom = findViewById(R.id.spn_one);
        spnTo = findViewById(R.id.spn_two);
        etReason = findViewById(R.id.et_rsn);
        btnApply =  findViewById(R.id.btn_lv_apply);
        llRoot = findViewById(R.id.ll_root);
        tvHeader = findViewById(R.id.header_text);
        ivBack = findViewById(R.id.header_back);
        etEmp = findViewById(R.id.txt_emp);
        etRep = findViewById(R.id.txt_rep);


   /*  llGrantReject = findViewById(R.id.ll_grant_reject);
        ivGrant = findViewById(R.id.header_grant);
        ivReject = findViewById(R.id.header_reject);*/

       /* llGrantReject.setVisibility(View.VISIBLE);
        ivReject.setVisibility(View.GONE);*/

        tvHeader.setText(R.string.apply_leave);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtDateFrom.setText(CommonMethods.changeDateFromyyyyMMdd(CommonMethods.mobileCurrentDate()));
        txtDateTo.setText(CommonMethods.changeDateFromyyyyMMdd(CommonMethods.mobileCurrentDate()));


        repPerson = CommonMethods.getPrefsData(context, PrefrenceKey.REP_PERSON, "");
        if (!repPerson.equals("")) {
            etRep.setText(repPerson);
        } else {
            etRep.setText(R.string.not_available);
        }
        etEmp.setText(CommonMethods.getPrefsData(this, PrefrenceKey.USER_NAME, ""));

        txtDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker = new DatePickerDialog(Leave.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
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
                DatePickerDialog mDatePicker = new DatePickerDialog(Leave.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
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

        String BAL_URL = ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/GetLeaveStatusDetail?"
                + "AssoCode=" + CommonMethods.getPrefsData(context, PrefrenceKey.ASS_CODE, "")
                + "&LOCATION_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "")
                + "&COMPANY_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "");

        LogMsg.e("BALANCE_URL", BAL_URL);
        leaveBalanceAPI(BAL_URL);


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

        spnLeaveType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etBal.setText(alLeaveType.get(position).getBalance());
                lvType = alLeaveType.get(position).getLeave_type();
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
                    mSaveDataAsyncTask = new SaveDataAsyncTask();
                    mSaveDataAsyncTask.execute();
                }
            }
        });
    }


    private void leaveBalanceAPI(String JSON_URL) {
        CustomProgressbar.showProgressBar(context, false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressbar.hideProgressBar();
                try {
                    JSONObject jsono = new JSONObject(response);
                    JSONObject LeaveStatusDetailResult = jsono.getJSONObject("LeaveStatusDetailResult");
                    JSONObject GetLeaveStatusMessage = LeaveStatusDetailResult.getJSONObject("GetLeaveStatusMessage");
                    String success = GetLeaveStatusMessage.optString("Success");
                    String error = GetLeaveStatusMessage.optString("ErrorMsg");
                    if (success.equals("true")) {
                        JSONArray LSDetails = LeaveStatusDetailResult.getJSONArray("LSDetails");
                        LeaveStatusPojo leaveStatusPojo;
                        JSONObject joLSDetails;
                        for (int i = 0; i < LSDetails.length(); i++) {
                            leaveStatusPojo = new LeaveStatusPojo();
                            joLSDetails = LSDetails.getJSONObject(i);
                            leaveStatusPojo.setAvailed(joLSDetails.getString("Availed"));
                            leaveStatusPojo.setBalance(joLSDetails.getString("Balance"));
                            leaveStatusPojo.setEntitled(joLSDetails.getString("Entitled"));
                            leaveStatusPojo.setLeave_type(joLSDetails.getString("Leave_type"));
                            leaveStatusPojo.setLeave_Decs(joLSDetails.getString("Leave_Decs"));
                            leaveStatusPojo.setOpening_bal(joLSDetails.getString("Opening_bal"));
                            alLeaveType.add(leaveStatusPojo);
                        }
                        leaveTypeAdapter = new ArrayAdapter<>(context,
                                android.R.layout.simple_spinner_dropdown_item, alLeaveType);
                        leaveTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnLeaveType.setAdapter(leaveTypeAdapter);
                    } else {
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

    private class SaveDataAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CustomProgressbar.showProgressBar(context, false);
        }

        @Override
        protected String doInBackground(String... params) {
            postLeaveActivity();
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            CustomProgressbar.hideProgressBar();
            if (!success.equals("true")) {
                CommonMethods.showAlert(errorMsg, Leave.this);
            } else {
                final Dialog dialog = new Dialog(Leave.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.logout);

                TextView txtMsg = dialog.findViewById(R.id.dlg_msg);
                txtMsg.setText(R.string.leave_apply);
                Button cancel = dialog.findViewById(R.id.cancel);
                cancel.setVisibility(View.GONE);
                Button yes = dialog.findViewById(R.id.yes);
                yes.setText(R.string.okay);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Leave.this.finish();
                    }
                });

                dialog.show();
            }
        }
    }

    public void postLeaveActivity() {
        String URL_DETIAL;
        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("COMPANY_NO", CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, ""));
                jsonObject.put("LOCATION_NO", CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, ""));
                jsonObject.put("emp_code", CommonMethods.getPrefsData(Leave.this, PrefrenceKey.ASS_CODE, ""));
                jsonObject.put("App_code", "");
                jsonObject.put("Leave_type", lvType);
                jsonObject.put("Notified_date", CommonMethods.mobileCurrentDate());
                jsonObject.put("From_date", CommonMethods.changeDateTOyyyyMMdd(txtDateFrom.getText().toString().trim()));
                jsonObject.put("To_date", CommonMethods.changeDateTOyyyyMMdd(txtDateTo.getText().toString().trim()));
                jsonObject.put("Calender_Code", CommonMethods.getPrefsData(Leave.this, PrefrenceKey.CAL_CODE, ""));
                jsonObject.put("From_session", snFrom);
                jsonObject.put("To_session", snTo);
                jsonObject.put("employee_reason", etReason.getText().toString().trim());
                jsonObject.put("employer_reason", "");
                jsonObject.put("Status", "0");//m-0, c-1 , a-2
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String jsonStr = jsonObject.toString();
            System.out.println("jsonString: " + jsonStr);

            URL_DETIAL = ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/SetLeaveDataADD";
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

    private boolean isValidate() {
        if (etReason.getText().toString().length() == 0) {
            CommonMethods.setSnackBar(llRoot, "Reason required");
            return false;
        }
        return true;
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
