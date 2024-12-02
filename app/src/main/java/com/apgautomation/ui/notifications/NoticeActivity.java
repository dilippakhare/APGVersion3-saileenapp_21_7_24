package com.apgautomation.ui.notifications;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.apgautomation.R;
import com.apgautomation.utility.CommonShare;

public class NoticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new NotificationsFragment()).commit();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notice");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(CommonShare.getRole(this).equalsIgnoreCase("Admin")  || CommonShare.getUserId(this)==2)
        {
                getMenuInflater().inflate(R.menu.createnotice, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        else if(item.getItemId()==R.id.action_create)
        {
            startActivity(new Intent(this,CreateNotice.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
