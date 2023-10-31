package cbs.hreye.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cbs.hreye.pojo.PaySlipDetailsPojo;
import cbs.hreye.R;

/**
 * Created by ANDROID-PC on 22/02/19
 */

public class PaySlipAmountDetailAdapter extends RecyclerView.Adapter<PaySlipAmountDetailAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<PaySlipDetailsPojo> alPaySlipAmountDetail;

    public PaySlipAmountDetailAdapter(Context context) {
        this.mContext = context;
        alPaySlipAmountDetail =new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.pay_slip_detail_item, parent, false);
        return new MyViewHolder(itemView);
    }

    public void addToArray(PaySlipDetailsPojo grantRejectPojo) {
          alPaySlipAmountDetail.add(grantRejectPojo);
    }

    public void clear(){
        alPaySlipAmountDetail.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return alPaySlipAmountDetail.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public PaySlipDetailsPojo getItem(int position) {
       return alPaySlipAmountDetail.get(position);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       final PaySlipDetailsPojo model = alPaySlipAmountDetail.get(position);
        holder.tvEarnings.setText(model.getEarning());
        holder.tvGrossAmount.setText(model.getGrossAmt());
        holder.tvAmount.setText(model.getAmount());
    }


    public class MyViewHolder extends RecyclerView.ViewHolder  {
         TextView tvEarnings, tvGrossAmount, tvAmount;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvEarnings = itemView.findViewById(R.id.tv_earnings);
            tvGrossAmount = itemView.findViewById(R.id.tv_gross_amount);
            tvAmount = itemView.findViewById(R.id.tv_amount);
        }

    }

}