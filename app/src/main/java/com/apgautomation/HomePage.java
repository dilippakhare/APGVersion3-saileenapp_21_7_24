package com.apgautomation;
import java.util.concurrent.TimeUnit;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apgautomation.model.GSONCustomerMasterBean;
import com.apgautomation.model.GsonCustomerProduct;
import com.apgautomation.model.GsonGroup;
import com.apgautomation.ui.dashboard.SyncFragment;
import com.apgautomation.ui.summary.CompaintDashboad;
import com.apgautomation.utility.serverutility.netrequest.ConnectionDetector;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.apgautomation.controller.SyncCustomerProductControlller;
import com.apgautomation.controller.SyncGroupAndEquipment;
import com.apgautomation.fcm.FcmUtility;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.model.database.ItemDAOAttendanceModel;
import com.apgautomation.model.database.ItemDAOLeaveMaster;
import com.apgautomation.ui.activities.ActivitiesFragment;
import com.apgautomation.ui.customer.UserCreation2;
import com.apgautomation.ui.dashboard.DashboardFragment;
import com.apgautomation.ui.notifications.NotificationsFragment;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.apgautomation.utility.syncutilities.AttendanceSyncUtility;
import com.apgautomation.utility.syncutilities.ComplaintSyncUtility;
import com.apgautomation.utility.syncutilities.VisitSyncUtility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import io.realm.Realm;

public class HomePage extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, DownloadUtility {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    FusedLocationProviderClient fusedLocationClient;
    LocationCallback locationCallback;
    //ACCESS_MEDIA_LOCATION
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        VisitSyncUtility visitSyncUtility=new VisitSyncUtility(this);
        visitSyncUtility.sync();
        SyncCustomerProductControlller productControlller=new SyncCustomerProductControlller(this);
        long pCNT=productControlller.GeCustProductCount();
        // CommonShare.alert(this,pCNT+"");
        // checkVersionSync();

        if(pCNT ==0)
        {
            SyncGroupAndEquipment syncGroupAndEquipment=new SyncGroupAndEquipment(this);
            syncGroupAndEquipment.sync();
            //SyncCustomerControlller ccc=new SyncCustomerControlller(this);
            // ccc.sync();

            String url= CommonShare.url+"Service1.svc/LoginUserByUserId?UserId="+CommonShare.getUserId(this);
            AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",20,this);
            utilities.hideProgressDialoge();
            utilities.execute();
        }
        else
        {
            Long lastMillisecond=  getSharedPreferences("LastCustomerSync",MODE_PRIVATE).getLong("time",0l);
            Long lastMonthMillisecond =getSharedPreferences("LastCustomerSync",MODE_PRIVATE).getLong("lastProduct",0l);
            if(System.currentTimeMillis()-lastMonthMillisecond> 2592000000l )
            {
                SyncGroupAndEquipment syncGroupAndEquipment=new SyncGroupAndEquipment(this);
                syncGroupAndEquipment.sync();

                String url= CommonShare.url+"Service1.svc/LoginUserByUserId?UserId="+CommonShare.getUserId(this);
                AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",20,this);
                utilities.hideProgressDialoge();
                utilities.execute();
            }
            else if(System.currentTimeMillis()-lastMillisecond> 86400000 )
            {
                String url= CommonShare.url+"Service1.svc/LoginUserByUserId?UserId="+CommonShare.getUserId(this);
                AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",20,this);
                utilities.hideProgressDialoge();
                utilities.execute();
                EmployeeModel m= CommonShare.getMyEmployeeModel(this);

                if(m.DeptId==2) {
                    SyncGroupAndEquipment syncGroupAndEquipment = new SyncGroupAndEquipment(this);
                    syncGroupAndEquipment.syncInBackgroud();
                }

                url= CommonShare.url+"Service1.svc/GetLastWeekAmcEnd?UserId="+CommonShare.getUserId(this);
                AsyncUtilities utilities1=new AsyncUtilities(this,false,url,"",21,this);
                utilities1.hideProgressDialoge();
                utilities1.execute();
            }
        }

