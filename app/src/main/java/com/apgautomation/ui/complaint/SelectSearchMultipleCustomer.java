package com.apgautomation.ui.complaint;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.controller.SyncCustomerControlller;
import com.apgautomation.model.GSONCustomerMasterBeanExtends;
import com.apgautomation.ui.adapter.MultipleCustomerSelectionAdapter;

import java.util.ArrayList;

public class SelectSearchMultipleCustomer extends AppCompatActivity {

    ListView listview;
    EditText edsearch;
    public  static  ArrayList<GSONCustomerMasterBeanExtends> selectedCustomer=new ArrayList<>();
    ArrayList<GSONCustomerMasterBeanExtends> custList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_search_multiple_customer);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Customer");
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


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult();
            }
        });
    }
    void serach(String text)
    {
         SyncCustomerControlller controlller=new SyncCustomerControlller(this);
         custList= controlller.getCustomerListwithFilterNonRelam(text);
         MultipleCustomerSelectionAdapter adapter=new MultipleCustomerSelectionAdapter(this,android.R.layout.simple_list_item_1,custList);
         listview.setAdapter(adapter);

    }
    void setResult()
    {
        try
        {
            for (GSONCustomerMasterBeanExtends obj:custList
                 )
            {
                  if(obj.isSelect())
                  {
                      selectedCustomer.add(obj);
                  }
            }
            setResult(RESULT_OK);
            finish();
        }
        catch (Exception ex)
        {}
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
