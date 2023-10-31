package cbs.hreye.activities;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cbs.hreye.R;
import cbs.hreye.adapters.NotificationsAdapter;
import cbs.hreye.pojo.NotificationPojo;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.DividerItemDecoration;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;

public class Notifications extends AppCompatActivity{
    private Context mContext;
    private Toolbar toolbar;
    private TextView tvTittle;
    private LinearLayout llRepunch;
    private RecyclerView rvNotification;
    private NotificationsAdapter notificationsAdapterDemo;
    private RecyclerView.LayoutManager mLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
      //private ActionMode actionMode;
    //private ActionModeCallback actionModeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);
        mContext = Notifications.this;
        getID();
        setToolbar();
    }

    private void getID() {
        toolbar = findViewById(R.id.toolbar);
        tvTittle = toolbar.findViewById(R.id.toolbar_title);
        rvNotification = findViewById(R.id.rv_notification);
        llRepunch = findViewById(R.id.ll_root);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        //actionModeCallback = new ActionModeCallback();

        rvNotification.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        rvNotification.setLayoutManager(mLayoutManager);
        rvNotification.setItemAnimator(new DefaultItemAnimator());
        rvNotification.addItemDecoration(new DividerItemDecoration(mContext, R.drawable.item_divider));
        notificationsAdapterDemo = new NotificationsAdapter(mContext);

        if (CommonMethods.isOnline(mContext)) {
            String jsonURL = ConsURL.baseURL(mContext) + "HRLoginService/LoginService.svc/notificationList?"
                    +"assoCode=" + CommonMethods.getPrefsData(mContext, PrefrenceKey.ASS_CODE, "")
                    +"&companyNo="+CommonMethods.getPrefsData(mContext, PrefrenceKey.COMPANY_NO, "")
                    +"&locationNo="+CommonMethods.getPrefsData(mContext, PrefrenceKey.LOCATION_NO, "");

            LogMsg.d("NOTIS_LIST", jsonURL);
            notificationsListService(jsonURL);
        } else {
            CommonMethods.setSnackBar(llRepunch, getString(R.string.net));
        }
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.backfilled);
            tvTittle.setText(getString(R.string.notifications));
        }
    }

    private void notificationsListService(String url) {
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject NotificationListResult = jsono.getJSONObject("NotificationListResult");
                            JSONObject LogMessage = NotificationListResult.getJSONObject("LogMessage");
                            String error = LogMessage.optString("ErrorMsg");
                            String success = LogMessage.optString("Success");
                            if (success.equals("true")) {
                                JSONArray notificationDetail = NotificationListResult.getJSONArray("notificationDetail");
                                NotificationPojo notificationPojo;
                                for (int i = 0; i < notificationDetail.length(); i++) {
                                    notificationPojo = new NotificationPojo();
                                    notificationPojo.setDateTime(notificationDetail.getJSONObject(i).getString("date"));
                                    notificationPojo.setDescription(notificationDetail.getJSONObject(i).getString("description"));
                                    notificationPojo.setNotificationId(notificationDetail.getJSONObject(i).getString("notificationId"));
                                    notificationPojo.setStatus(notificationDetail.getJSONObject(i).getString("status"));
                                    notificationPojo.setTittle(notificationDetail.getJSONObject(i).getString("title"));
                                    notificationsAdapterDemo.addToArray(notificationPojo);
                                }
                                 rvNotification.setAdapter(notificationsAdapterDemo);

                            } else {
                                CommonMethods.setSnackBar(llRepunch, error);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        Toast.makeText(mContext, CommonMethods.volleyError(error), Toast.LENGTH_LONG).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //active after long press on item
 /*   private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected messages
                    deleteMessages();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            notificationsAdapterDemo.clearSelections();
            actionMode = null;
            rvNotification.post(new Runnable() {
                @Override
                public void run() {
                    notificationsAdapterDemo.resetAnimationIndex();
                    notificationsAdapterDemo.notifyDataSetChanged();
                }
            });
        }
    }*/
   /* private void toggleSelection(int position) {
        notificationsAdapterDemo.toggleSelection(position);
        int count = notificationsAdapterDemo.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }


    // deleting the messages from recycler view
    private void deleteMessages() {
        notificationsAdapterDemo.resetAnimationIndex();
        List<Integer> selectedItemPositions =
                notificationsAdapterDemo.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            notificationsAdapterDemo.removeData(selectedItemPositions.get(i));
        }
        notificationsAdapterDemo.notifyDataSetChanged();
    }


    @Override
    public void onIconClicked(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);
    }

    @Override
    public void onMessageRowClicked(int position) {
        if (notificationsAdapterDemo.getSelectedItemCount() > 0) {
            enableActionMode(position);
        }
    }

    @Override
    public void onRowLongClicked(int position) {
        enableActionMode(position);
    }
*/


}
