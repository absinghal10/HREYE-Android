package cbs.hreye.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import cbs.hreye.pojo.CalenderPojo;
import cbs.hreye.R;

public class CalendarAdapter extends BaseAdapter {
    Context mContext;

    java.util.Calendar month;
    public GregorianCalendar pmonth; // calendar instance for previous month
    /**
     * calendar instance for previous month for getting complete view
     */
    public GregorianCalendar pmonthmaxset;
    private GregorianCalendar selectedDate;
    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int mnthlength;
    String itemvalue, curentDateString;
    DateFormat df;

    private ArrayList<String> lstString;
    private ArrayList<CalenderPojo> lstPojo;
    public static List<String> dayString;
    private View previousView;

    public CalendarAdapter(Context c, GregorianCalendar monthCalendar) {
        CalendarAdapter.dayString = new ArrayList<String>();
        Locale.setDefault(Locale.US);
        month = monthCalendar;
        selectedDate = (GregorianCalendar) monthCalendar.clone();
        mContext = c;
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);
        this.lstString = new ArrayList<>();
        this.lstPojo = new ArrayList<>();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        curentDateString = df.format(selectedDate.getTime());
        refreshDays();
    }

    public void setItems(ArrayList<String> lstString) {
        for (int i = 0; i != lstString.size(); i++) {
            if (lstString.get(i).length() == 1) {
                lstString.set(i, "0" + lstString.get(i));
            }
        }
        this.lstString = lstString;
    }

    public void setItems2(ArrayList<CalenderPojo> lstPojo) {
        for (int i = 0; i != lstPojo.size(); i++) {
            if (lstPojo.get(i).getDATE().length() == 1) {
                lstPojo.get(i).setDATE(lstPojo.get(i).getDATE());
            }
        }
        this.lstPojo = lstPojo;
    }

    public int getCount() {
        return dayString.size();
    }

    public Object getItem(int position) {
        return dayString.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new view for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        ImageView iw;
        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.calendar_item, null);

        }
        dayView = v.findViewById(R.id.date);
        iw = v.findViewById(R.id.date_icon);
        // separates daystring into parts.
        String[] separatedTime = dayString.get(position).split("-");
        // taking last part of date. ie; 2 from 2012-12-02
        String gridvalue = separatedTime[2].replaceFirst("^0*", "");
        // checking whether the day is in current month or not.
        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            // setting offdays to white color.
            //iw.setBackgroundColor(Color.parseColor("#0F2C61"));
            dayView.setTextColor(Color.WHITE);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            dayView.setTextColor(Color.WHITE);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else {
            // setting curent month's days in blue color.
            dayView.setTextColor(Color.BLUE);
        }

        if (dayString.get(position).equals(curentDateString)) {
            setSelected(v);
            previousView = v;
        } else {
            v.setBackgroundResource(R.color.white);
        }
        dayView.setText(gridvalue);

        // create date string for comparison
        String date = dayString.get(position);

        if (date.length() == 1) {
            date = "0" + date;
        }
        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }

        // show icon if date is not empty and it exists in the items array
      //  ImageView iw = v.findViewById(R.id.date_icon);
        if (date.length() > 0 && lstString != null && lstString.contains(date)) {
            int posi = lstString.indexOf(date);
            iw.setVisibility(View.VISIBLE);
            dayView.setTextColor(Color.WHITE);
            try {
                iw.setBackgroundColor(Color.parseColor(lstPojo.get(posi).getCOLOR_CODE()));
            } catch (IllegalArgumentException iae) {
                Toast.makeText(mContext, ""+iae, Toast.LENGTH_LONG).show();
            }
        } else {
            iw.setVisibility(View.INVISIBLE);
        }
        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            // setting offdays to TRANSPARENT  color.
           iw.setBackgroundColor(Color.TRANSPARENT);

        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            iw.setBackgroundColor(Color.TRANSPARENT);
        }
//        if (items2.contains(date)) {
//            iw2.setVisibility(View.VISIBLE);
//        } else {
//            iw2.setVisibility(View.INVISIBLE);
//        }
        return v;
    }

    public View setSelected(View view) {
        if (previousView != null) {
//          previousView.setBackgroundResource(R.drawable.list_item_background);
            previousView.setBackgroundResource(R.color.transparent_background);
        }
        previousView = view;
        view.setBackgroundResource(R.color.greenish);
        return view;
    }

    public void refreshDays() {
        // clear items
        lstString.clear();
        dayString.clear();

        Locale.setDefault(Locale.US);
        pmonth = (GregorianCalendar) month.clone();
        // month start day. ie; sun, mon, etc
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        // finding number of weeks in current month.
        maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        // allocating maximum row number for the gridview.
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP(); // previous month maximum day 31,30....
        calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
        /**
         * Calendar instance for getting a complete gridview including the three
         * month's (previous,current,next) dates.
         */
        pmonthmaxset = (GregorianCalendar) pmonth.clone();
        /**
         * setting the start date as previous month's required date.
         */
        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

        /**
         * filling calendar gridview.
         */
        for (int n = 0; n < mnthlength; n++) {
            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            dayString.add(itemvalue);
        }
    }

    private int getMaxP() {
        int maxP;
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            pmonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }
}
