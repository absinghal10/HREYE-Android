package cbs.hreye.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;

import cbs.hreye.pojo.AttendancelistPojo;
import cbs.hreye.R;

public class AttendanceAdapter extends BaseAdapter {

    Context context;
    ArrayList<AttendancelistPojo> result;
    LinearLayout ll_in, ll_out;
    TextView in_time_record;
    TextView in_time_loc_record;
    TextView out_time_record;
    TextView out_time_loc_record;

    public AttendanceAdapter(Context ctx, ArrayList<AttendancelistPojo> result) {
        super();
        this.context = ctx;
        this.result = result;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }



    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int pos, View convertView, final ViewGroup arg2) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.attendance_adapter_test, arg2, false);
        }

        ll_in = convertView.findViewById(R.id.in_time);
        ll_out = convertView.findViewById(R.id.out_time);


        in_time_record = convertView.findViewById(R.id.in_time_record);
        in_time_loc_record = convertView.findViewById(R.id.in_time_loc_record);
        out_time_record = convertView.findViewById(R.id.out_time_record);
        out_time_loc_record = convertView.findViewById(R.id.out_time_loc_record);

        if (!result.get(pos).getIN_TIME().equals("")) {
            StringTokenizer inDateTime = new StringTokenizer(result.get(pos).getIN_TIME(), " ");
            String inDate = inDateTime.nextToken();
            String inTime = inDateTime.nextToken();
            in_time_record.setText(inDate + " " + inTime);
        }

        if (!result.get(pos).getOUT_TIME().equals("")) {
            StringTokenizer outDateTime = new StringTokenizer(result.get(pos).getOUT_TIME(), " ");
            String outDate = outDateTime.nextToken();
            String outTime = outDateTime.nextToken();
            out_time_record.setText(outDate + " " + outTime);
        }
        in_time_loc_record.setText(result.get(pos).getIN_TIME_LOCATION());
       // out_time_loc_record.setText(result.get(pos).getOUT_TIME_LOCATION());


        if (result.get(pos).getIN_TIME().equals("")) {
            ll_in.setVisibility(View.GONE);
        } else if (result.get(pos).getOUT_TIME().equals("")) {
            ll_out.setVisibility(View.GONE);
        } else {
            ll_in.setVisibility(View.VISIBLE);
            //ll_out.setVisibility(View.VISIBLE);
        }






        return convertView;
    }
}