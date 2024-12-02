package com.apgautomation.ui.visit;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.controller.VisitModuleController;
import com.apgautomation.model.GsonVisitMasterNnRealm;
import com.apgautomation.ui.adapter.AllVisitScheduledapter;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;


public class CustomerVisitListActivity extends AppCompatActivity implements DownloadUtility {

    VisitModuleController controller;
    ListView listview1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_list);
        listview1=findViewById(R.id.listview1);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Service Visit List");
        controller=new VisitModuleController(this);
       // controller.loadPendingVisits();
        //controller.setInitial();
       // setAdapter();
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                  //  String token= controller.scheduleList.get(i).getVisitToken();
                VisitDetails.model=list.get(i);

                String url= CommonShare.url+"Service1.svc/GetVisitDetails?VisitId="+VisitDetails.model.VisitId;
                AsyncUtilities utilities=new AsyncUtilities(CustomerVisitListActivity.this,false,url,null,2, CustomerVisitListActivity.this);
                utilities.execute();


            }
        });

        boolean isAdamin=false;
        if(CommonShare.getRole(this).equalsIgnoreCase("Admin"))
            isAdamin=true;
        String url= CommonShare.url+"Service1.svc/GetCustomerVisit?UserId="+CommonShare.getUserId(this);
        AsyncUtilities utilities=new AsyncUtilities(this,false,url,null,1,this);
        utilities.execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    ArrayList<GsonVisitMasterNnRealm>  list=new ArrayList<>();
    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

       // setAdapter();
        if(requestCode==1 &&  responseCode==200 )
        {
            try
            {
                Gson gson=new Gson();
                JSONArray array=new JSONArray(str);
                for(int i=0;i<array.length();i++)
                {
                    GsonVisitMasterNnRealm bean=gson.fromJson(array.getJSONObject(i).toString(),GsonVisitMasterNnRealm.class);
                    list.add(bean);
                }
            }
            catch (Exception ex)
            {}
            setAdapter();
        }
        if(requestCode==2 && responseCode==200)
        {
            VisitDetails.str=str;
            startActivity(new Intent(CustomerVisitListActivity.this,VisitDetails.class));
        }
    }
    AllVisitScheduledapter adapter;
    void setAdapter()
    {
        adapter =new AllVisitScheduledapter(this,R.layout.item_visitschedule,list);
        listview1.setAdapter(adapter);
    }
}
