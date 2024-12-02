package com.apgautomation.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apgautomation.R;
import com.apgautomation.model.GSONCustomerMasterBeanExtends;
import com.apgautomation.ui.complaint.SelectSearchMultipleCustomer;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserCreation2 extends AppCompatActivity implements View.OnClickListener, DownloadUtility {


    EditText edUnm,edPass,edContactname,edMob;
    ListView listview;

    ArrayList<GSONCustomerMasterBeanExtends> selectedCustomer=new ArrayList<>();
    TextView txtUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listview=findViewById(R.id.listview);
        SelectSearchMultipleCustomer.selectedCustomer.clear();;

        txtUsers=findViewById(R.id.txtUsers);
        edUnm=findViewById(R.id.edUnm);
        edPass=findViewById(R.id.edPass);
        edContactname=findViewById(R.id.edContactname);
        edMob=findViewById(R.id.edMob);

        findViewById(R.id.btn).setOnClickListener(this);


       // String url= CommonShare.url+"Service1.svc/GetCustomerList?GroupId="+GroupId;

       // AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",2,this);
       // utilities.execute();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("User Creation");
        findViewById(R.id.btnSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(UserCreation2.this, SelectSearchMultipleCustomer.class),101);
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                 selectedCustomer.remove(i);
                 adapter.notifyDataSetChanged();
                 return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==101)
            {
                for (GSONCustomerMasterBeanExtends obj:SelectSearchMultipleCustomer.selectedCustomer)
                {
                    int flag=0;
                    for (GSONCustomerMasterBeanExtends obj1:selectedCustomer)
                    {
                       if(obj.getCustomerId()==obj1.getCustomerId())
                           flag=1;
                    }
                    if(flag==0)
                        selectedCustomer.add(obj);
                }
                //selectedCustomer.addAll(SelectSearchMultipleCustomer.selectedCustomer);
                adapter =new ArrayAdapter(this,android.R.layout.simple_list_item_1,selectedCustomer);
                listview.setAdapter(adapter);
            }
        }
    }
    ArrayAdapter adapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View view)
    {
        custids="";
        try
        {
            for (GSONCustomerMasterBeanExtends obj:selectedCustomer)
            {
                if(custids=="")
                    custids=obj.getCustomerId()+"";
                else
                    custids=custids+","+obj.getCustomerId()+"";
            }

            String url= CommonShare.url+"Service1.svc/CreateCustomerUser2?custids="+custids+"&unm="+edUnm.getText().toString()+"&pass="+edPass.getText().toString()+"&contactnm="+edContactname.getText().toString().replace(" ","%20" )+"&mobile="+edMob.getText().toString();

            AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",1,this);
            utilities.execute();
        }
        catch (Exception ex)
        {}
    }
    String custids="";
    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        String uText="";
        if(requestCode==2)
        {
                try {

                    JSONArray arr=new JSONArray(str);
                    for(int i=0;i<arr.length();i++) {
                        JSONObject jsonObject = arr.getJSONObject(i);
                        if(i==0) {
                            uText = (i + 1) + ".  User Name :- " + jsonObject.getString("uname") + "\n    Pass :- " + jsonObject.getString("upass");
                        }
                        else
                        {
                            uText =uText+"\n"+ (i + 1) + ".  User Name :- " + jsonObject.getString("uname") + "\n    Pass :- " + jsonObject.getString("upass");

                        }
                    }
                }
                catch (Exception ex)
                {}
                txtUsers.setText(uText);
                txtUsers.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                       // new CommonShare().send("User Of "+GroupName,"User Of "+GroupName+"\n"+txtUsers.getText().toString(), UserCreation2.this);
                        return false;
                    }
                });
        }
        else if(requestCode==1)
        try
        {
            int falg=0;
            JSONArray arr=new JSONArray(str);


            for(int i=0;i<arr.length();i++)
            {
                JSONObject jsonObject = arr.getJSONObject(i);
                if(i==0) {
                    uText = (i + 1) + ".  User Name :- " + jsonObject.getString("uname") + "\n    Pass :- " + jsonObject.getString("upass");
                }
                else
                {
                    uText =uText+"\n"+ (i + 1) + ".  User Name :- " + jsonObject.getString("uname") + "\n    Pass :- " + jsonObject.getString("upass");
                }
                falg=1;

            }
            if (falg==1)
            {
                Toast.makeText(this,"User Created",Toast.LENGTH_LONG).show();
                new CommonShare().send(txtUsers.getText().toString(),txtUsers.getText().toString(), UserCreation2.this);
                finish();
            }
            if (falg==0)
            {
                Toast.makeText(this, "User Name Allready Exist", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ex)
        {}

    }
}
