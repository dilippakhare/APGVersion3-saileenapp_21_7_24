package com.apgautomation.ui.visit;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.controller.SyncCustomerProductControlller;
import com.apgautomation.controller.VisitModuleController;
import com.apgautomation.model.GsonCustomerProduct;
import com.apgautomation.ui.adapter.ProductLastVisitAdapter;
import com.apgautomation.ui.adapter.VisitScheduledapter;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.DownloadUtility;

import java.util.ArrayList;


public class VisitCustomerList extends AppCompatActivity implements DownloadUtility {

    VisitModuleController controller;
    SyncCustomerProductControlller productControlller;
    ListView listview1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_list);
        listview1=findViewById(R.id.listview1);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Customer List With Products");
       // controller=new VisitModuleController(this);
       // controller.loadPendingVisits();
        //controller.setInitial();
       // setAdapter();
       /* listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String token= controller.scheduleList.get(i).getVisitToken();
                    startActivity(new Intent(VisitCustomerList.this,ProcessVisit.class).putExtra("VisitToken", token));
            }
        });*/

        //controller.getAllocatedCustomer();
        productControlller=new SyncCustomerProductControlller(this);
        list= productControlller.getAllocatedCustomer(CommonShare.getEmpId(this));
        setAdapter();
    }
    ArrayList<GsonCustomerProduct> list;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

        setAdapter();
        if(requestCode==1 &&  responseCode==200 )
        {
           // setAdapter();
          //  adapter.notifyDataSetChanged();
        }
    }
    VisitScheduledapter adapter;
    void setAdapter()
    {
       // adapter =new VisitScheduledapter(this,R.layout.item_visitschedule,controller.scheduleList);
       // listview1.setAdapter(adapter);
        ProductLastVisitAdapter adapter=new ProductLastVisitAdapter(this,R.layout.item_productlastvisit,list);
        listview1.setAdapter(adapter);
    }
}
