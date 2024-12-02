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
import com.apgautomation.model.LeaveModel;
import com.apgautomation.model.ServerModel.ServerLeaveDetails;
import com.apgautomation.model.ServerModel.ServerLeaveModel;
import com.apgautomation.ui.adapter.RecentLeaveAdapter;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LeaveApplicationsLiistForManager extends AppCompatActivity implements DownloadUtility
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
        floatingActionButton.setVisibility(View.GONE);

        int mode=0;
        try
        {
           if( CommonShare.getRole(this).equalsIgnoreCase("Admin"))
               mode=1;
        }
        catch (Exception ex)
        {}

        String url = CommonShare.url + "Service1.svc/LeaveRecords?Mode="+mode+"&EmpId="+CommonShare.getEmpId(this)+"&ManagerId="+CommonShare.getEmpId(this);
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
        RecentLeaveAdapter adapter=new RecentLeaveAdapter(this,R.layout.item_leavedate,list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LeaveApplicationDetails.model=list.get(i);
                startActivity(new Intent(LeaveApplicationsLiistForManager.this,LeaveApplicationDetails.class));
            }
        });
    }
    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        try
        {
            list.clear();
            Gson g=new Gson();
            JSONArray jsonArray=new JSONArray(str);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                ServerLeaveModel model = g.fromJson(jsonObject.getJSONObject("leaveApplication").toString(), ServerLeaveModel.class);
                LeaveModel m= model.toLeaveModel();
                try
                {
                    model.leaveDetails=new ArrayList<>();
                    JSONArray arr=jsonObject.getJSONArray("leaveDetails");
                    for(int j=0;j<arr.length();j++)
                    {
                        ServerLeaveDetails mOb = g.fromJson(arr.getJSONObject(j).toString(), ServerLeaveDetails.class);

                        model.leaveDetails.add(mOb);
                        m.leaveDetails.add(mOb.toLeaveDetils());
                    }
                }
                catch (Exception ex){}
                if(!m.DeleteStatus)
                  list.add(m);

            }

        }
        catch (Exception ex)
        {}
        setListview();
    }
}
