package cbs.hreye.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.PrefrenceKey;
import cbs.hreye.R;

public class AuthorizeLeaveTest extends AppCompatActivity implements View.OnClickListener {
    Context mContext;
    Spinner spnLeaveType, spnSessionFrom, spnSessionTo;
    TextView tvDateFrom, tvDateTo, tvHearder;
    EditText etReason, etAssName, etRepPerson, etBalance;
    Button btnApply;
    ImageView ivBack;
    LinearLayout llRepunch;
    SimpleDateFormat simpleDateFormat;
    String dateFromTo, sessionIDFrom, SessionIDTo;
    String[] arraySessionId, arraySession;
    ArrayAdapter<String> adapterSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize_leave);
        mContext = AuthorizeLeaveTest.this;
        getId();
        setClickListener();
    }
    private void getId() {
        etBalance = findViewById(R.id.et_bal);
        spnLeaveType = findViewById(R.id.spn_auth_type);
        tvDateFrom = findViewById(R.id.txt_auth_from);
        tvDateTo = findViewById(R.id.txt_auth_to);
        spnSessionFrom = findViewById(R.id.spn_auth_one);
        spnSessionTo = findViewById(R.id.spn_auth_two);
        etReason = findViewById(R.id.et_auth_rsn);
        btnApply = findViewById(R.id.btn_lv_auth);
        etAssName = findViewById(R.id.txt_emp);
        etRepPerson = findViewById(R.id.txt_rep);
        llRepunch = findViewById(R.id.ll_root);
        tvHearder =  findViewById(R.id.header_text);
        ivBack = findViewById(R.id.header_back);

        this.arraySession = new String[]{
                "Whole Day", "First Session", "Second Session"
        };

        this.arraySessionId = new String[]{
                "W", "F", "A"
        };

        if (LeaveList.operation.equals("m")) {
            tvHearder.setText("Modify Leave");
            btnApply.setText("Modify Leave");
        } else {
            tvHearder.setText("Authorize Leave");
            btnApply.setText("Authorize Leave");
        }

        etAssName.setText(CommonMethods.getPrefsData(mContext, PrefrenceKey.USER_NAME, ""));
        etRepPerson.setText(LeaveList.eName);
        tvDateTo.setText(LeaveList.dtTo);
        tvDateFrom.setText(LeaveList.dtFrm);
        etReason.setText(LeaveList.rsn);

       adapterSession = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_dropdown_item, arraySession);
        spnSessionFrom.setAdapter(adapterSession);
        spnSessionTo.setAdapter(adapterSession);

        ArrayAdapter<String> arrSessId = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_dropdown_item, arraySessionId);

        if (LeaveList.frmSess != null) {
            int spnFrmSess = arrSessId.getPosition(LeaveList.frmSess);
            spnSessionFrom.setSelection(spnFrmSess);
        }

        if (LeaveList.toSess != null) {
            int spnToSess = arrSessId.getPosition(LeaveList.toSess);
            spnSessionTo.setSelection(spnToSess);
        }

    }

    private void setClickListener() {
        ivBack.setOnClickListener(this);
        tvDateFrom.setOnClickListener(this);
        tvDateTo.setOnClickListener(this);

        spnSessionFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sessionIDFrom = arraySessionId[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnSessionTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SessionIDTo = arraySessionId[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_back:
                onBackPressed();
                break;
            case R.id.txt_auth_from:
                dateFromTo = "from";
                setDate(dateFromTo);
                break;
            case R.id.txt_auth_to:
                dateFromTo = "to";
                setDate(dateFromTo);
                break;
                default:
                    break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setDate(final String dateFromTo){
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog mDatePicker = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                Calendar c = Calendar.getInstance();
                c.set(selectedyear, selectedmonth, selectedday);
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
              String currentDate =  CommonMethods.pad(selectedday) + "/" + CommonMethods.pad(selectedmonth + 1) +
                        "/" + CommonMethods.pad(selectedyear);
               if (dateFromTo.equals("from")){
                   tvDateFrom.setText(currentDate);
               }else {
                   tvDateTo.setText(currentDate);
               }

            }
        }, mYear, mMonth, mDay);
        mDatePicker.getDatePicker().setCalendarViewShown(false);
        mDatePicker.show();
    }
}