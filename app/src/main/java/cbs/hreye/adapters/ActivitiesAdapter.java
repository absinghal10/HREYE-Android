package cbs.hreye.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;

import cbs.hreye.R;
import cbs.hreye.databinding.ActivitiesAdapterBinding;
import cbs.hreye.pojo.ActivitiesPojo;


public class ActivitiesAdapter extends BaseAdapter implements Filterable {

    Context context;
    private ArrayList<ActivitiesPojo> result;
    ArrayList<ActivitiesPojo> filterList;
    CustomFilter filter;
    public static ArrayList<ActivitiesPojo> filters;

    public ActivitiesAdapter(Context ctx, ArrayList<ActivitiesPojo> result) {
        // TODO Auto-generated constructor stub
        this.context =ctx;
        this.result=result;
        this.filterList=result;
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

    @SuppressLint("InflateParams")
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
         LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         ActivitiesAdapterBinding activitiesAdapBinding = DataBindingUtil.getBinding(convertView);

        if (activitiesAdapBinding == null) {
            activitiesAdapBinding = DataBindingUtil.inflate(inflater, R.layout.activities_adapter, parent, false);
        }

        activitiesAdapBinding.tvTranNo.setText(result.get(pos).getTRANSACTION_NO());
        activitiesAdapBinding.tvDate.setText(result.get(pos).getREPORTING_DATE());

        if(result.get(pos).getDOC_STATUS().equals("A")){
            activitiesAdapBinding.tvDocStatus.setText(R.string.authorized);
            activitiesAdapBinding.llActivity.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else{
            activitiesAdapBinding.llActivity.setBackgroundColor(Color.parseColor("#A1D4BA"));
            activitiesAdapBinding.tvDocStatus.setText(R.string.fresh);
        }
        return activitiesAdapBinding.getRoot();
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        if(filter == null) {
            filter=new CustomFilter();
        }
        return filter;
    }

    //INNER CLASS
    class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub
            FilterResults results=new FilterResults();

            if(constraint != null && constraint.length()>0)
            {
                constraint=constraint.toString().toUpperCase();
                 filters=new ArrayList<>();
                for(int i=0;i<filterList.size();i++)
                {
                    if(filterList.get(i).getREPORTING_DATE().toUpperCase().contains(constraint)){
                        ActivitiesPojo p=new ActivitiesPojo(filterList.get(i).getTRANSACTION_NO(),
                                filterList.get(i).getREPORTING_DATE(),filterList.get(i).getDOC_STATUS());
                        filters.add(p);
                    }

                }
                results.count=filters.size();
                results.values=filters;
            }else{
                results.count=filterList.size();
                results.values=filterList;

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // TODO Auto-generated method stub
            result = (ArrayList<ActivitiesPojo>) results.values;
            notifyDataSetChanged();
        }
    }
}
