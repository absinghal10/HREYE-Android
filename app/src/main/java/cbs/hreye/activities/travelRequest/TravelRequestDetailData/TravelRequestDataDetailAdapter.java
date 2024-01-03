package cbs.hreye.activities.travelRequest.TravelRequestDetailData;

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
import cbs.hreye.activities.travelRequest.TravelRequestModel;

public class TravelRequestDataDetailAdapter extends RecyclerView.Adapter<TravelRequestDataDetailAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<TravelRequestModel> travelRequestDataList;
    String[] travelReasonSpinnerData = {"Select","Sales","Presales","Client Visit","Relocation","SAP Meeting","Others"};
    private  OnTravelRequestDetailItemClickListener onTravelRequestDetailItemClickListener;

    public TravelRequestDataDetailAdapter(Context context, ArrayList<TravelRequestModel> travelRequestResponseData,OnTravelRequestDetailItemClickListener onTravelRequestDetailItemClickListener) {
        this.context = context;
        this.travelRequestDataList = travelRequestResponseData;
        this.onTravelRequestDetailItemClickListener=onTravelRequestDetailItemClickListener;
    }

    @NonNull
    @Override
    public TravelRequestDataDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_travel_request_detail_layout,parent,false);
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelRequestDataDetailAdapter.MyViewHolder holder, int position) {
        TravelRequestModel data = travelRequestDataList.get(position);

        holder.srNoTextView.setText("S.No :"+(position+1));
        holder.employeeTypeTextView.setText(data.getTypeOfEmpolyee().equalsIgnoreCase("0")?"Employee":"Non-Employee");
        holder.ageTextView.setText(data.getAge());
        holder.customerNameTextView.setText(data.getCustomer());
        holder.associateCodeTextView.setText(data.getAssociateCode());
        holder.documentyTextView.setText(data.getNameAsPerGovtDoc());
        holder.nameTextView.setText(data.getAssociateName());
        holder.tripTextView.setText(data.getTrip().startsWith("S")?"Single trip":"Round trip");



        String selectedTravelReason="";

        if(data.getReasonForTravel().equalsIgnoreCase("C1")){
            selectedTravelReason=travelReasonSpinnerData[1];
        }else if(data.getReasonForTravel().equalsIgnoreCase("C2")){
            selectedTravelReason=travelReasonSpinnerData[2];
        }if(data.getReasonForTravel().equalsIgnoreCase("C3")){
            selectedTravelReason=travelReasonSpinnerData[3];
        }if(data.getReasonForTravel().equalsIgnoreCase("C4")){
            selectedTravelReason=travelReasonSpinnerData[4];
        }if(data.getReasonForTravel().equalsIgnoreCase("C5")){
            selectedTravelReason=travelReasonSpinnerData[5];
        }if(data.getReasonForTravel().equalsIgnoreCase("C6")){
            selectedTravelReason=travelReasonSpinnerData[6];
        }

        holder.reasonForTravelTextView.setText(selectedTravelReason);


        holder.tranNoTextView.setText(data.getTransactionNo());
        holder.travelModeTextView.setText(data.getTravelMode());
        holder.fromDateTextView.setText(data.getTravelData());
        holder.toDateTextView.setText(data.getReturnDate());
        holder.hotelRequiredTextView.setText(data.getHotelRequired());
        holder.hotelFromDateTextView.setText(data.getHotelfrom());
        holder.hotelToDateTextView.setText(data.getHotelto());
        holder.travelFromTextView.setText(data.getFromLocation());
        holder.travelToTextView.setText(data.getToLocation());
        holder.passportTextView.setText(data.getPassport());
        holder.validityTextView.setText(data.getValidity());

//        holder.statusTextView.setText(data.getStatus());

        if(data.getStatus().equalsIgnoreCase("F")){
            holder.deleteDataImageView.setVisibility(View.VISIBLE);
        }else{
            holder.deleteDataImageView.setVisibility(View.GONE);
        }

        holder.deleteDataImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTravelRequestDetailItemClickListener.onDeleteItem(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return travelRequestDataList.size();
    }

    public void replaceData(ArrayList<TravelRequestModel> travelRequestModelArrayList) {

        // Create a new list to avoid modifying the original list
        ArrayList<TravelRequestModel> newList = new ArrayList<>();

        if (travelRequestModelArrayList != null) {
            newList.addAll(travelRequestModelArrayList);
        }

        travelRequestDataList.clear();
        travelRequestDataList.addAll(newList);
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView srNoTextView;
        TextView documentyTextView;
        TextView employeeTypeTextView;
        TextView associateCodeTextView;
        TextView nameTextView;
        TextView ageTextView;
        TextView customerNameTextView;
        TextView tranNoTextView;
        TextView travelModeTextView;
        TextView tripTextView;
        TextView fromDateTextView;
        TextView toDateTextView;
        TextView hotelRequiredTextView;
        TextView hotelFromDateTextView;
        TextView hotelToDateTextView;
        TextView travelFromTextView;
        TextView travelToTextView;
        TextView validityTextView;
        TextView passportTextView;
        TextView statusTextView;
        TextView reasonForTravelTextView;
        ImageView editDataImageView;
        ImageView deleteDataImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            srNoTextView = itemView.findViewById(R.id.travel_request_detail_sr_no);
            associateCodeTextView = itemView.findViewById(R.id.travel_request_detail_associate_code);
            nameTextView = itemView.findViewById(R.id.travel_request_detail_name);
            employeeTypeTextView = itemView.findViewById(R.id.travel_request_detail_employee_type);
            documentyTextView = itemView.findViewById(R.id.travel_request_detail_documentry_name);
            ageTextView = itemView.findViewById(R.id.travel_request_detail_age);
            customerNameTextView = itemView.findViewById(R.id.travel_request_detail_customer_name);
            tranNoTextView = itemView.findViewById(R.id.travel_request_detail_tran_no);
            travelModeTextView = itemView.findViewById(R.id.travel_request_detail_travel_mode);
            tripTextView = itemView.findViewById(R.id.travel_request_detail_trip);
            reasonForTravelTextView = itemView.findViewById(R.id.travel_request_detail_reason_for_travel);
            fromDateTextView = itemView.findViewById(R.id.travel_request_detail_from_date);
            toDateTextView = itemView.findViewById(R.id.travel_request_detail_to_date);
            hotelRequiredTextView = itemView.findViewById(R.id.travel_request_detail_hotel_required);
            hotelFromDateTextView = itemView.findViewById(R.id.travel_request_detail_hotel_from_date);
            hotelToDateTextView = itemView.findViewById(R.id.travel_request_detail_hotel_to_date);
            travelFromTextView = itemView.findViewById(R.id.travel_request_detail_travel_from);
            travelToTextView = itemView.findViewById(R.id.travel_request_detail_travel_to);
            validityTextView = itemView.findViewById(R.id.travel_request_detail_validity);
            passportTextView = itemView.findViewById(R.id.travel_request_detail_passport);
            statusTextView = itemView.findViewById(R.id.travel_request_detail_status);
            editDataImageView = itemView.findViewById(R.id.travel_request_detail_edit_imageview);
            deleteDataImageView = itemView.findViewById(R.id.travel_request_detail_delete_imageview);
        }
    }
}



