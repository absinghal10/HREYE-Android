package cbs.hreye.activities;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

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

import java.util.Calendar;

import cbs.hreye.R;
import cbs.hreye.adapters.PaySlipAmountDetailAdapter;
import cbs.hreye.adapters.PaySlipDeductionDetailAdapter;
import cbs.hreye.databinding.PaySlipActivityBinding;
import cbs.hreye.pojo.PaySlipDetailsPojo;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.MonthYearPickerDialogPaySlip;
import cbs.hreye.utilities.PrefrenceKey;

public class PaySlipActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private RecyclerView.LayoutManager mLayoutManager;
    private PaySlipAmountDetailAdapter paySlipAmountDetailAdapter;
    private PaySlipDeductionDetailAdapter paySlipDeductionDetailAdapter;
    private Calendar calendar;
    private PaySlipActivityBinding paySlipBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        paySlipBinding = DataBindingUtil.setContentView(this, R.layout.pay_slip_activity);
        mContext = PaySlipActivity.this;
        setClickListener();
    }


    private void setClickListener() {
        paySlipBinding.toolbarLayout.headerText.setText(R.string.salary_slip);

        paySlipBinding.toolbarLayout.headerBack.setOnClickListener(this);
        paySlipBinding.recDate.setOnClickListener(this);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year  = calendar.get(Calendar.YEAR);

        paySlipBinding.recDate.setText(CommonMethods.getMonth(month)+ " - " +year);
        paySlipAPI(year, month);

        paySlipBinding.rvPaySlipAmountDetail.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        paySlipBinding.rvPaySlipAmountDetail.setLayoutManager(mLayoutManager);
        paySlipAmountDetailAdapter = new PaySlipAmountDetailAdapter(mContext);
        paySlipBinding.rvPaySlipAmountDetail.setNestedScrollingEnabled(false);

        paySlipBinding.rvPaySlipDeductionDetail.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        paySlipBinding.rvPaySlipDeductionDetail.setLayoutManager(mLayoutManager);
        paySlipDeductionDetailAdapter = new PaySlipDeductionDetailAdapter(mContext);
        paySlipBinding.rvPaySlipDeductionDetail.setNestedScrollingEnabled(false);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.header_back) {
            onBackPressed();
        } else if (view.getId() == R.id.rec_date) {
            MonthYearPickerDialogPaySlip pd = new MonthYearPickerDialogPaySlip();
            pd.show(getFragmentManager(), "MonthYearPickerDialog");
        }
    }


    private void paySlipAPI(int year, int month){
        if (CommonMethods.isOnline(mContext)) {
            String JSON_URL = ConsURL.baseURL(mContext) +"GetLeaveCalendar/Service1.svc/PaySlipData?" +
                    "COMPANY_NO="+CommonMethods.getPrefsData(mContext, PrefrenceKey.COMPANY_NO, "")
                    +"&LOCATION_NO="+CommonMethods.getPrefsData(mContext, PrefrenceKey.LOCATION_NO, "")
                    +"&USER_ID="+CommonMethods.getPrefsData(mContext, PrefrenceKey.USER_ID, "")
                    +"&Year=" +year
                    +"&Month=" +CommonMethods.pad(month)
                    +"&FromAsso_code=" +CommonMethods.getPrefsData(mContext, PrefrenceKey.ASS_CODE, "")
                    +"&ToAsso_code="+CommonMethods.getPrefsData(mContext, PrefrenceKey.ASS_CODE, "");
            LogMsg.d("PAY_SLIP_AMOUNT_DETAIL_URL", JSON_URL);
            paySlipAmountDetailAPI(JSON_URL);
        } else {
            CommonMethods.setSnackBar(paySlipBinding.llRoot, getString(R.string.net));
        }
    }

    public void getResult(int month, int year) {
        paySlipBinding.recDate.setText(CommonMethods.getMonth(month)+ " - " +year);
        paySlipAPI(year, month);

    }

    private void paySlipAmountDetailAPI(String url) {

        CustomProgressbar.showProgressBar(mContext, false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CustomProgressbar.hideProgressBar();
                        try {
                            paySlipAmountDetailAdapter.clear();
                            paySlipDeductionDetailAdapter.clear();

                            JSONObject jsono = new JSONObject(response);
                            JSONObject payslipResult = jsono.getJSONObject("payslipResult");
                            JSONObject Msg = payslipResult.getJSONObject("Msg");
                            String error = Msg.optString("ErrorMsg");
                            String success = Msg.optString("Success");

                            if (success.equals("true")) {
                                paySlipBinding.llNoRecord.setVisibility(View.GONE);
                                paySlipBinding.nsvPayslip.setVisibility(View.VISIBLE);
                                JSONArray PaySlipAmountDetail = payslipResult.getJSONArray("PaySlipAmountDetail");
                                double totalGrossAmt = 0.0;
                                double totalAmt = 0.0;
                                double netPayableAmt;
                                for (int i = 0; i < PaySlipAmountDetail.length(); i++) {
                                    PaySlipDetailsPojo paySlipDetailsPojo = new PaySlipDetailsPojo();
                                    paySlipDetailsPojo.setAmount(PaySlipAmountDetail.getJSONObject(i).getString("Amount"));
                                    paySlipDetailsPojo.setEarning(PaySlipAmountDetail.getJSONObject(i).getString("Earning"));
                                    paySlipDetailsPojo.setGrossAmt(PaySlipAmountDetail.getJSONObject(i).getString("GrossAmt"));
                                    paySlipAmountDetailAdapter.addToArray(paySlipDetailsPojo);
                                    totalGrossAmt = totalGrossAmt + Double.parseDouble(PaySlipAmountDetail.getJSONObject(i).getString("GrossAmt"));
                                    totalAmt = totalAmt + Double.parseDouble(PaySlipAmountDetail.getJSONObject(i).getString("Amount"));
                                }
                                paySlipBinding.rvPaySlipAmountDetail.setAdapter(paySlipAmountDetailAdapter);

                                paySlipBinding.tvTotalGrossAmount.setText(String.valueOf(totalGrossAmt));
                                paySlipBinding.tvTotalAmount.setText(String.valueOf(totalAmt));

                                JSONArray PaySlipDeductionDetail = payslipResult.getJSONArray("PaySlipDeductionDetail");
                                double totalDeductionAmt = 0.0;
                                for (int i = 0; i < PaySlipDeductionDetail.length(); i++) {
                                    PaySlipDetailsPojo paySlipDetailsPojo = new PaySlipDetailsPojo();
                                    paySlipDetailsPojo.setDeduction(PaySlipDeductionDetail.getJSONObject(i).getString("Deduction"));
                                    paySlipDetailsPojo.setDeductionAmt(PaySlipDeductionDetail.getJSONObject(i).getString("DeductionAmt"));
                                    paySlipDeductionDetailAdapter.addToArray(paySlipDetailsPojo);
                                    totalDeductionAmt = totalDeductionAmt + Double.parseDouble(PaySlipDeductionDetail.getJSONObject(i).getString("DeductionAmt"));
                                }
                                paySlipBinding.rvPaySlipDeductionDetail.setAdapter(paySlipDeductionDetailAdapter);

                                netPayableAmt = totalAmt - totalDeductionAmt;
                                paySlipBinding.tvTotalDeductionAmount.setText(String.valueOf(totalDeductionAmt));
                                paySlipBinding.tvNetPayable.setText(String.valueOf(netPayableAmt));
                                paySlipBinding.tvAmtWords.setText(CommonMethods.convertToIndianCurrency(Double.toString(netPayableAmt)));

                                JSONObject paySlipPersonalDetail = payslipResult.getJSONObject("PaySlipPersonalDetail");
                                paySlipBinding.tvBankAcNo.setText(paySlipPersonalDetail.getString("BankAcNo"));
                                paySlipBinding.tvBankName.setText(paySlipPersonalDetail.getString("BankName"));
                                paySlipBinding.tvDepartment.setText(paySlipPersonalDetail.getString("Department"));
                                paySlipBinding.tvDesignation.setText(paySlipPersonalDetail.getString("Designation"));
                                paySlipBinding.tvEsiAcNo.setText(paySlipPersonalDetail.getString("ESIno"));
                                paySlipBinding.tvEmpId.setText(paySlipPersonalDetail.getString("EmpID"));
                                paySlipBinding.tvLocation.setText(paySlipPersonalDetail.getString("Location"));
                                paySlipBinding.tvName.setText(paySlipPersonalDetail.getString("Name"));
                                paySlipBinding.tvPayableDays.setText(paySlipPersonalDetail.getString("PayableDays"));
                                paySlipBinding.tvPfAcNo.setText(paySlipPersonalDetail.getString("PfNo"));
                                paySlipBinding.tvUanNo.setText(paySlipPersonalDetail.getString("UANno"));

                            } else {
                                paySlipBinding.nsvPayslip.setVisibility(View.GONE);
                                paySlipBinding.llNoRecord.setVisibility(View.VISIBLE);
                                CommonMethods.setSnackBar(paySlipBinding.llRoot, error);

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
                        Toast.makeText(mContext, CommonMethods.volleyError(error), Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
