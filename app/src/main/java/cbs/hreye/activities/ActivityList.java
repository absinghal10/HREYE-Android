package cbs.hreye.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import cbs.hreye.R;
import cbs.hreye.adapters.ActivitiesAdapter;
import cbs.hreye.databinding.ActivityListBinding;
import cbs.hreye.pojo.ActivitiesPojo;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;

public class ActivityList extends AppCompatActivity {
    public static String rptDate, trnNo, flag;
    public int mYear, mMonth, mDay;
    Context context;
    String JSON_URL;
    String error, success;
    ArrayList<ActivitiesPojo> mArray;
    ActivitiesAdapter activitiesAdapter;
    String searchDate;
    private int filterLength = 0;
    ActivityListBinding activityListBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityListBinding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        context = ActivityList.this;
        getID();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityListBinding.etSearch.setText("");
        if (CommonMethods.isOnline(context)) {
            JSON_URL = ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/GetTransactionDetail?"
                    + "UserId=" + CommonMethods.getPrefsData(context, PrefrenceKey.USER_ID, "")
                    + "&AssociateCode=" + CommonMethods.getPrefsData(context, PrefrenceKey.ASS_CODE, "").replaceAll(" ", "%20")
                    +"&LOCATION_NO="+CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "")
                    +"&COMPANY_NO="+CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "");

            LogMsg.d("ACTIVITY_LIST", JSON_URL);
            mArray = new ArrayList<>();
            loadActivitesService();
        } else {
            CommonMethods.setSnackBar(activityListBinding.llRoot, getString(R.string.net));
        }
    }


    private void getID() {
        activityListBinding.headerBack.headerText.setText(R.string.activities);
        activityListBinding.headerBack.headerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        activityListBinding.addActi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, DailyActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        activityListBinding.etSearch.setFocusable(false);
        activityListBinding.etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mArray.size() == 0) {
                    CommonMethods.setSnackBar(activityListBinding.etSearch, "No item available for search");
                } else {
                    Calendar mcurrentDate = Calendar.getInstance();
                    mYear = mcurrentDate.get(Calendar.YEAR);
                    mMonth = mcurrentDate.get(Calendar.MONTH);
                    mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            // TODO Auto-generated method stub
                            Calendar c = Calendar.getInstance();
                            c.set(selectedyear, selectedmonth, selectedday);
                            searchDate = CommonMethods.pad(selectedday) + "/" + CommonMethods.pad(selectedmonth + 1) +
                                    "/" + CommonMethods.pad(selectedyear);
                            activityListBinding.etSearch.setText(searchDate);
                            activityListBinding.searchView.setQuery(searchDate, true);
                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.getDatePicker().setCalendarViewShown(false);
                    mDatePicker.show();
                }
            }
        });

        activityListBinding.txtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityListBinding.etSearch.getText().toString().length() == 0) {
                    CommonMethods.setSnackBar(activityListBinding.etSearch, "Date is not entered");
                } else {
                    activityListBinding.etSearch.setText("");
                    activitiesAdapter = new ActivitiesAdapter(ActivityList.this, mArray);
                    activityListBinding.lstActivities.setAdapter(activitiesAdapter);
                    activitiesAdapter.notifyDataSetChanged();
                }
            }
        });

        activityListBinding.searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // TODO Auto-generated method stub
                activitiesAdapter.getFilter().filter(searchDate, new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int i) {
                        if (i == 0) {
                            CommonMethods.setSnackBar(activityListBinding.etSearch, "No activity available of this date.");
                        }
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                filterLength = query.length();
                return false;
            }
        });

        activityListBinding.lstActivities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                if (filterLength != 0) {
                    rptDate = activitiesAdapter.filters.get(position).getREPORTING_DATE();
                    flag = activitiesAdapter.filters.get(position).getDOC_STATUS();
                    trnNo = activitiesAdapter.filters.get(position).getTRANSACTION_NO();
                } else {
                    rptDate = mArray.get(position).getREPORTING_DATE();
                    flag = mArray.get(position).getDOC_STATUS();
                    trnNo = mArray.get(position).getTRANSACTION_NO();
                }
                filterLength = 0;
                startActivity(new Intent(context, AuthorizedActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        activityListBinding.lstActivities.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem < 15) {
                    activityListBinding.ivScrollUp.setVisibility(View.INVISIBLE);
                }else {
                   activityListBinding.ivScrollUp.setVisibility(View.VISIBLE);
                }
            }
        });

        activityListBinding.ivScrollUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityListBinding.lstActivities.smoothScrollToPosition(0);
            }
        });
    }

    private void loadActivitesService() {
        activityListBinding.shimmerViewContainer.setVisibility(View.VISIBLE);
        activityListBinding.shimmerViewContainer.startShimmerAnimation();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        activityListBinding.shimmerViewContainer.stopShimmerAnimation();
                        activityListBinding.shimmerViewContainer.setVisibility(View.GONE);
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject LocationDataResult = jsono.getJSONObject("TransactionDetailResult");
                            JSONObject CustomerMessage = LocationDataResult.getJSONObject("TransactionNoMessage");
                            error = CustomerMessage.optString("ErrorMsg");
                            success = CustomerMessage.optString("Success");

                            if (success.equals("true")) {
                                JSONArray TDetails = LocationDataResult.getJSONArray("TDetails");

                                for (int i = 0; i < TDetails.length(); i++) {
                                    ActivitiesPojo pj = new ActivitiesPojo();

                                    pj.setDOC_STATUS(TDetails.getJSONObject(i).getString("DOC_STATUS"));
                                    pj.setREPORTING_DATE(TDetails.getJSONObject(i).getString("REPORTING_DATE"));
                                    pj.setTRANSACTION_NO(TDetails.getJSONObject(i).getString("TRANSACTION_NO"));
                                    mArray.add(pj);
                                }
                                Collections.reverse(mArray);
                                activitiesAdapter = new ActivitiesAdapter(ActivityList.this, mArray);
                                activityListBinding.lstActivities.setAdapter(activitiesAdapter);
                                activitiesAdapter.notifyDataSetChanged();
                            } else {
                                CommonMethods.setSnackBar(activityListBinding.etSearch, error);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activityListBinding.shimmerViewContainer.stopShimmerAnimation();
                        activityListBinding.shimmerViewContainer.setVisibility(View.GONE);
                        Toast.makeText(context, CommonMethods.volleyError(error), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,//Socket time out in milies
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
