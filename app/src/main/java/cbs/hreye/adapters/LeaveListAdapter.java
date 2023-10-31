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
import cbs.hreye.pojo.LeaveListPojo;
import cbs.hreye.R;


public class LeaveListAdapter extends BaseAdapter {

    Context context;
    ArrayList<LeaveListPojo> result;

    public LeaveListAdapter(Context ctx, ArrayList<LeaveListPojo> result) {
        // TODO Auto-generated constructor stub
        this.context = ctx;
        this.result = result;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        return result.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return result.indexOf(getItem(pos));
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lvlist_adapter, null);
        }
        TextView lst_Type = convertView.findViewById(R.id.lv_type);
        TextView lst_dur = convertView.findViewById(R.id.lv_dur);
        TextView lst_app = convertView.findViewById(R.id.lv_NO);
        TextView lst_status = convertView.findViewById(R.id.lv_status);
        LinearLayout ll_leave = convertView.findViewById(R.id.ll_leave);

        lst_dur.setText(result.get(pos).getFROM_DATE() + " - " + result.get(pos).getTO_DATE());
        lst_app.setText(result.get(pos).getAppl_No());
        lst_Type.setText(result.get(pos).getLeave_Decs());

        if (result.get(pos).getStatus().trim().equals("4")) {
            lst_status.setText("Rejected");
            ll_leave.setVisibility(View.VISIBLE);
        }else if (result.get(pos).getStatus().trim().equals("1")) {
            lst_status.setText("Cancelled");
            ll_leave.setVisibility(View.VISIBLE);
        } else if (result.get(pos).getStatus().trim().equals("2")) {
            lst_status.setText("Submitted");
            ll_leave.setVisibility(View.VISIBLE);
        } else if (result.get(pos).getStatus().trim().equals("3")) {
            lst_status.setText("Granted");
            ll_leave.setVisibility(View.VISIBLE);
        }else if (result.get(pos).getStatus().trim().equals("0")) {
            lst_status.setText("Fresh");
            ll_leave.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}

