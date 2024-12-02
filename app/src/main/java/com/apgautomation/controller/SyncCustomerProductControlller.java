package com.apgautomation.controller;

import android.content.Context;
import android.util.Log;

import com.apgautomation.model.GsonCustomerProduct;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncParsingUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.internal.IOException;

public class SyncCustomerProductControlller implements  DownloadUtility, com.apgautomation.utility.serverutility.ParsingUtilities {
    Context context;
  //= Realm.getInstance(context);
    public SyncCustomerProductControlller(Context context)
    {
         this.context=context;
         try {
             Thread.sleep(600);
            // Realm realm = Realm.getDefaultInstance();
         }
         catch (io.realm.exceptions.RealmMigrationNeededException ex)
         {
            // RealmConfiguration.Builder builder=    RealmConfiguration.Builder;//.deleteRealmIfMigrationNeeded();
         }
         catch (Exception ex)
         {}
    }
    public RealmResults<GsonCustomerProduct> getCustomerWithProduct()
    {
        Realm realm=Realm.getDefaultInstance();
        return realm.where(GsonCustomerProduct.class).findAll();
    }
    public long GeCustProductCount()
    {
        Realm realm=Realm.getDefaultInstance();
            return   realm.where(GsonCustomerProduct.class).count();
    }
    public RealmResults<GsonCustomerProduct>   getCustomerWithProductWithGroup(int GroupId)
    {
        if(GroupId==0)
            return  getCustomerWithProduct();
        Realm realm=Realm.getDefaultInstance();
        //ArrayList<GsonCustomerProduct> list = new ArrayList(realm.where(GsonCustomerProduct.class).findAll());

        return realm.where(GsonCustomerProduct.class).equalTo("GroupId",GroupId).findAll();
    }
    public   ArrayList<GsonCustomerProduct>    getCustomerWithProductCustomerId(int CustomerId)
    {

        Realm realm=Realm.getDefaultInstance();
        //ArrayList<GsonCustomerProduct> list = new ArrayList(realm.where(GsonCustomerProduct.class).findAll());

       RealmResults<GsonCustomerProduct> cp= realm.where(GsonCustomerProduct.class).equalTo("CustomerId",CustomerId).findAll();
          ArrayList<GsonCustomerProduct>  list=new ArrayList<>(cp );
        return list;
    }
    void sync()
    {
        //SyncGroupAndEqipment
        //SyncCustomerProduct
        //http://117.218.170.92/app/Service1.svc/SyncCustomer
        removeBeforeSync();
        String url= CommonShare.url+"Service1.svc/SyncCustomerProduct";
        AsyncParsingUtilities utilities=new AsyncParsingUtilities(context,false,url,null,1,this,this);
       // utilities.setProgressDialoaugeVisibility(false);
        utilities.execute();
    }
    public void syncInsiviual()
    {
        //SyncGroupAndEqipment
        //SyncCustomerProduct
        //http://117.218.170.92/app/Service1.svc/SyncCustomer
        removeBeforeSync();
        String url= CommonShare.url+"Service1.svc/SyncCustomerProduct";
        AsyncParsingUtilities utilities=new AsyncParsingUtilities(context,false,url,null,2,this,this);
        // utilities.setProgressDialoaugeVisibility(false);
        utilities.execute();
    }
    void removeBeforeSync()
    {
        Realm realm=Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(GsonCustomerProduct.class);
        realm.commitTransaction();
        Log.d("APG","Record Deleted");
    }

