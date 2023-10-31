package cbs.hreye.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
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

import cbs.hreye.pojo.ProjectPojo;
import cbs.hreye.R;

public class CustomerDetailsAdapter extends BaseAdapter implements Filterable {

    Context context;
    private ArrayList<ProjectPojo> result;

    TextView lst_cust_id;
    TextView lst_cust_name;
    TextView lst_pro_id;
    TextView lst_pro_name;
    ArrayList<ProjectPojo> filterList;
    CustomFilter filter;
    public static ArrayList<ProjectPojo> filters;
    String searchString = "";

    public CustomerDetailsAdapter(Context ctx, ArrayList<ProjectPojo> result) {
        super();
        this.context = ctx;
        this.result = result;
        this.filterList = result;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        return result.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return result.indexOf(getItem(pos));
    }

    @Override
    public View getView(final int pos, View convertView, final ViewGroup arg2) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.customer_details_adapter_demo, arg2, false);
        }

        lst_cust_id = convertView.findViewById(R.id.tv_customer_id);
        lst_cust_name = convertView.findViewById(R.id.tv_customer_name);
        lst_pro_id = convertView.findViewById(R.id.tv_customer_pro_id);
        lst_pro_name = convertView.findViewById(R.id.tv_customer_pro_name);

        lst_cust_id.setText(result.get(pos).getCUSTOMER_ID());
        lst_cust_name.setText(result.get(pos).getCUSTOMER_NAME());
        lst_pro_id.setText(result.get(pos).getPROJECT_ID());
        lst_pro_name.setText(result.get(pos).getPROJECT_NAME());

        highlightedText(result.get(pos).getCUSTOMER_NAME().toLowerCase(Locale.getDefault()), result.get(pos).getPROJECT_NAME().toLowerCase(Locale.getDefault()));

        return convertView;
    }

    //highlighted text while searching..
    private void highlightedText(String customerName, String projectName){
        if (customerName.contains(searchString)) {
            int startPos = customerName.indexOf(searchString);
            int endPos = startPos + searchString.length();
            Spannable spanText = Spannable.Factory.getInstance().newSpannable(lst_cust_name.getText());
            spanText.setSpan(new ForegroundColorSpan(Color.BLUE), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            lst_cust_name.setText(spanText, TextView.BufferType.SPANNABLE);
        }

        if (projectName.contains(searchString)) {
            int startPos = projectName.indexOf(searchString);
            int endPos = startPos + searchString.length();
            Spannable spanText = Spannable.Factory.getInstance().newSpannable(lst_pro_name.getText());
            spanText.setSpan(new ForegroundColorSpan(Color.BLUE), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            lst_pro_name.setText(spanText, TextView.BufferType.SPANNABLE);
        }

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
            // TODO Auto-generated method stub
            FilterResults results = new FilterResults();
            searchString = constraint.toString();
            if (constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();
                filters = new ArrayList<>();
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getCUSTOMER_NAME().toUpperCase().contains(constraint) ||
                            filterList.get(i).getCUSTOMER_ID().toUpperCase().contains(constraint) ||
                            filterList.get(i).getPROJECT_NAME().toUpperCase().contains(constraint) ||
                            filterList.get(i).getPROJECT_ID().toUpperCase().contains(constraint)) {
                        ProjectPojo p = new ProjectPojo(filterList.get(i).getCUSTOMER_NAME(),
                                filterList.get(i).getCUSTOMER_ID(),filterList.get(i).getPROJECT_NAME(),
                                filterList.get(i).getPROJECT_ID());
                        filters.add(p);
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
            // TODO Auto-generated method stub
            result = (ArrayList<ProjectPojo>) results.values;
            notifyDataSetChanged();
        }
    }
}
