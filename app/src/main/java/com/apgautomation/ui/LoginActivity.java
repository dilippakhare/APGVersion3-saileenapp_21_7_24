package com.apgautomation.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.apgautomation.HomePage;
import com.apgautomation.R;
import com.apgautomation.fcm.FcmUtility;
import com.apgautomation.ui.customer.CustomerActivity;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.DeviceUuidFactory;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.apgautomation.utility.serverutility.netrequest.ConnectionDetector;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, DownloadUtility {

    EditText  edusername,edpass;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edpass=findViewById(R.id.edpass);
        edusername=findViewById(R.id.edusername);
        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);


        /*   older permisssion code
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_PHONE_STATE}
                ,
                1947

        );
requestPermissions(
  arrayOf(
    Manifest.permission.READ_MEDIA_AUDIO,
    Manifest.permission.READ_MEDIA_IMAGES,
    Manifest.permission.READ_MEDIA_VIDEO,
  ),
  REQUEST_CODE
)

         */
        ActivityCompat.requestPermissions(this,
                new String[]{ Manifest.permission.READ_MEDIA_AUDIO,
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_PHONE_STATE}
                ,
                1947

        );

        FcmUtility fcm=new FcmUtility();
        fcm.callProcedure(this);
    }

    @Override
    public void onClick(View view)
    {
          CommonShare.hideatInItInputBoard(this);
          CommonShare.hideSoftKeyBord(this);
          if(!new ConnectionDetector(this).isConnectingToInternet())
          {
               Toast.makeText(this,"No Internet Found",Toast.LENGTH_LONG).show();
                return;
          }
          if(edpass.getText().toString().equalsIgnoreCase("") || edusername.getText().toString().equalsIgnoreCase(""))
          {
              Toast.makeText(this,"Enter UserName and Password",Toast.LENGTH_LONG).show();
          }
          else
          {
              String deviceId="";
              try {
                  deviceId= new DeviceUuidFactory(this).getDeviceUuid().toString();
              }
              catch (Exception ex){}
              String url= CommonShare.url+"Service1.svc/loginUser?uname="+edusername.getText().toString()+"&upass="+edpass.getText().toString()+"&deviceid="+deviceId;
              AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",1,this);
              utilities.execute();
          }
    }

    String oldUser,oldPass;
    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
             if(responseCode==200  && requestCode==1)
             {
                   try {
                       JSONObject jsonObject=new JSONObject(str);
                       JSONObject jObj=  jsonObject.getJSONObject("user");
                       if(jObj.getInt("UserId")==-33)
                       {
                           CommonShare.alert(this,"You have allready login with another device. Please Logout from other device and login again or contact to administration .");
                           return;
                       }
                       if(jObj.getInt("UserId")>0)
                       {
                           CommonShare.saveLoginData(this,str);
                           finish();
                           if(jObj.getString("rolename").equalsIgnoreCase("Customer"))
                           {
                               startActivity(new Intent(this, CustomerActivity.class));
                           }
                           else
                           {
                               startActivity(new Intent(this, HomePage.class));
                           }
                       }
                       else
                       {
                           Toast.makeText(this,"Invalid User",Toast.LENGTH_LONG).show();

                       }
                   }
                   catch (Exception ex)
                   {}
             }
             else if(responseCode==200  && requestCode==2) {
              try
              {
                    JSONObject j=new JSONObject(str);
                    oldUser=  j.getString("uname");
                    oldPass=  j.getString("upass");
                    String empName=j.getString("EmpName");
                    AlertDialog.Builder alert=new AlertDialog.Builder(this);
                    alert.setTitle("User already associate with device");
                    alert.setMessage(empName+" already associate with device. Do you want to continue with "+empName+"?");
                    alert.setPositiveButton("Login as " + empName, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                              edusername.setText(oldUser);
                              edpass.setText(oldPass);
                              LoginActivity.this. onClick(null);
                        }
                    });
                    alert.setNegativeButton("Use different", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
              }
              catch (Exception ex){}

             }
             else
             {
                 Toast.makeText(this,"Unable to login at this moment",Toast.LENGTH_LONG).show();

             }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        switch (requestCode) {
            case 1947:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.

                   // CommonShare.alert(this,"OK  Permisssion");
                    callOldLogin();
                }  else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }


    void callOldLogin()
    {
        String deviceId="";
        try {
            deviceId= new DeviceUuidFactory(this).getDeviceUuid().toString();
        }
        catch (Exception ex){

        }
        String str=CommonShare.url+"Service1.svc/GetEmpByDeviceId?deviceid="+deviceId;
        AsyncUtilities utilities =new AsyncUtilities(this,false,str,null,2,this);
        utilities.execute();
    }
}
