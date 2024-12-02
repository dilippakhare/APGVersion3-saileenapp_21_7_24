package com.apgautomation.ui.taskmgmt.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.apgautomation.R;
import com.apgautomation.model.GsonTodo;
import com.apgautomation.ui.taskmgmt.ui.ui.main.SectionsPagerAdapter;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.google.gson.Gson;

import org.json.JSONArray;

import io.realm.Realm;
import io.realm.RealmResults;

public class TaskMgmt extends AppCompatActivity implements DownloadUtility
{
    ViewPager viewPager;
    SectionsPagerAdapter sectionsPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_mgmt);

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(TaskMgmt.this, CreateTask.class));
            }
        });

         Toolbar toolbar=findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
         getSupportActionBar().setHomeButtonEnabled(true);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setTitle("To Do");


         refresh();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menufilterrefresh,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        if(item.getItemId()==R.id.action_filter)
        {
           startActivityForResult(new Intent(this,Filter.class).putExtra("Option",viewPager.getCurrentItem()),1);
        }
        if(item.getItemId()==R.id.action_refresh)
        {
            refresh();
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh()
    {
        String url= CommonShare.url+"Service1.svc/GetTodotask?EmpId="+CommonShare.getEmpId(this);
        AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",1,this);
        utilities.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==1)
        {
            try
            {

            }
            catch (Exception ex)
            {}
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(50);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try
                                {
                                    sectionsPagerAdapter.filter(viewPager.getCurrentItem());
                                }
                                catch (Exception ex){}
                            }
                        });
                    }
                    catch (Exception ew){}

                }
            }).start();

        }

    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        if(requestCode==1 && responseCode==200)
        {
            try
            {
                parseData(str);
            }
            catch (Exception ex){
                Log.d("com.apgautomation",ex.toString());
            }
           // startActivityForResult(new Intent(this,Filter.class).putExtra("Option",viewPager.getCurrentItem()),1);
            sectionsPagerAdapter.setDefault();
        }
    }

    void parseData(String str) throws Exception {
        Gson gson=new Gson();
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonTodo> reslut= realm.where(GsonTodo.class).equalTo("IsModified",true).findAll();
        JSONArray jsonArray = new JSONArray(str);
        for (int i = 0; i < jsonArray.length(); i++)
        {
            GsonTodo d=gson.fromJson(jsonArray.getJSONObject(i).toString(),GsonTodo.class);
            if(reslut.where().notEqualTo("Token",d.getToken()).count()==0) {
                synchronized(CommonShare.todoSync) {
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(d);
                    realm.commitTransaction();
                }
            }
        }
    }
}