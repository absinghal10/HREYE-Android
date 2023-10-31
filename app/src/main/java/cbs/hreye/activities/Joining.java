package cbs.hreye.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cbs.hreye.R;
import cbs.hreye.adapters.JoiningAdapter;
import cbs.hreye.pojo.BirthPojo;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;

public class Joining extends AppCompatActivity {

    Context context;
    ListView lstJoining;
    String JSON_URL;
    String error, success;
    ArrayList<BirthPojo> mArray;
    JoiningAdapter mJoiningAdapter;
    LinearLayout llRoot;
    TextView tvhear;
    ImageView ivBack;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joining);
        context = Joining.this;

        getID();
    }

    private void getID() {
        llRoot = findViewById(R.id.ll_root);
        lstJoining = findViewById(R.id.lst_join);
        tvhear = findViewById(R.id.header_text);
        ivBack = findViewById(R.id.header_back);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);

        tvhear.setText(R.string.new_joining);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (CommonMethods.isOnline(context)) {
            JSON_URL = ConsURL.baseURL(context)+ "HRLoginService/LoginService.svc/GetNewJoiningDetail?LoginName="
                    +"&LOCATION_NO="+CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, "")
                    +"&COMPANY_NO="+CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, "");

            LogMsg.d("Load_Joining", JSON_URL);
            mArray = new ArrayList<>();
            loadJoinService();
        }else{
            CommonMethods.setSnackBar(llRoot, getString(R.string.net));
        }
    }

    private void loadJoinService() {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject LocationDataResult = jsono.getJSONObject("NewJoiningDetailResult");
                            JSONObject CustomerMessage = LocationDataResult.getJSONObject("NewJoiningsMsgMessage");
                            error = CustomerMessage.optString("ErrorMsg");
                            success = CustomerMessage.optString("Success");

                            if (success.equals("true")) {
                                JSONArray TDetails = LocationDataResult.getJSONArray("NJDetails");

                                for (int i = 0; i < TDetails.length(); i++) {
                                    BirthPojo pj = new BirthPojo();

                                    pj.setDEPARTMENT(TDetails.getJSONObject(i).getString("DEPARTMENT"));
                                    pj.setDate(TDetails.getJSONObject(i).getString("Date"));
                                    pj.setEMAIL(TDetails.getJSONObject(i).getString("EMAIL"));
                                    pj.setMOBILE_NO(TDetails.getJSONObject(i).getString("MOBILE_NO"));
                                    pj.setNAME(TDetails.getJSONObject(i).getString("NAME"));

                                    mArray.add(pj);
                                }

                                Collections.sort(mArray, new Comparator<BirthPojo>() {
                                    SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

                                    @Override
                                    public int compare(BirthPojo lhs, BirthPojo rhs) {
                                        try {
                                            return f.parse(lhs.getDate()).compareTo(f.parse(rhs.getDate()));
                                        } catch (Exception e) {
                                            throw new IllegalArgumentException(e);
                                        }
                                    }
                                });
                                Collections.reverse(mArray);

                                mJoiningAdapter = new JoiningAdapter(Joining.this, mArray);
                                lstJoining.setAdapter(mJoiningAdapter);
                                mJoiningAdapter.notifyDataSetChanged();
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
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}