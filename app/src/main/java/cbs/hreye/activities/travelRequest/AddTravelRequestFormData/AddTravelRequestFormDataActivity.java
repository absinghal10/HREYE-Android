package cbs.hreye.activities.travelRequest.AddTravelRequestFormData;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import cbs.hreye.R;
import cbs.hreye.activities.travelRequest.TravelRequestAddData.TravelRequestAddDataMvpView;
import cbs.hreye.activities.travelRequest.TravelRequestAddData.TravelRequestAddDataPresenter;
import cbs.hreye.activities.travelRequest.TravelRequestModel;
import cbs.hreye.adapters.CustomerDetailsAdapter;
import cbs.hreye.pojo.ADetail;
import cbs.hreye.pojo.AssociateResponseDataModel;
import cbs.hreye.pojo.ProjectPojo;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.CustomTextView;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;

public class AddTravelRequestFormDataActivity extends AppCompatActivity implements TravelRequestAddFormDataMvpView {

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

    String[] tripSpinnerData = {"Select", "Single trip", "Round trip"};
    String[] typeOfEmployeeSpinnerData = {"Select","Employee", "Non-Employee",};
    String[] travelModeSpinnerData = {"Select","Bus", "Train","Flight"};
    String[] travelReasonSpinnerData = {"Select","Sales","Presales","Client Visit","Relocation","SAP Meeting","Others"};
    String[] hotelRequiredSpinnerData = {"Select","Yes","No"};


    private TravelRequestAddFormDataPresenter travelRequestAddFormDataPresenter;

    private  String travelTypeValue="";

    int iLength = 0;


