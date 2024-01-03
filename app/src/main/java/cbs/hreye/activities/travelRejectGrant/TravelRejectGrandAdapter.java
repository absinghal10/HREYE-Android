package cbs.hreye.activities.travelRejectGrant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cbs.hreye.R;
import cbs.hreye.pojo.TravelGetModel.GDetail;
import cbs.hreye.utilities.CommonMethods;


public class TravelRejectGrandAdapter extends RecyclerView.Adapter<TravelRejectGrandAdapter.ViewHolder> {
    Context mContext;
    LinearLayout llGrantReject;
    //    List<TravelRejectPojo> grantRejectList;
    List<GDetail> grantRejectList;
    ArrayList<GDetail> alAddCheckedList;
    SparseBooleanArray itemStateArray = new SparseBooleanArray();
    private String etText;
    PassCheckedListDataTravel passCheckedListDataTravel;

    public TravelRejectGrandAdapter(Context mContext, LinearLayout llGrantReject, PassCheckedListDataTravel passCheckedListDataTravel) {
        this.mContext = mContext;
        this.llGrantReject = llGrantReject;
        this.passCheckedListDataTravel = passCheckedListDataTravel;
        grantRejectList = new ArrayList<>();
        alAddCheckedList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.travel_grand_reject_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GDetail model = grantRejectList.get(position);
        holder.tvAppNo.setText(model.getTranNo());
        holder.tvName.setText(model.getAssoName());
        holder.tvAssCode.setText(model.getAssoCode());
        holder.tvTransNo.setText(model.getTranNo());
        holder.tvStatus.setText(model.getStatusDesc());
        holder.tvAsPerDocName.setText(model.getDocomentryName());
        holder.tvTransDate.setText(model.getTranDate());
        holder.tvTrip.setText(model.getTrip());
        holder.tvTravelMode.setText(model.getTravelMode());
        holder.tvTravelDate.setText(model.getDate());
        holder.tvReturnDate.setText(model.getToDate());
        holder.tvHotelFromDate.setText(model.getHotelFromDate());
        holder.tvHotelToDate.setText(model.getHotelToDate());
        holder.tvTravelForm.setText(model.getTravelFrom());
        holder.tvTravelTo.setText(model.getTravelTo());
        holder.tvHotelRequired.setText(model.getHotelRequired());
        holder.tvRemark.setText(model.getRemarks());

        holder.etReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etText = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.etReason.setTag(position);
        holder.etReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.cbForRemarks.isChecked()) {
                    holder.etReason.setFocusableInTouchMode(false);
                    holder.etReason.setFocusable(false);
                } else {
                    holder.etReason.setFocusableInTouchMode(true);
                    holder.etReason.setFocusable(true);
                }
            }
        });

        holder.cbForRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = position;
                if (!holder.etReason.getText().toString().isEmpty()) {
                    GDetail grantRejectPojo = new GDetail();
                    if (!itemStateArray.get(adapterPosition, false)) {
                        holder.cbForRemarks.setChecked(true);
                        itemStateArray.put(adapterPosition, true);
                        holder.etReason.setFocusableInTouchMode(false);
                        holder.etReason.setFocusable(false);

                        grantRejectPojo.setAppNo(model.getAppNo());
                        grantRejectPojo.setLiNo(model.getLiNo());
                        grantRejectPojo.setAssoName(model.getAssoName());
                        grantRejectPojo.setAssoCode(model.getAssoCode());
                        grantRejectPojo.setTranNo(model.getTranNo());
                        grantRejectPojo.setStatusDesc(model.getStatusDesc());
                        grantRejectPojo.setDocomentryName(model.getDocomentryName());
                        grantRejectPojo.setTranDate(model.getTranDate());
                        grantRejectPojo.setTrip(model.getTrip());
                        grantRejectPojo.setTravelMode(model.getTravelMode());
                        grantRejectPojo.setDate(model.getDate());
                        grantRejectPojo.setToDate(model.getToDate());
                        grantRejectPojo.setHotelFromDate(model.getHotelFromDate());
                        grantRejectPojo.setHotelToDate(model.getHotelToDate());
                        grantRejectPojo.setHotelRequired(model.getHotelRequired());
                        grantRejectPojo.setStatus(model.getStatus());
                        grantRejectPojo.setTravelFrom(model.getTravelFrom());
                        grantRejectPojo.setTravelTo(model.getTravelTo());
                        grantRejectPojo.setRemarks(model.getRemarks());
                        grantRejectPojo.setReason(etText);
                        alAddCheckedList.add(grantRejectPojo);
                        passCheckedListDataTravel.onCheckedListTravel(alAddCheckedList);


                    } else {
                        for (int i = 0; i < alAddCheckedList.size(); i++) {
                            if (alAddCheckedList.get(i).getAppNo().equals(model.getAppNo())) {
                                alAddCheckedList.remove(i);
                            }
                        }
                        alAddCheckedList.remove(model.getAppNo());
                        alAddCheckedList.remove(model.getAssoName());
                        alAddCheckedList.remove(model.getAssoCode());
                        alAddCheckedList.remove(model.getTranNo());
                        alAddCheckedList.remove(model.getStatusDesc());
                        alAddCheckedList.remove(model.getStatus());
                        alAddCheckedList.remove(model.getDocomentryName());
                        alAddCheckedList.remove(model.getTranDate());
                        alAddCheckedList.remove(model.getTravelMode());
                        alAddCheckedList.remove(model.getTrip());
                        alAddCheckedList.remove(model.getDate());
                        alAddCheckedList.remove(model.getToDate());
                        alAddCheckedList.remove(model.getHotelFromDate());
                        alAddCheckedList.remove(model.getHotelToDate());
                        alAddCheckedList.remove(model.getTravelTo());
                        alAddCheckedList.remove(model.getTravelFrom());
                        alAddCheckedList.remove(model.getHotelRequired());
                        alAddCheckedList.remove(model.getRemarks());
                        alAddCheckedList.remove(model.getLiNo());
                        alAddCheckedList.remove(model.getReason());
                        notifyDataSetChanged();
                        holder.etReason.setText("");
                        holder.cbForRemarks.setChecked(false);
                        holder.etReason.setFocusableInTouchMode(false);
                        holder.etReason.setFocusable(false);
                        itemStateArray.put(adapterPosition, false);

                    }
                } else {
                    holder.cbForRemarks.setChecked(false);
                    itemStateArray.put(adapterPosition, false);
                    CommonMethods.setSnackBar(llGrantReject,"Please First Enter Reason");
//                    Toast.makeText(mContext, "Please First Enter Reason", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void addToArray(GDetail gDetail) {
        grantRejectList.add(gDetail);
    }


    @Override
    public int getItemViewType(int position) {
            return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return grantRejectList.size();
    }

    public GDetail getItem(int position) {
        return grantRejectList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAppNo, tvName, tvStatus, tvAssCode,
                tvAsPerDocName, tvTransNo, tvTransDate, tvTrip, tvTravelMode,
                tvTravelDate, tvReturnDate, tvHotelFromDate, tvHotelToDate,
                tvTravelForm, tvTravelTo, tvHotelRequired, tvRemark;
        public EditText etReason;
        public CheckBox cbForRemarks;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAppNo = itemView.findViewById(R.id.tv_app_no);
            tvName = itemView.findViewById(R.id.tv_name);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvAssCode = itemView.findViewById(R.id.tv_ass_code);
            tvTransNo = itemView.findViewById(R.id.tv_trans_no);
            tvTransDate = itemView.findViewById(R.id.tv_trans_date);
            tvTrip = itemView.findViewById(R.id.tv_trip);
            tvTravelMode = itemView.findViewById(R.id.tv_Travel_mode);
            etReason = itemView.findViewById(R.id.et_reason);
            cbForRemarks = itemView.findViewById(R.id.cb_remarks);
            //
            tvAsPerDocName = itemView.findViewById(R.id.tv_doc_name);
            tvTravelDate = itemView.findViewById(R.id.tv_travel_date);
            tvReturnDate = itemView.findViewById(R.id.tv_return_date);
            tvHotelFromDate = itemView.findViewById(R.id.tv_hotel_from_date);
            tvHotelToDate = itemView.findViewById(R.id.tv_hotel_to_date);
            tvTravelForm = itemView.findViewById(R.id.tv_travel_form);
            tvTravelTo = itemView.findViewById(R.id.tv_travel_to);
            tvHotelRequired = itemView.findViewById(R.id.tv_hotel_required);
            tvRemark = itemView.findViewById(R.id.tv_remark);
        }
    }

    public interface PassCheckedListDataTravel {
        void onCheckedListTravel(ArrayList<GDetail> grantRejectPojoArrayList);
    }

}
