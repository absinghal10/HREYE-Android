package cbs.hreye.activities.travelRequest.TravelRequestDetailData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cbs.hreye.R;
import cbs.hreye.activities.travelRequest.TravelRequestResponseData;

public class TravelRequestDataDetailActivity extends AppCompatActivity implements TravelRequestDataDetailMvpView {

    private TextView toolBarHeaderTextView;
    private ImageView backButtonImageView;
    private RecyclerView travelRequestDetailDataRecyclerView;
    private ArrayList<TravelRequestResponseData> travelRequest;

    private TravelRequestDataDetailAdapter travelRequestDataDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_request_data_detail);
        findViewIds();
        setToolBarTitle();

        Intent travelRequestintent = getIntent();
        Bundle receivedBundle = travelRequestintent.getExtras();
        if (receivedBundle != null) {
            travelRequest = (ArrayList<TravelRequestResponseData>) receivedBundle.getSerializable("travelRequestData");
            if(travelRequest!=null){
                travelRequestDataDetailAdapter=new TravelRequestDataDetailAdapter(this,travelRequest);
                travelRequestDetailDataRecyclerView.setAdapter(travelRequestDataDetailAdapter);
            }
        }

    }

    private void findViewIds() {
        travelRequestDetailDataRecyclerView=findViewById(R.id.travel_request_detail_recycler_view);
    }

    private void setToolBarTitle() {
        toolBarHeaderTextView = findViewById(R.id.header_text);
        toolBarHeaderTextView.setText("Authorized Travel Request");
        backButtonImageView = findViewById(R.id.header_back);
        backButtonImageView.setOnClickListener(v -> onBackPressed());
    }


    @Override
    public void getTravelRequestDetailData() {

    }

    @Override
    public void errorMessage(String msg) {
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }
}