    private String isNavigate="";
    private int position;
    private TravelRequestModel editDataModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel_request_form_data);
        getViewByFindViewById();
        setToolBarTitle();

        travelTypeValue=getIntent().getStringExtra("travelTypeValue");
        isNavigate=getIntent().getStringExtra("isNavigate");
        position=getIntent().getIntExtra("position",0);
        editDataModel= (TravelRequestModel) getIntent().getSerializableExtra("editdataModel");


        travelRequestAddFormDataPresenter=new TravelRequestAddFormDataPresenter(this,AddTravelRequestFormDataActivity.this);

        // Set up the spinners
        setupSpinner(tripSpinner, tripSpinnerData,"tripSpinner");
        setupSpinner(typeOfEmployeeSpinner, typeOfEmployeeSpinnerData,"typeOfEmployeeSpinner");
        setupSpinner(travelModeSpinner, travelModeSpinnerData,"travelModeSpinner");
        setupSpinner(travelReasonSpinner, travelReasonSpinnerData,"travelReasonSpinner");
        setupSpinner(hotelRequiredSpinner,hotelRequiredSpinnerData,"hotelRequiredSpinner");

        openDatePickerDialog(travelDateEditText);

        passportAndValidityEdittextValidation(travelTypeValue,passportEditText);
        passportAndValidityEdittextValidation(travelTypeValue,validityEditText);

        addButton.setText("Add to list");

        // edit case if it come from edit button

        validityTextValidation(travelTypeValue);


        editTravelRequestForm();


        customerDataEditText.setFocusable(false);
        customerDataEditText.setCursorVisible(false);
        customerDataEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              travelRequestAddFormDataPresenter.fetchCustomerAndProjectList();
            }
        });

        associateCodeEditText.setCursorVisible(false);
        associateCodeEditText.setFocusable(false);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputFieldValidation()) {
                    bindDataToModel();
                }
            }
        });

    }

    private void editTravelRequestForm() {
        if(isNavigate.equalsIgnoreCase("FromEdit")){
            addButton.setText("Update");
            if(editDataModel!=null){
                associateCodeEditText.setText(editDataModel.getAssociateCode());
                associateNameEditText.setText(editDataModel.getAssociateName());
                nameAsPerDocEditText.setText(editDataModel.getNameAsPerGovtDoc());
                ageEditText.setText(editDataModel.getAge());
                customerDataEditText.setText(editDataModel.getCustomer());

                // Reset Date in dd/MM/yyyy format
                if(TextUtils.isEmpty(editDataModel.getTravelData())){
                    travelDateEditText.setText(editDataModel.getTravelData());
                }else{
                    travelDateEditText.setText(CommonMethods.changeDateFromyyyyMMdd(editDataModel.getTravelData()));
                }

                if(TextUtils.isEmpty(editDataModel.getReturnDate())){
                    travelReturnDateEditText.setText(editDataModel.getReturnDate());
                }else{
                    travelReturnDateEditText.setText(CommonMethods.changeDateFromyyyyMMdd(editDataModel.getReturnDate()));
                }

                if(TextUtils.isEmpty(editDataModel.getHotelfrom())){
                    hotelFromEditText.setText(editDataModel.getHotelfrom());
                }else{
                    hotelFromEditText.setText(CommonMethods.changeDateFromyyyyMMdd(editDataModel.getHotelfrom()));
                }

                if(TextUtils.isEmpty(editDataModel.getHotelto())){
                    hotelToEditText.setText(editDataModel.getHotelto());
                }else{
                    hotelToEditText.setText(CommonMethods.changeDateFromyyyyMMdd(editDataModel.getHotelto()));
                }


                if(TextUtils.isEmpty(editDataModel.getValidity())){
                    validityEditText.setText(editDataModel.getValidity());
                }else{
                    String validityDate=CommonMethods.changeDateFromyyyyMMdd(editDataModel.getValidity());
                    validityEditText.setText(validityDate);
                }


                fromLocationEditText.setText(editDataModel.getFromLocation());
                toLocationEditText.setText(editDataModel.getToLocation());

                if(travelTypeValue.equalsIgnoreCase("International")){
                    passportEditText.setText(editDataModel.getPassport());
//                    validityEditText.setText(editDataModel.getValidity());
                }else{
                    passportEditText.setText("");
                    validityEditText.setText("");
                }

                if(editDataModel.getTrip().equalsIgnoreCase("S")){
                    editDataModel.setTrip("Single trip");
                }else{
                    editDataModel.setTrip("Round trip");
                }


                if(editDataModel.getReasonForTravel().equalsIgnoreCase("C1")){
                    editDataModel.setReasonForTravel("Sales");
                }else if(editDataModel.getReasonForTravel().equalsIgnoreCase("C2")){
                    editDataModel.setReasonForTravel("Presales");
                }else if(editDataModel.getReasonForTravel().equalsIgnoreCase("C3")){
                    editDataModel.setReasonForTravel("Client Visit");
                }else if(editDataModel.getReasonForTravel().equalsIgnoreCase("C4")){
                    editDataModel.setReasonForTravel("Relocation");
                }else if(editDataModel.getReasonForTravel().equalsIgnoreCase("C5")){
                    editDataModel.setReasonForTravel("SAP Meeting");
                }else if(editDataModel.getReasonForTravel().equalsIgnoreCase("C6")){
                    editDataModel.setReasonForTravel("Others");
                }

                // tye of employee CONDITION
                if (editDataModel.getTypeOfEmpolyee().equalsIgnoreCase("0")) {
                    editDataModel.setTypeOfEmpolyee("Employee");
                } else if (editDataModel.getTypeOfEmpolyee().equalsIgnoreCase("1")) {
                    editDataModel.setTypeOfEmpolyee("Non-Employee");
                }


                if(editDataModel.getTravelMode().length()==1){
                    if(editDataModel.getTravelMode().equalsIgnoreCase("T")){
                        editDataModel.setTravelMode("Train");
                    }else if(editDataModel.getTravelMode().equalsIgnoreCase("B")){
                        editDataModel.setTravelMode("Bus");
                    }else if(editDataModel.getTravelMode().equalsIgnoreCase("F")){
                        editDataModel.setTravelMode("Flight");
                    }
                }


                setSpinnerPosition(editDataModel.getTravelMode(),travelModeSpinnerData,travelModeSpinner);
                setSpinnerPosition(editDataModel.getTypeOfEmpolyee(),typeOfEmployeeSpinnerData,typeOfEmployeeSpinner);
                setSpinnerPosition(editDataModel.getTrip(),tripSpinnerData,tripSpinner);
                setSpinnerPosition(editDataModel.getReasonForTravel(),travelReasonSpinnerData,travelReasonSpinner);
                setSpinnerPosition(editDataModel.getHotelRequired(),hotelRequiredSpinnerData,hotelRequiredSpinner);

            }
        }

    }

    private void setSpinnerPosition(String selectedValue,String []data,Spinner spinner) {
        // Find the position of the selected value in your Spinner array
        int position = Arrays.asList(data).indexOf(selectedValue);
        if (position >= 0) {
            // Set the Spinner's selection to the position if the value exists in the array
            spinner.setSelection(position);
        } else {
            // The selected value doesn't exist in the array, so you can set a default selection, e.g., "Select"
            spinner.setSelection(0); // 0 corresponds to "Select" in your array
        }
    }

    private void passportAndValidityEdittextValidation(String value,EditText editText) {
        if(value.equalsIgnoreCase("Domestic")){
            editText.setFocusable(false);
            editText.setCursorVisible(false);
            editText.setAlpha(0.6f);
        }else{
            editText.setFocusable(true);
            editText.setCursorVisible(true);
            editText.setAlpha(1.0f);
        }
    }

    private void bindDataToModel() {

        // Get the values from the EditText fields
        String associateCode = associateCodeEditText.getText().toString();
        String associateName = associateNameEditText.getText().toString();
        String nameAsPerDoc = nameAsPerDocEditText.getText().toString();
        String age = ageEditText.getText().toString();
        String customerData = customerDataEditText.getText().toString();

        String travelDate="";
        if(TextUtils.isEmpty(travelDateEditText.getText().toString())){
            travelDate = travelDateEditText.getText().toString();
        }else{
            travelDate = CommonMethods.changeDateTOyyyyMMdd(travelDateEditText.getText().toString());
        }

        String travelReturnDate="";
        if(TextUtils.isEmpty(travelReturnDateEditText.getText().toString())){
            travelReturnDate = travelReturnDateEditText.getText().toString();
        }else{
            travelReturnDate = CommonMethods.changeDateTOyyyyMMdd(travelReturnDateEditText.getText().toString());
        }

        String hotelFrom="";
        if(TextUtils.isEmpty(hotelFromEditText.getText().toString())){
            hotelFrom = hotelFromEditText.getText().toString();
        }else{
            hotelFrom = CommonMethods.changeDateTOyyyyMMdd(hotelFromEditText.getText().toString());
        }


        String hotelTo="";
        if(TextUtils.isEmpty(hotelToEditText.getText().toString())){
            hotelTo = hotelToEditText.getText().toString();
        }else{
            hotelTo = CommonMethods.changeDateTOyyyyMMdd(hotelToEditText.getText().toString());
        }


        String fromLocation = fromLocationEditText.getText().toString();
        String toLocation = toLocationEditText.getText().toString();
        String passport = passportEditText.getText().toString();

        String validity="";
        if(TextUtils.isEmpty(validityEditText.getText().toString())){
            validity = validityEditText.getText().toString();
        }else{
            validity = CommonMethods.changeDateTOyyyyMMdd(validityEditText.getText().toString());
        }



        String selectedTravelReason="";

        if(travelReasonSpinner.getSelectedItem().toString().equalsIgnoreCase(travelReasonSpinnerData[1])){
            selectedTravelReason="C1";
        }else if(travelReasonSpinner.getSelectedItem().toString().equalsIgnoreCase(travelReasonSpinnerData[2])){
            selectedTravelReason="C2";
        }else if(travelReasonSpinner.getSelectedItem().toString().equalsIgnoreCase(travelReasonSpinnerData[3])){
            selectedTravelReason="C3";
        }else if(travelReasonSpinner.getSelectedItem().toString().equalsIgnoreCase(travelReasonSpinnerData[4])){
            selectedTravelReason="C4";
        }else if(travelReasonSpinner.getSelectedItem().toString().equalsIgnoreCase(travelReasonSpinnerData[5])){
            selectedTravelReason="C5";
        }else if(travelReasonSpinner.getSelectedItem().toString().equalsIgnoreCase(travelReasonSpinnerData[6])){
            selectedTravelReason="C6";
        }

        // Create a TravelRequestModel instance by passing the values
        TravelRequestModel requestModel = new TravelRequestModel(
                "1",
                "1",
                "",
                typeOfEmployeeSpinner.getSelectedItem().toString().equalsIgnoreCase("Employee")?"0":"1", // Get the selected value from the typeOfEmployeeSpinner
                associateCode,
                associateName,
                nameAsPerDoc,
                age,
                customerData,
                tripSpinner.getSelectedItem().toString().equalsIgnoreCase("Single trip")?"S":"R", // Get the selected value from the tripSpinner
                travelDate,
                travelReturnDate,
                travelModeSpinner.getSelectedItem().toString(),// Get the selected value from the travelModeSpinner
                selectedTravelReason, // Get the selected value from the travelReasonSpinner
                hotelRequiredSpinner.getSelectedItem().toString(), // Get the selected value from the hotelRequiredSpinner
                hotelFrom,
                hotelTo,
                fromLocation,
                toLocation,
                passport,
                validity,
                "F"
        );


        if(isNavigate.equalsIgnoreCase("FromEdit")){
            Intent intent = new Intent();
            intent.putExtra("inputRequest",requestModel);
            intent.putExtra("position",position);
            setResult(RESULT_OK, intent);
            finish();
        } else if (isNavigate.equalsIgnoreCase("FromGetData")){
            Intent intent = new Intent();
            intent.putExtra("inputRequest",requestModel);
            setResult(RESULT_OK, intent);
            finish();
        }else{
            Intent intent = new Intent();
            intent.putExtra("inputRequest",requestModel);
            setResult(RESULT_OK, intent);
            finish();
        }

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



        String selectText = "Select"; // The string to compare with


        if (typeOfEmployeeSpinner.getSelectedItem().toString().equalsIgnoreCase(selectText)){
            Toast.makeText(this, "Please select type of employee", Toast.LENGTH_SHORT).show();
            return false;
        }


        if(TextUtils.isEmpty(associateCode)&& typeOfEmployeeSpinner.getSelectedItem().toString().equalsIgnoreCase("Employee")){
            Toast.makeText(this,"Please enter your associate code",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(associateName)){
            Toast.makeText(this,"Please enter your associate name",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(nameAsPerDoc)){
            Toast.makeText(this,"Please enter your name as per gov. doc",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(age)){
            Toast.makeText(this,"Please enter your age",Toast.LENGTH_SHORT).show();
            return false;
        }


        if(TextUtils.isEmpty(customerData)){
            Toast.makeText(this,"Please select your customer",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (tripSpinner.getSelectedItem().toString().equalsIgnoreCase(selectText)){
            Toast.makeText(this, "Please select your trip", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(travelDate)){
            Toast.makeText(this,"Please select your travel date",Toast.LENGTH_SHORT).show();
            return false;
        }


        if(TextUtils.isEmpty(travelReturnDate)&& tripSpinner.getSelectedItem().toString().equalsIgnoreCase("Round trip")){
            Toast.makeText(this,"Please select your return date",Toast.LENGTH_SHORT).show();
            return false;
        }


        if (travelModeSpinner.getSelectedItem().toString().equalsIgnoreCase(selectText)){
            Toast.makeText(this, "Please select your travel mode", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (travelReasonSpinner.getSelectedItem().toString().equalsIgnoreCase(selectText)){
            Toast.makeText(this, "Please select your reason for travel", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (hotelRequiredSpinner.getSelectedItem().toString().equalsIgnoreCase(selectText)){
            Toast.makeText(this, "Please select hotel required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(hotelFrom)&& hotelRequiredSpinner.getSelectedItem().toString().equalsIgnoreCase("Yes")){
            Toast.makeText(this,"Please select your hotel from",Toast.LENGTH_SHORT).show();
            return false;
        }


        if(TextUtils.isEmpty(hotelTo)&& hotelRequiredSpinner.getSelectedItem().toString().equalsIgnoreCase("Yes")){
            Toast.makeText(this,"Please select your hotel to",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(fromLocation)){
            Toast.makeText(this,"Please enter your From location",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(toLocation)){
            Toast.makeText(this,"Please enter your To location",Toast.LENGTH_SHORT).show();
            return false;
        }


        if(travelTypeValue.equalsIgnoreCase("International")){
            if(TextUtils.isEmpty(passport)){
                Toast.makeText(this,"Please enter your passport number",Toast.LENGTH_SHORT).show();
                return false;
            }

            if(TextUtils.isEmpty(validity)){
                Toast.makeText(this,"Please enter your validity",Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true; // Validation passed
    }


    private void setupSpinner(Spinner spinner, String[] data,String spinnerType) {
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

                if(spinnerType.equalsIgnoreCase("hotelRequiredSpinner")){
                        hotetFromAndHotelToValidation(selectedItem,hotelFromEditText);
                        hotetFromAndHotelToValidation(selectedItem,hotelToEditText);
                }

                if(spinnerType.equalsIgnoreCase("typeOfEmployeeSpinner")){
                    associateValidation();
                }

                if(spinnerType.equalsIgnoreCase("tripSpinner")){
                    tripValueValidation();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here, but you can add logic if needed
            }
        });
    }

    private void tripValueValidation() {
       if(tripSpinner.getSelectedItem().toString().equalsIgnoreCase("Single trip")){
           travelReturnDateEditText.setFocusable(false);
           travelReturnDateEditText.setCursorVisible(false);
           travelReturnDateEditText.setAlpha(0.6f);
           travelReturnDateEditText.setText("");
           travelReturnDateEditText.setClickable(false);
           travelReturnDateEditText.setOnClickListener(null);
       }else{
           travelReturnDateEditText.setFocusable(true);
           travelReturnDateEditText.setCursorVisible(true);
           travelReturnDateEditText.setAlpha(1.0f);
           travelReturnDateEditText.setClickable(true);
           openDatePickerDialog(travelReturnDateEditText);
       }
    }


    private void validityTextValidation(String str){
        if(str.equalsIgnoreCase("Domestic")){
            validityEditText.setFocusable(false);
            validityEditText.setCursorVisible(false);
            validityEditText.setAlpha(0.6f);
            validityEditText.setText("");
            validityEditText.setClickable(false);
            validityEditText.setOnClickListener(null);
        }else{
            validityEditText.setFocusable(true);
            validityEditText.setCursorVisible(true);
            validityEditText.setAlpha(1.0f);
            validityEditText.setClickable(true);
            openDatePickerDialog(validityEditText);
        }

    }

    private void associateValidation() {
        if(typeOfEmployeeSpinner.getSelectedItem().toString().equalsIgnoreCase("Non-employee")){
            associateCodeEditText.setClickable(false);
            associateCodeEditText.setOnClickListener(null);
            associateCodeEditText.setAlpha(0.6f);
            associateCodeEditText.setText("");
        }else{
            associateCodeEditText.setClickable(true);
            associateCodeEditText.setAlpha(1.0f);
            associateCodeEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    travelRequestAddFormDataPresenter.fetchAssociateData();
                }
            });
        }
    }


    private void hotetFromAndHotelToValidation(String value,EditText editText) {
        if(value.equalsIgnoreCase("No")){
            editText.setFocusable(false);
            editText.setCursorVisible(false);
            editText.setAlpha(0.6f);
            editText.setClickable(false);
            editText.setOnClickListener(null);
            editText.setText("");
        }else{
            editText.setFocusable(true);
            editText.setCursorVisible(true);
            editText.setAlpha(1.0f);
            editText.setClickable(true);
            openDatePickerDialog(hotelFromEditText);
            openDatePickerDialog(hotelToEditText);
        }
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

    @Override
    public void getdata() {

    }

    @Override
    public void getProjectAndCustomerData(List<ProjectPojo> customerAndProjectList) {
        if(customerAndProjectList!=null){
            openCustomerSelectionDialog(customerAndProjectList);
        }
    }

    @Override
    public void getAssociateData(List<AssociateResponseDataModel> associateResponseDataModelList) {
        if(associateResponseDataModelList!=null){
            openAssociateSelectionDialog(associateResponseDataModelList);
        }
    }

    private void openAssociateSelectionDialog(List<AssociateResponseDataModel> associateResponseDataModelList) {
        try {
            final Dialog custDialog = new Dialog(this);
            custDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            custDialog.setContentView(R.layout.customer_details);
            custDialog.setCancelable(false);

            final SearchView mSearch = custDialog.findViewById(R.id.search_client);
            final ListView custList = custDialog.findViewById(R.id.customer_list);
            ImageView ivClose = custDialog.findViewById(R.id.dlg_close);
            final ImageView ivScrollUp = custDialog.findViewById(R.id.iv_scroll_up);

            TextView titleTextView=custDialog.findViewById(R.id.title_textview);
            titleTextView.setText("Select associate code");


            AssociateDetailAdapter mDet = new AssociateDetailAdapter(this, (ArrayList<AssociateResponseDataModel>) associateResponseDataModelList);
            custList.setAdapter(mDet);
            mDet.notifyDataSetChanged();

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    custDialog.dismiss();
                }
            });

            ivScrollUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    custList.smoothScrollToPosition(0);
                }
            });

            custList.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem < 10) {
                        ivScrollUp.setVisibility(View.INVISIBLE);
                    } else {
                        ivScrollUp.setVisibility(View.VISIBLE);
                    }
                }
            });

            custList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {

                    if (iLength != 0) {
                        associateCodeEditText.setText(AssociateDetailAdapter.filters.get(position).getAssoCode());
                        associateNameEditText.setText(AssociateDetailAdapter.filters.get(position).getName());
                    } else {
                        associateCodeEditText.setText(associateResponseDataModelList.get(position).getAssoCode());
                        associateNameEditText.setText(associateResponseDataModelList.get(position).getName());
                    }

                    iLength = 0;
                    custDialog.dismiss();
                }
            });
            mSearch.setQueryHint("Search associate code");
            mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String arg0) {
                    // TODO Auto-generated method stub
                    mSearch.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    iLength = query.length();
                    mDet.getFilter().filter(query);
                    return false;
                }
            });

            custDialog.show();
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            custDialog.getWindow().setLayout(width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openCustomerSelectionDialog(List<ProjectPojo> customerAndProjectList) {
            try {
                final Dialog custDialog = new Dialog(this);
                custDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                custDialog.setContentView(R.layout.customer_details);

                final SearchView mSearch = custDialog.findViewById(R.id.search_client);
                final ListView custList = custDialog.findViewById(R.id.customer_list);
                ImageView ivClose = custDialog.findViewById(R.id.dlg_close);
                final ImageView ivScrollUp = custDialog.findViewById(R.id.iv_scroll_up);


                final CustomerDetailsAdapter mDet = new CustomerDetailsAdapter(this, (ArrayList<ProjectPojo>) customerAndProjectList);
                custList.setAdapter(mDet);
                mDet.notifyDataSetChanged();

                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        custDialog.dismiss();
                    }
                });

                ivScrollUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        custList.smoothScrollToPosition(0);
                    }
                });

                custList.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (firstVisibleItem < 10) {
                            ivScrollUp.setVisibility(View.INVISIBLE);
                        } else {
                            ivScrollUp.setVisibility(View.VISIBLE);
                        }
                    }
                });

                custList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                        if (iLength != 0) {
                            customerDataEditText.setText(CustomerDetailsAdapter.filters.get(position).getCUSTOMER_NAME());
                        } else {
                            customerDataEditText.setText(customerAndProjectList.get(position).getCUSTOMER_NAME());
                        }

                        iLength = 0;
                        custDialog.dismiss();
                    }
                });
                mSearch.setQueryHint("Search customer");
                mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String arg0) {
                        // TODO Auto-generated method stub
                        mSearch.clearFocus();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String query) {
                        // TODO Auto-generated method stub
                        iLength = query.length();
                        mDet.getFilter().filter(query);
                        return false;
                    }
                });

                custDialog.show();
                int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
                custDialog.getWindow().setLayout(width, height);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    @Override
    public void errorMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }


}