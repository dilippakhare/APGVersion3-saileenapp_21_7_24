package com.apgautomation.ui.salesvisit;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.apgautomation.R;
import com.apgautomation.controller.SaleVisitController;
import com.apgautomation.model.ServerModel.GsonSalesVisit;
import com.apgautomation.ui.adapter.SalesVisitScheduledapter;
import com.apgautomation.ui.visit.FilterOption;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.DownloadUtility;

import java.util.ArrayList;

public class SalesVisitAllList extends AppCompatActivity implements DownloadUtility, AdapterView.OnItemClickListener {

    SaleVisitController controller;
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_visit_pending_list);
        listview=findViewById(R.id.listview);
        controller=new SaleVisitController(this);
        controller.callAllVisit();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Visit List");
        setAdapter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(android.R.id.home==item.getItemId())
            onBackPressed();
        if(item.getItemId()==R.id.action_filter)
        {
            startActivityForResult(new Intent(this, FilterOption.class).putExtra("IsSales",true)  ,400);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menufilter,menu);

        return super.onCreateOptionsMenu(menu);
    }
    ArrayList<GsonSalesVisit>  mList;
    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
       // CommonShare.alert(this,str);
        if(requestCode==1)
            setAdapter();
         else
        {
            mList=controller.pendingVisitList;
            if(mList.size() ==0)
            {
                Toast.makeText(this, "Visit record not found", Toast.LENGTH_SHORT).show();
            }
            SalesVisitScheduledapter adapter=new SalesVisitScheduledapter(this,1,mList);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(this);
        }
    }
    void setAdapter()
    {
        mList=controller.getAllVisitList();
        SalesVisitScheduledapter adapter=new SalesVisitScheduledapter(this,1,mList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        startActivity(new Intent(this,SalesVisitDetails.class).putExtra("VisitToken",mList.get(i).getVisitTokenId()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK  && requestCode==400)
        {
            int GroupId=FilterOption.GroupId;
            long FromDate=FilterOption.fromDateMiilisecond;
            long ToDate=FilterOption.toDateMillisecond;
            int EmpId=FilterOption.EmpId;


            String url= CommonShare.url+"Service1.svc/GetSalesAllVisitFilter?EmpId="+EmpId+
                    "&FromDate="+FromDate+"&ToDate="+ToDate+"&GroupId="+GroupId;
            controller.callAllVisitFilter(url);
                /*
            AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",50,this);
            utilities.execute();

             */
        }
    }
}
