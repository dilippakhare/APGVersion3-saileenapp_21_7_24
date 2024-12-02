package com.apgautomation.ui.customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.apgautomation.BuildConfig;
import com.apgautomation.MainActivityLauncher;
import com.apgautomation.R;
import com.apgautomation.fcm.FcmUtility;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.apgautomation.utility.syncutilities.ComplaintSyncUtility;

import org.json.JSONObject;

public class CustomerActivity extends AppCompatActivity implements DownloadUtility {

    LinearLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        container=(LinearLayout)findViewById(R.id.container);
        CustomerFragment frag=new CustomerFragment();
        getSupportFragmentManager().beginTransaction().add(container.getId(),frag).commit();


        ComplaintSyncUtility  utility=new ComplaintSyncUtility(this);

        checkeachPermission();
        FcmUtility fcm=new FcmUtility();
        fcm.callProcedure(this);

        String url= CommonShare.url+"Service1.svc/LoginUserByUserId?UserId="+CommonShare.getUserId(this);
        AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",20,this);
        utilities.hideProgressDialoge();
        utilities.execute();
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

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
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

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_logout)
        {
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setTitle("Logout");
            alert.setMessage("Do you want to logout this application ?");
            alert.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logout();
                }
            });
            alert.setNegativeButton("No",null);
            alert.show();

        }
        return super.onOptionsItemSelected(item);
    }

    public void logout()
    {
        try
        {
            CommonShare.clearLoginData(this);
            finish();
            startActivity(new Intent(this, MainActivityLauncher.class));
        }
        catch (Exception ex)
        {}
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        try
        {
          if(requestCode==20 && responseCode==200)
          {
              JSONObject j=new JSONObject(str);
              CommonShare.saveLoginData(this,str);
          }
        }
        catch (Exception ex)
        {}
    }

}
