     package com.apgautomation.controller;

import android.content.Context;

import com.apgautomation.model.GsonGroup;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncParsingUtilities;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.apgautomation.utility.serverutility.ParsingUtilities;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import io.realm.Realm;
import io.realm.internal.IOException;

public class SyncGroupAndEquipment implements DownloadUtility , ParsingUtilities
{
    Context context;
    public SyncGroupAndEquipment(Context context)
    {
        this.context=context;

    }
   public  void sync()
    {
        String url= CommonShare.url+"Service1.svc/SyncGroupAndEqipment";
        AsyncUtilities utilities=new AsyncUtilities(context,false,url,null,222,this);
        utilities.execute();


    }
    public  void syncInBackgroud()
    {
        String url= CommonShare.url+"Service1.svc/SyncGroupAndEqipmentInBackGround";
        AsyncUtilities utilities=new AsyncUtilities(context,false,url,null,101,this);
        utilities.hideProgressDialoge();
        utilities.execute();
    }
    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
       // CommonShare.alert(context,str+"GRRRRR");
         try
         {
            // Realm realm=Realm.getDefaultInstance();
             //JSONObject   j=new JSONObject(str);
             /*
             JSONArray array=j.getJSONArray("groups");
             try
             {
                 Gson gson=new Gson();
                 for(int i=0;i<array.length();i++)
                 {
                     GsonGroup group=  gson.fromJson(array.getJSONObject(i).toString(), GsonGroup .class);
                     realm.beginTransaction();
                     realm.copyToRealmOrUpdate(group);
                     realm.commitTransaction();
                 }
             }
             catch (Exception ex)
             {
                 Log.d("com.apgautomation",ex.toString());
             }*/
            // realm.close();

             if(requestCode==222) {
                 String url1= CommonShare.url+"Service1.svc/SyncGroupOnly";
                 AsyncParsingUtilities utilities1=new AsyncParsingUtilities(context,false,url1,null,1,this,this);
                 utilities1.execute();
                 JSONObject   j=new JSONObject(str);
                 JSONArray array1=j.getJSONArray("equipmenttype");
                 CommonShare.saveGroupAndEquipment(context, str);

             }
             if(requestCode==1)
             {
                 SyncCustomerControlller controlller = new SyncCustomerControlller(context);
                 controlller.sync();

             }
             if(requestCode==101)
             {
                 SyncCustomerControlller controlller = new SyncCustomerControlller(context);
                 controlller.syncInBackgroud();
             }
         }
         catch (Exception ex)
         {}

    }

    public GsonGroup getGroupById(int GroupId)
    {
        Realm realm=Realm.getDefaultInstance();
        GsonGroup group= realm.where(GsonGroup.class).equalTo("GroupId",GroupId).findFirst();
        return  group;
    }

    @Override
    public boolean parseStreamingData(InputStream stream)
    {
        try
        {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            reader.beginArray();
            Realm realm;   realm=Realm.getDefaultInstance();
            int counter=0;
            while (reader.hasNext())
            {
                GsonGroup gsonCustomerProduct = gson.fromJson(reader, GsonGroup.class);

                if(realm.isClosed())
                    realm=Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(gsonCustomerProduct);
                realm.commitTransaction();
                counter++;
                if(counter%500==0)
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
            e.printStackTrace();
        }

        return false;
    }

    public void syncIndiviual()
    {
        String url1= CommonShare.url+"Service1.svc/SyncGroupOnly";
        AsyncParsingUtilities utilities1=new AsyncParsingUtilities(context,false,url1,null,1000,this,this);
        utilities1.execute();

    }
}
