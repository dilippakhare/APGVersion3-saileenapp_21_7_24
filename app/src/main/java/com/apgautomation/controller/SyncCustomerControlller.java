package com.apgautomation.controller;

import android.content.Context;

import com.apgautomation.model.GSONCustomerMasterBean;
import com.apgautomation.model.GSONCustomerMasterBeanExtends;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.ParsingUtilities;
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
import io.realm.internal.IOException;

public class SyncCustomerControlller implements ParsingUtilities, DownloadUtility, com.apgautomation.utility.serverutility.ParsingUtilities {
     Context context;
  //= Realm.getInstance(context);
     public SyncCustomerControlller(Context context)
     {
         this.context=context;

     }
    public void sync()
     {
         String url= CommonShare.url+"Service1.svc/SyncCustomer";
         AsyncParsingUtilities utilities=new AsyncParsingUtilities(context,false,url,null,1,this,this);
        // utilities.setProgressDialoaugeVisibility(false);
         utilities.execute();
     }

    public void syncIndiual()
    {
        String url= CommonShare.url+"Service1.svc/SyncCustomer";
        AsyncParsingUtilities utilities=new AsyncParsingUtilities(context,false,url,null,1000,this,this);
        // utilities.setProgressDialoaugeVisibility(false);
        utilities.execute();
    }
    public void syncInBackgroud()
    {
        String url= CommonShare.url+"Service1.svc/SyncCustomerInBackGround";
        AsyncParsingUtilities utilities=new AsyncParsingUtilities(context,false,url,null,101,this,this);
        utilities.setProgressDialoaugeVisibility(false);
       // utilities.hidePrgressDiauloge();
        utilities.execute();
    }
     public ArrayList<GSONCustomerMasterBean>   getCustomerList()
     {
         Realm realm;   realm=Realm.getDefaultInstance();
            ArrayList<GSONCustomerMasterBean>  list=new ArrayList<>(realm.where(GSONCustomerMasterBean.class).findAll());
        return list;
     }

    public   ArrayList<GSONCustomerMasterBean>  getCustomerListwithFilter(String customerName)
    {
        if(customerName.equalsIgnoreCase(""))
             return  getCustomerList();
        Realm realm;   realm=Realm.getDefaultInstance();

        ArrayList<GSONCustomerMasterBean>  list=new ArrayList<>(realm.where(GSONCustomerMasterBean.class).contains("CustomerName",customerName, Case.INSENSITIVE).
                or().contains("GroupName",customerName, Case.INSENSITIVE).
                findAll());

        return list;
    }

    public   ArrayList<GSONCustomerMasterBean>  getCustomerListwithFilterVerified(String customerName)
    {
        if(customerName.equalsIgnoreCase(""))
            return  getCustomerList();
        Realm realm;   realm=Realm.getDefaultInstance();

        ArrayList<GSONCustomerMasterBean >  list=new ArrayList<>((realm.where(GSONCustomerMasterBean.class).contains("CustomerName",customerName, Case.INSENSITIVE).
                or().contains("GroupName",customerName, Case.INSENSITIVE)).findAll());//.and().equalTo("ISVeryfied",true).
               // findAll());
        ArrayList<GSONCustomerMasterBean > mlist=new ArrayList<>();
        for (GSONCustomerMasterBean b:list
             )
        {
            if(b.isISVeryfied())
            {
                mlist.add(b);
            }
        }
        return mlist;
    }

    public   ArrayList<GSONCustomerMasterBeanExtends>  getCustomerListwithFilterNonRelam(String customerName)
    {
        Gson gson=new Gson();
        ArrayList<GSONCustomerMasterBeanExtends> mList=new ArrayList<>();
        Realm realm;   realm=Realm.getDefaultInstance();
        if(customerName.equalsIgnoreCase(""))
        {
            ArrayList<GSONCustomerMasterBean>  list= getCustomerList();
            for(int i=0;i<list.size();i++)
            {
                GSONCustomerMasterBean bean= realm .copyFromRealm( list.get(i));

                GSONCustomerMasterBeanExtends b=  gson.fromJson(gson.toJson(bean),GSONCustomerMasterBeanExtends.class)   ;

                mList.add(b);
            }
            return mList;
        }

        ArrayList<GSONCustomerMasterBean>  list=new ArrayList<>(realm.where(GSONCustomerMasterBean.class).contains("CustomerName",customerName, Case.INSENSITIVE).
                or().contains("GroupName",customerName, Case.INSENSITIVE).
                findAll());

        for(int i=0;i<list.size();i++)
        {
            GSONCustomerMasterBean bean= realm .copyFromRealm( list.get(i));


            GSONCustomerMasterBeanExtends b=  gson.fromJson(gson.toJson(bean),GSONCustomerMasterBeanExtends.class)   ;

            mList.add(b);
        }

        return mList;
    }
    public   ArrayList<GSONCustomerMasterBeanExtends>  getCustomerListByGroupIdNonRelam(int gid)
    {
        float GroupId=gid;
        Gson gson=new Gson();
        ArrayList<GSONCustomerMasterBeanExtends> mList=new ArrayList<>();
        Realm realm;   realm=Realm.getDefaultInstance();
        ArrayList<GSONCustomerMasterBean>  list=new ArrayList<>(realm.where(GSONCustomerMasterBean.class).equalTo("GroupId",GroupId).findAll());
        for(int i=0;i<list.size();i++)
        {
            GSONCustomerMasterBean bean= realm .copyFromRealm( list.get(i));
            GSONCustomerMasterBeanExtends b=  gson.fromJson(gson.toJson(bean),GSONCustomerMasterBeanExtends.class)   ;
            mList.add(b);
        }
        return mList;
    }


    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        if(requestCode==1) {
            SyncCustomerProductControlller controlller = new SyncCustomerProductControlller(context);
            controlller.sync();
        }

        if(responseCode==200)
        {

        }

        if(responseCode==1000)
        {

        }
    }

    @Override
    public boolean parseStreamingData(InputStream stream) {
        try
        {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            reader.beginArray();
            Realm realm;   realm=Realm.getDefaultInstance();
            int counter=0;
            while (reader.hasNext())
            {
                GSONCustomerMasterBean gsonCustomerProduct = gson.fromJson(reader, GSONCustomerMasterBean.class);

                if(realm.isClosed())
                    realm=Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(gsonCustomerProduct);
                realm.commitTransaction();
                counter++;
                if(counter%800==0)
                {
                    try {

                        realm.close();

                        Thread.sleep(1000);
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
}
