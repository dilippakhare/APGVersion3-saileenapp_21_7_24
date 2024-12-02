package com.apgautomation.ui.attendance;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.model.ServerModel.EmployeeAttendanceListModel;
import com.apgautomation.ui.adapter.EmployeeDateWiseAttenacneAdapter;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

public class EmployeeRecentAttenance extends AppCompatActivity implements DownloadUtility {

    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_recent_attenance);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Datewise Attenance");
        listview=findViewById(R.id.listview);

        String url= CommonShare.url+"Service1.svc/GetAttendanceRecords?ManagerId="+0+"&EmpId="+CommonShare.getEmpId(this)+"&Mode="+2;
        AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",1,this);
        utilities.execute();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewAttenadnceDetails.model=list.get(i);
                startActivity(new Intent(EmployeeRecentAttenance.this,ViewAttenadnceDetails.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    ArrayList<EmployeeAttendanceListModel> list=new ArrayList<>();

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(responseCode==200)
      try
      {
          list.clear();
          try
          {
              Gson gson=new Gson();
              JSONArray arr=new JSONArray(str);
              for(int i=0;i<arr.length();i++)
              {
                  EmployeeAttendanceListModel model=(EmployeeAttendanceListModel)  gson.fromJson(arr.getJSONObject(i).toString(),EmployeeAttendanceListModel.class);
                  list.add(model);
              }
              EmployeeDateWiseAttenacneAdapter adapter=new EmployeeDateWiseAttenacneAdapter(this,R.layout.item_recent_attendance,list);
              listview.setAdapter(adapter);
              //CommonShare.alert(this,list.size()+"");
          }
          catch (Exception ex)
          {}
      }
      catch (Exception ex){}
    }
}
