package cbs.hreye.activities.travelRequest.travelRequestData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import cbs.hreye.R;
import cbs.hreye.activities.travelRequest.TravelRequestAddData.AddTravelRequestActivity;
import cbs.hreye.activities.travelRequest.TravelRequestResponseData;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.CustomTextView;

public class TravelRequestActivity extends AppCompatActivity implements TravelRequestDataMvpView{

    private TextView toolBarHeaderTextView;
    private ImageView backButtonImageView;
    private ImageView addNewTravelRequestFloatingImageView;
    private ArrayList<TravelRequestResponseData> travelrequestResponseDataList;
    private TravelRequestDataAdapter travelRequestDataAdapter;
    private RecyclerView travelRequestDataRecyclerView;
    private EditText searchEditText;
    private CustomTextView clearTextView;

    private SearchView searchView;

    public int mYear, mMonth, mDay;

    String searchDate="";

    private int filterLength = 0;


    private  TravelRequestDataPresenter travelRequestDataPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_request);
        findViewIds();

        travelRequestDataPresenter=new TravelRequestDataPresenter(this,TravelRequestActivity.this);
        setToolBarTitle();
        openDatePickerDialog();
        clearSearchView();

        travelRequestDataPresenter.fetchTravelRequestData();


        addNewTravelRequestFloatingImageView.setOnClickListener(v -> {
            startActivity(new Intent(this, AddTravelRequestActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        });

        travelrequestResponseDataList =new ArrayList<>();
        // Add dummy data to the ArrayList
        travelrequestResponseDataList.add(new TravelRequestResponseData("1", "ABC123", "Abhishek","TR123", "01/10/2023", "Air", "2023-11-01", "2023-11-10", "Yes", "2023-11-01", "2023-11-10", "City A", "City B", "Approved", "Great trip!", "Approved"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("2", "XYZ456","Abhishek", "TR124", "02/10/2023", "Train", "2023-12-01", "2023-12-15", "No", "", "", "City X", "City Y", "Pending", "Review needed", "Pending"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("3", "DEF789","Abhishek", "TR125", "03/11/2023", "Car", "2023-12-20", "2023-12-27", "Yes", "2023-12-20", "2023-12-27", "City C", "City D", "Rejected", "Not enough information", "Rejected"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("4", "GHI012", "Abhishek","TR126", "01/12/2023", "Bus", "2023-11-15", "2023-11-20", "No", "", "", "City P", "City Q", "Approved", "Approved without remarks", "Approved"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("5", "JKL345","Abhishek", "TR127", "14/08/2023", "Air", "2023-11-05", "2023-11-12", "Yes", "2023-11-05", "2023-11-12", "City M", "City N", "Pending", "Pending review", "Pending"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("6", "MNO678", "Abhishek","TR128", "03/10/2023", "Train", "2023-12-05", "2023-12-12", "No", "", "", "City E", "City F", "Pending", "Waiting for approval", "Pending"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("7", "PQR901","Abhishek", "TR129", "01/10/2023", "Car", "2023-11-10", "2023-11-15", "No", "", "", "City G", "City H", "Approved", "Approved with remarks", "Approved"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("8", "STU234","Abhishek", "TR130", "05/10/2023", "Air", "2023-11-20", "2023-11-27", "Yes", "2023-11-20", "2023-11-27", "City I", "City J", "Rejected", "Insufficient funds", "Rejected"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("9", "VWX567", "Abhishek","TR131", "01/10/2023", "Bus", "2023-12-01", "2023-12-10", "No", "", "", "City K", "City L", "Pending", "Pending review", "Pending"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("10", "YZA890", "Abhishek","TR132", "20/10/2023", "Train", "2023-11-25", "2023-12-02", "Yes", "2023-11-25", "2023-12-02", "City O", "City P", "Approved", "Approved without remarks", "Approved"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("11", "BCD123","Abhishek", "TR133", "01/12/2023", "Air", "2023-12-05", "2023-12-15", "Yes", "2023-12-05", "2023-12-15", "City R", "City S", "Pending", "Pending review", "Pending"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("12", "EFG456","Abhishek", "TR134", "12/04/2023", "Bus", "2023-11-15", "2023-11-20", "No", "", "", "City U", "City V", "Rejected", "Rejected due to late submission", "Rejected"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("13", "HIJ789", "Abhishek","TR135", "15/10/2023", "Car", "2023-12-20", "2023-12-27", "Yes", "2023-12-20", "2023-12-27", "City X", "City Y", "Approved", "Approved with remarks", "Approved"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("14", "KLM012","Abhishek", "TR136", "28/10/2023", "Train", "2023-11-30", "2023-12-05", "Yes", "2023-11-30", "2023-12-05", "City Z", "City AA", "Pending", "Pending review", "Pending"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("15", "NOP345", "Abhishek","TR137", "21/09/2023", "Bus", "2023-11-10", "2023-11-15", "No", "2023-11-30", "2023-11-30", "City AB", "City AC", "Approved", "Approved without remarks", "Approved"));

        travelRequestDataAdapter=new TravelRequestDataAdapter(TravelRequestActivity.this,travelrequestResponseDataList);
        travelRequestDataRecyclerView.setAdapter(travelRequestDataAdapter);

    }

    private void clearSearchView() {
       clearTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchEditText.getText().toString().length() == 0) {
                    CommonMethods.setSnackBar(searchEditText, "Date is not entered");
                } else {
                    searchEditText.setText("");
                    travelRequestDataAdapter = new TravelRequestDataAdapter(TravelRequestActivity.this, travelrequestResponseDataList);
                    travelRequestDataRecyclerView.setAdapter(travelRequestDataAdapter);
                    travelRequestDataAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void openDatePickerDialog() {

        searchEditText.setFocusable(false);
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (travelrequestResponseDataList.size() == 0) {
                    CommonMethods.setSnackBar(searchEditText, "No item available for search");
                } else {
                    Calendar mcurrentDate = Calendar.getInstance();
                    mYear = mcurrentDate.get(Calendar.YEAR);
                    mMonth = mcurrentDate.get(Calendar.MONTH);
                    mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog mDatePicker = new DatePickerDialog(TravelRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            Calendar c = Calendar.getInstance();
                            c.set(selectedyear, selectedmonth, selectedday);
                            searchDate = CommonMethods.pad(selectedday) + "/" + CommonMethods.pad(selectedmonth + 1) +
                                    "/" + CommonMethods.pad(selectedyear);
                            searchEditText.setText(searchDate);
                            searchView.setQuery(searchDate,true);
                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.getDatePicker().setCalendarViewShown(false);
                    mDatePicker.show();
                }
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String arg0) {
                travelRequestDataAdapter.getFilter().filter(searchDate, new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int i) {
                        if (i == 0) {
                            CommonMethods.setSnackBar(searchEditText, "No activity available of this date.");
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

    }

    private void findViewIds() {
        addNewTravelRequestFloatingImageView=findViewById(R.id.add_travel_request_data_floating_button);
        travelRequestDataRecyclerView=findViewById(R.id.travel_request_recycler_view);
        searchEditText=findViewById(R.id.et_search);
        clearTextView=findViewById(R.id.txt_clear);
        searchView=findViewById(R.id.search_view);

    }

    private void setToolBarTitle() {
        toolBarHeaderTextView = findViewById(R.id.header_text);
        toolBarHeaderTextView.setText("Travel Request");
        backButtonImageView = findViewById(R.id.header_back);
        backButtonImageView.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void getTravelRequestData() {

    }

    @Override
    public void errorMessage(String msg) {
        Toast.makeText(this,msg.toString(),Toast.LENGTH_SHORT).show();
    }
}