package cbs.hreye.activities.travelRequest.TravelRequestAddData;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cbs.hreye.R;
import cbs.hreye.activities.travelRequest.AddTravelRequestFormData.AddTravelRequestFormDataActivity;
import cbs.hreye.activities.travelRequest.TravelRequestDetailData.TravelRequestDataDetailActivity;
import cbs.hreye.activities.travelRequest.TravelRequestModel;
import cbs.hreye.activities.travelRequest.TravelRequestResponseData;
import cbs.hreye.pojo.TravelRequestPostDataModel;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.PrefrenceKey;

public class AddTravelRequestActivity extends AppCompatActivity implements OnTravelRequestItemClickListener,TravelRequestAddDataMvpView {
    private TextView toolBarHeaderTextView;
    private TextView transactionDateTextView;
    private TextView remarkTextView;
    private Spinner travelTypeSpinner;
    private ImageView backButtonImageView;
    private ImageView toolbarAddDataImageView;
    private ImageView toolbarUploadDataImageView;
    private LinearLayout toolbarIconLayout;
    private int ADD_TRAVEL_REQUEST_FORM_CODE=1289;
    private int EDIT_TRAVEL_REQUEST_FORM_CODE=1290;

    private RecyclerView travelRequestPostDataRecyclerView;
    private ArrayList<TravelRequestModel> travelRequestPostList;

    private TravelRequestAddDataAdapter travelRequestAddDataAdapter;

    private TravelRequestAddDataPresenter travelRequestAddDataPresenter;

    public int mYear, mMonth, mDay;

    String trasactionDate="";

    String travelType[]={"Select","Domestic","International"};

    private String travelTypeValue="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel_request);

        findViewIds();
        setToolBarTitle();

        setUpTravelTypeSpinner(travelTypeSpinner,travelType);

        transactionDateTextView.setText(CommonMethods.changeDateFromyyyyMMdd(CommonMethods.mobileCurrentDate()));

        travelRequestPostList =new ArrayList<>();
        travelRequestAddDataPresenter=new TravelRequestAddDataPresenter(this,AddTravelRequestActivity.this);
        travelRequestAddDataAdapter=new TravelRequestAddDataAdapter(this,travelRequestPostList,AddTravelRequestActivity.this);
        travelRequestPostDataRecyclerView.setAdapter(travelRequestAddDataAdapter);

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

    private void findViewIds() {
        travelRequestPostDataRecyclerView=findViewById(R.id.travel_request_add_recycler_view);
        transactionDateTextView=findViewById(R.id.act_date);
        toolbarUploadDataImageView=findViewById(R.id.header_grant);
        toolbarAddDataImageView=findViewById(R.id.header_reject);
        toolbarIconLayout=findViewById(R.id.ll_grant_reject);
        remarkTextView=findViewById(R.id.remark_textview);
        travelTypeSpinner=findViewById(R.id.travel_type_spinner);
        toolbarAddDataImageView.setImageResource(R.drawable.add_row);
        toolbarIconLayout.setVisibility(View.VISIBLE);
    }

