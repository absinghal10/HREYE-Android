package cbs.hreye.adapters;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cbs.hreye.activities.ActivityList;
import cbs.hreye.utilities.RemoveClickListener;
import cbs.hreye.pojo.DailyActivityPojo;
import cbs.hreye.R;

/**
 * Created by ANDROID-PC on 26/06/19
 */

public class AuthModifyAdapter extends RecyclerView.Adapter<AuthModifyAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<DailyActivityPojo> alAuthModify;
    RemoveClickListener removeClickListener;

    public AuthModifyAdapter(Context context, RemoveClickListener mListener) {
        this.mContext = context;
        alAuthModify =new ArrayList<>();
        this.removeClickListener= mListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.activity_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    public void addToArray(DailyActivityPojo dailyActivityPojo) {
        alAuthModify.add(dailyActivityPojo);
    }

    public void clear(){
        alAuthModify.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return alAuthModify.size();
    }

    public ArrayList<DailyActivityPojo> getAlAuthModify() {
        return alAuthModify;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       final DailyActivityPojo model = alAuthModify.get(position);
       holder.tvCustomerID.setText(model.getCustom_id());
       holder.tvActivityID.setText(model.getActivity_id());
       holder.tvActivityDetails.setText(model.getActivity_det());
       holder.tvHours.setText(model.getHours());
       holder.tvSlNo.setText("S.No : "+ (position + 1));

        if(model.getRemarks_one().equals("")){
            holder.tvRemarks.setText("Not available");
        }else {
            holder.tvRemarks.setText(model.getRemarks_one());
        }



        if (model.getStatusCheck().equals("1")) {
            holder.tvStatus.setText(model.getStatus());
        }else {
            switch (model.getStatus()) {
                case "0":
                    holder.tvStatus.setText("Complete");
                    break;
                case "1":
                    holder.tvStatus.setText("Progress");
                    break;
                case "2":
                    holder.tvStatus.setText("Pending");
                    break;
                case "3":
                    holder.tvStatus.setText("Assigned");
                    break;
                case "4":
                    holder.tvStatus.setText("Not Assigned");
                    break;
                default:
                    holder.tvStatus.setText("Select");
                    break;
            }
        }

        if (ActivityList.flag.equals("A")){
          holder.ivRemove.setVisibility(View.GONE);
            holder.ivEdit.setVisibility(View.GONE);
        }else {
            holder.ivRemove.setVisibility(View.VISIBLE);
            holder.ivEdit.setVisibility(View.VISIBLE);
        }

        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               alAuthModify.remove(position);
               notifyDataSetChanged();
            }
        });

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeClickListener.onRemove(v, position, "edit");
            }
        });

    }


    public static class MyViewHolder extends RecyclerView.ViewHolder  {
         TextView tvCustomerID, tvActivityID, tvActivityDetails, tvHours, tvStatus, tvRemarks,tvSlNo;
         ImageView ivRemove, ivEdit;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvCustomerID = itemView.findViewById(R.id.lst_cust_id);
            tvActivityID = itemView.findViewById(R.id.lst_act_id);
            tvActivityDetails = itemView.findViewById(R.id.lst_act_det);
            tvHours = itemView.findViewById(R.id.lst_hours);
            tvStatus = itemView.findViewById(R.id.lst_status);
            tvRemarks = itemView.findViewById(R.id.lst_re_one);
            tvSlNo = itemView.findViewById(R.id.lst_line);
            ivRemove=itemView.findViewById(R.id.iv_remove);
            ivEdit=itemView.findViewById(R.id.iv_edit);
        }

    }

}