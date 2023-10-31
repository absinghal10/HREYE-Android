package cbs.hreye.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import cbs.hreye.R;
import cbs.hreye.databinding.ForgotPasswordBinding;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private Context mContext;
    private ForgotPasswordBinding forgotPasswordBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgotPasswordBinding = DataBindingUtil.setContentView(this, R.layout.forgot_password);
        mContext = ForgotPassword.this;
        setOnClickListener();
    }

    private void setOnClickListener() {
        forgotPasswordBinding.toolbarLayout.headerText.setText(R.string.forgot_password1);

        forgotPasswordBinding.toolbarLayout.headerBack.setOnClickListener(this);
        forgotPasswordBinding.btnForgotPass.setOnClickListener(this);
        forgotPasswordBinding.etForgotPass.addTextChangedListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.header_back:
                onBackPressed();
                break;
            case R.id.btn_forgot_pass:
                if (CommonMethods.isOnline(mContext)) {
                    if (isValidate()) {
                        changePassAPI();
                    }
                } else {
                    CommonMethods.setSnackBar(forgotPasswordBinding.llRoot,getString(R.string.net));
                }
                break;
                default:
                    break;
        }
    }

  private void changePassAPI(){
      CustomProgressbar.showProgressBar(mContext, false);
      try {
          RequestQueue requestQueue = Volley.newRequestQueue(mContext);
          JSONObject jsonObject = new JSONObject();
          jsonObject.put("COMPANY_NO", CommonMethods.getPrefsData(mContext, PrefrenceKey.COMPANY_NO, ""));
          jsonObject.put("LOCATION_NO", CommonMethods.getPrefsData(mContext, PrefrenceKey.LOCATION_NO, ""));
          jsonObject.put("ASSO_CODE", CommonMethods.getPrefsData(mContext, PrefrenceKey.ASS_CODE, ""));
          jsonObject.put("FORGOT_PASSWORD", forgotPasswordBinding.etForgotPass.getText().toString().trim());
          final String mRequestBody = jsonObject.toString();
          LogMsg.i("mRequestBody", mRequestBody);
          String URL = ConsURL.baseURL(mContext)+"GetLeaveCalendar/Service1.svc/ModifyPassword";
          StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                  new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                  LogMsg.i("Response", response);
                  CustomProgressbar.hideProgressBar();
                  try {
                      JSONObject joResponse = new JSONObject(response);
                      JSONObject Messsage = joResponse.getJSONObject("Messsage");
                      boolean success = Messsage.optBoolean("Success");
                      String errorMsg = Messsage.optString("ErrorMsg");
                      if (success) {
                            forgotPasswordBinding.tvSentForgotPass.setVisibility(View.VISIBLE);
                            forgotPasswordBinding.tvSentForgotPass.setText(errorMsg);
                      } else {
                          forgotPasswordBinding.tvSentForgotPass.setVisibility(View.GONE);
                          CommonMethods.setSnackBar(forgotPasswordBinding.llRoot, errorMsg);
                      }
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
              }
          }, new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                  CustomProgressbar.hideProgressBar();
                  forgotPasswordBinding.tvSentForgotPass.setVisibility(View.GONE);
                  Toast.makeText(mContext, CommonMethods.volleyError(error), Toast.LENGTH_LONG).show();
              }
          }) {
              @Override
              public String getBodyContentType() {
                  return "Content-Type/text/plain; charset=utf-8";
              }

              @Override
              public byte[] getBody() {
                  return mRequestBody == null ? null : mRequestBody.getBytes(StandardCharsets.UTF_8);
              }
              @Override
              protected Response<String> parseNetworkResponse(NetworkResponse response) {
                  String.valueOf(response.statusCode);
                  return super.parseNetworkResponse(response);
              }
          };

          requestQueue.add(stringRequest);
      } catch (JSONException e) {
          e.printStackTrace();
      }
  }
    private boolean isValidate() {
        if (forgotPasswordBinding.etForgotPass.getText().toString().length() == 0) {
            forgotPasswordBinding.etForgotPass.requestFocus();
            forgotPasswordBinding.tilForgotPass.setError("User id required");
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.equals(forgotPasswordBinding.etForgotPass.getEditableText())) {
            if (forgotPasswordBinding.etForgotPass.getText().length() > 0) {
                forgotPasswordBinding.tilForgotPass.setErrorEnabled(false);
            }
        }
    }
}
