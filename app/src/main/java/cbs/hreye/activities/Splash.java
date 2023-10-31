package cbs.hreye.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.databinding.DataBindingUtil;
import cbs.hreye.R;
import cbs.hreye.databinding.ActivitySplashBinding;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.PrefrenceKey;

public class Splash extends Activity {
    Context context;
    private String userID;
    String serverVersionCode;
    String URL_VERSION;
    private static int SPLASH_TIME_OUT = 2000;
    ActivitySplashBinding activitySplashBinding;

   /* private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 11;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        context = Splash.this;
        userID = CommonMethods.getPrefsData(context, PrefrenceKey.USER_ID, "0");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(userID.equals("0")) {
                    startActivity(new Intent(context, Login.class));
                }else {
                    startActivity(new Intent(context, DashBoardMain.class));
                }
                finish();
            }
        }, SPLASH_TIME_OUT);

        Animation myanim = AnimationUtils.loadAnimation(context, R.anim.splash_anim);
        activitySplashBinding.hrEye.startAnimation(myanim);
    }

/*    @Override
    protected void onStart() {
        super.onStart();

        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        mAppUpdateManager.registerListener(installStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE *//*AppUpdateType.IMMEDIATE*//*)){

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE *//*AppUpdateType.IMMEDIATE*//*, Splash.this, RC_APP_UPDATE);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED){
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                popupSnackbarForCompleteUpdate();
            } else {
                LogMsg.e("Splash", "checkForAppUpdateAvailability: something else");
                Toast.makeText(context, "checkForAppUpdateAvailability: something else", Toast.LENGTH_LONG).show();
            }
        });

    }

    InstallStateUpdatedListener installStateUpdatedListener = new
            InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADED){
                        //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                        popupSnackbarForCompleteUpdate();
                    } else if (state.installStatus() == InstallStatus.INSTALLED){
                        if (mAppUpdateManager != null){
                            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                        }

                    } else {
                        LogMsg.e("Splash", "InstallStateUpdatedListener: state: " + state.installStatus());
                        Toast.makeText(context, "InstallStateUpdatedListener: state: " + state.installStatus(), Toast.LENGTH_LONG).show();
                    }
                }
            };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                LogMsg.e("Splash", "onActivityResult: app download failed");
                Toast.makeText(context, "onActivityResult: app download failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAppUpdateManager != null) {
            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.ll_root),
                        "New app is ready!",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Install", view -> {
            if (mAppUpdateManager != null){
                mAppUpdateManager.completeUpdate();
            }
        });


        snackbar.setActionTextColor(getResources().getColor(R.color.blue));
        snackbar.show();
    }*/

    /*@Override
    protected void onResume() {
        super.onResume();
        if (userID.equals("0")) {
            startActivity(new Intent(context,Login.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            finish();
        } else {
            startActivity(new Intent(context,DashBoardMain.class));
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            *//*if (CommonMethods.isOnline(context)) {
                URL_VERSION = "http://hbmas.cogniscient.in/HRLoginService/LoginService.svc/GetVersion";
                LogMsg.d("Load_Version", URL_VERSION);
                loadVerService(URL_VERSION);
            } else {
                CommonMethods.setSnackBar(activitySplashBinding.llRoot, getString(R.string.net));
            }*//*
        }
    }*/

  /*  private void loadVerService(String urlVesion) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlVesion,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String projectVesrionCode = CommonMethods.getPackageInfo(context, "versionCode");
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject GetVersionResult = jsono.getJSONObject("GetVersionResult");
                            JSONObject VersionMessage = GetVersionResult.getJSONObject("VersionMessage");
                            String error = VersionMessage.optString("ErrorMsg");
                            String success = VersionMessage.optString("Success");

                            if (success.equals("true")) {
                                JSONArray VersionDetails = GetVersionResult.getJSONArray("VersionDetails");
                                for (int i = 0; i < VersionDetails.length(); i++) {
                                    serverVersionCode = VersionDetails.getJSONObject(i).getString("VersionId");
                                }
                                try {
                                    // put serverVersionCode here after change server side
                                    if (projectVesrionCode != null) {
                                        if (!projectVesrionCode.equals(serverVersionCode)) {
                                            final Dialog cDialog = new Dialog(context);
                                            cDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            cDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            cDialog.setCancelable(false);
                                            cDialog.setContentView(R.layout.logout);
                                            TextView txtMsg = cDialog.findViewById(R.id.dlg_msg);
                                            txtMsg.setText(R.string.please_update_app);
                                            Button noBtn = cDialog.findViewById(R.id.cancel);
                                            Button yesBtn = cDialog.findViewById(R.id.yes);
                                            yesBtn.setVisibility(View.GONE);
                                            noBtn.setText(R.string.update);
                                            noBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    cDialog.dismiss();
                                                    //clear pref for logout..
                                                    // CommonMethods.delPrefsData(context);
                                                    finish();
                                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                    try {
                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                    } catch (android.content.ActivityNotFoundException anfe) {
                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                    }
                                                }
                                            });
                                            cDialog.show();
                                        } else {
                                            Intent i = new Intent(context, DashBoardMain.class);
                                            startActivity(i);
                                            finish();
                                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                CommonMethods.setSnackBar(activitySplashBinding.llRoot, error);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar snackbar = Snackbar.make(activitySplashBinding.llRoot, CommonMethods.volleyError(error), Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadVerService(URL_VERSION);
                            }
                        });
                        snackbar.show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,//Socket time out in milies
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }*/


}