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

public class PaySlipDeductionDetailAdapter extends RecyclerView.Adapter<PaySlipDeductionDetailAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<PaySlipDetailsPojo> alPaySlipDeductionDetail;

    public PaySlipDeductionDetailAdapter(Context context) {
        this.mContext = context;
        alPaySlipDeductionDetail =new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.pay_slip_detail_item, parent, false);
        return new MyViewHolder(itemView);
    }

    public void addToArray(PaySlipDetailsPojo paySlipDetailsPojo) {
       alPaySlipDeductionDetail.add(paySlipDetailsPojo);
    }

    public void  clear(){
        alPaySlipDeductionDetail.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return alPaySlipDeductionDetail.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public PaySlipDetailsPojo getItem(int position) {
       return alPaySlipDeductionDetail.get(position);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       final PaySlipDetailsPojo model = alPaySlipDeductionDetail.get(position);
         holder.tvDeduction.setText(model.getDeduction());
         holder.tvDeductionAmt.setText(model.getDeductionAmt());
         holder.tvGrossAmount.setVisibility(View.INVISIBLE);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView tvDeduction, tvDeductionAmt, tvGrossAmount;
        public MyViewHolder(View itemView) {
            super(itemView);
           tvDeduction =itemView.findViewById(R.id.tv_earnings);
           tvGrossAmount =itemView.findViewById(R.id.tv_gross_amount);
           tvDeductionAmt =itemView.findViewById(R.id.tv_amount);
        }
    }

}