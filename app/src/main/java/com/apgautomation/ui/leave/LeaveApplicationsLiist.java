package com.apgautomation.ui.leave;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.model.LeaveDetails;
import com.apgautomation.model.LeaveModel;
import com.apgautomation.model.ServerModel.ServerLeaveDetails;
import com.apgautomation.model.ServerModel.ServerLeaveModel;
import com.apgautomation.model.database.ItemDAOLeaveMaster;
import com.apgautomation.ui.adapter.RecentLeaveAdapter;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.apgautomation.utility.syncutilities.LeaveSyncUtility;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LeaveApplicationsLiist extends AppCompatActivity implements DownloadUtility
{

    ListView listview;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_applications_liist);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Leave Applications");
        listview=findViewById(R.id.listview);
        floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   startActivity(new Intent(LeaveApplicationsLiist.this,LeaveApplications.class));
            }
        });

        LeaveSyncUtility utility=new LeaveSyncUtility(this);
        utility.sync();

        String url = CommonShare.url + "Service1.svc/LeaveRecords?Mode=2&EmpId="+CommonShare.getEmpId(this)+"&ManagerId="+CommonShare.getEmpId(this);
        AsyncUtilities utilities=new AsyncUtilities(this,false,url,null,1,this);
        utilities.execute();
        setListview();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    ArrayList<LeaveModel> list=new ArrayList<>();
    void setListview()
    {
          ItemDAOLeaveMaster itemDAOLeaveMaster=new ItemDAOLeaveMaster(this);
         list= itemDAOLeaveMaster.getRecentLeave();
         for(int i=0;i<list.size();i++)
         {
             ItemDAOLeaveMaster item=  new ItemDAOLeaveMaster(this);
             ArrayList<LeaveDetails> d= item.getLeaveDetails(list.get(i).Token);
             list.get(i).leaveDetails=d;
         }
        RecentLeaveAdapter adapter=new RecentLeaveAdapter(this,R.layout.item_leavedate,list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LeaveApplicationDetails.model=list.get(i);
                startActivity(new Intent(LeaveApplicationsLiist.this,LeaveApplicationDetails.class));
            }
        });
    }
    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        try
        {
            Gson g=new Gson();
            JSONArray jsonArray=new JSONArray(str);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                ServerLeaveModel model = g.fromJson(jsonObject.getJSONObject("leaveApplication").toString(), ServerLeaveModel.class);
                try
                {
                    model.leaveDetails=new ArrayList<>();
                    JSONArray arr=jsonObject.getJSONArray("leaveDetails");
                    for(int j=0;j<arr.length();j++)
                    {
                        ServerLeaveDetails mOb = g.fromJson(arr.getJSONObject(j).toString(), ServerLeaveDetails.class);
                        model.leaveDetails.add(mOb);
                    }
                }
                catch (Exception ex){}
                ItemDAOLeaveMaster itemDAOLeaveMaster = new ItemDAOLeaveMaster(this);
                itemDAOLeaveMaster.deleteRecord(model.toLeaveModel());
                itemDAOLeaveMaster.insertRecord(model.toLeaveModel());
                if (model.leaveDetails != null) {
                    for (int index = 0; index < model.leaveDetails.size(); index++) {
                        itemDAOLeaveMaster.insertLeaveDetailsRecord(model.leaveDetails.get(index).toLeaveDetils());
                    }
                }
            }

        }
        catch (Exception ex)
        {}
        setListview();
    }
}
