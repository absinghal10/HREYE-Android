package cbs.hreye.activities.travelRequest.TravelRequestAddData;

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
import cbs.hreye.activities.travelRequest.TravelRequestResponseData;

public class TravelRequestAddDataAdapter extends RecyclerView.Adapter<TravelRequestAddDataAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<TravelRequestModel> travelRequestDataList;

    String[] travelReasonSpinnerData = {"Select","Sales","Presales","Client Visit","Relocation","SAP Meeting","Others"};


    private OnTravelRequestItemClickListener onTravelRequestItemClickListener;
    public TravelRequestAddDataAdapter(Context context,  ArrayList<TravelRequestModel> travelRequestResponseData, OnTravelRequestItemClickListener onTravelRequestItemClickListener) {
        this.context = context;
        this.travelRequestDataList = travelRequestResponseData;
        this.onTravelRequestItemClickListener=onTravelRequestItemClickListener;
    }

    public void replaceData(ArrayList<TravelRequestModel> travelRequestResponseDataArrayList){
        // Create a new list to avoid modifying the original list
        ArrayList<TravelRequestModel> newList = new ArrayList<>();

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
        TravelRequestModel data = travelRequestDataList.get(position);

        if(data.getTypeOfEmpolyee().equalsIgnoreCase("0")){
            holder.typeOfEmpolyee.setText("Employee");
        }else{
            holder.typeOfEmpolyee.setText("Non-Employee");
        }

        holder.srNoTextView.setText("S.No :"+data.getSrNo());
        holder.associateCodeTextView.setText(data.getAssociateCode());
        holder.associateNameTextView.setText(data.getAssociateName());
        holder.nameTextView.setText(data.getNameAsPerGovtDoc());
        holder.ageTextView.setText(data.getAge());
        holder.customerTextView.setText(data.getCustomer());

        holder.tripTextView.setText(data.getTrip().equalsIgnoreCase("S")?"Single trip":"Round trip");

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


        holder.travelReasonTextView.setText(selectedTravelReason);
        holder.travelModeTextView.setText(data.getTravelMode());
        holder.fromDateTextView.setText(data.getTravelData());
        holder.toDateTextView.setText(data.getReturnDate());
        holder.hotelRequiredTextView.setText(data.getHotelRequired());
        holder.hotelFromDateTextView.setText(data.getHotelfrom());
        holder.hotelToDateTextView.setText(data.getHotelto());
        holder.travelFromLocationTextView.setText(data.getFromLocation());
        holder.travelToLocationTextView.setText(data.getToLocation());
        holder.passportTextView.setText(data.getPassport());
        holder.validityTextView.setText(data.getValidity());

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
        TextView typeOfEmpolyee;
        TextView associateCodeTextView;
        TextView associateNameTextView;
        TextView nameTextView;
        TextView travelReasonTextView;
        TextView ageTextView;
        TextView customerTextView;
        TextView tripTextView;
        TextView travelModeTextView;
        TextView fromDateTextView;
        TextView toDateTextView;
        TextView hotelRequiredTextView;
        TextView hotelFromDateTextView;
        TextView hotelToDateTextView;
        TextView travelFromLocationTextView;
        TextView travelToLocationTextView;
        TextView passportTextView;
        TextView validityTextView;
        ImageView editDataImageView;
        ImageView deleteDataImageView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            srNoTextView = itemView.findViewById(R.id.travel_request_detail_sr_no);
            typeOfEmpolyee = itemView.findViewById(R.id.travel_request_add_type_of_employee_textview);
            associateCodeTextView = itemView.findViewById(R.id.travel_request_detail_associate_code);
            associateNameTextView = itemView.findViewById(R.id.travel_request_add_associate_name_textview);
            nameTextView = itemView.findViewById(R.id.travel_request_detail_name);
            ageTextView = itemView.findViewById(R.id.travel_request_add_age_textview);
            customerTextView = itemView.findViewById(R.id.travel_request_add_customer_textview);
            tripTextView = itemView.findViewById(R.id.travel_request_add_trip_textview);
            travelReasonTextView = itemView.findViewById(R.id.travel_request_add_travel_reason);
            travelModeTextView = itemView.findViewById(R.id.travel_request_detail_travel_mode);
            fromDateTextView = itemView.findViewById(R.id.travel_request_detail_from_date);
            toDateTextView = itemView.findViewById(R.id.travel_request_detail_to_date);
            hotelRequiredTextView = itemView.findViewById(R.id.travel_request_detail_hotel_required);
            hotelFromDateTextView = itemView.findViewById(R.id.travel_request_detail_hotel_from_date);
            hotelToDateTextView = itemView.findViewById(R.id.travel_request_detail_hotel_to_date);
            travelFromLocationTextView = itemView.findViewById(R.id.travel_request_detail_travel_from_location);
            travelToLocationTextView = itemView.findViewById(R.id.travel_request_detail_travel_to_location);
            passportTextView = itemView.findViewById(R.id.travel_request_detail_add_passport_textview);
            validityTextView = itemView.findViewById(R.id.travel_request_detail_add_validity);
            editDataImageView = itemView.findViewById(R.id.travel_request_detail_edit_imageview);
            deleteDataImageView = itemView.findViewById(R.id.travel_request_detail_delete_imageview);
        }
    }
}