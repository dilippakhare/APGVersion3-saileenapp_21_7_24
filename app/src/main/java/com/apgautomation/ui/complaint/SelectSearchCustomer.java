package com.apgautomation.ui.complaint;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.controller.SyncCustomerControlller;
import com.apgautomation.model.GSONCustomerMasterBean;
import com.apgautomation.ui.customer.UserCreation;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;

public class SelectSearchCustomer extends AppCompatActivity {

    ListView listview;
    EditText edsearch;
    ArrayList<GSONCustomerMasterBean> custList;

    public String type="complaint";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_search_customer);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Customer to book complaint");
        listview=findViewById(R.id.listview);
        edsearch=findViewById(R.id.edsearch);
        edsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                 serach(editable.toString());
            }
        });

        serach("");

       /* if(CommonShare.getUserId(this)==2)
        {
            listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                    UserCreation.   GroupId=(int)custList.get(i).GroupId;
                    UserCreation.GroupName=custList.get(i).GroupName;

                    startActivity(new Intent(SelectSearchCustomer.this, UserCreation.class));
                    return false;
                }
            });
        }*/
        try {
            type = getIntent().getExtras().getString("type");
            if(type==null)
                type="Complaint";
        }
        catch (Exception ex){}
    }
    void serach(String text)
    {
         SyncCustomerControlller controlller=new SyncCustomerControlller(this);
         custList= controlller.getCustomerListwithFilterVerified(text);
         ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,custList);
         listview.setAdapter(adapter);

         listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                 if(CommonShare.getUserId(SelectSearchCustomer. this)==2   &&(type!=null&&!type.equalsIgnoreCase("Muterial")))
                 {
                     UserCreation.GroupId = (int) custList.get(i).getGroupId();
                     UserCreation.GroupName = custList.get(i).getGroupName();

                     startActivity(new Intent(SelectSearchCustomer.this, UserCreation.class));
                 }
                 else
                 {
                     if(type.equalsIgnoreCase("Complaint")) {
                         BookComplaint.CustObject = custList.get(i);
                         startActivity(new Intent(SelectSearchCustomer.this, BookComplaint.class));
                     }
                     if(type.equalsIgnoreCase("Muterial")) {
                         finish();
                         BookComplaint.CustObject = custList.get(i);
                         startActivity(new Intent(SelectSearchCustomer.this, SelectProducts.class).putExtra("type","Muterial"));
                     }
                 }
             }
         });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
