package cbs.hreye.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import cbs.hreye.R;
import cbs.hreye.databinding.ActivityProfileBinding;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;


public class Profile extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_KITKAT_GALLERY = 2;
    private static final int REQUEST_CAMERA = 3;
    private static final int REQUEST_READ_CAMERA = 4;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 5;
    private static final int PIC_CROP = 6;
    Context context;
    Bitmap bitmap;
    String encodeBitmap;
    String imagePath;
    BottomSheetDialog mBottomSheetDialog;
    String defaultValue = "Not Available";
    String assCode, userName, userEmail, userMob, repPerson;
    ActivityProfileBinding activityProfileBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        context = Profile.this;
        setClickListener();
    }

    private void setClickListener() {
        activityProfileBinding.headerBack.llGrantReject.setVisibility(View.VISIBLE);
        activityProfileBinding.headerBack.headerGrant.setVisibility(View.GONE);
        activityProfileBinding.headerBack.headerReject.setImageResource(R.drawable.setting);

        encodeBitmap = CommonMethods.getPrefsData(context, PrefrenceKey.PROFILE_BITMAP, "");
        if (!encodeBitmap.equals("")) {
            activityProfileBinding.ivProfile.setImageBitmap(CommonMethods.Base64StringToBitMap(encodeBitmap));
        } else {
            activityProfileBinding.ivProfile.setImageResource(R.drawable.profile);
        }

        activityProfileBinding.headerBack.headerText.setText(R.string.my_profile);
        assCode = CommonMethods.getPrefsData(context, PrefrenceKey.ASS_CODE, "");
        userName = CommonMethods.getPrefsData(context, PrefrenceKey.USER_NAME, "");
        userEmail = CommonMethods.getPrefsData(context, PrefrenceKey.USER_EMAIL, "");
        userMob = CommonMethods.getPrefsData(context, PrefrenceKey.USER_MOB, "");
        repPerson = CommonMethods.getPrefsData(context, PrefrenceKey.REP_PERSON, "");

        if (!assCode.equals("")) {
            activityProfileBinding.assCode.setText(CommonMethods.getPrefsData(context, PrefrenceKey.ASS_CODE, ""));
        } else {
            activityProfileBinding.assCode.setText(defaultValue);
        }

        if (!userName.equals("")) {
            activityProfileBinding.assoName.setText(CommonMethods.getPrefsData(context, PrefrenceKey.USER_NAME, ""));
        } else {
            activityProfileBinding.assoName.setText(defaultValue);
        }

        if (!userEmail.equals("")) {
            activityProfileBinding.email.setText(CommonMethods.getPrefsData(context, PrefrenceKey.USER_EMAIL, ""));
        } else {
            activityProfileBinding.email.setText(defaultValue);
        }

        if (!userMob.equals("")) {
            activityProfileBinding.mobile.setText(CommonMethods.getPrefsData(context, PrefrenceKey.USER_MOB, ""));
        } else {
            activityProfileBinding.mobile.setText(defaultValue);
        }

        if (!repPerson.equals("")) {
            activityProfileBinding.rePerson.setText(CommonMethods.getPrefsData(context, PrefrenceKey.REP_PERSON, ""));
        } else {
            activityProfileBinding.rePerson.setText(defaultValue);
        }

        activityProfileBinding.headerBack.headerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        activityProfileBinding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDailog();
            }
        });

        activityProfileBinding.headerBack.headerReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuItem();
            }
        });

    }

    private void menuItem(){
        PopupMenu popup = new PopupMenu(context, activityProfileBinding.headerBack.headerReject);
        /*  The below code in try catch is responsible to display icons*/
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popup.getMenuInflater().inflate(R.menu.setting_menu , popup.getMenu());
 /*       String companyName = "cogni";
        if (companyName.equalsIgnoreCase("lahag")) {
           popup.getMenu().findItem(R.id.changePass).setVisible(false);
        }*/
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.changePass:
                        Intent intent = new Intent(context, ChangePassword.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        return true;
                    case R.id.logout:
                        setLogout();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void setLogout(){
        final Dialog cDialog = new Dialog(context);
        cDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cDialog.setCancelable(true);
        cDialog.setContentView(R.layout.logout);
        TextView txtMsg = cDialog.findViewById(R.id.dlg_msg);
        txtMsg.setText(R.string.are_you_sure_logout);
        Button noBtn = cDialog.findViewById(R.id.cancel);
        Button yesBtn = cDialog.findViewById(R.id.yes);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cDialog.dismiss();
                CommonMethods.delPrefsData(context);
                Intent intent = new Intent(context, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cDialog.dismiss();
            }
        });
        cDialog.show();
    }
    @SuppressLint("InflateParams")
    private void bottomSheetDailog() {
        mBottomSheetDialog = new BottomSheetDialog(context);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
        mBottomSheetDialog.setContentView(sheetView);


        LinearLayout llCamera = sheetView.findViewById(R.id.ll_camera);
        LinearLayout llGallery = sheetView.findViewById(R.id.ll_gallery);
        LinearLayout llRemove = sheetView.findViewById(R.id.ll_remove);

        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonMethods.checkPermission(Manifest.permission.CAMERA, context)) {
                    getCamera();
                } else {
                    CommonMethods.askForPermission(Manifest.permission.CAMERA, REQUEST_READ_CAMERA, Profile.this);
                }
            }
        });
        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonMethods.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, context)) {
                    getGallery();
                } else {
                    CommonMethods.askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_READ_EXTERNAL_STORAGE, Profile.this);
                }
            }
        });
        llRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityProfileBinding.ivProfile.setImageResource(R.drawable.profile);
                CommonMethods.setPrefsData(context, PrefrenceKey.PROFILE_BITMAP, "");
                mBottomSheetDialog.dismiss();
            }
        });
        mBottomSheetDialog.show();
    }



    public void getGallery() {
        mBottomSheetDialog.dismiss();
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image*//*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_KITKAT_GALLERY);
        }

    }

    public void getCamera() {
        mBottomSheetDialog.dismiss();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImageUri = null;
        switch (requestCode) {
            case REQUEST_GALLERY:
                if (data == null || data.getData() == null)
                    return;
                // performCrop(selectedImageUri);
                imagePath = getRealPathFromURI(selectedImageUri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                // options.inJustDecodeBounds = true;
                options.inScaled = false;
                options.inDither = false;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                bitmap = BitmapFactory.decodeFile(imagePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
               // byte[] imageBytes = baos.toByteArray();
                bitmap = CommonMethods.modifyOrientation(bitmap, imagePath);
                activityProfileBinding.ivProfile.setImageBitmap(bitmap);
              //  updateProfileAPI(CommonMethods.bitMapToBase64String(bitmap));
               CommonMethods.setPrefsData(context, PrefrenceKey.PROFILE_BITMAP, CommonMethods.bitMapToBase64String(bitmap));
                break;
            case REQUEST_KITKAT_GALLERY:
                if (data == null || data.getData() == null)
                    return;
                selectedImageUri = data.getData();
                // performCrop(selectedImageUri);
                imagePath = getRealPathFromURI(selectedImageUri);
                BitmapFactory.Options options1 = new BitmapFactory.Options();
                options1.inSampleSize = 2;
                // options.inJustDecodeBounds = true;
                options1.inScaled = false;
                options1.inDither = false;
                options1.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bitmap = BitmapFactory.decodeFile(imagePath);

                ByteArrayOutputStream byteArrayOutputStreamaos = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamaos);
               // byte[] imageBytes1 = byteArrayOutputStreamaos.toByteArray();
                bitmap = CommonMethods.modifyOrientation(bitmap, imagePath);
                activityProfileBinding.ivProfile.setImageBitmap(bitmap);
                //updateProfileAPI(CommonMethods.bitMapToBase64String(bitmap));
               CommonMethods.setPrefsData(context, PrefrenceKey.PROFILE_BITMAP, CommonMethods.bitMapToBase64String(bitmap));
                break;
            case REQUEST_CAMERA:
                try {
                    if (resultCode == Activity.RESULT_CANCELED) {
                        return;
                    }
                    bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos2);
                  // byte[] imageBytes2 = baos2.toByteArray();
                    activityProfileBinding.ivProfile.setImageBitmap(bitmap);
                  //  updateProfileAPI(CommonMethods.bitMapToBase64String(bitmap));
                   CommonMethods.setPrefsData(context, PrefrenceKey.PROFILE_BITMAP, CommonMethods.bitMapToBase64String(bitmap));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case PIC_CROP:
                if (data != null) {
                    // get the returned data
                    Bundle extras = data.getExtras();
                    // get the cropped bitmap
                    Bitmap selectedBitmap = extras.getParcelable("data");

                    activityProfileBinding.ivProfile.setImageBitmap(selectedBitmap);
                    //updateProfileAPI(CommonMethods.bitMapToBase64String(bitmap));
                    CommonMethods.setPrefsData(context, PrefrenceKey.PROFILE_BITMAP, CommonMethods.bitMapToBase64String(bitmap));
                }
                break;
            default:
                break;
        }
    }



    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case 5:
                    getGallery();
                    break;
                case 4:
                    getCamera();
                    break;
            }
        }
    }


    private void updateProfileAPI(final String base64Image){
        activityProfileBinding.pbProfile.setVisibility(View.VISIBLE);
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("COMPANY_NO", CommonMethods.getPrefsData(context, PrefrenceKey.COMPANY_NO, ""));
            jsonObject.put("LOCATION_NO", CommonMethods.getPrefsData(context, PrefrenceKey.LOCATION_NO, ""));
            jsonObject.put("ASSO_CODE", CommonMethods.getPrefsData(context, PrefrenceKey.ASS_CODE, ""));
            jsonObject.put("PROFILE_PIC", base64Image);
            final String mRequestBody = jsonObject.toString();
            LogMsg.i("JSON_PROFILE", mRequestBody);
            String URL = ConsURL.baseURL(context)+"GetLeaveCalendar/Service1.svc/SetProfilePicData";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            LogMsg.i("Response", response);
                            activityProfileBinding.pbProfile.setVisibility(View.GONE);
                            try {
                                JSONObject joResponse = new JSONObject(response);
                                JSONObject Messsage = joResponse.getJSONObject("Messsage");
                                boolean Success = Messsage.optBoolean("Success");
                                String Error = Messsage.optString("ErrorMsg");
                                if (Success) {
                                    CommonMethods.setPrefsData(context, PrefrenceKey.PROFILE_BITMAP, base64Image);
                                }
                                Toast.makeText(context, Error, Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    activityProfileBinding.pbProfile.setVisibility(View.GONE);
                    Toast.makeText(context, CommonMethods.volleyError(error), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "Content-Type/text/plain; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    return mRequestBody == null ? null : mRequestBody.getBytes(StandardCharsets.UTF_8);
                }


                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String.valueOf(response.statusCode);
                    return super.parseNetworkResponse(response);
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,-1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

/* this is in app rating from app.
 private void rateApp(){
        ReviewManager manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();
                //Toast.makeText(context, ""+reviewInfo, Toast.LENGTH_LONG).show();
                Task<Void> flow = manager.launchReviewFlow(this, reviewInfo);
                flow.addOnCompleteListener(task1 -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                    Toast.makeText(context, ""+task1, Toast.LENGTH_LONG).show();
                });
            } else {
                // There was some problem, continue regardless of the result.
                Toast.makeText(context, "Heloo", Toast.LENGTH_LONG).show();
            }
        });

    }*/



}
