package cbs.hreye.activities.travelRequest.AddTravelRequestFormData;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;
import cbs.hreye.R;
import cbs.hreye.pojo.AssociateResponseDataModel;


public class AssociateDetailAdapter extends BaseAdapter implements Filterable {

    Context context;
    private ArrayList<AssociateResponseDataModel> result;

    TextView associateCodeTextView;
    TextView nameTextView;
    TextView departmentTextview;
    TextView designationTextView;
    TextView joiningDateTextView;
    TextView leavingDateTextView;
    ArrayList<AssociateResponseDataModel> filterList;

    AssociateDetailAdapter.CustomFilter filter;
    public static ArrayList<AssociateResponseDataModel> filters;
    String searchString = "";

    public AssociateDetailAdapter(Context ctx, ArrayList<AssociateResponseDataModel> result) {
        super();
        this.context = ctx;
        this.result = result;
        this.filterList = result;
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int pos) {
        return result.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return result.indexOf(getItem(pos));
    }

    @Override
    public View getView(final int pos, View convertView, final ViewGroup arg2) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.associate_item_row_dialog_layout, arg2, false);
        }

        associateCodeTextView = convertView.findViewById(R.id.associatecode_textview);
        nameTextView = convertView.findViewById(R.id.name_textview);
        departmentTextview = convertView.findViewById(R.id.department_textview);
        designationTextView = convertView.findViewById(R.id.designation_textview);
        joiningDateTextView=convertView.findViewById(R.id.joining_textview);
        leavingDateTextView=convertView.findViewById(R.id.leaving_textview);


        associateCodeTextView.setText(result.get(pos).getAssoCode());
        nameTextView.setText(result.get(pos).getName());
        departmentTextview.setText(result.get(pos).getDepartment());
        designationTextView.setText(result.get(pos).getDesignation());
        joiningDateTextView.setText(result.get(pos).getJoiningDate());
        if(TextUtils.isEmpty(result.get(pos).getLeavingDate())){
            leavingDateTextView.setVisibility(View.GONE);
        }else{
            leavingDateTextView.setText(result.get(pos).getLeavingDate()+"");
            leavingDateTextView.setVisibility(View.VISIBLE);
        }

        highlightedText(result.get(pos).getName().toLowerCase(Locale.getDefault()));

        return convertView;
    }

    //highlighted text while searching..
    private void highlightedText(String customerName){
        if (customerName.contains(searchString)) {
            int startPos = customerName.indexOf(searchString);
            int endPos = startPos + searchString.length();
            Spannable spanText = Spannable.Factory.getInstance().newSpannable(nameTextView.getText());
            spanText.setSpan(new ForegroundColorSpan(Color.BLUE), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            nameTextView.setText(spanText, TextView.BufferType.SPANNABLE);
        }

//        if (projectName.contains(searchString)) {
//            int startPos = projectName.indexOf(searchString);
//            int endPos = startPos + searchString.length();
//            Spannable spanText = Spannable.Factory.getInstance().newSpannable(lst_pro_name.getText());
//            spanText.setSpan(new ForegroundColorSpan(Color.BLUE), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            lst_pro_name.setText(spanText, TextView.BufferType.SPANNABLE);
//        }

    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        if (filter == null) {
            filter = new CustomFilter();
        }
        return filter;
    }

    //INNER CLASS
    class CustomFilter extends Filter {
        boolean msgReq = false;
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            searchString = constraint.toString();
            if (constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();
                filters = new ArrayList<>();
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint) ||
                            filterList.get(i).getAssoCode().toUpperCase().contains(constraint)) {
                        filters.add(filterList.get(i));
                        msgReq = true;
                    }
                }
                if (filters.size() == 0) {
                    if (msgReq) {
                        Toast.makeText(context, "No Result found !", Toast.LENGTH_LONG).show();
                        msgReq = false;
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
            result = (ArrayList<AssociateResponseDataModel>) results.values;
            notifyDataSetChanged();
        }
    }
}

