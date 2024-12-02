package com.apgautomation.ui.visit;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.controller.VisitModuleController;
import com.apgautomation.model.GsonVisitMasterNnRealm;
import com.apgautomation.ui.adapter.VisitScheduledapter;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.google.gson.Gson;

import org.json.JSONArray;


public class VisitList extends AppCompatActivity implements DownloadUtility {

    VisitModuleController controller;
    ListView listview1;
    int clickId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_list);
        listview1=findViewById(R.id.listview1);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Scheduled Visit List");
        controller=new VisitModuleController(this);
        controller.loadPendingVisits();
        //controller.setInitial();
       // setAdapter();
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(controller.scheduleList.get(i).getVisitStatus().equalsIgnoreCase("Inprogress-Fsr-Pending"))
                    {
                        clickId =controller.scheduleList.get(i).getVisitId();
                        String url= CommonShare.url+"Service1.svc/GetCurrentVIsitFilter?EmpId="+CommonShare.getEmpId(VisitList.this)+
                                "&FromDate="+(System.currentTimeMillis()-2592000000l)+"&ToDate="+(System.currentTimeMillis()+2592000000l)+"&GroupId="+controller.scheduleList.get(i).getGroupId()
                                +"&visitId="+controller.scheduleList.get(i).getVisitId();


                        AsyncUtilities utilities=new AsyncUtilities(VisitList.this,false,url,null,2,VisitList.this);
                        utilities.execute();

                        return;
                    }
                    String token= controller.scheduleList.get(i).getVisitToken();
                    startActivity(new Intent(VisitList.this,ProcessVisit.class).putExtra("VisitToken", token));


            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {


        if(requestCode==1 &&  responseCode==200 )
        {
           // setAdapter();
          //  adapter.notifyDataSetChanged();
            setAdapter();
        }
        else if(requestCode==2 && responseCode==200)
        {
            try {
                String   str1=  str;
                JSONArray arr=new JSONArray(str1);
                str1=arr.getJSONObject(0).toString();
                Gson g=new Gson();
                GsonVisitMasterNnRealm bb=  g.fromJson( str1,GsonVisitMasterNnRealm.class);
                VisitDetails.model=bb;

                String url= CommonShare.url+"Service1.svc/GetVisitDetails?VisitId="+clickId;
                AsyncUtilities utilities=new AsyncUtilities(VisitList.this,false,url,null,3,VisitList.this);
                utilities.execute();
            } catch (Exception exception) {
                exception.printStackTrace();
            }


        }
        else if(requestCode==3 &&  responseCode==200 ) {
            VisitDetails.str=str;
            finish();

            startActivity(new Intent(VisitList.this,VisitDetails.class));
        }
    }
    VisitScheduledapter adapter;
    void setAdapter()
    {
        adapter =new VisitScheduledapter(this,R.layout.item_visitschedule,controller.scheduleList);
        listview1.setAdapter(adapter);
    }
}
