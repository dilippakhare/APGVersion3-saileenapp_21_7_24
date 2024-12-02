package com.apgautomation.ui.quotation;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.apgautomation.R;
import com.apgautomation.model.ModelMuterialRequestList;
import com.apgautomation.ui.adapter.MuterialAdapter;
import com.apgautomation.ui.complaint.SelectSearchCustomer;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;

import org.json.JSONArray;

import java.util.ArrayList;

public class MuteilaRequestList extends AppCompatActivity implements DownloadUtility {

    ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muteila_request_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listview=findViewById(R.id.listview);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //  startActivity(new Intent(MuteilaRequestList.this,MuterialRequest.class));
                startActivity(new Intent(MuteilaRequestList.this, SelectSearchCustomer.class).putExtra("type","Muterial"));
            }
        });
        listview=findViewById(R.id.listview);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Material Request");

        String url= CommonShare.url+"Service1.svc/GetMuterialRequest";
        AsyncUtilities utilities=new AsyncUtilities(this,false,url,null,1,this);
        utilities.execute();
        MuterialRequest.editModel=null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    ArrayList<ModelMuterialRequestList>  list=new ArrayList<>();
    @Override
    public void onComplete(String str, int requestCode, int responseCode) {


        try
        {
            Gson gson=new Gson();
            JSONArray array=new JSONArray(str);

            for(int i=0;i<array.length();i++)
            {
                ModelMuterialRequestList m=gson.fromJson(array.getJSONObject(i).toString(),ModelMuterialRequestList.class);
                list.add(m);
               // list.add(m);
            }
        }
        catch (Exception ex){}

        MuterialAdapter adapter=new MuterialAdapter(this, android.R.layout.simple_list_item_1,list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MuterialRequest.editModel=list.get(position);
                startActivity(new Intent(MuteilaRequestList.this,MuterialRequest.class).putExtra("mode",1));
            }
        });
    }
}