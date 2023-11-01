package cbs.hreye.activities.travelRequest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import cbs.hreye.R;

public class TravelRequestDataAdapter extends RecyclerView.Adapter<TravelRequestDataAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private ArrayList<TravelRequestResponseData> travelRequestDataList;

    ArrayList<TravelRequestResponseData> filterList;
    TravelRequestDataAdapter.CustomFilter filter;
    public static ArrayList<TravelRequestResponseData> filters;

    public TravelRequestDataAdapter(Context context, ArrayList travelRequestDataList) {
        this.context = context;
        this.travelRequestDataList = travelRequestDataList;
        this.filterList=travelRequestDataList;
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

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new TravelRequestDataAdapter.CustomFilter();
        }
        return filter;
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


    //INNER CLASS
    public class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();
                filters = new ArrayList<>();
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getTransactionDate().toUpperCase().contains(constraint)) {
                        filters.add(filterList.get(i));
                    }
                }
                results.count = filters.size();
                results.values = filters;
            } else {
                results.count = filterList.size();
                results.values = filterList;

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            travelRequestDataList = (ArrayList<TravelRequestResponseData>) results.values;
            notifyDataSetChanged();
        }
    }

}
