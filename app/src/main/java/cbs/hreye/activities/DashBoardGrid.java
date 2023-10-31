package cbs.hreye.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cbs.hreye.adapters.CustomGrid;
import cbs.hreye.utilities.CommonMethods;
import cbs.hreye.utilities.ConsURL;
import cbs.hreye.utilities.LogMsg;
import cbs.hreye.utilities.PrefrenceKey;
import cbs.hreye.R;

public class DashBoardGrid extends Activity {

    Context context;
    TextView marque;
    Button btnLog;
    String JSON_URL, error, success, message;
    String JSON_VER, VersionId;
    ProgressDialog type;

    GridView grid;
    String[] name = {
            "My Profile",
            "Daily Activity",
            "Leave",
            "Leaves",
            "Leave Status",
            "Holiday List",
            "Birthday",
            "New Joining",
            "Attendance"
    } ;

    int[] imageId = {
            R.mipmap.profile,
            R.mipmap.activityy,
            R.mipmap.leave,
            R.mipmap.calendar,
            R.mipmap.status,
            R.mipmap.holi,
            R.mipmap.birthday,
            R.mipmap.joining,
            R.mipmap.attendence
    };

    private static final int PERMISSION_REQUEST_CODE = 1;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        context = DashBoardGrid.this;
        getID();
    }

    private void getID() {

        if (CommonMethods.isOnline(context)) {
            JSON_VER = ConsURL.baseURL(context) + "HRLoginService/LoginService.svc/GetVersion";
            LogMsg.d("Load_Version", JSON_VER);
            loadVerService();
        } else {
            Toast.makeText(context, R.string.net, Toast.LENGTH_SHORT).show();
        }

        marque = this.findViewById(R.id.marque_text);
        marque.setText("Welcome " + CommonMethods.getPrefsData(DashBoardGrid.this, PrefrenceKey.USER_NAME, "") + " into HR Eye Activity Portal");
        marque.setSelected(true);
        TextView txtName = this.findViewById(R.id.ass_name);
        txtName.setText(CommonMethods.getPrefsData(DashBoardGrid.this, PrefrenceKey.USER_NAME, ""));

        btnLog = findViewById(R.id.logout);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog cDialog = new Dialog(DashBoardGrid.this);
                cDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                cDialog.setCancelable(false);
                cDialog.setContentView(R.layout.logout);

                TextView txtMsg = cDialog.findViewById(R.id.dlg_msg);
                txtMsg.setText("Sure to Logout ?");
                Button cancel = cDialog.findViewById(R.id.cancel);
                cancel.setText("No");
                Button yes = cDialog.findViewById(R.id.yes);
                yes.setText("Yes");
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cDialog.dismiss();
                    }
                });
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cDialog.dismiss();
                        try {
                            CommonMethods.delPrefsData(DashBoardGrid.this);
                            Intent d = new Intent(DashBoardGrid.this, Login.class);
                            startActivity(d);
                        } catch (Exception e) {
                        }
                    }
                });
                cDialog.show();
            }
        });

        TextView bday_count = findViewById(R.id.tv_bday_count);
       // bday_count.setText(String.valueOf(Splash.countBday));

        TextView txtJoin = this.findViewById(R.id.tv_join_count);
        //txtJoin.setText(String.valueOf(Splash.countJoin));

        CustomGrid adapter = new CustomGrid(DashBoardGrid.this, name, imageId);
        grid= findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(DashBoardGrid.this, "You Clicked at " + name[+position], Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMsgService() {

        type = new ProgressDialog(context);
        type.setMessage("Please wait");
        type.show();
        type.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        type.cancel();
                        //hiding the progressbar after completion
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject UserDashBoardNewsDetailResult = jsono.getJSONObject("UserDashBoardNewsDetailResult");
                            JSONObject GetUserDashBoardNewsMsgMessage = UserDashBoardNewsDetailResult.getJSONObject("GetUserDashBoardNewsMsgMessage");
                            error = GetUserDashBoardNewsMsgMessage.optString("ErrorMsg");
                            success = GetUserDashBoardNewsMsgMessage.optString("Success");

                            if (success.equals("true")) {
                                JSONArray LSDetails = UserDashBoardNewsDetailResult.getJSONArray("LSDetails");

                                for (int i = 0; i < LSDetails.length(); i++) {
                                    message = LSDetails.getJSONObject(i).getString("NEWS_CONTENT");
                                }
                            } else {
                                Toast.makeText(DashBoardGrid.this, error, Toast.LENGTH_SHORT).show();
                            }
                            final Dialog mDialog = new Dialog(DashBoardGrid.this);
                            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            mDialog.setCancelable(false);
                            mDialog.setContentView(R.layout.hr_message);

                            TextView txtMsg = mDialog.findViewById(R.id.hr_msg);
                            txtMsg.setText(message);
                            Button okay = mDialog.findViewById(R.id.btn_ok);
                            okay.setText("OK");
                            okay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                }
                            });
                            mDialog.show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadVerService() {

        type = new ProgressDialog(context);
        type.setMessage("Please wait...");
        type.show();
        type.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_VER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        type.cancel();
                        //hiding the progressbar after completion
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONObject GetVersionResult = jsono.getJSONObject("GetVersionResult");
                            JSONObject VersionMessage = GetVersionResult.getJSONObject("VersionMessage");
                            error = VersionMessage.optString("ErrorMsg");
                            success = VersionMessage.optString("Success");

                            if (success.equals("true")) {
                                JSONArray VersionDetails = GetVersionResult.getJSONArray("VersionDetails");
                                for (int i = 0; i < VersionDetails.length(); i++) {
                                    VersionId = VersionDetails.getJSONObject(i).getString("VersionId");
                                }
                            } else {
                                Toast.makeText(DashBoardGrid.this, error, Toast.LENGTH_SHORT).show();
                            }

                            try {
                                if (!VersionId.equals("19")) {
                                    final Dialog dialog = new Dialog(context);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(false);
                                    dialog.setContentView(R.layout.time);

                                    TextView b_msg = dialog.findViewById(R.id.b_msg);
                                    //b_msg.setText("Old version has been obsolete" + "\n" + "Please update your app version" + "\n" + "To get discount 11:30 AM - 12:00 PM");
                                    b_msg.setText("Old version has been obsolete" + "\n" + "Please update your app");
                                    Button cancel = dialog.findViewById(R.id.tcancel);
                                    cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finish();
                                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                            try {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                            } catch (android.content.ActivityNotFoundException anfe) {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                            }
                                        }
                                    });
                                    dialog.show();
                                }
                            } catch (Exception e) {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Enable your GPS for punching Attendance?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream("/sdcard/Holiday_List.pdf");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
                mProgressDialog.dismiss();
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (Exception e) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else {
                final Dialog cDialog = new Dialog(DashBoardGrid.this);
                cDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                cDialog.setCancelable(false);
                cDialog.setContentView(R.layout.logout);

                TextView txtMsg = cDialog.findViewById(R.id.dlg_msg);
                txtMsg.setText("Want to Open pdf file?");
                Button cancel = cDialog.findViewById(R.id.cancel);
                cancel.setText("No");
                Button yes = cDialog.findViewById(R.id.yes);
                yes.setText("Yes");
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cDialog.dismiss();
                    }
                });
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cDialog.dismiss();
                        try {
                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Holiday_List.pdf");
                            Intent target = new Intent(Intent.ACTION_VIEW);
                            target.setDataAndType(Uri.fromFile(file), "application/pdf");
                            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                            Intent intent = Intent.createChooser(target, "Open File");
                            try {
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                // Instruct the user to install a PDF reader here, or something
                            }
                        } catch (Exception e) {
                        }
                    }
                });
                cDialog.show();
            }
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(DashBoardGrid.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(DashBoardGrid.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(DashBoardGrid.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(DashBoardGrid.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LogMsg.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    LogMsg.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
}