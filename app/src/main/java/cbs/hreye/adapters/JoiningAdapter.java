package cbs.hreye.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cbs.hreye.R;
import cbs.hreye.pojo.BirthPojo;

public class JoiningAdapter extends BaseAdapter {

    Context context;
    ArrayList<BirthPojo> result;
    TextView name;
    TextView Date;
    TextView Depart;
    TextView EMAIL;
    TextView Mobile;

    public JoiningAdapter(Context ctx, ArrayList<BirthPojo> result) {
        super();
        this.context = ctx;
        this.result = result;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int pos, View convertView, final ViewGroup arg2) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.joining_adapter, arg2, false);
        }

        name = convertView.findViewById(R.id.j_name);
        Date = convertView.findViewById(R.id.j_date);
        Depart = convertView.findViewById(R.id.j_dep);
        EMAIL = convertView.findViewById(R.id.j_mail);
        Mobile = convertView.findViewById(R.id.j_mob);

        name.setText(result.get(pos).getNAME());
        Date.setText(result.get(pos).getDate());

        if (result.get(pos).getDEPARTMENT().equals("")) {
            Depart.setText(R.string.not_available);
        } else {
            Depart.setText(result.get(pos).getDEPARTMENT());
        }
        if (result.get(pos).getEMAIL().equals("")) {
            EMAIL.setText(R.string.not_available);
        } else {
            EMAIL.setText(result.get(pos).getEMAIL());
        }
        if (result.get(pos).getMOBILE_NO().equals("")) {
            Mobile.setText(R.string.not_available);
        } else {
            Mobile.setText(result.get(pos).getMOBILE_NO());
        }
        return convertView;
    }
}
