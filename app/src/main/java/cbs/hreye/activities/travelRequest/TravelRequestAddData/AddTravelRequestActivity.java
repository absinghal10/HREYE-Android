package cbs.hreye.activities.travelRequest.TravelRequestAddData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cbs.hreye.R;
import cbs.hreye.activities.travelRequest.AddTravelRequestFormData.AddTravelRequestFormDataActivity;
import cbs.hreye.activities.travelRequest.TravelRequestResponseData;
import cbs.hreye.utilities.CommonMethods;

public class AddTravelRequestActivity extends AppCompatActivity implements OnTravelRequestItemClickListener,TravelRequestAddDataMvpView {


    private TextView toolBarHeaderTextView;
    private TextView transactionDateTextView;
    private ImageView backButtonImageView;
    private ImageView toolbarAddDataImageView;
    private ImageView toolbarUploadDataImageView;

    private LinearLayout toolbarIconLayout;



    private RecyclerView travelRequestPostDataRecyclerView;
    private ArrayList<TravelRequestResponseData> travelRequestPostList;

    private TravelRequestAddDataAdapter travelRequestAddDataAdapter;

    private TravelRequestAddDataPresenter travelRequestAddDataPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel_request);

        findViewIds();
        setToolBarTitle();

        transactionDateTextView.setText(CommonMethods.changeDateFromyyyyMMdd(CommonMethods.mobileCurrentDate()));

        travelRequestPostList =new ArrayList<>();
        travelRequestPostList.add(new TravelRequestResponseData("1", "ABC123", "Abhishek","TR123", "01/10/2023", "Air", "2023-11-01", "2023-11-10", "Yes", "2023-11-01", "2023-11-10", "City A", "City B", "Approved", "Great trip!", "Approved"));
        travelRequestPostList.add(new TravelRequestResponseData("2", "XYZ456","Abhishek", "TR124", "02/10/2023", "Train", "2023-12-01", "2023-12-15", "No", "", "", "City X", "City Y", "Pending", "Review needed", "Pending"));
        travelRequestPostList.add(new TravelRequestResponseData("3", "DEF789","Abhishek", "TR125", "03/11/2023", "Car", "2023-12-20", "2023-12-27", "Yes", "2023-12-20", "2023-12-27", "City C", "City D", "Rejected", "Not enough information", "Rejected"));

        travelRequestAddDataPresenter=new TravelRequestAddDataPresenter(this,AddTravelRequestActivity.this);
        travelRequestAddDataAdapter=new TravelRequestAddDataAdapter(this,travelRequestPostList,AddTravelRequestActivity.this);
        travelRequestPostDataRecyclerView.setAdapter(travelRequestAddDataAdapter);

    }

    private void findViewIds() {
        travelRequestPostDataRecyclerView=findViewById(R.id.travel_request_add_recycler_view);
        transactionDateTextView=findViewById(R.id.act_date);
        toolbarUploadDataImageView=findViewById(R.id.header_grant);
        toolbarAddDataImageView=findViewById(R.id.header_reject);
        toolbarIconLayout=findViewById(R.id.ll_grant_reject);
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
                Intent intent=new Intent(AddTravelRequestActivity.this, AddTravelRequestFormDataActivity.class);
                startActivity(intent);
            }
        });

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
}