package cbs.hreye.activities.travelRequest.TravelRequestDetailData;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cbs.hreye.R;
import cbs.hreye.activities.travelRequest.AddTravelRequestFormData.AddTravelRequestFormDataActivity;
import cbs.hreye.activities.travelRequest.TravelRequestAddData.AddTravelRequestActivity;
import cbs.hreye.activities.travelRequest.TravelRequestModel;
import cbs.hreye.activities.travelRequest.TravelRequestResponseData;
import cbs.hreye.pojo.TravelRequestGetDetailData;
import cbs.hreye.pojo.TravelRequestPostDataModel;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.PrefrenceKey;

public class TravelRequestDataDetailActivity extends AppCompatActivity implements TravelRequestDataDetailMvpView, OnTravelRequestDetailItemClickListener {

    private TextView toolBarHeaderTextView;
    private ImageView backButtonImageView;

    private ImageView rightImageView, addImageView;

    private LinearLayout rightAddRowLinearLayout;
    private RecyclerView travelRequestDetailDataRecyclerView;

    private String transactionNo="";
    private String transactionDate="";
    private String remark="";
    private String travelTypeValueData="";
    private String status="";
    private TravelRequestDataDetailAdapter travelRequestDataDetailAdapter;
    private TravelRequestDataDetailPresenter travelRequestDataDetailPresenter;
    private LinearLayout showSnakbarLayout;
    private Spinner travelTypeSpinner;
    private TextView remarkTextView;
    String travelType[]={"Select","Domestic","International"};
    ArrayList<TravelRequestModel> travelRequestModelArrayList;
    private String travelTypeValue="";

