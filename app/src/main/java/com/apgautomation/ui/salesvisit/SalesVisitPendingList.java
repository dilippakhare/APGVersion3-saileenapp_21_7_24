package com.apgautomation.ui.salesvisit;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.apgautomation.R;
import com.apgautomation.controller.SaleVisitController;
import com.apgautomation.model.ServerModel.GsonSalesVisit;
import com.apgautomation.ui.adapter.SalesVisitScheduledapter;
import com.apgautomation.utility.serverutility.DownloadUtility;

import java.util.ArrayList;

public class SalesVisitPendingList extends AppCompatActivity implements DownloadUtility, AdapterView.OnItemClickListener {

    SaleVisitController controller;
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_visit_pending_list);
        listview=findViewById(R.id.listview);
        controller=new SaleVisitController(this);
        controller.callScheduleVisit();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Visit Schedule List");
        setAdapter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(android.R.id.home==item.getItemId())
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    ArrayList<GsonSalesVisit>  mList;
    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
       // CommonShare.alert(this,str);
        setAdapter();
    }
    void setAdapter()
    {
        mList=controller.getPendingSalesVisit();
        if(mList.size() ==0)
        {
            Toast.makeText(this, "Sales Visit not found", Toast.LENGTH_SHORT).show();
        }
        SalesVisitScheduledapter adapter=new SalesVisitScheduledapter(this,1,mList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        startActivity(new Intent(this,SalesVisitDetails.class).putExtra("VisitToken",mList.get(i).getVisitTokenId()));
    }
}