    private void setToolBarTitle() {
        toolBarHeaderTextView = findViewById(R.id.header_text);
        toolBarHeaderTextView.setText("Travel Request");
        backButtonImageView = findViewById(R.id.header_back);
        backButtonImageView.setOnClickListener(v -> onBackPressed());
        toolbarUploadDataImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check remark field is empty or not using TextUtils Class.
                if(TextUtils.isEmpty(remarkTextView.getText().toString().trim())){
                    Toast.makeText(AddTravelRequestActivity.this, "Please enter remark", Toast.LENGTH_SHORT).show();
                    return;
                }

                String companyName=CommonMethods.getPrefsData(AddTravelRequestActivity.this, PrefrenceKey.COMPANY_NO, "");
                String locationNo=CommonMethods.getPrefsData(AddTravelRequestActivity.this, PrefrenceKey.LOCATION_NO, "");

                TravelRequestPostDataModel travelRequestPostDataModel=new TravelRequestPostDataModel();
                travelRequestPostDataModel.setTravelRequestList(travelRequestPostList);
                travelRequestPostDataModel.setTran_No("");
                String userID=CommonMethods.getPrefsData(AddTravelRequestActivity.this, PrefrenceKey.USER_ID, "");
                travelRequestPostDataModel.setUserID(userID);
                travelRequestPostDataModel.setTransMode("1");
                travelRequestPostDataModel.setStatus("F");
                travelRequestPostDataModel.setCompanyNo(companyName);
                travelRequestPostDataModel.setLocationNo(locationNo);
                travelRequestPostDataModel.setTravel(travelTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("Domestic")?"D":"I");
                travelRequestPostDataModel.setRemarks(remarkTextView.getText().toString());
                travelRequestPostDataModel.setTransactionDate(CommonMethods.changeDateTOyyyyMMdd(transactionDateTextView.getText().toString()));

                if(travelRequestPostList.size()==0){
                    Toast.makeText(AddTravelRequestActivity.this,getString(R.string.enter_minimum_record),Toast.LENGTH_SHORT).show();
                }else{

                    Dialog dialog = new Dialog(AddTravelRequestActivity.this);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.logout);
                    TextView txtMsg = dialog.findViewById(R.id.dlg_msg);
                    txtMsg.setText("Sure to add travel request?");
                    Button cancel = dialog.findViewById(R.id.cancel);
                    Button yes = dialog.findViewById(R.id.yes);
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            if (CommonMethods.isOnline(AddTravelRequestActivity.this)) {
                                travelRequestAddDataPresenter.postTravelRequest(travelRequestPostDataModel);
                            } else {
                                Toast.makeText(AddTravelRequestActivity.this,getString(R.string.net),Toast.LENGTH_SHORT).show();
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

        toolbarAddDataImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(travelTypeValue.equalsIgnoreCase("Select")){
                    Toast.makeText(AddTravelRequestActivity.this,"Please select travel type",Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent=new Intent(AddTravelRequestActivity.this, AddTravelRequestFormDataActivity.class);
                intent.putExtra("travelTypeValue",travelTypeValue);
                intent.putExtra("isNavigate","FromAdd");
                startActivityForResult(intent,ADD_TRAVEL_REQUEST_FORM_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TRAVEL_REQUEST_FORM_CODE && resultCode == RESULT_OK) {
            // Now you have the single model in the previous activity
            if (data != null) {
                TravelRequestModel travelRequestModel= (TravelRequestModel) data.getSerializableExtra("inputRequest");
                if(travelRequestModel!=null){
                    travelRequestModel.setSrNo(String.valueOf(travelRequestPostList.size()+1));
                    travelRequestPostList.add(travelRequestModel);
                    travelRequestAddDataAdapter.replaceData(travelRequestPostList);
                    int lastPosition = travelRequestAddDataAdapter.getItemCount() - 1;
                    if (lastPosition >= 0) {
                        travelRequestPostDataRecyclerView.scrollToPosition(lastPosition);
                    }
                }
            }
        }

        if(requestCode==EDIT_TRAVEL_REQUEST_FORM_CODE&&resultCode==RESULT_OK){
            if (data != null) {
                int position=data.getIntExtra("position",0);
                TravelRequestModel travelRequestModel= (TravelRequestModel) data.getSerializableExtra("inputRequest");
                travelRequestPostList.set(position,travelRequestModel);
                travelRequestAddDataAdapter.replaceData(travelRequestPostList);
                int lastPosition = travelRequestAddDataAdapter.getItemCount() - 1;
                if (lastPosition >= 0) {
                    travelRequestPostDataRecyclerView.scrollToPosition(lastPosition);
                }
            }
        }

    }


    @Override
    public void onEditItem(int position) {

        if(travelTypeValue.equalsIgnoreCase("Select")){
            Toast.makeText(AddTravelRequestActivity.this,"Please select travel type",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent=new Intent(AddTravelRequestActivity.this, AddTravelRequestFormDataActivity.class);
        intent.putExtra("travelTypeValue",travelTypeValue);
        intent.putExtra("editdataModel",travelRequestPostList.get(position));
        intent.putExtra("position",position);
        intent.putExtra("isNavigate","FromEdit");
        startActivityForResult(intent,EDIT_TRAVEL_REQUEST_FORM_CODE);
    }

    @Override
    public void onDeleteItem(int position) {
        travelRequestPostList.remove(position);
        travelRequestAddDataAdapter.replaceData(travelRequestPostList);
    }


    @Override
    public void postTravelRequestDataStatus(String status) {
        Toast.makeText(this,status,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void errorMessage(String msg) {
        // Check null condition
        if (msg != null) {
            showMessageDialog(msg);
        }else {
            showMessageDialog("Something went wrong,please try again later.");
        }
    }

    private void showMessageDialog(String msg) {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.logout);
        TextView txtMsg = dialog.findViewById(R.id.dlg_msg);
        Button okay = dialog.findViewById(R.id.cancel);
        Button yes = dialog.findViewById(R.id.yes);
        yes.setVisibility(View.GONE);
        okay.setText(getString(R.string.okay));
        txtMsg.setText(msg);

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

}