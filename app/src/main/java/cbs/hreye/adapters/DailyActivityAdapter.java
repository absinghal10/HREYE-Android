package cbs.hreye.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cbs.hreye.utilities.RemoveClickListener;
import cbs.hreye.pojo.DailyActivityPojo;
import cbs.hreye.R;

public class DailyActivityAdapter extends BaseAdapter {

    Context context;
    ArrayList<DailyActivityPojo> result;
    TextView lst_cust_id;
    TextView lst_act_id;
    TextView lst_act_det;
    TextView lst_hours;
    TextView lst_status;
    TextView lst_re_one;
    TextView lst_line;
    TextView txtResCode, txtslNo, txtResName, txtDetails, txtQuantity, txtUOM, txtAmount, txtBillNo, txtBillAtch, txtDate, txtTransactionNo;
    ImageView imgDelete, imgedit;
    ImageView ivRemove, ivEdit;
    RemoveClickListener removeClickListener;

    public DailyActivityAdapter(Context ctx, ArrayList<DailyActivityPojo> result, RemoveClickListener mListener) {
        super();
        this.context = ctx;
        this.result = result;
        this.removeClickListener=mListener;

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
            convertView = inflater.inflate(R.layout.activity_adapter, arg2, false);
        }

        lst_cust_id = convertView.findViewById(R.id.lst_cust_id);
        lst_act_id = convertView.findViewById(R.id.lst_act_id);
        lst_act_det = convertView.findViewById(R.id.lst_act_det);
        lst_hours = convertView.findViewById(R.id.lst_hours);
        lst_status = convertView.findViewById(R.id.lst_status);
        lst_re_one = convertView.findViewById(R.id.lst_re_one);
        lst_line = convertView.findViewById(R.id.lst_line);
        ivRemove=convertView.findViewById(R.id.iv_remove);
        ivEdit=convertView.findViewById(R.id.iv_edit);


        lst_cust_id.setText(result.get(pos).getCustom_id());
        lst_act_id.setText(result.get(pos).getActivity_id());
        lst_act_det.setText(result.get(pos).getActivity_det());
        lst_hours.setText(result.get(pos).getHours());
        lst_status.setText(result.get(pos).getStatus());
        lst_line.setText("S.No : " + (pos + 1));

        if(result.get(pos).getRemarks_one().equals("")){
            lst_re_one.setText(R.string.not_available);
        }else {
            lst_re_one.setText(result.get(pos).getRemarks_one());
        }
        ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeClickListener.onRemove(view, pos, "remove");
            }
        });

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeClickListener.onRemove(v, pos, "edit");
            }
        });
        return convertView;
    }
}
