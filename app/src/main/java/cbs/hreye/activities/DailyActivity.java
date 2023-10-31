package cbs.hreye.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import cbs.hreye.R;
import cbs.hreye.adapters.CustomerDetailsAdapter;
import cbs.hreye.adapters.DailyActivityAdapter;
import cbs.hreye.databinding.ActivityDailyBinding;
import cbs.hreye.pojo.DailyActivityPojo;
import cbs.hreye.pojo.ProjectPojo;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;
import cbs.hreye.utilities.RemoveClickListener;

public class DailyActivity extends AppCompatActivity {
    Context context;
    ArrayList<DailyActivityPojo> mArray = new ArrayList<>();
    DailyActivityAdapter mAdap;
    Dialog dialog;
    private String[] arrayHours, arrayStatus;
    public int mYear, mMonth, mDay;
    String JSON_URL, JSON_PROJECT, Error, Success, Activity_id, sendMailMsgResult;
    String postAddActivity, TRANSACTION_NO, ActivityCode;
    ArrayList<ProjectPojo> mArrayProject;
    Spinner spnActivityID;
    ArrayList<String> mActCode, mActDes;
    EditText etCust, etProject_Id;
    int iLength = 0, editListPos;
    SimpleDateFormat timeFormat;
    Date totalTime;
    long sum;
    ArrayAdapter<String> adapterAct;
    RemoveClickListener removeClickListener;
    ActivityDailyBinding dailyBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dailyBinding = DataBindingUtil.setContentView(this, R.layout.activity_daily);
        context = DailyActivity.this;

