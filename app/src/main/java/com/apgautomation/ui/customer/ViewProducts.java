package com.apgautomation.ui.customer;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.model.ServerModel.ServerCustomerProducts;
import com.apgautomation.ui.adapter.ProductAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewProducts extends AppCompatActivity {


    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);
        parseProducts();
        listview=(ListView)findViewById(R.id.listview);

        ProductAdapter adapter=new ProductAdapter(this,android.R.layout.simple_list_item_1  ,list);
        listview.setAdapter(adapter);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Products");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    ArrayList<ServerCustomerProducts> list=new ArrayList<>();
    void parseProducts()
    {
        try {
            String str=   getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONArray array=  jsonObject.getJSONArray("productList");
            Gson gson=new Gson();
            for(int i=0;i<array.length();i++)
            {
                String strObj=array.getJSONObject(i).toString();
                ServerCustomerProducts p=gson.fromJson(strObj,ServerCustomerProducts.class);
                list.add(p);
            }
           // return jObj.getInt("UserId");
        }
        catch (Exception ex){

        }
    }
}
