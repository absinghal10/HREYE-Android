package cbs.hreye.activities;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.Toast;

import cbs.hreye.R;

public class CalendarViewLeaveTest extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_leave_test1);
        mContext = CalendarViewLeaveTest.this;
        getID();
    }

    private void getID() {
        CalendarView calendarView = findViewById(R.id.calenderView);
       calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
           @Override
           public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
               String date = (i+1)+"/"+i2+"/"+i1;
               Toast.makeText(mContext, date, Toast.LENGTH_LONG).show();

           }
       });


    }

}
