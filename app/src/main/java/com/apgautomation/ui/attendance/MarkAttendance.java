package com.apgautomation.ui.attendance;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apgautomation.CameraActivity;
import com.apgautomation.HomePage;
import com.apgautomation.MainActivityLauncher;
import com.apgautomation.R;
import com.apgautomation.controller.AttendanceController;
import com.apgautomation.model.AttendanceModel;
import com.apgautomation.model.AttendanceStatusBean;
import com.apgautomation.utility.BitmapUtilities;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.ConnectServer;
import com.apgautomation.utility.serverutility.ResponseBody;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.otaliastudios.cameraview.BitmapCallback;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class MarkAttendance extends AppCompatActivity {


    //FusedLocationProviderClient

    public int isSecondTime = 0;
    private FusedLocationProviderClient fusedLocationClient;


    @BindView(R.id.btnSelect)
    Button btnSelect;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @BindView(R.id.imgStart)
    ImageView imgStart;
    @BindView(R.id.imgEnd)
    ImageView imgEnd;

    @BindView(R.id.txtStartTime)
    TextView txtStartTime;
    @BindView(R.id.txtEndTime)
    TextView txtEndTime;
    @BindView(R.id.txtCurrentStatus)
    TextView txtCurrentStatus;

    @BindView(R.id.edStartKm)
    EditText edStartKm;
    @BindView(R.id.edEndKm)
    EditText edEndKm;

    @BindView(R.id.chk)
    CheckBox chk;

    @BindView(R.id.linearStart)
    LinearLayout linearStart;
    @BindView(R.id.linearEnd)
    LinearLayout linearEnd;

    @BindView(R.id.txtStartLocationStatus)
    TextView txtStartLocationStatus;
    @BindView(R.id.txtEndLocationStatus)
    TextView txtEndLocationStatus;

    @BindView(R.id.btnStartLocation)
    Button btnStartLocation;
    @BindView(R.id.btnEndLocation)
    Button btnEndLocation;
    @BindView(R.id.btnStart)
    Button btnStart;

    @BindView(R.id.btnSaveStartLocation)
    Button btnSaveStartLocation;
    @BindView((R.id.btnSaveEndLocation))
    Button btnSaveEndLocation;

    boolean IsLastDay = false;
    AttendanceController controller;
    AttendanceModel model;
    String locationStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Mark Attendance");
        ButterKnife.bind(this);
        //LocationServices
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean enabled1 = service
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        try {
            IsLastDay = getIntent().getExtras().getBoolean("IsLastDay");
        } catch (Exception ex) {
        }

        //  Settings.ACTION_LOCATION_SOURCE_SETTINGS
        if (enabled || enabled1) {

        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Location Off");
            alert.setMessage("Dou you want to on location");
            alert.setPositiveButton("Open Location Setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);

                }
            });
            alert.show();

            //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            // startActivity(intent);
        }


        initView();
        callLocationRequest();

        CommonShare.hideatInItInputBoard(this);

        btnStartLocation.setVisibility(View.INVISIBLE);
        btnEndLocation.setVisibility(View.INVISIBLE);


        txtStartTime.setVisibility(View.INVISIBLE);
        txtEndTime.setVisibility(View.INVISIBLE);

        viewTime();
    }

    void viewTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = CommonShare.url + "Service1.svc/getAttendanceModelByToken?Token=" + model.Token;
                    ResponseBody rs = new ConnectServer().getData(url);
                    JSONArray j = new JSONArray(rs.getResponseString());
                    final long sTime = CommonShare.parseDate(j.getJSONObject(0).getString("StartTime"));
                    final long eTime = CommonShare.parseDate(j.getJSONObject(0).getString("EndTime"));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showStartTime(sTime, eTime);
                        }
                    });
                } catch (Exception w) {
                }
            }
        }).start();
    }

    void showStartTime(long sTime, long dTime) {
        if (sTime > 863640000) {
            txtStartTime.setText(CommonShare.getDateTime(sTime));
            txtStartTime.setVisibility(View.VISIBLE);
        }
        if (dTime > 863640000) {
            txtEndTime.setText(CommonShare.getDateTime(dTime));
            txtEndTime.setVisibility(View.VISIBLE);
        }
    }

    ArrayList<AttendanceStatusBean> statusList = new ArrayList<>();

    void initView() {
        btnSubmit.setVisibility(View.INVISIBLE);
        chk.setVisibility(View.GONE);
        controller = new AttendanceController(this);
        statusList = CommonShare.getAttendanceStatusList(this);
        if (!IsLastDay) {
            model = controller.getTodaysAttendanceToken();
        } else {
            model = controller.getLastDayAttendanceToken();
        }

        if (model == null) {
            model = controller.generateTodaysAttendanceModel();
            //model.CurrentStatus="Started";
            model.AttendanceDate = System.currentTimeMillis();
            // CommonShare.getForwordDay(  System.currentTimeMillis() ,-1);
            model.EmpId = CommonShare.getEmpId(this);
            model.DeleteStatus = false;
            btnSelect.setTag(statusList.get(0).StatusId);

        } else {
            isSecondTime = 1;
            initModelView();
        }


    }

    @OnClick(R.id.btnStart)
    void dayStart() {
        if (btnSelect.getText().toString().equalsIgnoreCase("SELECT")) {
            Toast.makeText(this, "Select Attendance Status", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isSecondTime == 0 && model.CurrentStatus == null) {
            model.IsTaApplicable = false;
            model.CurrentStatus = "Started";
            model.AttendanceDate = System.currentTimeMillis();
            // CommonShare.getForwordDay(  System.currentTimeMillis() ,-1);
            controller.saveLocalAttendanceModel(model);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Attendance Started");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (!CommonShare.getRole(MarkAttendance.this).equalsIgnoreCase("Back Office")) {
                        chk.setChecked(true);
                    }
                    initView();
                    btnSubmit.setVisibility(View.INVISIBLE);
                }
            });
            alert.setCancelable(false);
            alert.show();
        }
    }

    @OnClick(R.id.btnSaveStartLocation)
    void saveStartLocation() {
        model.IsTaApplicable = true;
        if (model.LocalStartPhotoPath == null || model.LocalStartPhotoPath.equalsIgnoreCase("") || model.LocalStartPhotoPath.equalsIgnoreCase("null")) {
            Toast.makeText(this, "Take photo of start kilometers first", Toast.LENGTH_LONG).show();

            return;
        }
        if (edStartKm.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Enter Start KM", Toast.LENGTH_LONG).show();
            return;
        }
        if (locationStr == null || locationStr.equalsIgnoreCase("")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Location off");
            alert.setMessage("Unable to find location,Please check location setting");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);

                }
            });
            alert.show();
            callLocationRequest();
            return;
        }


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Save Start Kilometers");
        alert.setMessage("Once start kilometers filled ,it can not be changed .Do you want to save start kilometers? ");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                processStartLocation();

            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();

    }

    @OnClick(R.id.btnSaveEndLocation)
    void saveEndLocation() {
        if (edEndKm.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Enter End KM", Toast.LENGTH_LONG).show();
            return;
        }
        if (model.LocalEndPhotoPath == null || model.LocalEndPhotoPath.equalsIgnoreCase("") || model.LocalEndPhotoPath.equalsIgnoreCase("null")) {
            Toast.makeText(this, "Take photo of end kilometers first", Toast.LENGTH_LONG).show();

            return;
        }
        if (locationStr == null || locationStr.equalsIgnoreCase("")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Location off");
            alert.setMessage("Unable to find location,Please check location setting");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);

                }
            });
            alert.show();
            callLocationRequest();
            return;
        }
        int endKM = Integer.parseInt(edEndKm.getText().toString());
        if (endKM - model.StartKm > 500) {
            //Toast.makeText(this,"End Km must greater than Start Km",Toast.LENGTH_LONG).show();
            CommonShare.alert(this, "Start end Killometer difference is more than 500 km ,Killometer is rongly entered");
            return;
        }
        if (endKM < model.StartKm) {
            Toast.makeText(this, "End Km must greater than Start Km", Toast.LENGTH_LONG).show();
            return;
        }

        model.EndKm = endKM;
        model.EndLocation = locationStr;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Save End Kilometers");
        alert.setMessage("Once end kilometers filled ,it  can not be changed .Do you want to save end kilometers? ");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                processEndLocation();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }

    void callLocationRequest() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.

                        if (location == null) {
                            //
                            try {
                                Intent i = new Intent();
                                i.setClass(getApplicationContext(), HomePage.class);
                                i.setAction("ConnectGoogle");
                                ((Activity) MarkAttendance.this).sendBroadcast(i);
                            } catch (Exception ex) {
                            }
                        }

                        if (location != null) {
                            // Logic to handle location object
                            //   Toast.makeText(MarkAttendance.this,location.toString(),Toast.LENGTH_LONG).show();
                            // Toast.makeText(MarkAttendance.this,location.toString(),Toast.LENGTH_LONG).show();
                            locationStr = location.getLatitude() + "," + location.getLongitude();

                            if (model.StartLocation == null || model.StartLocation.length() < 5) {
                                if (model.StartLocation != null && model.StartLocation.length() > 5) {
                                    //  btnStartLocation.setVisibility(View.INVISIBLE);
                                    txtStartLocationStatus.setText("Found");
                                } else if (model.CurrentStatus == null || model.CurrentStatus.equalsIgnoreCase("Started")) {
                                    //  btnStartLocation.setVisibility(View.INVISIBLE);
                                    txtStartLocationStatus.setText("Found");
                                }
                            }

                            if (model.EndLocation == null || model.EndLocation.length() < 5) {
                                if (model.EndLocation != null && model.EndLocation.length() > 5) {
                                    txtEndLocationStatus.setText("Found");
                                } else {
                                    txtEndLocationStatus.setText("Found");
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(MarkAttendance.this, e.toString(), Toast.LENGTH_LONG).show();

            }
        });

    }
    boolean isTaUser()
    {
        return  false;
    }
    void initModelView()
    {
        chk.setVisibility(View.VISIBLE);
        btnStart.setVisibility(View.GONE);

        if(model.CurrentStatus==null)
        {
            model.CurrentStatus="";
        }
        if(model.CurrentStatus.equalsIgnoreCase("Started"))
        {
            txtCurrentStatus.setText("Day Started");
        }
        if(model.CurrentStatus.equalsIgnoreCase("Closed"))
        {
            txtCurrentStatus.setText("Day Closed");
        }
        btnSelect.setText(CommonShare.getSelectedStatusTextById(model.StatusId,this));
        btnSelect.setTag(model.StatusId);
        if(model.IsTaApplicable)
        {
            if(model.StartTime>0)
            {
                chk.setChecked(true);
                chk.setEnabled(false);
                linearStart.setVisibility(View.VISIBLE);
                txtStartTime.setText(CommonShare.getDateTime(model.StartTime));
                edStartKm.setText(model.StartKm+"");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(model.LocalStartPhotoPath, options);
                imgStart.setImageBitmap(bitmap);

                linearEnd.setVisibility(View.VISIBLE);
                edStartKm.setEnabled(false);
                imgStart.setEnabled(false);
                btnSaveStartLocation.setVisibility(View.GONE);

                if(model.StartLocation!=null && model.StartLocation.length()>5)
                {
                    txtStartLocationStatus.setText("Found");
                }
            }
            if(model.EndTime>0)
            {
                txtEndTime.setText(CommonShare.getDateTime(model.EndTime));
                edEndKm.setText(model.EndKm+"");

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(model.LocalEndPhotoPath, options);
                imgEnd.setImageBitmap(bitmap);

                if(!model.CurrentStatus.equalsIgnoreCase("Closed"))
                {
                    btnSubmit.setText("Close the day");
                }
                edEndKm.setEnabled(false);
                imgEnd.setEnabled(false);
                btnSaveEndLocation.setVisibility(View.GONE);
                if(!model.CurrentStatus.equalsIgnoreCase("Closed"))
                {
                    btnSubmit.setVisibility(View.VISIBLE);
                    btnSubmit.setText("Close the day");
                }
                if(model.EndLocation!=null && model.EndLocation.length()>5)
                {
                    txtEndLocationStatus.setText("Found");
                }
            }
            if(model.CurrentStatus.equalsIgnoreCase("Closed"))
            {
                 btnSubmit.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            if(!model.CurrentStatus.equalsIgnoreCase("Closed"))
            {
                btnSubmit.setVisibility(View.VISIBLE);
                btnSubmit.setText("Close the day");
            }
        }
        if(model.CurrentStatus.equalsIgnoreCase("Closed"))
        {
               btnSubmit.setVisibility(View.GONE);
               chk.setEnabled(false);
               btnSelect.setEnabled(false);
        }
        try
        {
           if(CommonShare.convertToDate(CommonShare.getForwordDay(  System.currentTimeMillis(),-1)).equalsIgnoreCase(CommonShare.convertToDate(model.AttendanceDate)))
           {
               chk.setEnabled(false);
           }
        }
        catch (Exception ex)
        {}
   }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnSelect)
    void statusClick()
    {
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("--Select--");

        alert.setAdapter(new ArrayAdapter<AttendanceStatusBean>(this, android.R.layout.simple_list_item_1, statusList), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                btnSelect.setTag(statusList.get(i).StatusId);
                btnSelect.setText(statusList.get(i).toString());
                model.StatusId=statusList.get(i).StatusId;

            }
        });
        alert.show();
    }
    @OnClick(R.id.imgStart)
    void click() {

        if(CommonShare.getMyEmployeeModel(this).IsOldCamera) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            File file = new File(getAccessPath() + "/img.jpg");
            //Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(intent, 1);
        }
        else {
            startActivityForResult(new Intent(this, CameraActivity.class), 701);
        }
    }
    String getAccessPath()
    {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        Log.d("ROOT", root);
        File myDir = new File(root +"/"+ getString(R.string.app_name).replace(" ",""));
        myDir.mkdirs();

       return    myDir.getAbsoluteFile().toString();
    }

    @OnClick(R.id.imgEnd)
    void click1() {

        if(CommonShare.getMyEmployeeModel(this).IsOldCamera)
        {

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File file = new File(getAccessPath()+ "/img.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, 2);
        }
        else {
            startActivityForResult(new Intent(this, CameraActivity.class), 702);
        }
    }

    @OnCheckedChanged(R.id.chk)
    void checkedChanged()
    {
        if(chk.isChecked())
        {
            linearStart.setVisibility(View.VISIBLE);
            //linearEnd.setVisibility(View.VISIBLE);
        }
        else
        {
            model.StartKm=0;
            model.StartTime=0;
            model.LocalStartPhotoPath="";
            model.IsTaApplicable=false;
            imgStart.setImageResource(android.R.drawable.ic_menu_camera);
        }

        if(chk.isChecked())
        {

            if(model.EndKm==0)
            {
                btnSubmit.setVisibility(View.INVISIBLE);
            }
        }

        if(!chk.isChecked())
        {
              linearStart.setVisibility(View.INVISIBLE);
            if(model.CurrentStatus.equalsIgnoreCase("started") && model.StartKm==0)
            {
                btnSubmit.setVisibility(View.VISIBLE);
            }
        }

    }

    @OnClick(R.id.btnSubmit)
    void Submit()
    {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Do you want to Close the day ?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                model.CurrentStatus="Closed";
                model.CloseTime=System.currentTimeMillis();
                controller.saveLocalAttendanceModel(model);
                Toast.makeText(MarkAttendance.this,"Attendance Closed ",Toast.LENGTH_LONG).show();
                finish();
            }
        });
        alert.setNegativeButton("No",null);
       // alert.setCancelable(false);
        alert.show();

