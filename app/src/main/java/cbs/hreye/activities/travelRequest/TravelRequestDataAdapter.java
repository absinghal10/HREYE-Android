package cbs.hreye.activities.travelRequest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cbs.hreye.R;

public class TravelRequestDataAdapter extends RecyclerView.Adapter<TravelRequestDataAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<TravelRequestResponseData> travelRequestDataList;

    public TravelRequestDataAdapter(Context context, ArrayList travelRequestDataList) {
        this.context = context;
        this.travelRequestDataList = travelRequestDataList;
    }

    @NonNull
    @Override
    public TravelRequestDataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travel_request_row_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelRequestDataAdapter.MyViewHolder holder, int position) {
        holder.statusTextview.setText(travelRequestDataList.get(position).getStatus());
        holder.transactionDateTextview.setText(travelRequestDataList.get(position).getTransactionDate());
        holder.transactionNoTextview.setText(travelRequestDataList.get(position).getTransactionNo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent travelRequestintent=new Intent(context,TravelRequestDataDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("travelRequestData",travelRequestDataList);
                travelRequestintent.putExtras(bundle);
                context.startActivity(travelRequestintent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return travelRequestDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView transactionNoTextview;
        TextView transactionDateTextview;
        TextView statusTextview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            transactionNoTextview=itemView.findViewById(R.id.travel_request_tran_no_textview);
            transactionDateTextview=itemView.findViewById(R.id.travel_request_date_textview);
            statusTextview=itemView.findViewById(R.id.travel_request_doc_status_textview);
        }
    }
}
