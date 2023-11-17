package cbs.hreye.activities.travelRequest.travelRequestData;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cbs.hreye.R;
import cbs.hreye.activities.travelRequest.TravelRequestAddData.AddTravelRequestActivity;
import cbs.hreye.activities.travelRequest.TravelRequestResponseData;
import cbs.hreye.pojo.TravelRequestGetModel;
import cbs.hreye.pojo.TravelRequestGetResponseModel;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.CustomTextView;

public class TravelRequestActivity extends AppCompatActivity implements TravelRequestDataMvpView{

    private TextView toolBarHeaderTextView;
    private ImageView backButtonImageView;
    private ImageView addNewTravelRequestFloatingImageView;
    private List<TravelRequestGetModel> travelrequestResponseDataList;
    private TravelRequestDataAdapter travelRequestDataAdapter;
    private RecyclerView travelRequestDataRecyclerView;
    private EditText searchEditText;
    private CustomTextView clearTextView;

    private SearchView searchView;

    public int mYear, mMonth, mDay;

    String searchDate="";

    private int filterLength = 0;

    private int REQUEST_CODE_ADD_TRAVEL_REQUEST=1295;


    private  TravelRequestDataPresenter travelRequestDataPresenter;

    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_request);
        findViewIds();

        travelRequestDataPresenter=new TravelRequestDataPresenter(this,TravelRequestActivity.this);
        setToolBarTitle();
        openDatePickerDialog();
        clearSearchView();

        shimmerFrameLayout.setVisibility(View.VISIBLE);
        travelRequestDataPresenter.fetchTravelRequestData();


        addNewTravelRequestFloatingImageView.setOnClickListener(v -> {
            Intent intent=new Intent(this,AddTravelRequestActivity.class);
            startActivityForResult(intent,REQUEST_CODE_ADD_TRAVEL_REQUEST);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        });


    }

    private void clearSearchView() {
       clearTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchEditText.getText().toString().length() == 0) {
                    CommonMethods.setSnackBar(searchEditText, "Date is not entered");
                } else {
                    searchEditText.setText("");
                    travelRequestDataAdapter = new TravelRequestDataAdapter(TravelRequestActivity.this,(ArrayList)travelrequestResponseDataList);
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
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);

    }

    private void setToolBarTitle() {
        toolBarHeaderTextView = findViewById(R.id.header_text);
        toolBarHeaderTextView.setText("Travel Request");
        backButtonImageView = findViewById(R.id.header_back);
        backButtonImageView.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void getTravelRequestData(List<TravelRequestGetModel> travelRequestGetResponseModelList) {

        shimmerFrameLayout.setVisibility(View.GONE);

        if(travelRequestGetResponseModelList!=null) {
            travelrequestResponseDataList = travelRequestGetResponseModelList;
            // Create a LinearLayoutManager
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            // Set reverseLayout to true to reverse the layout
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
            travelRequestDataRecyclerView.setLayoutManager(layoutManager);
            travelRequestDataAdapter = new TravelRequestDataAdapter(TravelRequestActivity.this, (ArrayList) travelrequestResponseDataList);
            travelRequestDataRecyclerView.setAdapter(travelRequestDataAdapter);
        }

    }

    @Override
    public void errorMessage(String msg) {
        shimmerFrameLayout.setVisibility(View.GONE);
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_ADD_TRAVEL_REQUEST){
            travelRequestDataPresenter.fetchTravelRequestData();
        }

    }
}