    private int REQUEST_CODE_ADD_TRAVEL_REQUEST_FORM=1295;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_request_data_detail);
        findViewIds();

        travelRequestDataDetailPresenter=new TravelRequestDataDetailPresenter(this,TravelRequestDataDetailActivity.this);

        Intent travelRequestintent = getIntent();
        Bundle receivedBundle = travelRequestintent.getExtras();
        if (receivedBundle != null) {
            transactionNo =  receivedBundle.getString("transactionNo");
            transactionDate =  receivedBundle.getString("transactionDate");
            status = receivedBundle.getString("status");
            remark =  receivedBundle.getString("remark");
            travelTypeValueData =  receivedBundle.getString("travelType");


            if(!TextUtils.isEmpty(remark)){
                remarkTextView.setText(remark);
            }

            if(transactionNo!=null){
                travelRequestDataDetailPresenter.fetchTravelRequestDetailData(transactionNo);
            }

        }

        setToolBarTitle();

        setUpTravelTypeSpinner(travelTypeSpinner,travelType);

        if(travelTypeValueData!=null){
            setSpinnerPosition(travelTypeValueData,travelType,travelTypeSpinner);
        }

    }

    private void findViewIds() {
        travelRequestDetailDataRecyclerView=findViewById(R.id.travel_request_detail_recycler_view);
        remarkTextView=findViewById(R.id.remark_textview);
        travelTypeSpinner=findViewById(R.id.travel_type_spinner);
    }

    private void setToolBarTitle() {
        toolBarHeaderTextView = findViewById(R.id.header_text);
        toolBarHeaderTextView.setText("Authorized Request");
        backButtonImageView = findViewById(R.id.header_back);
        backButtonImageView.setOnClickListener(v -> onBackPressed());

        rightAddRowLinearLayout = findViewById(R.id.ll_grant_reject);
        rightImageView =findViewById(R.id.header_grant);
        addImageView = findViewById(R.id.header_reject);
        addImageView.setImageResource(R.drawable.add_row);
        showSnakbarLayout=findViewById(R.id.ll_root);

        if(status.equalsIgnoreCase("A")){
            rightAddRowLinearLayout.setVisibility(View.GONE);
            addImageView.setVisibility(View.GONE);
        }else{
            rightAddRowLinearLayout.setVisibility(View.VISIBLE);
            addImageView.setVisibility(View.VISIBLE);
        }




        rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authorizeModifyDialog();
            }
        });


        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(travelTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("Select")){
                    Toast.makeText(TravelRequestDataDetailActivity.this,"Please select travel type",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Intent intent=new Intent(TravelRequestDataDetailActivity.this, AddTravelRequestFormDataActivity.class);
                    intent.putExtra("travelTypeValue",travelTypeValue);
                    intent.putExtra("isNavigate","FromGetData");
                    startActivityForResult(intent,REQUEST_CODE_ADD_TRAVEL_REQUEST_FORM);
                }
            }
        });



    }


    @Override
    public void getTravelRequestDetailData(List<TravelRequestModel> travelRequestGetDetailDataList) {
        if(travelRequestGetDetailDataList!=null){
            travelRequestModelArrayList= (ArrayList<TravelRequestModel>) travelRequestGetDetailDataList;
            travelRequestDataDetailAdapter=new TravelRequestDataDetailAdapter(this, (ArrayList<TravelRequestModel>) travelRequestGetDetailDataList,TravelRequestDataDetailActivity.this);
            travelRequestDetailDataRecyclerView.setAdapter(travelRequestDataDetailAdapter);
        }
    }

    @Override
    public void errorMessage(String msg) {
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void postTravelRequestDataStatus(String status) {
        Toast.makeText(this,status,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }


    private void authorizeModifyDialog() {
        final Dialog cDialog = new Dialog(this);
        cDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cDialog.setCancelable(true);
        cDialog.setContentView(R.layout.logout);
        TextView txtMsg = cDialog.findViewById(R.id.dlg_msg);
        txtMsg.setText(R.string.want_auth_modify);
        Button modify = cDialog.findViewById(R.id.cancel);
        modify.setText(getString(R.string.modify));
        Button Authorize = cDialog.findViewById(R.id.yes);
        Authorize.setText(getString(R.string.authorize));
        Authorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (travelRequestDataDetailAdapter.getItemCount() == 0){
                    CommonMethods.setSnackBar(showSnakbarLayout,getString(R.string.enter_minimum_record));
                }else {
                    if (CommonMethods.isOnline(TravelRequestDataDetailActivity.this)) {

                        if(travelRequestModelArrayList!=null && travelRequestModelArrayList.size()>0){

                            String companyNo=CommonMethods.getPrefsData(TravelRequestDataDetailActivity.this, PrefrenceKey.COMPANY_NO, "");
                            String locationNo=CommonMethods.getPrefsData(TravelRequestDataDetailActivity.this, PrefrenceKey.LOCATION_NO, "");
                            String userID=CommonMethods.getPrefsData(TravelRequestDataDetailActivity.this, PrefrenceKey.USER_ID, "");

                            TravelRequestPostDataModel travelRequestPostDataModel=new TravelRequestPostDataModel();
                            travelRequestPostDataModel.setTravelRequestList(travelRequestModelArrayList);
                            travelRequestPostDataModel.setTran_No(transactionNo);
                            travelRequestPostDataModel.setTransactionDate(CommonMethods.changeDateTOyyyyMMdd(transactionDate));
                            travelRequestPostDataModel.setTravel(travelTypeValueData);
                            travelRequestPostDataModel.setCompanyNo(companyNo);
                            travelRequestPostDataModel.setLocationNo(locationNo);
                            travelRequestPostDataModel.setRemarks("");
                            travelRequestPostDataModel.setUserID(userID);
                            travelRequestPostDataModel.setStatus("Authorized");
                            travelRequestPostDataModel.setTransMode("3");
                            travelRequestDataDetailPresenter.postTravelAuthrorizedRequestData(travelRequestPostDataModel);

                        }


                    } else {
                        CommonMethods.setSnackBar(showSnakbarLayout, getString(R.string.net));
                    }
                }
                cDialog.dismiss();
            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (travelRequestDataDetailAdapter.getItemCount() == 0){
                    CommonMethods.setSnackBar(showSnakbarLayout,getString(R.string.enter_minimum_record));
                }else {
                    if (CommonMethods.isOnline(TravelRequestDataDetailActivity.this)) {
                        // Modify Data



                    } else {
                        CommonMethods.setSnackBar(showSnakbarLayout, getString(R.string.net));
                    }
                }
                cDialog.dismiss();
            }
        });

        cDialog.show();
    }


    private void setUpTravelTypeSpinner(Spinner spinner, String[] data) {
        // Create an ArrayAdapter using the sample data and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);

        // Set the layout for the dropdown items
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter for the Spinner
        spinner.setAdapter(adapter);

        // Add an OnItemSelectedListener to handle item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item
                travelTypeValue = (String) parentView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here, but you can add logic if needed
            }
        });
    }

    private void setSpinnerPosition(String selectedValue,String []data,Spinner spinner) {
        // Find the position of the selected value in your Spinner array
        int position = Arrays.asList(data).indexOf(selectedValue);
        if (position >= 0) {
            // Set the Spinner's selection to the position if the value exists in the array
            spinner.setSelection(position);
        } else {
            // The selected value doesn't exist in the array, so you can set a default selection, e.g., "Select"
            spinner.setSelection(0); // 0 corresponds to "Select" in your array
        }
    }

    @Override
    public void onEditItem(int position) {

    }

    @Override
    public void onDeleteItem(int position) {
        travelRequestModelArrayList.remove(position);
        travelRequestDataDetailAdapter.replaceData(travelRequestModelArrayList);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_TRAVEL_REQUEST_FORM && resultCode == RESULT_OK) {
            // Now you have the single model in the previous activity
            if (data != null) {
                TravelRequestModel travelRequestModel= (TravelRequestModel) data.getSerializableExtra("inputRequest");
                if(travelRequestModel!=null){
                    travelRequestModel.setSrNo(String.valueOf(travelRequestModelArrayList.size()+1));
                    travelRequestModelArrayList.add(travelRequestModel);
                    travelRequestDataDetailAdapter.replaceData(travelRequestModelArrayList);
                    int lastPosition = travelRequestDataDetailAdapter.getItemCount() - 1;
                    if (lastPosition >= 0) {
                        travelRequestDetailDataRecyclerView.scrollToPosition(lastPosition);
                    }
                }
            }
        }


    }
}