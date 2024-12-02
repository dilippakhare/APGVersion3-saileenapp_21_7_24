package com.apgautomation.ui.complaint;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.model.ServerModel.ServerComplaintModel;
import com.apgautomation.ui.adapter.ComplaintGridAdapter;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;

public class ViewAllComplaint extends AppCompatActivity implements DownloadUtility {

    ListView listview;
    int CustomerId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complaint);
        listview=findViewById(R.id.listview);


        try
        {

        }
        catch (Exception e)
        {}
        String url= CommonShare.url+"Service1.svc/GetAllComplaint?EmployeeId="+CommonShare.getEmpId(this);

        AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",1,this);
        utilities.execute();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Complaints");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    ArrayList<ServerComplaintModel> list=new ArrayList<>();
    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

        list.clear();
        try
        {
            Gson g=new Gson();
            JSONArray array=new JSONArray(str);
            for(int i=0;i<array.length();i++)
            {
                ServerComplaintModel model=    g.fromJson(array.getJSONObject(i).toString(), ServerComplaintModel.class);
                list.add(model);
            }
            Collections.reverse(list);
            // ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
            //listview.setAdapter(adapter);
            ComplaintGridAdapter adapter1=new ComplaintGridAdapter(this,R.layout.item_complaint_grid,list);
            adapter1.isCustomerNameVisible=true;
            listview.setAdapter(adapter1);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ViewComplaintDetails.model=list.get(i);
                    startActivity(new Intent(ViewAllComplaint.this,ViewComplaintDetails.class));

                }
            });
        }
        catch (Exception ex)
        {}
    }
}
