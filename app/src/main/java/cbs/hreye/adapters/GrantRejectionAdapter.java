package cbs.hreye.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

import cbs.hreye.utilities.PassCheckedListData;
import cbs.hreye.pojo.GrantRejectPojo;
import cbs.hreye.R;

/**
 * Created by ANDROID-PC on 01/02/19
 */

public class GrantRejectionAdapter extends RecyclerView.Adapter<GrantRejectionAdapter.MyViewHolder> {
    List<GrantRejectPojo> grantRejectList;
    Context mContext;
    boolean isSelectedAll;
    SparseBooleanArray itemStateArray = new SparseBooleanArray();
    LinearLayout llGrantReject;
    private String etText;
    ArrayList<GrantRejectPojo> alAddCheckedList;
    ArrayList<GrantRejectPojo> alEtText;
    PassCheckedListData passCheckedListData;

    public GrantRejectionAdapter(Context context, LinearLayout llGrantReject, PassCheckedListData passCheckedListData) {
        this.mContext = context;
        grantRejectList = new ArrayList<>();
        alAddCheckedList =new ArrayList<>();
        alEtText =new ArrayList<>();
        this.passCheckedListData = passCheckedListData;
        this.llGrantReject = llGrantReject;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.grant_reject_item, parent, false);
        return new MyViewHolder(itemView);
    }

    public void addToArray(GrantRejectPojo grantRejectPojo) {
        grantRejectList.add(grantRejectPojo);
    }

    public void selectAll(boolean isSelectedAll) {
        this.isSelectedAll = isSelectedAll;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return grantRejectList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public GrantRejectPojo getItem(int position) {
        return grantRejectList.get(position);
    }

    public void removeAt(int position) {
        grantRejectList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final GrantRejectPojo model = grantRejectList.get(position);
        holder.tvAppNo.setText(model.getAppNo());
        holder.tvName.setText(model.getName());
        holder.tvStatus.setText(model.getStatusDesc().substring(0, 4));
        holder.tvEmpCode.setText(model.getEmpCode());
        holder.tvLeaveType.setText(model.getLeaveType());
        holder.tvApplyDate.setText(model.getApplyDate());
        holder.tvDuration.setText(model.getDurationDate());
        holder.tvTotalDays.setText(model.getTotalDays());
        holder.tvReason.setText(model.getReason());

        holder.etRemarks.addTextChangedListener(new TextWatcher() {
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
        holder.etRemarks.setTag(position);

      //this code is use for all items checked.
      /*  if (isSelectedAll == false) {
            if (!itemStateArray.get(position, false)) {
                holder.cbForRemarks.setChecked(false);
              //  holder.etRemarks.setFocusableInTouchMode(false);
              //  holder.etRemarks.setFocusable(false);
            } else {
                holder.cbForRemarks.setChecked(true);
              //  holder.etRemarks.setFocusableInTouchMode(true);
              //  holder.etRemarks.setFocusable(true);
            }
        } else {
            if (isSelectedAll == true) {
                holder.cbForRemarks.setChecked(isSelectedAll);
                holder.etRemarks.setFocusableInTouchMode(isSelectedAll);
                holder.etRemarks.setFocusable(isSelectedAll);
            } else {
                holder.cbForRemarks.setChecked(isSelectedAll);
                holder.etRemarks.setFocusableInTouchMode(isSelectedAll);
                holder.etRemarks.setFocusable(isSelectedAll);

            }
        }*/

        holder.etRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  if (holder.cbForRemarks.isChecked()) {
                      holder.etRemarks.setFocusableInTouchMode(false);
                      holder.etRemarks.setFocusable(false);
                  }else {
                      holder.etRemarks.setFocusableInTouchMode(true);
                      holder.etRemarks.setFocusable(true);
                  }
            }
        });

        holder.cbForRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = position;
                if (!holder.etRemarks.getText().toString().isEmpty()) {
                    GrantRejectPojo grantRejectPojo = new GrantRejectPojo();
                    if (!itemStateArray.get(adapterPosition, false)) {
                        holder.cbForRemarks.setChecked(true);
                        itemStateArray.put(adapterPosition, true);
                        holder.etRemarks.setFocusableInTouchMode(false);
                        holder.etRemarks.setFocusable(false);

                        grantRejectPojo.setAppNo(model.getAppNo());
                        grantRejectPojo.setApplyDate(model.getApplyDate());
                        grantRejectPojo.setEmpCode(model.getEmpCode());
                        grantRejectPojo.setName(model.getName());
                        grantRejectPojo.setReason(model.getReason());
                        grantRejectPojo.setDurationDate(model.getDurationDate());
                        grantRejectPojo.setLeaveType(model.getLeaveType());
                        grantRejectPojo.setStatus(model.getStatus());
                        grantRejectPojo.setStatusDesc(model.getStatusDesc());
                        grantRejectPojo.setTotalDays(model.getTotalDays());
                        grantRejectPojo.setRemarks(etText);
                        alAddCheckedList.add(grantRejectPojo);
                        passCheckedListData.onCheckedList(alAddCheckedList);

                    } else {
                        for (int i = 0; i < alAddCheckedList.size(); i++) {
                            if (alAddCheckedList.get(i).getAppNo().equals(model.getAppNo())) {
                                alAddCheckedList.remove(i);
                            }
                        }
                        alAddCheckedList.remove(model.getAppNo());
                        alAddCheckedList.remove(model.getApplyDate());
                        alAddCheckedList.remove(model.getName());
                        alAddCheckedList.remove(model.getReason());
                        alAddCheckedList.remove(model.getDurationDate());
                        alAddCheckedList.remove(model.getLeaveType());
                        alAddCheckedList.remove(model.getStatus());
                        alAddCheckedList.remove(model.getStatusDesc());
                        alAddCheckedList.remove(model.getTotalDays());
                        alAddCheckedList.remove(model.getRemarks());
                        notifyDataSetChanged();

                        holder.etRemarks.setText("");
                        holder.cbForRemarks.setChecked(false);
                        holder.etRemarks.setFocusableInTouchMode(false);
                        holder.etRemarks.setFocusable(false);
                        itemStateArray.put(adapterPosition, false);

                    }
                }else {
                    holder.cbForRemarks.setChecked(false);
                    itemStateArray.put(adapterPosition, false);
                    Toast.makeText(mContext, "Please first enter remarks", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvAppNo, tvName, tvStatus, tvEmpCode, tvLeaveType;
        public TextView tvApplyDate, tvDuration, tvReason, tvTotalDays;
        public EditText etRemarks;
        public CheckBox cbForRemarks;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvAppNo = itemView.findViewById(R.id.tv_app_no);
            tvName = itemView.findViewById(R.id.tv_name);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvEmpCode = itemView.findViewById(R.id.tv_emp_code);
            tvLeaveType = itemView.findViewById(R.id.tv_leave_type);
            tvApplyDate = itemView.findViewById(R.id.tv_apply_date);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvReason = itemView.findViewById(R.id.tv_reason);
            tvTotalDays = itemView.findViewById(R.id.tv_total_days);
            etRemarks = itemView.findViewById(R.id.et_remarks);
            cbForRemarks = itemView.findViewById(R.id.cb_remarks);
        }


    }


}