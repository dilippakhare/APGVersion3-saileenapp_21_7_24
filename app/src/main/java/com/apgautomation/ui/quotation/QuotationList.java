package com.apgautomation.ui.quotation;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.model.GsonQuotationRequestModel;
import com.apgautomation.ui.adapter.QuotationAdapter;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;

public class QuotationList extends AppCompatActivity implements DownloadUtility {

    ListView listview;
    FloatingActionButton floatingActionButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_list);
        listview=findViewById(R.id.listview);


        String url= CommonShare.url+ "Service1.svc/GetQuatation?EmpId="+CommonShare.getEmpId(this);
        AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",1,this);
        utilities.execute();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quotation Request");
        floatingActionButton2=findViewById(R.id.floatingActionButton2);
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuotationList.this,GenrateQuotationRequest.class));
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    ArrayList<GsonQuotationRequestModel>   list=new ArrayList<>();
    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        try
        {
            Gson gson=new Gson();
            JSONArray array=new JSONArray(str);
            for(int i=0;i<array.length();i++)
            {
                GsonQuotationRequestModel model=gson.fromJson(array.getJSONObject(i).toString(),GsonQuotationRequestModel.class);
                list.add(model);
            }

            Collections.reverse(list);
            QuotationAdapter adapter=new QuotationAdapter(this,android.R.layout.simple_list_item_1,list);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    QuotationDetails.model=list.get(i);
                    startActivity(new Intent(QuotationList.this,QuotationDetails.class));
                }
            });
        }
        catch (Exception ex)
        {}
    }
}
