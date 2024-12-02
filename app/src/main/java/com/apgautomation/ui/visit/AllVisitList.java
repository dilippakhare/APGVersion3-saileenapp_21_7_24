package com.apgautomation.ui.visit;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
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


public class AllVisitList extends AppCompatActivity implements DownloadUtility {

    VisitModuleController controller;
    ListView listview1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_list);
        listview1=findViewById(R.id.listview1);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Scheduled Visit List");
        controller=new VisitModuleController(this);
       // controller.loadPendingVisits();
        //controller.setInitial();
       // setAdapter();
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                  //  String token= controller.scheduleList.get(i).getVisitToken();
                Gson g=new Gson();
                GsonVisitMasterNnRealm bb=  g.fromJson(   g.toJson(list.get(i)),GsonVisitMasterNnRealm.class);
                VisitDetails.model=bb;
                        //list.get(i);

                String url= CommonShare.url+"Service1.svc/GetVisitDetails?VisitId="+VisitDetails.model.VisitId;
                AsyncUtilities utilities=new AsyncUtilities(AllVisitList.this,false,url,null,2,AllVisitList.this);
                utilities.execute();


            }
        });

       callSimple();
    }
    void callSimple()
    {
        boolean isAdamin=false;
        if(CommonShare.getRole(this).equalsIgnoreCase("Admin"))
            isAdamin=true;
        String url= CommonShare.url+"Service1.svc/GetRecentVIsit?EmpId="+CommonShare.getEmpId(this)+"&IsAdmin="+isAdamin;
        AsyncUtilities utilities=new AsyncUtilities(this,false,url,null,1,this);
        utilities.execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        if(item.getItemId()==R.id.action_filter)
        {
            startActivityForResult(new Intent(this,FilterOption.class) .putExtra("IsSales",false) ,400);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menufilter,menu);

        return super.onCreateOptionsMenu(menu);
    }

    ArrayList<GsonVisitMasterNnRealm>  list=new ArrayList<>();
    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

       // setAdapter();

        if(requestCode==1 &&  responseCode==200 )
        {
            list.clear();
            try
            {
                Gson gson=new Gson();
                JSONArray array=new JSONArray(str);
                for(int i=0;i<array.length();i++)
                {
                    GsonVisitMasterNnRealm bean=gson.fromJson(array.getJSONObject(i).toString(),GsonVisitMasterNnRealm.class);
                    list.add(bean);
                }
                //CommonShare.alert(this,list.size()+"");
            }
            catch (Exception ex)
            {}
            setAdapter();
        }
        if(requestCode==2 && responseCode==200)
        {
            VisitDetails.str=str;
            startActivity(new Intent(AllVisitList.this,VisitDetails.class));
        }
    }
    AllVisitScheduledapter adapter;
    void setAdapter()
    {
        adapter =new AllVisitScheduledapter(this,R.layout.item_visitschedule,list);
        listview1.setAdapter(adapter);
    }



    int filterFlag=0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK  && requestCode==400)
        {
            filterFlag=1;
            callFilterView();
        }
    }
    void callFilterView()
    {
        int GroupId =FilterOption.GroupId;
        long FromDate=FilterOption.fromDateMiilisecond;
        long ToDate=FilterOption.toDateMillisecond;
        int EmpId=FilterOption.EmpId;


        String url= CommonShare.url+"Service1.svc/GetRecentVIsitFilter?EmpId="+EmpId+
                "&FromDate="+FromDate+"&ToDate="+ToDate+"&GroupId="+GroupId;
        //controller.callAllVisitFilter(url);

        AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",1,this);
        utilities.execute();
    }
}
