package cbs.hreye.activities.travelRequest.TravelRequestAddData;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import cbs.hreye.activities.travelRequest.TravelRequestModel;
import cbs.hreye.activities.travelRequest.TravelRequestResponseData;
import cbs.hreye.utilities.CommonMethods;

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
        openDatePickerDialog(transactionDateTextView);

        travelRequestPostList =new ArrayList<>();
        travelRequestPostList.add(new TravelRequestModel("1", "ABC123", "Emp","TR123", "Abhi","Abhi","30","Maa Infra","01/10/2023", "Air", "2023-11-01", "2023-11-10", "Yes", "2023-11-01", "2023-11-10", "City A", "City B", "Approved", "yes", "Approved"));
        travelRequestPostList.add(new TravelRequestModel("2", "ABC123", "Non-Emp","TR123", "Abhi","Abhi","30","Maa Infra","01/10/2023", "Air", "2023-11-01", "2023-11-10", "Yes", "2023-11-01", "2023-11-10", "City A", "City B", "Approved", "No", "Approved"));
        travelRequestPostList.add(new TravelRequestModel("3", "ABC1234", "Emp","TR123", "Abhi","Abhi","30","Maa Infra","01/10/2023", "Air", "2023-11-01", "2023-11-10", "Yes", "2023-11-01", "2023-11-10", "City A", "City B", "Approved", "yes", "Approved"));

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
                Toast.makeText(AddTravelRequestActivity.this,"Data",Toast.LENGTH_SHORT).show();
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
                    travelRequestPostList.add(travelRequestModel);
                    travelRequestAddDataAdapter.replaceData(travelRequestPostList);
                    int lastPosition = travelRequestAddDataAdapter.getItemCount() - 1;
                    if (lastPosition >= 0) {
                        travelRequestPostDataRecyclerView.scrollToPosition(lastPosition);
                    }
                }

            }
        }
    }


    @Override
    public void onEditItem(int position) {
        Toast.makeText(this,"Edit"+position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteItem(int position) {
        travelRequestPostList.remove(position);

        travelRequestAddDataAdapter.replaceData(travelRequestPostList);
        Toast.makeText(this,"clicked"+position,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void postTravelRequestDataStatus(String status) {
        if(status.equalsIgnoreCase("Success")){
            Toast.makeText(this,"Data insert successfully",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void errorMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }



    private void openDatePickerDialog(TextView textView) {
        textView.setFocusable(false);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker = new DatePickerDialog(AddTravelRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar c = Calendar.getInstance();
                        c.set(selectedyear, selectedmonth, selectedday);
                        trasactionDate = CommonMethods.pad(selectedday) + "/" + CommonMethods.pad(selectedmonth + 1) +
                                "/" + CommonMethods.pad(selectedyear);
                        textView.setText(trasactionDate);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setCalendarViewShown(false);
                mDatePicker.show();
            }
        });
    }
}