    @Override
    public boolean parseStreamingData(InputStream stream)
    {
        try
        {
            // InputStream stream = new ByteArrayInputStream(str.getBytes("UTF-8"));
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            reader.beginArray();
            Realm realm=Realm.getDefaultInstance();
            int counter=0;
            while (reader.hasNext())
            {
                if(realm.isClosed())
                    realm=Realm.getDefaultInstance();
                GsonCustomerProduct gsonCustomerProduct = gson.fromJson(reader, GsonCustomerProduct.class);
               if(gsonCustomerProduct!=null && gsonCustomerProduct.RecId>0) {
                   gsonCustomerProduct.convertDates();
                   realm.beginTransaction();
                   realm.copyToRealmOrUpdate(gsonCustomerProduct);
                   realm.commitTransaction();
               }
                counter++;
                if(counter%300==0)
                {
                    try {
                        realm.close();
                        Thread.sleep(500);
                    }
                    catch (Exception ex){}
                }
            }
            reader.endArray();
            if(!realm.isClosed())
              realm.close();
            return  true;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();

        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
        catch (Exception e)
        {
        }

        return false;
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

        if(requestCode==1) {
            context.getSharedPreferences("Version2", context.MODE_PRIVATE).edit().putBoolean("Version2", true).commit();
            context.getSharedPreferences("LastCustomerSync", context.MODE_PRIVATE).edit().putLong("time", System.currentTimeMillis()).commit();
            context.getSharedPreferences("LastCustomerSync", context.MODE_PRIVATE).edit().putLong("lastProduct", System.currentTimeMillis()).commit();
        }

        if(requestCode==2) {
            context.getSharedPreferences("Version2", context.MODE_PRIVATE).edit().putBoolean("Version2", true).commit();
            context.getSharedPreferences("LastCustomerSync", context.MODE_PRIVATE).edit().putLong("lastProduct", System.currentTimeMillis()).commit();
        }
    }

    public ArrayList<GsonCustomerProduct> getCustomerWithProductGroupId(int GroupId) {
        Realm realm=Realm.getDefaultInstance();
        //ArrayList<GsonCustomerProduct> list = new ArrayList(realm.where(GsonCustomerProduct.class).findAll());

        RealmResults<GsonCustomerProduct> cp= realm.where(GsonCustomerProduct.class).equalTo("GroupId",GroupId).findAll();
        ArrayList<GsonCustomerProduct>  list=new ArrayList<>(cp );
        return list;
    }
    public ArrayList<GsonCustomerProduct> getCustomerWithProductGroupIdAndCustomerId(int GroupId,int CustomerId) {
        Realm realm=Realm.getDefaultInstance();
        //ArrayList<GsonCustomerProduct> list = new ArrayList(realm.where(GsonCustomerProduct.class).findAll());

        RealmResults<GsonCustomerProduct> cp= realm.where(GsonCustomerProduct.class).equalTo("GroupId",GroupId).and().equalTo("CustomerId",CustomerId).findAll();
        ArrayList<GsonCustomerProduct>  list=new ArrayList<>(cp );
        return list;
    }
    public GsonCustomerProduct getProductWithProductId(int RecId) {
        Realm realm=Realm.getDefaultInstance();
        //ArrayList<GsonCustomerProduct> list = new ArrayList(realm.where(GsonCustomerProduct.class).findAll());

        GsonCustomerProduct cp= realm.where(GsonCustomerProduct.class).equalTo("RecId",RecId).findFirst();

        return cp;
    }

    public ArrayList<GsonCustomerProduct> getAllocatedCustomer(int EmpId) {

       float f=EmpId;
        Realm realm=Realm.getDefaultInstance();
        //ArrayList<GsonCustomerProduct> list = new ArrayList(realm.where(GsonCustomerProduct.class).findAll());
          //                                                                                      EngineerId
        RealmResults<GsonCustomerProduct> cp= realm.where(GsonCustomerProduct.class).equalTo("EngineerId",f).sort("LastVisitMilliSecond", Sort.ASCENDING).findAll();
      //  RealmResults<GsonCustomerProduct> cp= realm.where(GsonCustomerProduct.class).sort("LastVisitMilliSecond", Sort.ASCENDING).findAll();
        ArrayList<GsonCustomerProduct>  list=new ArrayList<>(cp );
     /*   ArrayList<GsonCustomerProduct>  mlist=new ArrayList<>( );
        for (GsonCustomerProduct m:list
             ) {
            if(m.getEngineerId()==f)
            {
                mlist.add(m);
            }
        }*/
        return list;
    }

    public ArrayList<GsonCustomerProduct> getProductListBySearch(String text)
    {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonCustomerProduct> cp= realm.where(GsonCustomerProduct.class)
                .contains("SerialNumber",text).
                        or().contains("EquipmentName",text,Case.INSENSITIVE).
                        or().contains("CustomerName",text,Case.INSENSITIVE).
                        or().contains("GroupName",text,Case.INSENSITIVE).
                        findAll();
        ArrayList<GsonCustomerProduct>  list=new ArrayList<>(cp );
       /* ArrayList<InterfaceCustomerProduct>  list1=new ArrayList<InterfaceCustomerProduct>( );
        for (GsonCustomerProduct item: list)
        {
            list1.add(item);
        }*/
        return list;
    }
}
