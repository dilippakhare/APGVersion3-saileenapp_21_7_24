package com.apgautomation.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.controller.SyncCustomerProductControlller;
import com.apgautomation.model.GsonCustomerProduct;
import com.apgautomation.ui.adapter.ProductSelectAdapter;

import java.util.ArrayList;

public class SelectSearchCustomerProduct extends AppCompatActivity {

    ListView listview;
    EditText edsearch;
    ArrayList<GsonCustomerProduct> custList;
    public static  GsonCustomerProduct selectProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectProduct=null;
        setContentView(R.layout.searchproduct);
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
    }
    void serach(String text)
    {
        SyncCustomerProductControlller controlller=new SyncCustomerProductControlller(this);
        custList= controlller.getProductListBySearch(text);
        ProductSelectAdapter adapter=new ProductSelectAdapter(this,android.R.layout.simple_list_item_1  ,custList);
        listview.setAdapter(adapter);

         listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
             {

                   //  startActivity(new Intent(SelectSearchCustomerProduct.this, BookComplaint.class));
             }
         });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void callBack() {

        setResult(RESULT_OK);
        setIntent(new Intent());
        finish();
    }
}
