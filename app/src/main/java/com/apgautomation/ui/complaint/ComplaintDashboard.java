package com.apgautomation.ui.complaint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.apgautomation.HomePage;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;

import com.apgautomation.R;
import com.apgautomation.ui.complaint.ui.main.SectionsPagerAdapter;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;

public class ComplaintDashboard extends AppCompatActivity implements DownloadUtility {
    SectionsPagerAdapter sectionsPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_dashboard);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Complaint Dashboard");
        call();
        if (android.os.Build.VERSION.SDK_INT >= 34 && ComplaintDashboard.this.getApplicationInfo().targetSdkVersion >= 34) {

            registerReceiver(reciever, new IntentFilter("RefreshComplaint"),Context.RECEIVER_NOT_EXPORTED);
        }
        else {
            registerReceiver(reciever, new IntentFilter("RefreshComplaint"));
        }
    }

    @Override
    public void finish() {
        super.finish();
        unregisterReceiver(reciever);
    }

    void call()
    {
        String url= CommonShare.url+"Service1.svc/GetAllComplaint?EmployeeId="+CommonShare.getEmpId(this);

        AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",1,this);
        utilities.execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        sectionsPagerAdapter.onComplete(str,requestCode,responseCode);
    }
    BroadcastReceiver reciever=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                call();
        }
    };
}