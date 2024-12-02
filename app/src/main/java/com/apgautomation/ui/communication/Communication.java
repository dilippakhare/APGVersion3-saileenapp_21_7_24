package com.apgautomation.ui.communication;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.model.GsonAskQuery;
import com.apgautomation.ui.adapter.CommunicationAdapter;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.google.gson.Gson;

import org.json.JSONArray;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class Communication extends AppCompatActivity   implements DownloadUtility
{

    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(Communication.this,AskQuery.class));

            }
        });
        listview=findViewById(R.id.listview);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ask Query");
        setAdapter();
        refresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    RealmResults<GsonAskQuery>  list;
    void setAdapter()
    {
        Realm realm=Realm.getDefaultInstance();
        list= realm.where(GsonAskQuery.class).equalTo("IsParentMsg",true).findAll().sort("EnterDateMillisecond", Sort.DESCENDING);
       // Collections.reverse(list);

        CommunicationAdapter adapter=new CommunicationAdapter(this,1,list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(Communication.this,CommunicationDetails.class).putExtra("Token",list.get(i).getToken()));
            }
        });
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

        try
        {
            str=str.trim();
            Realm realm=Realm.getDefaultInstance();
            Gson g=new Gson();
            JSONArray array=new JSONArray(str);
            for(int i=0;i<array.length();i++)
            {
                GsonAskQuery  c=  g.fromJson(array.getJSONObject(i).toString(),GsonAskQuery.class);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(c);
                realm.commitTransaction();
            }

        }
        catch (Exception ex)
        {}

        setAdapter();
    }
    private void refresh()
    {
        String url= CommonShare.url+"Service1.svc/GetAskQuery?EmpId="+CommonShare.getEmpId(this);
        AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",1,this);
        utilities.execute();
    }
}
