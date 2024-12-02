package com.apgautomation.ui.complaint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.controller.SyncCustomerProductControlller;
import com.apgautomation.model.GsonCustomerProduct;
import com.apgautomation.model.InterfaceCustomerProduct;
import com.apgautomation.model.ServerModel.ServerCustomerProducts;
import com.apgautomation.ui.adapter.ProductComplaintAdapter;
import com.google.gson.Gson;
import com.apgautomation.ui.quotation.MuterialRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectProducts extends AppCompatActivity {


    public static     int mGroupId=0, mCustomerID=0;;
    ListView listview;
    boolean isForGroup=false;
    int GroupId=0;

    public  static   GsonCustomerProduct visitSelectionProduct;

    public  String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);
        visitSelectionProduct=null;
        try
        {
            GroupId= getIntent().getExtras().getInt("GroupId");
            if(GroupId>0)
                isForGroup=true;
        }
        catch (Exception ex)
        {}
        try
        {
            type=getIntent().getExtras().getString("type");
        }
        catch (Exception ex){}
        if(isForGroup)
        {
            parseProductsAPGwithGroupId(GroupId);
        }
        if(type!=null && type.equalsIgnoreCase("Muterial"))
        {
            parseProductsAPGwithGroupId((int)BookComplaint.CustObject.GroupId);
        }
        if(type!=null && type.equalsIgnoreCase("Installation"))
        {
            BookComplaint.model=null;

            parseProductsAPGwithGroupIdANdCustomer(mGroupId,mCustomerID);
        }
        else if(BookComplaint.CustObject==null)
        {
            parseProducts();
        }
        else
        {
            parseProductsAPG();
        }
        listview=(ListView)findViewById(R.id.listview);

        ProductComplaintAdapter adapter=new ProductComplaintAdapter(this,android.R.layout.simple_list_item_1  ,list);
        listview.setAdapter(adapter);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Products");
    }

    private void parseProductsAPGwithGroupIdANdCustomer(int mGroupId, int mCustomerID) {

        try {
            Gson gson=new Gson();
            SyncCustomerProductControlller controlller=new SyncCustomerProductControlller(this);
            ArrayList<GsonCustomerProduct> pList=   controlller.getCustomerWithProductGroupIdAndCustomerId(mGroupId,mCustomerID);
            list.addAll(pList);

        }
        catch (Exception ex){

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    ArrayList<InterfaceCustomerProduct> list=new ArrayList<>();
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
    void parseProductsAPG()
    {
        try {
             Gson gson=new Gson();
             SyncCustomerProductControlller controlller=new SyncCustomerProductControlller(this);
             ArrayList<GsonCustomerProduct> pList=   controlller.getCustomerWithProductCustomerId(BookComplaint.CustObject.getCustomerId());

             list.addAll(pList);

        }
        catch (Exception ex){

        }
    }
    void parseProductsAPGwithGroupId(int GroupId)
    {
        try {
            Gson gson=new Gson();
            SyncCustomerProductControlller controlller=new SyncCustomerProductControlller(this);
            ArrayList<GsonCustomerProduct> pList=   controlller.getCustomerWithProductGroupId(GroupId);

            list.addAll(pList);

        }
        catch (Exception ex){

        }
    }
   public  void setData(InterfaceCustomerProduct model)
    {

        if(type!=null&&type.equalsIgnoreCase("Muterial"))
        {
            Intent intent = new Intent();
            //intent.setData(model);
            try {
                if (isForGroup)
                    visitSelectionProduct = (GsonCustomerProduct) model;
            } catch (Exception ex) {
            }
            visitSelectionProduct = (GsonCustomerProduct) model;
            startActivity(new Intent(this, MuterialRequest.class));
            finish();
        }
       else  if(type!=null&&type.equalsIgnoreCase("Installation"))
        {
            Intent intent = new Intent();
            visitSelectionProduct = (GsonCustomerProduct) model;
            setResult(RESULT_OK, intent);
            finish();
        }
        else {
            Intent intent = new Intent();
            //intent.setData(model);
            try {
                if (isForGroup)
                    visitSelectionProduct = (GsonCustomerProduct) model;
            } catch (Exception ex) {
            }
            BookComplaint.model = model;
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
