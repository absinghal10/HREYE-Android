package cbs.hreye.activities.travelRequest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

import cbs.hreye.R;

public class AddTravelRequestActivity extends AppCompatActivity {

    private TravelRequestActivity travelRequestActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel_request);
    }
}