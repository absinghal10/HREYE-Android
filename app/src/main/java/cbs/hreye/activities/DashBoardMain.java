package cbs.hreye.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
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
import cbs.hreye.activities.travelRejectGrant.TravelRejectGrant;
import cbs.hreye.activities.travelRequest.travelRequestData.TravelRequestActivity;
import cbs.hreye.databinding.ActivityDashboardBinding;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DashBoardMain extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSION_REQUEST_CODE = 200;
    Context context;
    String error, success;
    String URL_NEWS_NOT;
    String encodeBitmap;
    String[] permissionList = {ACCESS_FINE_LOCATION};
    boolean doubleBackToExitPressedOnce = false;
    String noOFBirthDay = "", noOfJoining = "", displayFlag = "", newsContent = "", calendarCode = "", calendarDescription = "", employeeStatus = "", base64Image = "";
    private Dialog mDialog;
    private ActivityDashboardBinding activityDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        context = DashBoardMain.this;
        setClickListener();
    }

    private void setClickListener() {

        setCollapsingToolbarLayout();
        hrNewsApi();
        hideDailyActivity();

        if (CommonMethods.getPrefsData(context, PrefrenceKey.FLAG_PERSON, "").equals("0")) {
            activityDashboardBinding.dashboardItem.llGrantReject.setVisibility(View.GONE);
        } else {
            activityDashboardBinding.dashboardItem.llGrantReject.setVisibility(View.VISIBLE);
            activityDashboardBinding.dashboardItem.viewLs.setVisibility(View.VISIBLE);
        }


        // Hide the Travel Request if associated code is "cogni"
        if (!CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "").equalsIgnoreCase("cogni")) {
            activityDashboardBinding.dashboardItem.travelRequestRootLinearLayout.setVisibility(View.GONE);
            activityDashboardBinding.dashboardItem.travelRequestBottomView.setVisibility(View.GONE);
        } else {
            activityDashboardBinding.dashboardItem.travelRequestRootLinearLayout.setVisibility(View.VISIBLE);
            activityDashboardBinding.dashboardItem.travelRequestBottomView.setVisibility(View.VISIBLE);
        }


        // Hide the Travel Grant/Reject if PrefrenceKey.FLAG_PERSON is "0" && associated code is "cogni"

        if (CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "").equalsIgnoreCase("cogni")) {

            if(!CommonMethods.getPrefsData(context, PrefrenceKey.FLAG_PERSON, "").equals("0")){
                activityDashboardBinding.dashboardItem.travelGrantedOrRejectedRootLinearLayout.setVisibility(View.GONE);
                activityDashboardBinding.dashboardItem.travelGrantedOrRejectedBottomView.setVisibility(View.GONE);
            }else{
                activityDashboardBinding.dashboardItem.travelGrantedOrRejectedRootLinearLayout.setVisibility(View.VISIBLE);
                activityDashboardBinding.dashboardItem.travelGrantedOrRejectedBottomView.setVisibility(View.VISIBLE);
            }
        }else {
            activityDashboardBinding.dashboardItem.travelGrantedOrRejectedRootLinearLayout.setVisibility(View.GONE);
            activityDashboardBinding.dashboardItem.travelGrantedOrRejectedBottomView.setVisibility(View.GONE);
        }



        activityDashboardBinding.dashboardItem.llPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, Profile.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        activityDashboardBinding.dashboardItem.llAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ActivityList.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        activityDashboardBinding.dashboardItem.llLs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, LeaveStatusDemo.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        activityDashboardBinding.dashboardItem.llBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, Birthday.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        activityDashboardBinding.dashboardItem.llJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, Joining.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        activityDashboardBinding.dashboardItem.llGrantReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, LeaveGrantRejection.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        activityDashboardBinding.dashboardItem.llLv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, LeaveList.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        activityDashboardBinding.dashboardItem.llLeaveCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, CalendarViewLeave.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        activityDashboardBinding.dashboardItem.llAtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    startActivity(new Intent(context, Attendence.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                } else {
                    CommonMethods.mutipleRequestPermission(DashBoardMain.this, permissionList, PERMISSION_REQUEST_CODE);
                }
            }
        });

        activityDashboardBinding.dashboardItem.llHolidays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, HolidayList.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        activityDashboardBinding.dashboardItem.llAttList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, AttendanceList.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        activityDashboardBinding.dashboardItem.llPaySlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, PaySlipActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        // Add Two New Field Travel Request and travel Granted/Rejected
        activityDashboardBinding.dashboardItem.travelRequestRootLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, TravelRequestActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });


        activityDashboardBinding.dashboardItem.travelGrantedOrRejectedRootLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, TravelRejectGrant.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

    }

    private void hrNewsApi() {
        if (CommonMethods.isOnline(context)) {
            URL_NEWS_NOT = ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/GetUserDashBoardNewsDetail?"
                    + "LOCATION_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "")
                    + "&COMPANY_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "")
                    + "&LOGIN_NAME=" + CommonMethods.getPrefsData(context, PrefrenceKey.ASS_CODE, "");
            hrNewsNotification(URL_NEWS_NOT);
            LogMsg.d("URL_NEWS_NOT", URL_NEWS_NOT);
        } else {
            CommonMethods.setSnackBar(activityDashboardBinding.llRoot, getString(R.string.net));
        }
    }

    private void hrNewsNotification(String URL) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @SuppressLint("SetJavaScriptEnabled")
                    @Override
                    public void onResponse(String response) {
                        LogMsg.d("response", response);
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject UserDashBoardNewsDetailResult = jsono.getJSONObject("UserDashBoardNewsDetailResult");
                            JSONObject GetUserDashBoardNewsMsgMessage = UserDashBoardNewsDetailResult.getJSONObject("GetUserDashBoardNewsMsgMessage");
                            JSONObject LogMessage = UserDashBoardNewsDetailResult.getJSONObject("LogMessage");
                            error = LogMessage.optString("ErrorMsg");
                            success = LogMessage.optString("Success");

                            if (success.equals("true")) {
                                noOFBirthDay = GetUserDashBoardNewsMsgMessage.optString("BDAY");
                                displayFlag = GetUserDashBoardNewsMsgMessage.optString("DISPLAY_FLAG");
                                noOfJoining = GetUserDashBoardNewsMsgMessage.optString("NEWJOINING");
                                newsContent = GetUserDashBoardNewsMsgMessage.optString("NEWS_CONTENT");
                                calendarCode = GetUserDashBoardNewsMsgMessage.optString("calendarCode");
                                calendarDescription = GetUserDashBoardNewsMsgMessage.optString("calendarDescription");
                                employeeStatus = GetUserDashBoardNewsMsgMessage.optString("employeeStatus");
                             //   base64Image = GetUserDashBoardNewsMsgMessage.optString("IMAGE");

                               // CommonMethods.setPrefsData(context, PrefrenceKey.PROFILE_BITMAP, base64Image);
                                CommonMethods.setPrefsData(context, PrefrenceKey.CAL_CODE, calendarCode);
                                CommonMethods.setPrefsData(context, PrefrenceKey.CAL_DESC, calendarDescription);

                                //updateProfile();

                                if (employeeStatus.equals("D")) {
                                    CommonMethods.delPrefsData(context);
                                    Intent intent = new Intent(context, Login.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                            Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }


                                if (!noOfJoining.equals("0")) {
                                    activityDashboardBinding.dashboardItem.flJoinCount.setVisibility(View.VISIBLE);
                                    activityDashboardBinding.dashboardItem.joinCount.setText(noOfJoining);
                                } else {
                                    activityDashboardBinding.dashboardItem.flJoinCount.setVisibility(View.INVISIBLE);
                                }

                                if (!noOFBirthDay.equals("0")) {
                                    activityDashboardBinding.dashboardItem.flBdayCount.setVisibility(View.VISIBLE);
                                    activityDashboardBinding.dashboardItem.bdayCount.setText(noOFBirthDay);
                                } else {
                                    activityDashboardBinding.dashboardItem.flBdayCount.setVisibility(View.INVISIBLE);
                                }


                                if (displayFlag.equals("1")) {
                                    int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.85);
                                    int weight = (int) (getResources().getDisplayMetrics().widthPixels * 0.80);
                                    mDialog = new Dialog(context);
                                    mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                    mDialog.setCancelable(true);
                                    mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                    mDialog.setContentView(R.layout.hr_news_dailog);
                                    ImageView ivClose = mDialog.findViewById(R.id.iv_close);
                                    WebView webView = mDialog.findViewById(R.id.wb_hrnews1);

                                    webView.getSettings().setDomStorageEnabled(true);
                                    webView.setWebViewClient(new WebViewClient());
                                    webView.getSettings().setJavaScriptEnabled(true);
                                    webView.getSettings().setBuiltInZoomControls(true);
                                    webView.setVerticalScrollBarEnabled(false);
                                    // webView.loadData(newsContent, "text/html", "UTF-8");
                                    webView.loadDataWithBaseURL(null, newsContent, "text/html", "UTF-8", null);
                                    ivClose.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mDialog.dismiss();
                                        }
                                    });

                                    mDialog.show();
                                    mDialog.getWindow().setLayout(weight, height);
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activityDashboardBinding.dashboardItem.joinCount.setVisibility(View.INVISIBLE);
                        activityDashboardBinding.dashboardItem.bdayCount.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, CommonMethods.volleyError(error), Toast.LENGTH_LONG).show();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,//Socket time out in milies
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void hideDailyActivity() {
        //TODO: hide daily activity according to server ip.
        if (CommonMethods.getPrefsDataURL(context, PrefrenceKey.SERVER_IP, "").equals(ConsURL.CBS_BASE_URL) ||
                CommonMethods.getPrefsDataURL(context, PrefrenceKey.SERVER_IP, "").equals(ConsURL.LOCAL_BASE_URL)) {
            activityDashboardBinding.dashboardItem.llAct.setVisibility(View.VISIBLE);
        } else {
            activityDashboardBinding.dashboardItem.llAct.setVisibility(View.GONE);
            activityDashboardBinding.dashboardItem.viewProfile.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateProfile();
    }

    private void updateProfile() {
        encodeBitmap = CommonMethods.getPrefsData(context, PrefrenceKey.PROFILE_BITMAP, "");
        if (!encodeBitmap.equals("")) {
            activityDashboardBinding.ivProfile.setImageBitmap(CommonMethods.Base64StringToBitMap(encodeBitmap));
        } else {
            activityDashboardBinding.ivProfile.setImageResource(R.drawable.profile);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (locationAccepted) {
                    startActivity(new Intent(context, Attendence.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                } else {
                    CommonMethods.openSettings(context);
                }
            }
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    //double BackPressed to exits
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        CommonMethods.setSnackBar(activityDashboardBinding.llRoot, "Please click BACK again to exit");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_notis) {
            startActivity(new Intent(context, Notifications.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setCollapsingToolbarLayout() {
        String displayName = "Welcome " + CommonMethods.getPrefsData(context, PrefrenceKey.USER_NAME, "") + " into HR Eye Activity Portal";
        activityDashboardBinding.dashboardItem.tvMarqueName.setText(displayName);
        activityDashboardBinding.dashboardItem.tvMarqueName.setSelected(true);

        //set CollapsingToolbarLayout here..
        activityDashboardBinding.toolbar.setTitle(CommonMethods.getPrefsData(context, PrefrenceKey.USER_NAME, ""));
        setSupportActionBar(activityDashboardBinding.toolbar);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.audiowide_ttf);
        activityDashboardBinding.toolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        activityDashboardBinding.toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        activityDashboardBinding.toolbarLayout.setCollapsedTitleTypeface(typeface);
        activityDashboardBinding.toolbarLayout.setExpandedTitleTypeface(typeface);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }


}
