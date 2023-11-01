package cbs.hreye.activities.travelRequest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cbs.hreye.R;

public class TravelRequestAddDataAdapter extends RecyclerView.Adapter<TravelRequestAddDataAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<TravelRequestResponseData> travelRequestDataList;

    private OnTravelRequestItemClickListener onTravelRequestItemClickListener;
    public TravelRequestAddDataAdapter(Context context,  ArrayList<TravelRequestResponseData> travelRequestResponseData, OnTravelRequestItemClickListener onTravelRequestItemClickListener) {
        this.context = context;
        this.travelRequestDataList = travelRequestResponseData;
        this.onTravelRequestItemClickListener=onTravelRequestItemClickListener;
    }

    public void replaceData(ArrayList<TravelRequestResponseData> travelRequestResponseDataArrayList){
        // Create a new list to avoid modifying the original list
        ArrayList<TravelRequestResponseData> newList = new ArrayList<>();

        if (travelRequestResponseDataArrayList != null) {
            newList.addAll(travelRequestResponseDataArrayList);
        }

        travelRequestDataList.clear();
        travelRequestDataList.addAll(newList);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TravelRequestAddDataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_travel_request_add_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelRequestAddDataAdapter.MyViewHolder holder, int position) {
        TravelRequestResponseData data = travelRequestDataList.get(position);

        holder.srNoTextView.setText("S.No :"+data.getSrNo());
        holder.associateCodeTextView.setText(data.getAssociateCode());
        holder.nameTextView.setText(data.getName());
        holder.tranNoTextView.setText(data.getTransactionNo());
        holder.travelModeTextView.setText(data.getTravelMode());
        holder.fromDateTextView.setText(data.getFromDate());
        holder.toDateTextView.setText(data.getToDate());
        holder.hotelRequiredTextView.setText(data.getHotelRequired());
        holder.hotelFromDateTextView.setText(data.getHotelFromDate());
        holder.hotelToDateTextView.setText(data.getHotelToDate());
        holder.travelFromTextView.setText(data.getTravelFrom());
        holder.travelToTextView.setText(data.getTravelTo());
        holder.grRemarksTextView.setText(data.getGrantOrRejectRemarks());
        holder.deskRemarksTextView.setText(data.getDeskRemarks());
        holder.statusTextView.setText(data.getStatus());


        holder.editDataImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTravelRequestItemClickListener.onEditItem(position);
            }
        });

        holder.deleteDataImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTravelRequestItemClickListener.onDeleteItem(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return travelRequestDataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView srNoTextView;
        TextView associateCodeTextView;
        TextView nameTextView;
        TextView tranNoTextView;
        TextView travelModeTextView;
        TextView fromDateTextView;
        TextView toDateTextView;
        TextView hotelRequiredTextView;
        TextView hotelFromDateTextView;
        TextView hotelToDateTextView;
        TextView travelFromTextView;
        TextView travelToTextView;
        TextView grRemarksTextView;
        TextView deskRemarksTextView;
        TextView statusTextView;
        ImageView editDataImageView;
        ImageView deleteDataImageView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            srNoTextView = itemView.findViewById(R.id.travel_request_detail_sr_no);
            associateCodeTextView = itemView.findViewById(R.id.travel_request_detail_associate_code);
            nameTextView = itemView.findViewById(R.id.travel_request_detail_name);
            tranNoTextView = itemView.findViewById(R.id.travel_request_detail_tran_no);
            travelModeTextView = itemView.findViewById(R.id.travel_request_detail_travel_mode);
            fromDateTextView = itemView.findViewById(R.id.travel_request_detail_from_date);
            toDateTextView = itemView.findViewById(R.id.travel_request_detail_to_date);
            hotelRequiredTextView = itemView.findViewById(R.id.travel_request_detail_hotel_required);
            hotelFromDateTextView = itemView.findViewById(R.id.travel_request_detail_hotel_from_date);
            hotelToDateTextView = itemView.findViewById(R.id.travel_request_detail_hotel_to_date);
            travelFromTextView = itemView.findViewById(R.id.travel_request_detail_travel_from);
            travelToTextView = itemView.findViewById(R.id.travel_request_detail_travel_to);
            grRemarksTextView = itemView.findViewById(R.id.travel_request_detail_gr_remarks);
            deskRemarksTextView = itemView.findViewById(R.id.travel_request_detail_desk_remarks);
            statusTextView = itemView.findViewById(R.id.travel_request_detail_status);
            editDataImageView = itemView.findViewById(R.id.travel_request_detail_edit_imageview);
            deleteDataImageView = itemView.findViewById(R.id.travel_request_detail_delete_imageview);
        }
    }
}