        getID();
    }

    private void getID() {
        dailyBinding.headerBack.headerText.setText(R.string.daily_activity);

        dailyBinding.headerBack.llGrantReject.setVisibility(View.VISIBLE);
        dailyBinding.headerBack.headerReject.setImageResource(R.drawable.add_row);

        dailyBinding.headerBack.headerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //TODO: add daily activity.
        dailyBinding.headerBack.headerReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailyBinding.txtCl.setVisibility(View.GONE);
                addActivityDialog();
            }
        });

        //TODO: activity authorize/modify.
        dailyBinding.headerBack.headerGrant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mArray.size() == 0) {
                    CommonMethods.setSnackBar(dailyBinding.llRoot, getString(R.string.enter_minimum_record));
                } else {

                    dialog = new Dialog(context);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.logout);

                    TextView txtMsg = dialog.findViewById(R.id.dlg_msg);
                    txtMsg.setText(R.string.add_to_activity);
                    Button cancel = dialog.findViewById(R.id.cancel);
                    Button yes = dialog.findViewById(R.id.yes);

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            if (CommonMethods.isOnline(context)) {
                                postAddActivity();
                            } else {
                                CommonMethods.setSnackBar(dailyBinding.llRoot, getString(R.string.net));
                            }
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });


        //TODO: set current data and date picker dialog
        dailyBinding.actDate.setText(CommonMethods.changeDateFromyyyyMMdd(CommonMethods.mobileCurrentDate()));
        dailyBinding.actDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker = new DatePickerDialog(DailyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        Calendar c = Calendar.getInstance();
                        c.set(selectedyear, selectedmonth, selectedday);
                        dailyBinding.actDate.setText(CommonMethods.pad(selectedday) + "/" + CommonMethods.pad(selectedmonth + 1) +
                                "/" + CommonMethods.pad(selectedyear));

                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setCalendarViewShown(false);
                mDatePicker.show();
            }
        });

        //TODO: remove daily activity from list.
        removeClickListener = new RemoveClickListener() {
            @Override
            public void onRemove(View view, int position, String removeEDit) {
                if (removeEDit.equals("remove")) {
                    removeActivityDialog(position);
                } else {
                    editActivityDialog(position);
                }
            }
        };

        //TODO: fetch customers list.
        if (CommonMethods.isOnline(context)) {
            JSON_URL = ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/GetCustomerDetails?" +
                    "COMPANY_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "") +
                    "&LOCATION_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "");

            LogMsg.e("customer_url", JSON_URL);
            mArrayProject = new ArrayList<>();
            loadCustomersService();
        } else {
            CommonMethods.setSnackBar(dailyBinding.llRoot, getString(R.string.net));
        }
    }

    private void removeActivityDialog(final int position) {

        dialog = new Dialog(DailyActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.logout);

        TextView txtMsg = dialog.findViewById(R.id.dlg_msg);
        txtMsg.setText("Sure to remove : " + mArray.get(position).getCustom_id());
        Button cancel = dialog.findViewById(R.id.cancel);
        Button yes = dialog.findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sum = 0;
                mArray.remove(position);
                mAdap.notifyDataSetChanged();
                dialog.cancel();
                for (int i = 0; i < mArray.size(); i++) {
                    try {
                        totalTime = timeFormat.parse(mArray.get(i).getHours());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    sum = sum + totalTime.getTime();
                }
                dailyBinding.actHours.setText(timeFormat.format(new Date(sum)));

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void editActivityDialog(final int position) {
        try {
            final int newPos = position;
            final Dialog conDialog = new Dialog(context, R.style.MyCustomTheme);
            conDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            conDialog.setCancelable(false);
            conDialog.setContentView(R.layout.dialog_daily_activity);
            etCust = conDialog.findViewById(R.id.et_customer);
            etProject_Id = conDialog.findViewById(R.id.et_project_id);
            Button btn_add = conDialog.findViewById(R.id.btn_add);
            final ImageView dlg_cancel = conDialog.findViewById(R.id.dlg_cancel);
            final EditText etDetais = conDialog.findViewById(R.id.et_details);
            final EditText etRemarks_one = conDialog.findViewById(R.id.et_daily_act_remarks);
            spnActivityID = conDialog.findViewById(R.id.spn_act_id);
            final Spinner spnHours = conDialog.findViewById(R.id.spn_hours);
            final Spinner spnStatus = conDialog.findViewById(R.id.spn_status);
            btn_add.setText(R.string.edit_list);

            this.arrayHours = new String[]{
                    "Hours",
                    "00.30", "01.00", "01.30", "02.00", "02.30", "03.00", "03.30", "04.00", "04.30", "05.00",
                    "05.30", "06.00", "06.30", "07.00", "07.30", "08.00", "08.30", "09.00", "09.30", "10.00"
            };

            this.arrayStatus = new String[]{
                    "Status",
                    "Complete", "Progress", "Pending", "Assigned", "Not Assigned"};
            etCust.setFocusable(false);
            etProject_Id.setFocusable(false);

            ArrayAdapter<String> adapterHours = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, arrayHours);
            spnHours.setAdapter(adapterHours);

            ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, arrayStatus);
            spnStatus.setAdapter(adapterStatus);


            //Activity and its Id services after edit call.
            editListPos = position;
            JSON_PROJECT = ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/ActivityDetails?"
                    + "ProjId=" + mArray.get(position).getProject_id() + "&"
                    + "CustId=" + mArray.get(position).getCustom_id() + "&"
                    + "ActivityId="
                    + "&LOCATION_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "")
                    + "&COMPANY_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "");
            LogMsg.e("JSON_PROJECT", JSON_PROJECT);
            loadProjectId("edit");


            etCust.setText(mArray.get(position).getCustom_id());
            etProject_Id.setText(mArray.get(position).getProject_id());
            etDetais.setText(mArray.get(position).getActivity_det());
            etRemarks_one.setText(mArray.get(position).getRemarks_one());
            spnHours.setSelection(adapterHours.getPosition(mArray.get(position).getHours()));
            spnStatus.setSelection(adapterStatus.getPosition(mArray.get(position).getStatus()));

            dlg_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    conDialog.dismiss();
                }
            });

            etCust.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCustomerList();
                }
            });

            spnActivityID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Activity_id = mActCode.get(position);
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


            btn_add.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SimpleDateFormat")
                @Override
                public void onClick(View view) {
                    if (isValidate()) {
                        timeFormat = new SimpleDateFormat("HH.mm");
                        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                        DailyActivityPojo ap = new DailyActivityPojo();
                        ap.setCustom_id(etCust.getText().toString().trim());
                        ap.setProject_id(etProject_Id.getText().toString().trim());
                        ap.setActivity(spnActivityID.getSelectedItem().toString().trim());
                        ap.setActivity_id(Activity_id);
                        ap.setActivity_det(etDetais.getText().toString().trim());
                        ap.setHours(spnHours.getSelectedItem().toString());
                        ap.setStatus(spnStatus.getSelectedItem().toString());
                        ap.setRemarks_one(etRemarks_one.getText().toString().trim());
                        ap.setRemarks_two(etRemarks_one.getText().toString().trim());

                        mArray.set(newPos, ap);
                        mAdap = new DailyActivityAdapter(context, mArray, removeClickListener);
                        dailyBinding.lstDaily.setAdapter(mAdap);
                        mAdap.notifyDataSetChanged();
                        dailyBinding.lstDaily.smoothScrollToPosition(newPos);

                        sum = 0;
                        for (int i = 0; i < mArray.size(); i++) {
                            try {
                                totalTime = timeFormat.parse(mArray.get(i).getHours());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            sum = sum + totalTime.getTime();
                        }
                        dailyBinding.actHours.setText(timeFormat.format(new Date(sum)));
                        conDialog.dismiss();
                    }

                }

                private boolean isValidate() {

                    if (etCust.getText().toString().length() == 0) {
                        Toast.makeText(context, "Customer ID required", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if (spnActivityID.getSelectedItem().equals("Select")) {
                        Toast.makeText(context, "Please select Activity ID", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if (etDetais.getText().toString().length() == 0) {
                        etDetais.requestFocus();
                        etDetais.setError("Activity Details required");
                        return false;
                    }
                    if (spnHours.getSelectedItem().equals("Hours")) {
                        Toast.makeText(context, "Please select Hours", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if (spnStatus.getSelectedItem().equals("Status")) {
                        Toast.makeText(context, "Please select Status", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    return true;
                }
            });
            conDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addActivityDialog() {
        try {
            final Dialog conDialog = new Dialog(context, R.style.MyCustomTheme);
            conDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            conDialog.setCancelable(false);
            conDialog.setContentView(R.layout.dialog_daily_activity);

            ImageView dlg_cancel = conDialog.findViewById(R.id.dlg_cancel);
            etCust = conDialog.findViewById(R.id.et_customer);
            etProject_Id = conDialog.findViewById(R.id.et_project_id);
            final EditText etDetais = conDialog.findViewById(R.id.et_details);
            final EditText etRemarks_one = conDialog.findViewById(R.id.et_daily_act_remarks);
            spnActivityID = conDialog.findViewById(R.id.spn_act_id);
            final Spinner spnHours = conDialog.findViewById(R.id.spn_hours);
            final Spinner spnStatus = conDialog.findViewById(R.id.spn_status);
            Button btn_add = conDialog.findViewById(R.id.btn_add);

            dlg_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    conDialog.dismiss();
                }
            });

            etCust.setFocusable(false);
            etCust.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCustomerList();
                }
            });


            etProject_Id.setFocusable(false);


            this.arrayHours = new String[]{
                    "Hours",
                    "00.30", "01.00", "01.30", "02.00", "02.30", "03.00", "03.30", "04.00", "04.30", "05.00",
                    "05.30", "06.00", "06.30", "07.00", "07.30", "08.00", "08.30", "09.00", "09.30", "10.00"
            };

            ArrayAdapter<String> adapterHours = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, arrayHours);
            spnHours.setAdapter(adapterHours);

            this.arrayStatus = new String[]{
                    "Status",
                    "Complete", "Progress", "Pending", "Assigned", "Not Assigned"};

            ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, arrayStatus);
            spnStatus.setAdapter(adapterStatus);

            spnActivityID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Activity_id = mActCode.get(position);
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


            btn_add.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SimpleDateFormat")
                @Override
                public void onClick(View view) {
                    if (isValidate()) {
                        timeFormat = new SimpleDateFormat("HH.mm");
                        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        DailyActivityPojo ap = new DailyActivityPojo();
                        ap.setCustom_id(etCust.getText().toString().trim());
                        ap.setProject_id(etProject_Id.getText().toString().trim());
                        ap.setActivity(spnActivityID.getSelectedItem().toString().trim());
                        ap.setActivity_id(Activity_id);
                        ap.setActivity_det(etDetais.getText().toString().trim());
                        ap.setHours(spnHours.getSelectedItem().toString());
                        ap.setStatus(spnStatus.getSelectedItem().toString());
                        ap.setRemarks_one(etRemarks_one.getText().toString().trim());
                        ap.setRemarks_two(etRemarks_one.getText().toString().trim());

                        mArray.add(ap);
                        conDialog.dismiss();
                        mAdap = new DailyActivityAdapter(DailyActivity.this, mArray, removeClickListener);
                        dailyBinding.lstDaily.setAdapter(mAdap);
                        mAdap.notifyDataSetChanged();
                        dailyBinding.lstDaily.smoothScrollToPosition(mAdap.getCount());


                        sum = 0;
                        for (int i = 0; i < mArray.size(); i++) {
                            try {
                                totalTime = timeFormat.parse(mArray.get(i).getHours());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            sum = sum + totalTime.getTime();
                        }
                        dailyBinding.actHours.setText(timeFormat.format(new Date(sum)));
                    }
                }

                private boolean isValidate() {
                    if (etCust.getText().toString().length() == 0) {
                        Toast.makeText(context, "Customer ID required", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if (spnActivityID.getSelectedItem().equals("Select")) {
                        Toast.makeText(context, "Activity ID required", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if (etDetais.getText().toString().length() == 0) {
                        etDetais.requestFocus();
                        etDetais.setError("Activity Details required");
                        return false;
                    }
                    if (spnHours.getSelectedItem().equals("Hours")) {
                        Toast.makeText(context, "Hours value required", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if (spnStatus.getSelectedItem().equals("Status")) {
                        Toast.makeText(context, "Status value required", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    return true;
                }
            });
            conDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //company list with search.
    private void showCustomerList() {
        try {
            final Dialog custDialog = new Dialog(context);
            custDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            custDialog.setContentView(R.layout.customer_details);

            final SearchView mSearch = custDialog.findViewById(R.id.search_client);
            final ListView custList = custDialog.findViewById(R.id.customer_list);
            ImageView ivClose = custDialog.findViewById(R.id.dlg_close);
            final ImageView ivScrollUp = custDialog.findViewById(R.id.iv_scroll_up);


            final CustomerDetailsAdapter mDet = new CustomerDetailsAdapter(context, mArrayProject);
            custList.setAdapter(mDet);
            mDet.notifyDataSetChanged();

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    custDialog.dismiss();
                }
            });

            ivScrollUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    custList.smoothScrollToPosition(0);
                }
            });

            custList.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem < 10) {
                        ivScrollUp.setVisibility(View.INVISIBLE);
                    } else {
                        ivScrollUp.setVisibility(View.VISIBLE);
                    }
                }
            });

            custList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {

                    mActCode = new ArrayList<>();
                    mActDes = new ArrayList<>();

                    if (iLength != 0) {
                        etCust.setText(CustomerDetailsAdapter.filters.get(position).getCUSTOMER_ID().replaceAll("\\s+", ""));
                        etProject_Id.setText(CustomerDetailsAdapter.filters.get(position).getPROJECT_ID().replaceAll("\\s+", ""));

                        JSON_PROJECT = ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/ActivityDetails?"
                                + "ProjId=" + CustomerDetailsAdapter.filters.get(position).getPROJECT_ID().replaceAll("\\s+", "") + "&"
                                + "CustId=" + CustomerDetailsAdapter.filters.get(position).getCUSTOMER_ID().replaceAll("\\s+", "") + "&"
                                + "ActivityId="
                                + "&LOCATION_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "")
                                + "&COMPANY_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "");

                    } else {
                        etCust.setText(mArrayProject.get(position).getCUSTOMER_ID().replaceAll("\\s+", ""));
                        etProject_Id.setText(mArrayProject.get(position).getPROJECT_ID().replaceAll("\\s+", ""));

                        JSON_PROJECT = ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/ActivityDetails?"
                                + "ProjId=" + mArrayProject.get(position).getPROJECT_ID().replaceAll("\\s+", "") + "&"
                                + "CustId=" + mArrayProject.get(position).getCUSTOMER_ID().replaceAll("\\s+", "") + "&"
                                + "ActivityId="
                                + "&LOCATION_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "")
                                + "&COMPANY_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "");
                    }

                    LogMsg.e("JSON_PROJECT", JSON_PROJECT);
                    loadProjectId("fresh");
                    iLength = 0;
                    custDialog.dismiss();
                }
            });
            mSearch.setQueryHint("Search customer");
            mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String arg0) {
                    // TODO Auto-generated method stub
                    mSearch.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    // TODO Auto-generated method stub
                    iLength = query.length();
                    mDet.getFilter().filter(query);
                    return false;
                }
            });

            custDialog.show();
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            custDialog.getWindow().setLayout(width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCustomersService() {
        CustomProgressbar.showProgressBar(context, false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CustomProgressbar.hideProgressBar();
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject LocationDataResult = jsono.getJSONObject("CustomerDetailsResult");
                            JSONObject CustomerMessage = LocationDataResult.getJSONObject("CustomerMessage");
                            Error = CustomerMessage.optString("ErrorMsg");
                            Success = CustomerMessage.optString("Success");

                            if (Success.equals("true")) {
                                JSONArray CSTDetails = LocationDataResult.getJSONArray("CSTDetails");

                                for (int i = 0; i < CSTDetails.length(); i++) {

                                    ProjectPojo pj = new ProjectPojo();
                                    pj.setCUSTOMER_ID(CSTDetails.getJSONObject(i).getString("CUSTOMER_ID").replaceAll("\\s+$", ""));
                                    pj.setCUSTOMER_NAME(CSTDetails.getJSONObject(i).getString("CUSTOMER_NAME").replaceAll("\\s+$", ""));
                                    pj.setPROJECT_ID(CSTDetails.getJSONObject(i).getString("PROJECT_ID").replaceAll("\\s+$", ""));
                                    pj.setPROJECT_NAME(CSTDetails.getJSONObject(i).getString("PROJECT_NAME").replaceAll("\\s+$", ""));

                                    mArrayProject.add(pj);
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

    private void loadProjectId(final String editValue) {
        CustomProgressbar.showProgressBar(context, false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_PROJECT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CustomProgressbar.hideProgressBar();
                        mActCode.clear();
                        mActDes.clear();
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject ActivityDetailsResult = jsono.getJSONObject("ActivityDetailsResult");
                            JSONObject ActivityMessage = ActivityDetailsResult.getJSONObject("ActivityMessage");
                            Error = ActivityMessage.optString("ErrorMsg");
                            Success = ActivityMessage.optString("Success");

                            if (Success.equals("true")) {
                                JSONArray ACDetails = ActivityDetailsResult.getJSONArray("ACDetails");
                                for (int i = 0; i < ACDetails.length(); i++) {
                                    mActCode.add(ACDetails.getJSONObject(i).getString("Activity_Id"));
                                    mActDes.add(ACDetails.getJSONObject(i).getString("Activity_Description"));
                                }
                                mActDes.add(0, "Select");
                                mActCode.add(0, "Select");
                                adapterAct = new ArrayAdapter<>(context,
                                        android.R.layout.simple_spinner_dropdown_item, mActDes);
                                spnActivityID.setAdapter(adapterAct);
                                adapterAct.notifyDataSetChanged();

                                if (editValue.equals("edit")) {
                                    spnActivityID.setSelection(adapterAct.getPosition(mArray.get(editListPos).getActivity()));
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

    public void postAddActivity() {
        CustomProgressbar.showProgressBar(context, false);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject;
        try {
            try {
                int mode = 1;
                jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                if (mArray.size() > 0) {
                    for (int i = 0; i < mArray.size(); i++) {
                        JSONObject jsonob2 = new JSONObject();
                        try {
                            jsonob2.put("LI_NO", String.valueOf(mode));
                            jsonob2.put("CUSTOMER_ID", mArray.get(i).getCustom_id());
                            jsonob2.put("PROJECT_ID", mArray.get(i).getProject_id());
                            jsonob2.put("Activity_Id", mArray.get(i).getActivity_id());
                            jsonob2.put("ACTIVITY", mArray.get(i).getActivity());
                            jsonob2.put("ACTIVITY_DETAILS", mArray.get(i).getActivity_det());
                            jsonob2.put("TOTAL_HOURS", mArray.get(i).getHours());
                            double hours = Double.parseDouble(mArray.get(i).getHours()) * 100 / 8.00;
                            jsonob2.put("PERCENTAGE", String.valueOf(hours));
                            switch (mArray.get(i).getStatus()) {
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
                            }
                            jsonob2.put("REMARKS1", mArray.get(i).getRemarks_one());
                            jsonob2.put("REMARKS2", "");
                            jsonob2.put("REMARKS3", "");
                            jsonob2.put("user_id", CommonMethods.getPrefsData(DailyActivity.this, PrefrenceKey.USER_ID, ""));
                            jsonob2.put("Doc_Status", "F");
                            jsonob2.put("ConvertedTime", mArray.get(i).getHours());
                            jsonob2.put("CaseId", "0");
                            mode = mode + 1;
                            jsonArray.put(jsonob2);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                jsonObject.put("Associate_Name", CommonMethods.getPrefsData(DailyActivity.this, PrefrenceKey.USER_NAME, ""));
                jsonObject.put("Associate_Code", CommonMethods.getPrefsData(DailyActivity.this, PrefrenceKey.ASS_CODE, ""));
                jsonObject.put("Transaction_No", "");
                jsonObject.put("TRANSACTION_MODE", "0");
                jsonObject.put("COMPANY_NO", CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, ""));
                jsonObject.put("LOCATION_NO", CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, ""));
                jsonObject.put("REPORTING_DATE", CommonMethods.changeDateTOyyyyMMdd(dailyBinding.actDate.getText().toString().trim()));
                jsonObject.put("SERVER_DATE", CommonMethods.mobileCurrentDate());
                jsonObject.put("ItemDtail", jsonArray);
                postAddActivity = jsonObject.toString(2);
                LogMsg.d("ADD_FRESH", jsonObject.toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/SetAllActivity",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            LogMsg.d("addResponse", response);
                            CustomProgressbar.hideProgressBar();
                            try {
                                JSONObject jsono = new JSONObject(response);
                                JSONObject Messsage = jsono.getJSONObject("Messsage");
                                String Success = Messsage.optString("Success");
                                String Error = Messsage.optString("ErrorMsg");
                                ActivityCode = Messsage.optString("ActivityCode");
                                if (!Success.equals("true")) {
                                    CommonMethods.setSnackBar(dailyBinding.llRoot, Error);
                                } else {
                                    dialog = new Dialog(context);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(false);
                                    dialog.setContentView(R.layout.logout);
                                    TextView txtMsg = dialog.findViewById(R.id.dlg_msg);
                                    txtMsg.setText("Activity added successfully" + "\n" + "Sure to Authorize now?");
                                    Button cancel = dialog.findViewById(R.id.cancel);
                                    Button yes = dialog.findViewById(R.id.yes);
                                    yes.setText(getString(R.string.yes));
                                    cancel.setText(getString(R.string.no));

                                    yes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            TRANSACTION_NO = ActivityCode;
                                            submitActivity();
                                        }
                                    });

                                    cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    });
                                    dialog.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressbar.hideProgressBar();
                    Toast.makeText(context, CommonMethods.volleyError(error), Toast.LENGTH_LONG).show();
                    finish();

                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "Content-Type/text/plain; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    return postAddActivity == null ? null : postAddActivity.getBytes(StandardCharsets.UTF_8);
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString;
                    responseString = String.valueOf(response.statusCode);
                    LogMsg.e("response", responseString);
                    return super.parseNetworkResponse(response);
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void submitActivity() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject;
        try {
            try {
                int mode = 1;
                jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                if (mArray.size() > 0) {
                    for (int i = 0; i < mArray.size(); i++) {
                        JSONObject jsonob2 = new JSONObject();
                        try {
                            jsonob2.put("LI_NO", String.valueOf(mode));
                            jsonob2.put("CUSTOMER_ID", mArray.get(i).getCustom_id());
                            jsonob2.put("PROJECT_ID", mArray.get(i).getProject_id());
                            jsonob2.put("Activity_Id", mArray.get(i).getActivity_id());
                            jsonob2.put("ACTIVITY", mArray.get(i).getActivity_id());
                            jsonob2.put("ACTIVITY_DETAILS", mArray.get(i).getActivity_det());
                            jsonob2.put("TOTAL_HOURS", mArray.get(i).getHours());
                            double hours = Double.parseDouble(mArray.get(i).getHours()) * 100 / 8.00;
                            jsonob2.put("PERCENTAGE", String.valueOf(hours));

                            switch (mArray.get(i).getStatus()) {
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
                                default:
                                    jsonob2.put("STATUS", "4");
                                    break;
                            }

                            jsonob2.put("REMARKS1", mArray.get(i).getRemarks_one());
                            jsonob2.put("REMARKS2", "");
                            jsonob2.put("REMARKS3", "");
                            jsonob2.put("user_id", CommonMethods.getPrefsData(DailyActivity.this, PrefrenceKey.USER_ID, ""));
                            jsonob2.put("Doc_Status", "A");
                            jsonob2.put("ConvertedTime", mArray.get(i).getHours());
                            jsonob2.put("CaseId", "0");

                            mode = mode + 1;
                            jsonArray.put(jsonob2);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                jsonObject.put("Associate_Name", CommonMethods.getPrefsData(DailyActivity.this, PrefrenceKey.USER_NAME, ""));
                jsonObject.put("Associate_Code", CommonMethods.getPrefsData(DailyActivity.this, PrefrenceKey.ASS_CODE, ""));
                jsonObject.put("Transaction_No", TRANSACTION_NO);
                jsonObject.put("TRANSACTION_MODE", "2");
                jsonObject.put("COMPANY_NO", CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, ""));
                jsonObject.put("LOCATION_NO", CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, ""));
                jsonObject.put("REPORTING_DATE", CommonMethods.changeDateTOyyyyMMdd(dailyBinding.actDate.getText().toString().trim()));
                jsonObject.put("SERVER_DATE", CommonMethods.mobileCurrentDate());

                jsonObject.put("ItemDtail", jsonArray);

                postAddActivity = jsonObject.toString(2);
                LogMsg.d("AUTH_ACT", jsonObject.toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/SetAllActivity",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            LogMsg.d("addResponse", response);
                            try {
                                JSONObject jsono = new JSONObject(response);
                                JSONObject Messsage = jsono.getJSONObject("Messsage");
                                String Success = Messsage.optString("Success");
                                String Error = Messsage.optString("ErrorMsg");
                                if (!Success.equals("true")) {
                                    CommonMethods.setSnackBar(dailyBinding.llRoot, Error);
                                } else {
                                    String MAIL_URL = ConsURL.baseURL(context) + "DailyActivityMailService/AutoMailService.svc/SendEmail?" +
                                            "COMPANY_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "") + "&" +
                                            "LOCATION_NO=" + CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "") + "&" +
                                            "tran_no=" + TRANSACTION_NO + "&" +
                                            "USER_ID=" + CommonMethods.getPrefsData(DailyActivity.this, PrefrenceKey.USER_ID, "") + "&" +
                                            "SERVER_DATE=" + CommonMethods.mobileCurrentDate() + "&" +
                                            "REPORTING_DATE=" + CommonMethods.changeDateTOyyyyMMdd(dailyBinding.actDate.getText().toString().trim()) + "&" +
                                            "SPIN_FLAG=" + "V" + "&" +
                                            "ASSOCIATE_CODE=" + CommonMethods.getPrefsData(DailyActivity.this, PrefrenceKey.ASS_CODE, "") + "&" +
                                            "ASSOCIATE_NAME=" + CommonMethods.getPrefsData(DailyActivity.this, PrefrenceKey.USER_NAME, "").replaceAll(" ", "%20");

                                    LogMsg.e("MAIL_URL", MAIL_URL);
                                    //  new sendMailAsyncTask().execute(MAIL_URL);
                                    sendActivityMailService(MAIL_URL);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, CommonMethods.volleyError(error), Toast.LENGTH_LONG).show();
                    finish();

                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "Content-Type/text/plain; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    return postAddActivity == null ? null : postAddActivity.getBytes(StandardCharsets.UTF_8);
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString;
                    responseString = String.valueOf(response.statusCode);
                    LogMsg.e("response", responseString);
                    return super.parseNetworkResponse(response);
                }
            };
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendActivityMailService(String mailURL) {
        CustomProgressbar.showProgressBar(context, false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, mailURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CustomProgressbar.hideProgressBar();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            sendMailMsgResult = jsonObject.optString("SendEmailResult");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        dialog = new Dialog(context);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.logout);

                        TextView txtMsg = dialog.findViewById(R.id.dlg_msg);
                        txtMsg.setText(R.string.activity_authorized);
                        Button cancel = dialog.findViewById(R.id.cancel);
                        cancel.setText(R.string.okay);
                        Button yes = dialog.findViewById(R.id.yes);
                        yes.setVisibility(View.GONE);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Toast.makeText(DailyActivity.this, sendMailMsgResult, Toast.LENGTH_SHORT).show();
                                DailyActivity.this.finish();
                            }
                        });
                        dialog.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomProgressbar.hideProgressBar();
                        Toast.makeText(context, CommonMethods.volleyError(error), Toast.LENGTH_SHORT).show();
                        DailyActivity.this.finish();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
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

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
