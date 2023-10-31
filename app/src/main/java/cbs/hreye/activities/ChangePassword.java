package cbs.hreye.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.DefaultRetryPolicy;
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
import cbs.hreye.databinding.ChangePasswordBinding;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private Context mContext;
    private ImageView ivBack;
    private ChangePasswordBinding changePasswordBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changePasswordBinding = DataBindingUtil.setContentView(this, R.layout.change_password);
        mContext = ChangePassword.this;
        setOnClickListener();
    }

    private void setOnClickListener() {
        ivBack = findViewById(R.id.header_back);
        ivBack.setOnClickListener(this);
        changePasswordBinding.btnChangePass.setOnClickListener(this);
        changePasswordBinding.etOldPass.addTextChangedListener(this);
        changePasswordBinding.etNewPass.addTextChangedListener(this);
        changePasswordBinding.etRetypePass.addTextChangedListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.header_back:
                onBackPressed();
                break;
            case R.id.btn_change_pass:
                if (CommonMethods.isOnline(mContext)) {
                    if (isValidate()) {
                        changePassAPI();
                    }
                } else {
                    CommonMethods.setSnackBar(changePasswordBinding.llRoot,getString(R.string.net));
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
          jsonObject.put("OLD_PASSWORD", changePasswordBinding.etOldPass.getText().toString().trim());
          jsonObject.put("NEW_PASSWORD", changePasswordBinding.etNewPass.getText().toString().trim());
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
                      boolean Success = Messsage.optBoolean("Success");
                      String Error = Messsage.optString("ErrorMsg");
                      if (Success) {
                          Toast.makeText(mContext, Error, Toast.LENGTH_LONG).show();
                          CommonMethods.delPrefsData(mContext);
                          Intent intent = new Intent(mContext, Login.class);
                          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                  Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                  Intent.FLAG_ACTIVITY_NEW_TASK);
                          startActivity(intent);
                          finish();
                      } else {
                          CommonMethods.setSnackBar(changePasswordBinding.llRoot, Error);
                      }
                  } catch (JSONException e) {
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
                  return mRequestBody == null ? null : mRequestBody.getBytes(StandardCharsets.UTF_8);
              }
              @Override
              protected Response<String> parseNetworkResponse(NetworkResponse response) {
                  String.valueOf(response.statusCode);
                  return super.parseNetworkResponse(response);
              }
          };
          stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,-1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
          requestQueue.add(stringRequest);

      } catch (JSONException e) {
          e.printStackTrace();
      }
  }


    private boolean isValidate() {
        if (changePasswordBinding.etOldPass.getText().toString().length() == 0) {
            changePasswordBinding.etOldPass.requestFocus();
            changePasswordBinding.tilOldPass.setError("Old Password required");
            return false;
        }

        if (changePasswordBinding.etNewPass.getText().toString().length() == 0) {
            changePasswordBinding.etNewPass.requestFocus();
            changePasswordBinding.tilNewPass.setError("New Password required");
            return false;
        }

        if (changePasswordBinding.etRetypePass.getText().toString().length() == 0) {
            changePasswordBinding.etRetypePass.requestFocus();
            changePasswordBinding.tilRetypePass.setError("Retype Password required");
            return false;
        }

        if (!changePasswordBinding.etNewPass.getText().toString().equals(changePasswordBinding.etRetypePass.getText().toString())){
            CommonMethods.setSnackBar(changePasswordBinding.llRoot,"New Password and Re-Type Password must be same.");
            return  false;
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
        if (s.equals(changePasswordBinding.etOldPass.getEditableText())) {
            if (changePasswordBinding.etOldPass.getText().length() > 0) {
                changePasswordBinding.tilOldPass.setErrorEnabled(false);
            }
        } else if (s.equals(changePasswordBinding.etNewPass.getEditableText())) {
            if (changePasswordBinding.etNewPass.getText().length() > 0) {
                changePasswordBinding.tilNewPass.setErrorEnabled(false);
            }
        } else  if (s.equals(changePasswordBinding.etRetypePass.getEditableText())) {
            if (changePasswordBinding.etRetypePass.getText().length() > 0) {
                changePasswordBinding.tilRetypePass.setErrorEnabled(false);
            }
        }
    }
}
