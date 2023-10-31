package cbs.hreye.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cbs.hreye.R;
import cbs.hreye.pojo.BirthPojo;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.CustomProgressbar;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;

public class BirthAdapter extends BaseAdapter {

    Context context;
    ArrayList<BirthPojo> result;
    String JSON_URL;
    TextView llBirth, name, Date, OCCASION, EMAIL, Mobile;
    String date = new SimpleDateFormat("dd-MMM", Locale.getDefault()).format(new Date());

    public BirthAdapter(Context ctx, ArrayList<BirthPojo> result) {
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
            convertView = inflater.inflate(R.layout.birth_adapter, arg2, false);
        }

        llBirth = convertView.findViewById(R.id.ll_birth);
        name = convertView.findViewById(R.id.bday_name);
        Date = convertView.findViewById(R.id.bday_date);
        OCCASION = convertView.findViewById(R.id.bday_occ);
        EMAIL = convertView.findViewById(R.id.bday_mail);
        Mobile = convertView.findViewById(R.id.bday_mob);

        name.setText(result.get(pos).getNAME());
        Date.setText(result.get(pos).getDate());
        OCCASION.setText(result.get(pos).getOCCASION());
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
        if (date.equals(result.get(pos).getDate())) {
            llBirth.setVisibility(View.VISIBLE);
        } else {
            llBirth.setVisibility(View.GONE);
        }

        llBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonMethods.isOnline(context)) {
                    JSON_URL = ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/Getmaildetail?" +
                            "Mailfrom=" + CommonMethods.getPrefsData(context, PrefrenceKey.USER_EMAIL, "") + "&" +
                            "MailTo=" + result.get(pos).getEMAIL();
                    LogMsg.d("SendMailService", JSON_URL);

                    if (result.get(pos).isEnabled()) {
                        result.get(pos).setEnabled(false);
                        SendMailService();
                    }

                }else{
                    Toast.makeText(context, R.string.net, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return convertView;
    }

    private void SendMailService() {
        CustomProgressbar.showProgressBar(context, false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CustomProgressbar.hideProgressBar();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(context, "Email Sent", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomProgressbar.hideProgressBar();
                        Toast.makeText(context, CommonMethods.volleyError(error), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}