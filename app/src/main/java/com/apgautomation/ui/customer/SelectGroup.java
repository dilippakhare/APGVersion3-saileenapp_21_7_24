package com.apgautomation.ui.customer;

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
import com.apgautomation.model.GsonGroup;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;

public class SelectGroup extends AppCompatActivity {

    ListView listview;
    EditText edsearch;
    ArrayList<GsonGroup> mainList;
    ArrayList<GsonGroup> searchList;
    boolean IsSales=false;
    public  static  GsonGroup selectedGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);
        selectedGroup=null;
        try
        {
            IsSales=getIntent().getExtras().getBoolean("IsSales",false);
        }
        catch (Exception dex)
        {}
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Customer Group");
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

        if(IsSales)
        {
            mainList = CommonShare.getGroups(this);
        }
        else {
            mainList = CommonShare.getGroupsVerified(this);
        }
         serach("");

    }
    void serach(String text)
    {
        //SyncCustomerControlller controlller=new SyncCustomerControlller(this);
        //custList= controlller.getCustomerListwithFilter(text);
        if(text.equalsIgnoreCase(""))
            searchList=mainList;
        else
        {
            searchList=new ArrayList<>();
            for (GsonGroup g:mainList
                 ) {
                try {

                    if (g.getGroupName().toUpperCase().contains(text.toUpperCase())) {
                        searchList.add(g);
                    }
                }
                catch (Exception ex){}

            }
        }
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,searchList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              //  BookComplaint.CustObject= custList.get(i);
             //   startActivity(new Intent(SelectSearchCustomer.this,BookComplaint.class));
                selectedGroup=searchList.get(i);
                setResult(RESULT_OK,new Intent());
                finish();

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
