package cbs.hreye.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cbs.hreye.pojo.HolidayPojo;
import cbs.hreye.R;

public class HolidayAdapter extends BaseAdapter {

    Context context;
    ArrayList<HolidayPojo> result;
    TextView date;
    TextView day;
    TextView occ;

    public HolidayAdapter(Context ctx, ArrayList<HolidayPojo> result) {
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

    @Override
    public View getView(final int pos, View convertView, final ViewGroup arg2) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.holiday_adapter, arg2, false);
        }

        date = convertView.findViewById(R.id.h_date);
        day = convertView.findViewById(R.id.h_day);
        occ = convertView.findViewById(R.id.h_occ);

        occ.setText(result.get(pos).getHoliday_Occasion());
        date.setText(result.get(pos).getHoliday_Date());
        day.setText(result.get(pos).getHoliday_Day());

        return convertView;
    }
}
