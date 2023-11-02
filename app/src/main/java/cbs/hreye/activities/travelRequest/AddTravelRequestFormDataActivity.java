package cbs.hreye.activities.travelRequest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import cbs.hreye.R;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.CustomTextView;

public class AddTravelRequestFormDataActivity extends AppCompatActivity {

    private TextView toolBarHeaderTextView;
    private ImageView backButtonImageView;
    private CustomTextView dialogHeadingTextView;
    private ImageView dialogCancelImageView;
    private Spinner typeOfEmployeeSpinner;
    private EditText associateCodeEditText;
    private EditText associateNameEditText;
    private EditText nameAsPerDocEditText;
    private EditText ageEditText;
    private EditText customerDataEditText;
    private Spinner tripSpinner;
    private EditText travelDateEditText;
    private EditText travelReturnDateEditText;
    private Spinner travelModeSpinner;
    private Spinner travelReasonSpinner;
    private Spinner hotelRequiredSpinner;
    private EditText hotelFromEditText;
    private EditText hotelToEditText;
    private EditText fromLocationEditText;
    private EditText toLocationEditText;
    private EditText passportEditText;
    private EditText validityEditText;
    private Button addButton;


    public int mYear, mMonth, mDay;

    String searchDate="";

    String[] tripSpinnerData = {"Select", "Singe trip", "Round trip"};
    String[] typeOfEmployeeSpinnerData = {"Select","Employee", "Non-Employee",};
    String[] travelModeSpinnerData = {"Select","Bus", "Train","Flight"};
    String[] travelReasonSpinnerData = {"Select","Sales", "Client Visit","Relocation","SAP Meeting"};
    String[] hotelRequiredSpinnerData = {"Select","Yes","No"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel_request_form_data);
        getViewByFindViewById();
        setToolBarTitle();

        // Set up the spinners
        setupSpinner(tripSpinner, tripSpinnerData);
        setupSpinner(typeOfEmployeeSpinner, typeOfEmployeeSpinnerData);
        setupSpinner(travelModeSpinner, travelModeSpinnerData);
        setupSpinner(travelReasonSpinner, travelReasonSpinnerData);
        setupSpinner(hotelRequiredSpinner,hotelRequiredSpinnerData);

