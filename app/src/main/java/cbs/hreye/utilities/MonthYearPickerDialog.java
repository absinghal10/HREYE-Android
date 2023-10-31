package cbs.hreye.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import java.util.Calendar;

import cbs.hreye.activities.AttendanceList;
import cbs.hreye.R;

public class MonthYearPickerDialog extends DialogFragment {
    private static final int MAX_YEAR = 2099;
    private static final int MINYEAR = 2000;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        Calendar cal = Calendar.getInstance();

        View dialog = inflater.inflate(R.layout.date_picker_dialog, null);
        final NumberPicker monthPicker =dialog.findViewById(R.id.picker_month);
        final NumberPicker yearPicker = dialog.findViewById(R.id.picker_year);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setDisplayedValues( new String[] { "Jan", "Feb", "Mar", "Apr","May","Jun", "Jul","Aug", "Sep","Oct","Nov","Dec" } );
        monthPicker.setValue(cal.get(Calendar.MONTH)+1);


        int year = cal.get(Calendar.YEAR);
        yearPicker.setMinValue(MINYEAR);
        yearPicker.setMaxValue(MAX_YEAR);
        yearPicker.setValue(year);


        builder.setView(dialog)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                         ((AttendanceList) getActivity()).getResult(monthPicker.getValue(),yearPicker.getValue());
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MonthYearPickerDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}