        // checkVersionSync();
        AttendanceSyncUtility utility=new AttendanceSyncUtility(this);
        utility.sync();
        CommonShare.GenerateEmployeeMap(this);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        CommonShare.checkPhoneStatePermission(this);
        // Passing each menu ID as a set of Ids because each
        //  menu should be considered as top level destinations.
       /*
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        (
        */

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                // Toast.makeText(HomePage.this, "Click-"+menuItem.getItemId(), Toast.LENGTH_SHORT).show();
                if(menuItem.getItemId()==R.id.navigation_activities)
                {
                    // Toast.makeText(HomePage.this, "Click  Home", Toast.LENGTH_SHORT).show();
                    ActivitiesFragment f=    new ActivitiesFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,f).commit();
                    // CommonShare.alert(HomePage.this,"Activities");
                    return  true;

                }
                else  if(menuItem.getItemId()==R.id.navigation_notifications)
                {
                    NotificationsFragment f=    new NotificationsFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,f).commit();
                    // CommonShare.alert(HomePage.this,"DashBoard");
                    return  true;

                }
                else  if(menuItem.getItemId()==R.id.navigation_dashboard)
                {
                    DashboardFragment f=    new DashboardFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,f).commit();
                    // CommonShare.alert(HomePage.this,"DashBoard");
                    ComplaintSyncUtility utility=new ComplaintSyncUtility(HomePage.this);
                    return  true;
                }
                else  if(menuItem.getItemId()==R.id.navigation_Complaints)
                {
                    CompaintDashboad f=    new CompaintDashboad();
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,f).commit();
                    // CommonShare.alert(HomePage.this,"DashBoard");
                    ComplaintSyncUtility utility=new ComplaintSyncUtility(HomePage.this);
                    return  true;
                }
                else  if(menuItem.getItemId()==R.id.navigation_sync)
                {
                    SyncFragment f=new SyncFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,f).commit();
                    return  true;
                }
                return true;
            }

        });
        // ActivitiesFragment f=    new ActivitiesFragment();
        //getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment,f).commit();

        ActivitiesFragment f1=    new ActivitiesFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,f1).commit();

        try {
            if (android.os.Build.VERSION.SDK_INT >= 34 && HomePage.this.getApplicationInfo().targetSdkVersion >= 34) {
                //Log.d("YourClassTag","API level is 34 or higher, and target SDK version is 34 or higher.");
                /// activity.registerReceiver(reloadReceiver, new IntentFilter(RELOAD_BROADCAST), Context.RECEIVER_EXPORTED);
                registerReceiver(attendanceSender, new IntentFilter("SendAttendance"), Context.RECEIVER_NOT_EXPORTED);
                registerReceiver(attendanceReciever, new IntentFilter("RefreshAttendance"), Context.RECEIVER_NOT_EXPORTED);
                registerReceiver(googleConnector, new IntentFilter("ConnectGoogle"), Context.RECEIVER_NOT_EXPORTED);
            } else {
                registerReceiver(attendanceSender, new IntentFilter("SendAttendance"));
                registerReceiver(attendanceReciever, new IntentFilter("RefreshAttendance"));
                registerReceiver(googleConnector, new IntentFilter("ConnectGoogle"));
            }
        }
        catch (Exception ex) {
            CommonShare.alert(HomePage.this,ex.toString());
        }
        checkLocation();
        callSync(this);

        FcmUtility fcm=new FcmUtility();
        fcm.callProcedure(this);

        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setHomeButtonEnabled(true);

        // getSupportActionBar().setSubtitle();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                }
            };
        };
       /* if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_MEDIA_AUDIO,
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO, android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}
                    ,
                    1947

            );
        }*/
 /*       ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_MEDIA_AUDIO,
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO, android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}
                ,
                1947

        );*/

        checkeachPermission();




    }
    BroadcastReceiver attendanceReciever=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            List<Fragment> lst= getSupportFragmentManager().getFragments();
            for (Fragment f:lst
            ) {
                if(f instanceof ActivitiesFragment)
                {
                    ActivitiesFragment fr= (ActivitiesFragment) f;
                    ((ActivitiesFragment) f).refreshAttendance();


                }
            }
        }
    };
    BroadcastReceiver attendanceSender=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            callSync(context);
            callTimeCheck();
        }
    };
    BroadcastReceiver googleConnector=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            //callSync(context);
            callGoogleConnect();
        }
    };


    void callSync(Context context)
    {
        AttendanceSyncUtility utility=new AttendanceSyncUtility(context);
        utility.sync();
    }
    void callGoogleConnect()
    {
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(CommonShare.getUserId(this)==2)
            getMenuInflater().inflate(R.menu.syncmenu2,menu);
        else
            getMenuInflater().inflate(R.menu.syncmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_sync)
        {
          /*  AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setTitle("Logout");
            alert.setMessage("Do you want to logout this application ?");
            alert.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logout();
                }
            });
            alert.setNegativeButton("No",null);
            alert.show();*/
            if(new ConnectionDetector(this).isConnectingToInternet())
            {
                sync();
            }
            else
            {
                Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show();
            }

        }
        if(item.getItemId()==R.id.action_createuser)
        {
            startActivity(new Intent(this, UserCreation2.class));
        }
        if(item.getItemId()==R.id.action_ResetApp)
        {
            android.app.AlertDialog.Builder alert=new android.app.AlertDialog.Builder(this);
            alert.setTitle("Reset the app");
            alert.setMessage("Re-setting app will cause delete cache data and app will be force close , Do you want to Reset the app?");
            alert.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteAppData();
                }
            });
            alert.setNegativeButton("No",null);
            alert.show();

        }
        return super.onOptionsItemSelected(item);
    }

    public  void sync()
    {
        try
        {
            Realm realm=Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.delete(GsonGroup.class);
            realm.delete(GSONCustomerMasterBean.class);
            realm.commitTransaction();
        }
        catch (Exception ex)
        {}
        SyncGroupAndEquipment syncGroupAndEquipment=new SyncGroupAndEquipment(this);
        syncGroupAndEquipment.sync();

        String url= CommonShare.url+"Service1.svc/LoginUserByUserId?UserId="+CommonShare.getUserId(this);
        AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",20,this);
        utilities.hideProgressDialoge();
        utilities.execute();
    }

    @Override
    public void finish() {
        super.finish();
        unregisterReceiver(attendanceReciever);
        unregisterReceiver(attendanceSender);
        try
        {
            unregisterReceiver(googleConnector);
        }
        catch (Exception ex){}
    }

    void checkLocation()
    {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean enabled1 = service
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        //  Settings.ACTION_LOCATION_SOURCE_SETTINGS
        if (enabled  )
        {

        }
        else
        {
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
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
        }
    }

    public void logout()
    {
        try
        {
            ItemDAOAttendanceModel itemDAOAttendanceModel=new ItemDAOAttendanceModel(this);
            itemDAOAttendanceModel.deleteAllRecord();

            ItemDAOLeaveMaster itemDAOLeaveMaster=new ItemDAOLeaveMaster(this);
            itemDAOLeaveMaster.deleteRecordAllRecords();


            CommonShare.clearLoginData(this);
            finish();
            startActivity(new Intent(this, MainActivityLauncher.class));
        }
        catch (Exception ex)
        {}
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Toast.makeText(this, "Google Connect", Toast.LENGTH_SHORT).show();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second
        mLocationRequest.setNumUpdates(2);
        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,  mLocationRequest, this);
        fusedLocationClient.requestLocationUpdates(mLocationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }

    @Override
    public void onLocationChanged(Location location) {

    }


    void checkeachPermission()
    {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            AlertDialog.Builder  alert=new AlertDialog.Builder(this);
            alert.setTitle("Location Permission Not Given");
            alert.setMessage("Location Permission Not Given.Please Give Location.");
            alert.setPositiveButton("Open Permission Setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                    startActivity(intent);
                }
            });
            alert.setNegativeButton("No",null);
            alert.show();

        }


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            AlertDialog.Builder  alert=new AlertDialog.Builder(this);
            alert.setTitle("Phone State Permission Not Given");
            alert.setMessage("Phone State  Permission Not Given.Please Give Permission.");
            alert.setPositiveButton("Open Permission Setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                    startActivity(intent);
                }
            });
            alert.setNegativeButton("No",null);
            alert.show();

        }

        // if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
       /* if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED)
        {
            AlertDialog.Builder  alert=new AlertDialog.Builder(this);
            alert.setTitle("Permission Not Given");
            alert.setMessage("Storage Permission Not Given.Please Give Permission.");
            alert.setPositiveButton("Open Permission Setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                    startActivity(intent);
                }
            });
            alert.setNegativeButton("No",null);
            alert.show();

        }*/

        ActivityCompat.requestPermissions(this,
                new String[]{
                        //Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_PHONE_STATE}
                        Manifest.permission.READ_MEDIA_AUDIO,
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE
                }
                        ,
                1947

        );
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        if(responseCode==200 && requestCode==20) {
            try
            {
                getSharedPreferences("LastCustomerSync",MODE_PRIVATE).edit().putLong("time",System.currentTimeMillis()).commit();

                if(str.contains("LOGOUT"))
                {
                    CommonShare.clearLoginData(this);
                    finish();
                    startActivity(new Intent(this, MainActivityLauncher.class));
                }
                JSONObject jsonObject = new JSONObject(str);
                JSONObject jObj = jsonObject.getJSONObject("user");

                if (jObj.getInt("UserId") > 0) {
                    CommonShare.saveLoginData(this, str);
                }
            } catch (Exception ex) {
            }
        }
        if(requestCode==17 &&responseCode==200)
        {
            checkTime(str);
        }
        if(requestCode==21 &&responseCode==200)
        {
            //checkTime(str);
            try
            {
                Realm realm=Realm.getDefaultInstance();
                Gson gson=new Gson();
                JSONArray jsonArray=new JSONArray(str);
                for (int i=0;i<jsonArray.length();i++)
                {
                    String jString = jsonArray.getJSONObject(i).toString();
                    GsonCustomerProduct gsonCustomerProduct = gson.fromJson(jString, GsonCustomerProduct.class);
                    gsonCustomerProduct.convertDates();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(gsonCustomerProduct);
                    realm.commitTransaction();
                }
            }
            catch (Exception ex)
            {}
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // callTimeCheck();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                               String s= CommonShare.getMyEmployeeModel(HomePage.this).EmpName;
                               getSupportActionBar().setSubtitle(s);
                            }
                            catch (Exception ex){}
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    void callTimeCheck()
    {
        String url=CommonShare.url+"Service1.svc/GetServerTime";
        AsyncUtilities utilities=new AsyncUtilities(this,false,url,null,17,this);
        utilities.hideProgressDialoge();
        utilities.execute();
    }
    void checkTime(String str)
    {
        if(str!=null && str.contains("Date("))
            try
            {
                long serverTime  =  CommonShare.parseDate1(str.trim());
                long minutes = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()-serverTime);
                if(minutes>10 || minutes<-10)
                {
                    CommonShare.alert(this,"Your Mobile Time is not match with server.Please Correct your time .This information will be provided to administration");
                    String url=CommonShare.url+"Service1.svc/insertTimeFraud?UserId="+CommonShare.getUserId(this)+"&entertime="+System.currentTimeMillis()+"&timedifference="+minutes;

                    AsyncUtilities utilities=new AsyncUtilities(this,false,url,null,18,this);
                    utilities.hideProgressDialoge();
                    utilities.execute();
                }
            }
            catch (Exception ex)
            {

            }
    }


    int Version=2;
    void checkVersionSync()
    {
        if(!getSharedPreferences("Version2",MODE_PRIVATE).getBoolean("Version2",false))
        {
            Realm realm=Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();

            SyncGroupAndEquipment syncGroupAndEquipment=new SyncGroupAndEquipment(this);
            syncGroupAndEquipment.sync();
            //SyncCustomerControlller ccc=new SyncCustomerControlller(this);
            // ccc.sync();

            String url= CommonShare.url+"Service1.svc/LoginUserByUserId?UserId="+CommonShare.getUserId(this);
            AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",20,this);
            utilities.hideProgressDialoge();
            utilities.execute();

        }
    }

    private void deleteAppData() {
        try
        {
            // clearing app data
            String packageName = getApplicationContext().getPackageName();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear "+packageName);

        } catch (Exception e) {
            e.printStackTrace();
        } }
}