        openDatePickerDialog(travelDateEditText);
        openDatePickerDialog(travelReturnDateEditText);
        openDatePickerDialog(hotelFromEditText);
        openDatePickerDialog(hotelToEditText);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputFieldValidation() && validateSpinners()) {
                    Toast.makeText(AddTravelRequestFormDataActivity.this,"Validate",Toast.LENGTH_SHORT).show();

                    bindDataToModel();

                }else{
                    Toast.makeText(AddTravelRequestFormDataActivity.this,"Not Validate",Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    private void bindDataToModel() {

        // Get the values from the EditText fields
        String associateCode = associateCodeEditText.getText().toString();
        String associateName = associateNameEditText.getText().toString();
        String nameAsPerDoc = nameAsPerDocEditText.getText().toString();
        String age = ageEditText.getText().toString();
        String customerData = customerDataEditText.getText().toString();
        String travelDate = travelDateEditText.getText().toString();
        String travelReturnDate = travelReturnDateEditText.getText().toString();
        String hotelFrom = hotelFromEditText.getText().toString();
        String hotelTo = hotelToEditText.getText().toString();
        String fromLocation = fromLocationEditText.getText().toString();
        String toLocation = toLocationEditText.getText().toString();
        String passport = passportEditText.getText().toString();
        String validity = validityEditText.getText().toString();

        // Create a TravelRequestModel instance by passing the values
        TravelRequestModel requestModel = new TravelRequestModel(
                "1", // Replace with the actual value for srNo
                "TX123", // Replace with the actual value for transactionNo
                typeOfEmployeeSpinner.getSelectedItem().toString(), // Get the selected value from the typeOfEmployeeSpinner
                associateCode,
                associateName,
                nameAsPerDoc,
                age,
                customerData,
                tripSpinner.getSelectedItem().toString(), // Get the selected value from the tripSpinner
                travelDate,
                travelReturnDate,
                travelModeSpinner.getSelectedItem().toString(), // Get the selected value from the travelModeSpinner
                travelReasonSpinner.getSelectedItem().toString(), // Get the selected value from the travelReasonSpinner
                hotelRequiredSpinner.getSelectedItem().toString(), // Get the selected value from the hotelRequiredSpinner
                hotelFrom,
                hotelTo,
                fromLocation,
                toLocation,
                passport,
                validity
        );
    }


    private boolean validateSpinners() {
        String selectText = "Select"; // The string to compare with

        if (typeOfEmployeeSpinner.getSelectedItem().toString().equalsIgnoreCase(selectText) ||
                tripSpinner.getSelectedItem().toString().equalsIgnoreCase(selectText) ||
                travelModeSpinner.getSelectedItem().toString().equalsIgnoreCase(selectText) ||
                travelReasonSpinner.getSelectedItem().toString().equalsIgnoreCase(selectText) ||
                hotelRequiredSpinner.getSelectedItem().toString().equalsIgnoreCase(selectText)) {

            // Show an error message or toast indicating that all spinners need to be selected
            Toast.makeText(this, "Please select values for all spinners", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // All spinners have valid selections
    }

    private boolean inputFieldValidation() {
        // Get the values from the EditText fields
        String associateCode = associateCodeEditText.getText().toString();
        String associateName = associateNameEditText.getText().toString();
        String nameAsPerDoc = nameAsPerDocEditText.getText().toString();
        String age = ageEditText.getText().toString();
        String customerData = customerDataEditText.getText().toString();
        String travelDate = travelDateEditText.getText().toString();
        String travelReturnDate = travelReturnDateEditText.getText().toString();
        String hotelFrom = hotelFromEditText.getText().toString();
        String hotelTo = hotelToEditText.getText().toString();
        String fromLocation = fromLocationEditText.getText().toString();
        String toLocation = toLocationEditText.getText().toString();
        String passport = passportEditText.getText().toString();
        String validity = validityEditText.getText().toString();

        // Perform validation checks using TextUtils
        if (TextUtils.isEmpty(associateCode) || TextUtils.isEmpty(associateName) || TextUtils.isEmpty(nameAsPerDoc)
                || TextUtils.isEmpty(age) || TextUtils.isEmpty(customerData) || TextUtils.isEmpty(travelDate)
                || TextUtils.isEmpty(travelReturnDate) || TextUtils.isEmpty(hotelFrom) || TextUtils.isEmpty(hotelTo)
                || TextUtils.isEmpty(fromLocation) || TextUtils.isEmpty(toLocation) || TextUtils.isEmpty(passport)
                || TextUtils.isEmpty(validity)) {
            // Show an error message or toast indicating that all fields are required
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // Validation passed
    }


    private void setupSpinner(Spinner spinner, String[] data) {
        // Create an ArrayAdapter using the sample data and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);

        // Set the layout for the dropdown items
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter for the Spinner
        spinner.setAdapter(adapter);

        // Add an OnItemSelectedListener to handle item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item
                String selectedItem = (String) parentView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here, but you can add logic if needed
            }
        });
    }




    private void getViewByFindViewById() {
        // Initialize your views using findViewById
        dialogHeadingTextView = findViewById(R.id.dialog_heading_textview);
        dialogCancelImageView = findViewById(R.id.dialog_cancel_imageview);
        typeOfEmployeeSpinner = findViewById(R.id.type_of_employee_spinner);
        associateCodeEditText = findViewById(R.id.dialog_associate_code_edittext);
        associateNameEditText = findViewById(R.id.dialog_associate_name_edittext);
        nameAsPerDocEditText = findViewById(R.id.dialog_name_edittext_as_per_doc);
        ageEditText = findViewById(R.id.dialog_age_edittext);
        customerDataEditText = findViewById(R.id.dialog_customer_data_edittext);
        tripSpinner = findViewById(R.id.dialog_trip_spinner);
        travelDateEditText = findViewById(R.id.dialog_travel_date_edittext);
        travelReturnDateEditText = findViewById(R.id.dialog_travel_return_date_edittext);
        travelModeSpinner = findViewById(R.id.dialog_travel_mode_spinner);
        travelReasonSpinner = findViewById(R.id.dialog_travel_reason_spinner);
        hotelRequiredSpinner = findViewById(R.id.dialog_hotel_required_spinner);
        hotelFromEditText = findViewById(R.id.dialog_hotel_from_edittext);
        hotelToEditText = findViewById(R.id.dialog_hotel_to_edittext);
        fromLocationEditText = findViewById(R.id.dialog_from_location_edittext);
        toLocationEditText = findViewById(R.id.dialog_to_location_edittext);
        passportEditText = findViewById(R.id.dialog_passport_edittext);
        validityEditText = findViewById(R.id.dialog_validity_edittext);
        addButton = findViewById(R.id.btn_add);
    }


    private void setToolBarTitle() {
        toolBarHeaderTextView = findViewById(R.id.header_text);
        toolBarHeaderTextView.setText("Travel Request");
        backButtonImageView = findViewById(R.id.header_back);
        backButtonImageView.setOnClickListener(v -> onBackPressed());
    }


    private void openDatePickerDialog(EditText editText) {

        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Calendar mcurrentDate = Calendar.getInstance();
                    mYear = mcurrentDate.get(Calendar.YEAR);
                    mMonth = mcurrentDate.get(Calendar.MONTH);
                    mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog mDatePicker = new DatePickerDialog(AddTravelRequestFormDataActivity.this, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            Calendar c = Calendar.getInstance();
                            c.set(selectedyear, selectedmonth, selectedday);
                            searchDate = CommonMethods.pad(selectedday) + "/" + CommonMethods.pad(selectedmonth + 1) +
                                    "/" + CommonMethods.pad(selectedyear);
                            editText.setText(searchDate);
                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.getDatePicker().setCalendarViewShown(false);
                    mDatePicker.show();
                }
        });
    }

}