package com.apgautomation.ui.customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apgautomation.R;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserCreation extends AppCompatActivity implements View.OnClickListener, DownloadUtility {


    EditText edUnm,edPass,edContactname,edMob;
    public static int GroupId;
    public static String GroupName;
    TextView txtUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtUsers=findViewById(R.id.txtUsers);
        edUnm=findViewById(R.id.edUnm);
        edPass=findViewById(R.id.edPass);
        edContactname=findViewById(R.id.edContactname);
        edMob=findViewById(R.id.edMob);

        findViewById(R.id.btn).setOnClickListener(this);


        String url= CommonShare.url+"Service1.svc/GetCustomerList?GroupId="+GroupId;

        AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",2,this);
        utilities.execute();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("User Creation");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View view)
    {
        try
        {
            String url= CommonShare.url+"Service1.svc/CreateCustomerUser?GroupId="+GroupId+"&unm="+edUnm.getText().toString()+"&pass="+edPass.getText().toString()+"&contactnm="+edContactname.getText().toString().replace(" ","%20" )+"&mobile="+edMob.getText().toString();

            AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",1,this);
            utilities.execute();
        }
        catch (Exception ex)
        {}
    }

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

                        new CommonShare().send("User Of "+GroupName,"User Of "+GroupName+"\n"+txtUsers.getText().toString(),UserCreation.this);
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
                JSONObject jsonObject=arr.getJSONObject(i);
                if(jsonObject.getString("uname").equals(edUnm.getText().toString()))
                {
                    Toast.makeText(this,"User Created Successfully",Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(this,UserCreation.class));
                    falg=1;
                    break;
                }
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
