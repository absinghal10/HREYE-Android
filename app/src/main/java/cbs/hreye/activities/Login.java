package cbs.hreye.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import cbs.hreye.R;
import cbs.hreye.databinding.LoginActivityDemoBinding;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;

public class Login extends AppCompatActivity implements TextWatcher {
    Context mContext;
    String error, Success;
    String JSON_URL;
    String UserId, UserName;
    String aCode, UserEmail, Mob, rePer, flag, locationNo, companyNo, image;
    boolean doubleBackToExitPressedOnce = false;
    LoginActivityDemoBinding loginActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivityBinding = DataBindingUtil.setContentView(this, R.layout.login_activity_demo);
        mContext = Login.this;

        clickListener();
    }

    @SuppressLint("MissingPermission")
    private void clickListener() {
        Animation myanim = AnimationUtils.loadAnimation(mContext, R.anim.splash_anim);
        loginActivityBinding.hrEye.startAnimation(myanim);

        if (ConsURL.baseURL(mContext).equals("")) {
            loginActivityBinding.tilServerIp.setVisibility(View.VISIBLE);
            loginActivityBinding.etServerIp.setImeOptions(EditorInfo.IME_ACTION_DONE);
            loginActivityBinding.etPassword.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        } else {
            loginActivityBinding.tilServerIp.setVisibility(View.GONE);
            loginActivityBinding.etPassword.setImeOptions(EditorInfo.IME_ACTION_DONE);
            loginActivityBinding.etServerIp.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        }


        loginActivityBinding.etUserId.addTextChangedListener(this);
        loginActivityBinding.etPassword.addTextChangedListener(this);
        loginActivityBinding.etServerIp.addTextChangedListener(this);

        loginActivityBinding.tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ForgotPassword.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        loginActivityBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.hideKeyboard(mContext);
                if (CommonMethods.isOnline(mContext)) {
                    if (isValidate()) {
                        if (ConsURL.baseURL(mContext).isEmpty()) {
                            JSON_URL = loginActivityBinding.etServerIp.getText().toString().trim() + "HRLoginService/LoginService.svc/UserLogin?" +
                                    "UserName=" + loginActivityBinding.etUserId.getText().toString().trim() + "&" +
                                    "Password=" + loginActivityBinding.etPassword.getText().toString().trim();
                        } else {
                            JSON_URL = ConsURL.baseURL(mContext) + "HRLoginService/LoginService.svc/UserLogin?" +
                                    "UserName=" + loginActivityBinding.etUserId.getText().toString().trim() + "&" +
                                    "Password=" + loginActivityBinding.etPassword.getText().toString().trim();
                        }
                        LogMsg.d("LOGIN_TASK", JSON_URL);
                        loadLoginService(JSON_URL);

                    }
                } else {
                    CommonMethods.setSnackBar(loginActivityBinding.rlRoot, getString(R.string.net));
                }
            }
        });

    }


    private void loadLoginService(String JSON_URL) {
        CustomProgressbar.showProgressBar(mContext, false);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CustomProgressbar.hideProgressBar();
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject LoginUserResult = jsono.getJSONObject("LoginUserResult");
                            JSONObject LoginMessage = LoginUserResult.getJSONObject("LoginMessage");
                            error = LoginMessage.optString("ErrorMsg");
                            Success = LoginMessage.optString("Success");

                            JSONObject UserDetails = LoginUserResult.getJSONObject("UserDetails");
                            UserId = UserDetails.optString("UserId");
                            UserName = UserDetails.optString("UserName");
                            aCode = UserDetails.optString("LoginName");
                            UserEmail = UserDetails.optString("Email");
                            Mob = UserDetails.optString("MOBILE_NO");
                            rePer = UserDetails.optString("REP_PERSON");
                            flag = UserDetails.optString("FLAG");
                            companyNo = UserDetails.optString("COMPANY_NO");
                            locationNo = UserDetails.optString("LOCATION_NO");
                            //image = UserDetails.optString("IMAGE");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (Success.equals("true")) {

                            CommonMethods.setPrefsData(mContext, PrefrenceKey.USER_ID, UserId);
                            CommonMethods.setPrefsData(mContext, PrefrenceKey.USER_NAME, UserName);
                            CommonMethods.setPrefsData(mContext, PrefrenceKey.ASS_CODE, aCode);
                            CommonMethods.setPrefsData(mContext, PrefrenceKey.USER_EMAIL, UserEmail);
                            CommonMethods.setPrefsData(mContext, PrefrenceKey.USER_MOB, Mob);
                            CommonMethods.setPrefsData(mContext, PrefrenceKey.REP_PERSON, rePer);
                            CommonMethods.setPrefsData(mContext, PrefrenceKey.FLAG_PERSON, flag);
                            CommonMethods.setPrefsData(mContext, PrefrenceKey.COMPANY_NO, companyNo);
                            CommonMethods.setPrefsData(mContext, PrefrenceKey.LOCATION_NO, locationNo);
                           // CommonMethods.setPrefsData(context, PrefrenceKey.PROFILE_BITMAP, image);

                            if (!loginActivityBinding.etServerIp.getText().toString().isEmpty()) {
                                CommonMethods.setPrefsDataURL(mContext, PrefrenceKey.SERVER_IP, loginActivityBinding.etServerIp.getText().toString().trim());
                            }

                            startActivity(new Intent(mContext, DashBoardMain.class));
                            finish();
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                        } else {
                            CommonMethods.setSnackBar(loginActivityBinding.rlRoot, error);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomProgressbar.hideProgressBar();
                        if (error.getMessage() != null) {
                            String errorMsg = error.getMessage();
                            String subString = errorMsg.substring(errorMsg.lastIndexOf(":") + 1);
                            if (subString.contains("No address associated with hostname")) {
                                CommonMethods.setSnackBar(loginActivityBinding.rlRoot, subString + ", Check server IP");
                            } else {
                                CommonMethods.setSnackBar(loginActivityBinding.rlRoot, errorMsg);
                            }
                        } else {
                            CommonMethods.setSnackBar(loginActivityBinding.rlRoot, "Failed to retrieve data");
                        }
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,//Socket time out in milies
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


    private boolean isValidate() {
        if (loginActivityBinding.etUserId.getText().toString().length() == 0) {
            loginActivityBinding.etUserId.requestFocus();
            loginActivityBinding.tilUserId.setError("User Id required");
            return false;
        }
        if (loginActivityBinding.etPassword.getText().toString().length() == 0) {
            loginActivityBinding.etPassword.requestFocus();
            loginActivityBinding.tilPassword.setError("Password required");
            return false;
        }

        if (ConsURL.baseURL(mContext).isEmpty()) {
            if (!Patterns.WEB_URL.matcher(loginActivityBinding.etServerIp.getText().toString().trim()).matches()) {
                if (loginActivityBinding.etServerIp.getText().toString().length() == 0) {
                    loginActivityBinding.etServerIp.requestFocus();
                    loginActivityBinding.tilServerIp.setError("Server IP required");
                    return false;
                }
                loginActivityBinding.tilServerIp.setError("Please enter valid server IP");
                return false;
            }
        }
        return true;
    }

    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        CommonMethods.setSnackBar(loginActivityBinding.rlRoot, "Please click BACK again to exit");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.equals(loginActivityBinding.etUserId.getEditableText())) {
            if (loginActivityBinding.etUserId.getText().length() > 0) {
                loginActivityBinding.tilUserId.setErrorEnabled(false);
            }
        } else if (s.equals(loginActivityBinding.etPassword.getEditableText())) {
            if (loginActivityBinding.etPassword.getText().length() > 0) {
                loginActivityBinding.tilPassword.setErrorEnabled(false);
            }
        } else if (s.equals(loginActivityBinding.etServerIp.getEditableText())) {
            if (loginActivityBinding.etServerIp.getText().length() > 0) {
                loginActivityBinding.tilServerIp.setErrorEnabled(false);
            }
        }
    }
}