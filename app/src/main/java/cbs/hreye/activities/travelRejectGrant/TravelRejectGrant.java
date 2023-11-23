package cbs.hreye.activities.travelRejectGrant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import cbs.hreye.R;
import cbs.hreye.network.RetrofitClient;
import cbs.hreye.pojo.TravelGetModel.GDetail;
import cbs.hreye.pojo.TravelGetModel.TravelGrandRejectDetails;
import cbs.hreye.pojo.TravelGrantRejectPostModel.Tgr;
import cbs.hreye.pojo.TravelGrantRejectPostModel.TravelGrandPostDetails;
import cbs.hreye.pojo.TravelGrantRejectPostModel.TravelGrandResponse;
import cbs.hreye.pojo.TravelGrantRejectPostModel.TravelRejectResponse;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.PrefrenceKey;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravelRejectGrant extends AppCompatActivity implements TravelRejectGrandAdapter.PassCheckedListDataTravel {
    private Context mContext;
    private TextView tvHeader;
    private Dialog dialog;
    private ImageView ivBack, ivGrant, ivReject;
    private LinearLayout llGrantReject;
    private CheckBox cbSelectAll;
    private RecyclerView rvGrantReject;
    private RecyclerView.LayoutManager mLayoutManager;
    private TravelRejectGrandAdapter travelRejectGrandAdapter;
    private LinearLayout llRepunch;

    private String error, success, grantRejectMsg, errorMsg;
    ArrayList<GDetail> travelDetails = new ArrayList<>();
    String companyName, LocationNo;
    public ArrayList<GDetail> alGrantRejectList = new ArrayList<GDetail>();
    TravelGrandPostDetails travelGrandPostDetails = new TravelGrandPostDetails();
    ArrayList<Tgr> travelPostDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_reject_grant);
        mContext = TravelRejectGrant.this;

        getID();
    }

    private void getID() {
        tvHeader = findViewById(R.id.header_text);
        ivBack = findViewById(R.id.header_back);
        ivGrant = findViewById(R.id.header_grant);
        ivReject = findViewById(R.id.header_reject);
        llGrantReject = findViewById(R.id.ll_grant_reject);
        rvGrantReject = findViewById(R.id.rv_grant_reject);
        cbSelectAll = findViewById(R.id.cb_select_all);
        llRepunch = findViewById(R.id.ll_root);
        tvHeader.setText("Travel Grant/Rejection");
        llGrantReject.setVisibility(View.VISIBLE);

        rvGrantReject.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        rvGrantReject.setLayoutManager(mLayoutManager);
        travelRejectGrandAdapter = new TravelRejectGrandAdapter(mContext, llGrantReject, this);

        if (CommonMethods.isOnline(mContext)) {
            getTravelGrandRejectDetailsData();
        } else {
            CommonMethods.setSnackBar(llRepunch, getString(R.string.net));
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivGrant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonMethods.isOnline(mContext)) {
                    OnTravelGrandPostData();
                } else {
                    CommonMethods.setSnackBar(llRepunch, getString(R.string.net));
                }
            }
        });

        ivReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonMethods.isOnline(mContext)) {
                    OnTravelRejectPostData();
                } else {
                    CommonMethods.setSnackBar(llRepunch, getString(R.string.net));
                }

            }
        });
    }
    @Override
    public void onCheckedListTravel(ArrayList<GDetail> grantRejectPojoArrayList) {
        alGrantRejectList = grantRejectPojoArrayList;
    }

    public void getTravelGrandRejectDetailsData() {
        CustomProgressbar.showProgressBar(mContext, false);

        RetrofitClient.getInstance(mContext).getMyApi().getTravelGrantRejectList(CommonMethods.getPrefsData(mContext, PrefrenceKey.COMPANY_NO, ""), CommonMethods.getPrefsData(mContext, PrefrenceKey.LOCATION_NO, "")).enqueue(new Callback<TravelGrandRejectDetails>() {
            @Override
            public void onResponse(Call<TravelGrandRejectDetails> call, Response<TravelGrandRejectDetails> response) {
                if (response.code() == 200 && response.body() != null) {

                    TravelGrandRejectDetails travelGrandRejectDetails = response.body();
                    travelDetails.addAll(travelGrandRejectDetails.getGrantRejectResult().getGDetail());
                    if (travelDetails.size() != 0) {
                        for (int i = 0; i < travelDetails.size(); i++) {
                            GDetail gDetail = new GDetail();

                            gDetail.setAppNo(String.valueOf(i + 1));
                            gDetail.setAssoCode(travelDetails.get(i).getAssoCode());
                            gDetail.setAssoName(travelDetails.get(i).getAssoName());
                            gDetail.setStatusDesc(travelDetails.get(i).getStatusDesc());
                            gDetail.setDocomentryName(travelDetails.get(i).getDocomentryName());
                            gDetail.setTranNo(travelDetails.get(i).getTranNo());
                            gDetail.setTranDate(travelDetails.get(i).getTranDate());
                            gDetail.setTrip(travelDetails.get(i).getTrip());
                            gDetail.setTravelMode(travelDetails.get(i).getTravelMode());
                            gDetail.setDate(travelDetails.get(i).getDate());
                            gDetail.setToDate(travelDetails.get(i).getToDate());
                            gDetail.setHotelFromDate(travelDetails.get(i).getHotelFromDate());
                            gDetail.setHotelToDate(travelDetails.get(i).getHotelToDate());
                            gDetail.setTravelFrom(travelDetails.get(i).getTravelFrom());
                            gDetail.setTravelTo(travelDetails.get(i).getTravelTo());
                            gDetail.setHotelRequired(travelDetails.get(i).getHotelRequired());
                            gDetail.setRemarks(travelDetails.get(i).getRemarks());
                            gDetail.setStatus(travelDetails.get(i).getStatus());
                            gDetail.setLiNo(travelDetails.get(i).getLiNo());

                            travelRejectGrandAdapter.addToArray(gDetail);
                        }
                        rvGrantReject.setAdapter(travelRejectGrandAdapter);
                        CustomProgressbar.hideProgressBar();
                    } else {
                        CommonMethods.setSnackBar(llRepunch, "No Record Found");
                        CustomProgressbar.hideProgressBar();
                    }
                }
            }

            @Override
            public void onFailure(Call<TravelGrandRejectDetails> call, Throwable t) {
                CustomProgressbar.hideProgressBar();
                CommonMethods.setSnackBar(llRepunch, t.toString());
            }
        });
    }

    public void OnTravelGrandPostData() {
        if (alGrantRejectList.size() > 0) {
            final Dialog cDialog = new Dialog(mContext);
            cDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            cDialog.setCancelable(true);
            cDialog.setContentView(R.layout.logout);
            TextView txtMsg = cDialog.findViewById(R.id.dlg_msg);
            txtMsg.setText("Sure to Grand Travel Request ?");
            Button cancel = cDialog.findViewById(R.id.cancel);
            cancel.setText(getString(R.string.no));
            Button YES = cDialog.findViewById(R.id.yes);
            YES.setText(R.string.yes);
            YES.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cDialog.dismiss();
                    for (int i = 0; i < alGrantRejectList.size(); i++) {
                        Tgr tgr = new Tgr();
                        tgr.setCompanyNo(CommonMethods.getPrefsData(mContext, PrefrenceKey.COMPANY_NO, ""));
                        tgr.setLocationNo(CommonMethods.getPrefsData(mContext, PrefrenceKey.LOCATION_NO, ""));
                        tgr.setLiNo(alGrantRejectList.get(i).getLiNo());
                        tgr.setTrip(alGrantRejectList.get(i).getTrip());
                        tgr.setTransactionMode("0");
                        tgr.setAssoCode(alGrantRejectList.get(i).getAssoCode());
                        tgr.setAssoName(alGrantRejectList.get(i).getAssoName());
                        tgr.setTravelMode(alGrantRejectList.get(i).getTravelMode());
                        String travelFromDate = alGrantRejectList.get(i).getDate();
                        if (!Objects.equals(travelFromDate, "")) {
                            String convertFromDate = CommonMethods.changeDateTOyyyyMMdd(travelFromDate);
                            tgr.setTravelFromDate(convertFromDate);
                        } else {
                            tgr.setTravelFromDate("");
                        }
                        String travelToDate = alGrantRejectList.get(i).getToDate();
                        if (!Objects.equals(travelToDate, "")) {
                            String convertToDate = CommonMethods.changeDateTOyyyyMMdd(travelToDate);
                            tgr.setTravelToDate(convertToDate);
                        } else {
                            tgr.setTravelToDate("");
                        }
                        tgr.setTranNo(alGrantRejectList.get(i).getTranNo());
                        String transDate = alGrantRejectList.get(i).getTranDate();
                        if (!Objects.equals(transDate, "")) {
                            String convertTransDate = CommonMethods.changeDateTOyyyyMMdd(transDate);
                            tgr.setTranDate(convertTransDate);
                        } else {
                            tgr.setTranDate("");
                        }

                        tgr.setTravelFrom(alGrantRejectList.get(i).getTravelFrom());
                        tgr.setToTravel(alGrantRejectList.get(i).getTravelTo());
                        tgr.setHotelRequired(alGrantRejectList.get(i).getHotelRequired());
                        tgr.setReason(alGrantRejectList.get(i).getReason());
                        tgr.setRemarks(alGrantRejectList.get(i).getRemarks());
                        tgr.setStatus(alGrantRejectList.get(i).getStatus());
                        travelPostDetails.add(tgr);
                    }
                    travelGrandPostDetails.setTgr(travelPostDetails);

                    CustomProgressbar.showProgressBar(mContext, false);
                    RetrofitClient.getInstance(mContext).getMyApi().getPostTravelGrand(travelGrandPostDetails).enqueue(new Callback<TravelGrandResponse>() {
                        @Override
                        public void onResponse(Call<TravelGrandResponse> call, Response<TravelGrandResponse> response) {
                            if (response.code() == 200 && response.body() != null) {
                                CustomProgressbar.hideProgressBar();
                                TravelGrandResponse travelGrandResponse = response.body();
                                Boolean successMsg = travelGrandResponse.getPostTravelGrantResult().getMessage().getSuccess();

                                if (successMsg == true) {
                                    String grandMsg = travelGrandResponse.getPostTravelGrantResult().getMessage().getErrorMsg();
                                    dialog = new Dialog(mContext);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(false);
                                    dialog.setContentView(R.layout.logout);
                                    TextView txtMsg = dialog.findViewById(R.id.dlg_msg);
                                    Button okay = dialog.findViewById(R.id.cancel);
                                    Button yes = dialog.findViewById(R.id.yes);
                                    yes.setVisibility(View.GONE);
                                    okay.setText(getString(R.string.okay));
                                    txtMsg.setText(grandMsg);

                                    okay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            alGrantRejectList.clear();
                                            finish();
                                        }
                                    });
                                    dialog.show();

                                } else {
                                    String msg = travelGrandResponse.getPostTravelGrantResult().getMessage().getErrorMsg();
                                    CommonMethods.setSnackBar(llRepunch, msg);
                                }


                            }
                        }

                        @Override
                        public void onFailure(Call<TravelGrandResponse> call, Throwable t) {
                            CustomProgressbar.hideProgressBar();
                            CommonMethods.setSnackBar(llRepunch, t.toString());
                        }
                    });
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cDialog.dismiss();
                }
            });
            cDialog.show();
        } else {
            CommonMethods.setSnackBar(llRepunch, "Please select at least one item");
        }
    }

    public void OnTravelRejectPostData() {
        if (alGrantRejectList.size() > 0) {
            final Dialog cDialog = new Dialog(mContext);
            cDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            cDialog.setCancelable(true);
            cDialog.setContentView(R.layout.logout);
            TextView txtMsg = cDialog.findViewById(R.id.dlg_msg);
            txtMsg.setText("Sure to Reject Travel Request ?");
            Button cancel = cDialog.findViewById(R.id.cancel);
            cancel.setText(getString(R.string.no));
            Button YES = cDialog.findViewById(R.id.yes);
            YES.setText(R.string.yes);

            YES.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cDialog.dismiss();
                    for (int i = 0; i <= alGrantRejectList.size() - 1; i++) {
                        Tgr tgr = new Tgr();
                        tgr.setCompanyNo(CommonMethods.getPrefsData(mContext, PrefrenceKey.COMPANY_NO, ""));
                        tgr.setLocationNo(CommonMethods.getPrefsData(mContext, PrefrenceKey.LOCATION_NO, ""));
                        tgr.setLiNo(alGrantRejectList.get(i).getLiNo());
                        tgr.setTrip(alGrantRejectList.get(i).getTrip());
                        tgr.setTransactionMode("0");
                        tgr.setAssoCode(alGrantRejectList.get(i).getAssoCode());
                        tgr.setAssoName(alGrantRejectList.get(i).getAssoName());
                        tgr.setTravelMode(alGrantRejectList.get(i).getTravelMode());
                        String travelFromDate = alGrantRejectList.get(i).getDate();
                        if (!Objects.equals(travelFromDate, "")) {
                            String convertFromDate = CommonMethods.changeDateTOyyyyMMdd(travelFromDate);
                            tgr.setTravelFromDate(convertFromDate);
                        } else {
                            tgr.setTravelFromDate("");
                        }
                        String travelToDate = alGrantRejectList.get(i).getToDate();
                        if (!Objects.equals(travelToDate, "")) {
                            String convertTravelToDate = CommonMethods.changeDateTOyyyyMMdd(travelToDate);
                            tgr.setTravelToDate(convertTravelToDate);
                        } else {
                            tgr.setTravelToDate("");
                        }
                        tgr.setTranNo(alGrantRejectList.get(i).getTranNo());
                        String transDate = alGrantRejectList.get(i).getTranDate();
                        if (transDate != null) {
                            String convertTransDate = CommonMethods.changeDateTOyyyyMMdd(transDate);
                            tgr.setTranDate(convertTransDate);
                        } else {
                            tgr.setTranDate("");
                        }
                        tgr.setTravelFrom(alGrantRejectList.get(i).getTravelFrom());
                        tgr.setToTravel(alGrantRejectList.get(i).getTravelTo());
                        tgr.setHotelRequired(alGrantRejectList.get(i).getHotelRequired());
                        tgr.setReason(alGrantRejectList.get(i).getReason());
                        tgr.setRemarks(alGrantRejectList.get(i).getRemarks());
                        tgr.setStatus(alGrantRejectList.get(i).getStatus());
                        travelPostDetails.add(tgr);
                    }
                    travelGrandPostDetails.setTgr(travelPostDetails);

                    CustomProgressbar.showProgressBar(mContext, false);
                    RetrofitClient.getInstance(mContext).getMyApi().getPostTravelReject(travelGrandPostDetails).enqueue(new Callback<TravelRejectResponse>() {
                        @Override
                        public void onResponse(Call<TravelRejectResponse> call, Response<TravelRejectResponse> response) {
                            if (response.code() == 200 && response.body() != null) {
                                CustomProgressbar.hideProgressBar();
                                TravelRejectResponse travelRejectResponse = response.body();
                                Boolean successMsg = travelRejectResponse.getPostTravelRejectResult().getMessage().getSuccess();
                                if (successMsg == true) {
                                    String rejectMsg = travelRejectResponse.getPostTravelRejectResult().getMessage().getErrorMsg();
                                    dialog = new Dialog(mContext);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(false);
                                    dialog.setContentView(R.layout.logout);
                                    TextView txtMsg = dialog.findViewById(R.id.dlg_msg);
                                    Button okay = dialog.findViewById(R.id.cancel);
                                    Button yes = dialog.findViewById(R.id.yes);
                                    yes.setVisibility(View.GONE);
                                    okay.setText(getString(R.string.okay));
                                    txtMsg.setText(rejectMsg);

                                    okay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            alGrantRejectList.clear();
                                            finish();
                                        }
                                    });
                                    dialog.show();
                                } else {
                                    String msg = travelRejectResponse.getPostTravelRejectResult().getMessage().getErrorMsg();
                                    CommonMethods.setSnackBar(llRepunch, msg);
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<TravelRejectResponse> call, Throwable t) {
                            CustomProgressbar.hideProgressBar();
                            CommonMethods.setSnackBar(llRepunch, t.toString());
                        }
                    });
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cDialog.dismiss();
                }
            });
            cDialog.show();

        } else {
            CommonMethods.setSnackBar(llRepunch, "Please select at least one item");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        alGrantRejectList.clear();
    }
}