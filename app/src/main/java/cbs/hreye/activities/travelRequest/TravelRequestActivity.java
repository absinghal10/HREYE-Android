package cbs.hreye.activities.travelRequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cbs.hreye.R;

public class TravelRequestActivity extends AppCompatActivity {

    private TextView toolBarHeaderTextView;
    private ImageView backButtonImageView;
    private ImageView addNewTravelRequestFloatingImageView;
    private ArrayList<TravelRequestResponseData> travelrequestResponseDataList;
    private  TravelRequestDataAdapter travelRequestDataAdapter;
    private RecyclerView travelRequestDataRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_request);
        findViewIds();
        setToolBarTitle();

        addNewTravelRequestFloatingImageView.setOnClickListener(v -> {
            startActivity(new Intent(this, AddTravelRequestActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        });

        travelrequestResponseDataList =new ArrayList<>();
        // Add dummy data to the ArrayList
        travelrequestResponseDataList.add(new TravelRequestResponseData("1", "ABC123", "Abhishek","TR123", "2023-10-01", "Air", "2023-11-01", "2023-11-10", "Yes", "2023-11-01", "2023-11-10", "City A", "City B", "Approved", "Great trip!", "Approved"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("2", "XYZ456","Abhishek", "TR124", "2023-10-02", "Train", "2023-12-01", "2023-12-15", "No", "", "", "City X", "City Y", "Pending", "Review needed", "Pending"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("3", "DEF789","Abhishek", "TR125", "2023-10-03", "Car", "2023-12-20", "2023-12-27", "Yes", "2023-12-20", "2023-12-27", "City C", "City D", "Rejected", "Not enough information", "Rejected"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("4", "GHI012", "Abhishek","TR126", "2023-10-04", "Bus", "2023-11-15", "2023-11-20", "No", "", "", "City P", "City Q", "Approved", "Approved without remarks", "Approved"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("5", "JKL345","Abhishek", "TR127", "2023-10-05", "Air", "2023-11-05", "2023-11-12", "Yes", "2023-11-05", "2023-11-12", "City M", "City N", "Pending", "Pending review", "Pending"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("6", "MNO678", "Abhishek","TR128", "2023-10-06", "Train", "2023-12-05", "2023-12-12", "No", "", "", "City E", "City F", "Pending", "Waiting for approval", "Pending"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("7", "PQR901","Abhishek", "TR129", "2023-10-07", "Car", "2023-11-10", "2023-11-15", "No", "", "", "City G", "City H", "Approved", "Approved with remarks", "Approved"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("8", "STU234","Abhishek", "TR130", "2023-10-08", "Air", "2023-11-20", "2023-11-27", "Yes", "2023-11-20", "2023-11-27", "City I", "City J", "Rejected", "Insufficient funds", "Rejected"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("9", "VWX567", "Abhishek","TR131", "2023-10-09", "Bus", "2023-12-01", "2023-12-10", "No", "", "", "City K", "City L", "Pending", "Pending review", "Pending"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("10", "YZA890", "Abhishek","TR132", "2023-10-10", "Train", "2023-11-25", "2023-12-02", "Yes", "2023-11-25", "2023-12-02", "City O", "City P", "Approved", "Approved without remarks", "Approved"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("11", "BCD123","Abhishek", "TR133", "2023-10-11", "Air", "2023-12-05", "2023-12-15", "Yes", "2023-12-05", "2023-12-15", "City R", "City S", "Pending", "Pending review", "Pending"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("12", "EFG456","Abhishek", "TR134", "2023-10-12", "Bus", "2023-11-15", "2023-11-20", "No", "", "", "City U", "City V", "Rejected", "Rejected due to late submission", "Rejected"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("13", "HIJ789", "Abhishek","TR135", "2023-10-13", "Car", "2023-12-20", "2023-12-27", "Yes", "2023-12-20", "2023-12-27", "City X", "City Y", "Approved", "Approved with remarks", "Approved"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("14", "KLM012","Abhishek", "TR136", "2023-10-14", "Train", "2023-11-30", "2023-12-05", "Yes", "2023-11-30", "2023-12-05", "City Z", "City AA", "Pending", "Pending review", "Pending"));
        travelrequestResponseDataList.add(new TravelRequestResponseData("15", "NOP345", "Abhishek","TR137", "2023-10-15", "Bus", "2023-11-10", "2023-11-15", "No", "", "", "City AB", "City AC", "Approved", "Approved without remarks", "Approved"));

        travelRequestDataAdapter=new TravelRequestDataAdapter(TravelRequestActivity.this,travelrequestResponseDataList);
        travelRequestDataRecyclerView.setAdapter(travelRequestDataAdapter);

    }

    private void findViewIds() {
        addNewTravelRequestFloatingImageView=findViewById(R.id.add_travel_request_data_floating_button);
        travelRequestDataRecyclerView=findViewById(R.id.travel_request_recycler_view);
    }

    private void setToolBarTitle() {
        toolBarHeaderTextView = findViewById(R.id.header_text);
        toolBarHeaderTextView.setText("Travel Request");
        backButtonImageView = findViewById(R.id.header_back);
        backButtonImageView.setOnClickListener(v -> onBackPressed());
    }





}