/*
        if(chk.isChecked())
        {
            if(model. CurrentStatus.equalsIgnoreCase( "Started") && model.StartKm>0 && model.StartTime>0 )
            {


            }
            else if(model. CurrentStatus.equalsIgnoreCase( "Started"))
            {

            }
        }
        else {

            if (isSecondTime == 1)
            {
                model.IsTaApplicable = false;
                model.CurrentStatus="Closed";
                controller.saveLocalAttendanceModel(model);
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Attendance Closed");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                alert.setCancelable(false);
                alert.show();
            }
        }
*/

    }


    @OnClick(R.id.btnStartLocation)
    void viewStartLocation()
    {
        if(model.StartLocation!=null &&  model.StartLocation.length()>4)
           CommonShare.openMapWithGeo(this,"Start Location",model.StartLocation);
    }
    @OnClick(R.id.btnEndLocation)
    void viewEndLocation()
    {

        if(model.EndLocation!=null &&  model.EndLocation.length()>4)
        CommonShare.openMapWithGeo(this,"Start Location",model.EndLocation);
    }

    private void processEndLocation()
    {
        model.EndTime=System.currentTimeMillis();
        controller.saveLocalAttendanceModel(model);

       /*
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setMessage("Location Closed");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.setCancelable(false);
        alert.show();
        */
        Toast.makeText(this,"Location Closed",Toast.LENGTH_LONG).show();

        initView();
    }

    private void processStartLocation()
    {

        try
        {
            model.StartKm = Integer.parseInt(edStartKm.getText().toString());
            model.StartLocation=locationStr;
            model.StartTime=System.currentTimeMillis();
            model.IsTaApplicable=true;

        }
        catch (Exception ex)
        {}
        controller.saveLocalAttendanceModel(model);
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setMessage("Kilometer  Started");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alert.show();
        initView();
    }

    int locationCnt=0;
    String path;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            File file = new File(getAccessPath() + File.separator + "img.jpg");
            path = file.getAbsolutePath();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            Log.e("Width height", width + "   " + height);

            float constant = ((float) (600 / (float) width));
            width = 600;
            Log.e("Contatnt", constant + "");
            float m = ((float) height) * constant;
            height = (int) m;
            Log.e("new width heith ", width + "   " + height);


            Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap, width, height);

            final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, this);

            model.LocalStartPhotoPath=bitmapFile.getAbsolutePath();
            model.StartTime=System.currentTimeMillis();

            txtStartTime.setText(CommonShare.getDateTime(model.StartTime));
            imgStart.setImageBitmap(thePic);

        }

        if (requestCode ==2  && resultCode == RESULT_OK)
        {
            if(locationStr==null ||locationStr.length()==0)
            {
                locationCnt++;
                if(locationCnt>=2)
                {
                    AlertDialog.Builder alert=new AlertDialog.Builder(this);
                    alert.setTitle("Location Not Found");
                    alert.setMessage("Unable to find location, Please Open Map Application to get location.");
                    alert.setPositiveButton("Open map", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                             CommonShare.openMap(MarkAttendance.this);
                        }
                    });
                    alert.setNegativeButton("Cancel",null);
                    alert.show();
                }
                Toast.makeText(this,"Location Not Found",Toast.LENGTH_LONG).show();
                return;
            }

            File file = new File(getAccessPath()+ File.separator + "img.jpg");
            path = file.getAbsolutePath();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            Log.e("Width height", width + "   " + height);

            float constant = ((float) (500f / (float) width));
            width = 500;
            Log.e("Contatnt", constant + "");
            float m = ((float) height) * constant;
            height = (int) m;
            Log.e("new width heith ", width + "   " + height);


            Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap, width, height);

            final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, this);

            model.LocalEndPhotoPath=bitmapFile.getAbsolutePath();
            model.EndTime=System.currentTimeMillis();

            txtEndTime.setText(CommonShare.getDateTime(model.EndTime));
            imgEnd.setImageBitmap(thePic);

        }


        if(requestCode==701   && MainActivityLauncher.pictureResult!=null )
        {
            MainActivityLauncher.pictureResult.toBitmap(2000, 2000, new BitmapCallback() {

                @Override
                public void onBitmapReady(@Nullable Bitmap bitmap) {


                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();

                    Log.e("Width height", width + "   " + height);

                    float constant = ((float) (500 / (float) width));
                    width = 500;
                    Log.e("Contatnt", constant + "");
                    float m = ((float) height) * constant;
                    height = (int) m;
                    Log.e("new width heith ", width + "   " + height);


                    Bitmap thePic = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
                        thePic = ThumbnailUtils.extractThumbnail(bitmap, width, height);
                    }

                    final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, MarkAttendance.this);

                    model.LocalStartPhotoPath=bitmapFile.getAbsolutePath();
                    model.StartTime=System.currentTimeMillis();

                    txtStartTime.setText(CommonShare.getDateTime(model.StartTime));
                    imgStart.setImageBitmap(thePic);
                }
            });
        }

        if (requestCode ==702  && MainActivityLauncher.pictureResult!=null )
        {
            MainActivityLauncher.pictureResult.toBitmap(1000, 1000, new BitmapCallback() {

                @Override
                public void onBitmapReady(@Nullable Bitmap bitmap) {
                    if(locationStr==null ||locationStr.length()==0)
                    {
                        locationCnt++;
                        if(locationCnt>=2)
                        {
                            AlertDialog.Builder alert=new AlertDialog.Builder(MarkAttendance.this);
                            alert.setTitle("Location Not Found");
                            alert.setMessage("Unable to find location, Please Open Map Application to get location.");
                            alert.setPositiveButton("Open map", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    CommonShare.openMap(MarkAttendance.this);
                                }
                            });
                            alert.setNegativeButton("Cancel",null);
                            alert.show();
                        }
                        Toast.makeText(MarkAttendance. this,"Location Not Found",Toast.LENGTH_LONG).show();
                        return;
                    }



                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();

                    Log.e("Width height", width + "   " + height);

                    float constant = ((float) (500f / (float) width));
                    width = 500;
                    Log.e("Contatnt", constant + "");
                    float m = ((float) height) * constant;
                    height = (int) m;
                    Log.e("new width heith ", width + "   " + height);


                    Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap, width, height);

                    final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, MarkAttendance.this);

                    model.LocalEndPhotoPath=bitmapFile.getAbsolutePath();
                    model.EndTime=System.currentTimeMillis();

                    txtEndTime.setText(CommonShare.getDateTime(model.EndTime));
                    imgEnd.setImageBitmap(thePic);

                }
            });




        }
    }
}
