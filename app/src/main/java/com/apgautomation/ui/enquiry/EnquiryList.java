package com.apgautomation.ui.enquiry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.ui.adapter.EnquiryAdapter;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

public class EnquiryList extends AppCompatActivity implements DownloadUtility {

    private ArrayList<EnquiryDTO> list=new ArrayList<>();
    ListView listview;
    private View floatingActionButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry_list3);
        listview=findViewById(R.id.listview);
        AsyncUtilities utilities=new AsyncUtilities(this,false, CommonShare.url+"service1.svc/GetEnquiries?empid="+CommonShare.getEmpId(this),null,1,this);
        utilities.execute();

        floatingActionButton2=findViewById(R.id.floatingActionButton2);
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EnquiryList.this, AddEnquiry.class));
            }
        });
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Enquiry List");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddEnquiry.EditEnquiry=list.get(position);
                startActivity(new Intent(EnquiryList.this,AddEnquiry.class).putExtra("IsEdit",true));
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        if(responseCode==200)
        {
            if(requestCode==1)
            {
                try {
                    Gson g = new Gson();
                    JSONArray array = new JSONArray(str);
                    for (int i = 0; i < array.length(); i++) {
                        EnquiryDTO model = g.fromJson(array.getJSONObject(i).toString(), EnquiryDTO.class);
                        list.add(model);
                    }
                    listview.setAdapter(new EnquiryAdapter(this, android.R.layout.simple_list_item_1,list));
                }
                catch (Exception ex){}
            }
        }
    }
}