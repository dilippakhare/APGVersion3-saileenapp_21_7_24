package com.apgautomation.controller;

import android.content.Context;
import android.widget.Toast;

import com.apgautomation.model.GsonQuatationRequestModel;
import com.apgautomation.model.ServerModel.GsonSalesVisit;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.apgautomation.utility.serverutility.netrequest.HttpURLConnectionExample1;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class SaleVisitController implements DownloadUtility {

    Context context;
    public   SaleVisitController(Context context)
    {
        this.context=context;
    }
    public  void saveVisitSchedule(GsonSalesVisit model)
    {
        Realm realm=Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(model);
        realm.commitTransaction();
       // String s=model.getVisitId()+"   ,  "+ model.getVisitStatus()+"  ,  " + model.getAssigntoEmpId();
        //Log.d("Update Value",s);

        new Thread(new Runnable() {
            @Override
            public void run() {
                saveScheduleVisitToServer();
            }
        }).start();

    }

    public  void saveScheduleVisitToServer()
    {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonSalesVisit> cp= realm.where(GsonSalesVisit.class).equalTo("SalesVisitId",0).findAll();
        ArrayList<GsonSalesVisit> list=new ArrayList<>(cp );
        ArrayList<GsonSalesVisit> newlist=new ArrayList<>();
        for (GsonSalesVisit item:list)
        {
            GsonSalesVisit newItem=realm.copyFromRealm(item);
            newlist.add(newItem);
        }
        try
        {
            Gson gson=new Gson();
            for (GsonSalesVisit item:newlist)
            {
                String jsonStr = gson.toJson(item);
                HttpURLConnectionExample1 servercall = new HttpURLConnectionExample1();
                String url = CommonShare.url + "Service1.svc/ScheduleSalesVisit";
                try
                {
                    String res = servercall.performPostCallJson(url, jsonStr);
                    JSONObject j = new JSONObject(res);

                    GsonSalesVisit t = gson.fromJson(res, GsonSalesVisit.class);
                    t.setModified(false);
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(t);
                    realm.commitTransaction();
                }
                catch (Exception ex)
                {

                }
            }
        }
        catch (Exception ex)
        {}
    }


    public void saveSalesVisitModelTOServer()
    {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonSalesVisit> cp= realm.where(GsonSalesVisit.class)
                .equalTo("IsModified",true).and().notEqualTo("SalesVisitId",0).findAll();
        ArrayList<GsonSalesVisit> list=new ArrayList<>(cp );
        ArrayList<GsonSalesVisit> newlist=new ArrayList<>();
        for (GsonSalesVisit item:list)
        {
            GsonSalesVisit newItem=realm.copyFromRealm(item);
            newlist.add(newItem);
        }
        try
        {
            Gson gson=new Gson();
            for (GsonSalesVisit item:newlist)
            {
                String jsonStr = gson.toJson(item);
                HttpURLConnectionExample1 servercall = new HttpURLConnectionExample1();
                String url = CommonShare.url + "Service1.svc/saveSalesVisit";
                try
                {
                    String res = servercall.performPostCallJson(url, jsonStr);
                    JSONObject j = new JSONObject(res);

                    GsonSalesVisit t = gson.fromJson(res, GsonSalesVisit.class);
                    t.setModified(false);
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(t);
                    realm.commitTransaction();
                }
                catch (Exception ex)
                {

                }
            }
        }
        catch (Exception ex)
        {}
    }

    public ArrayList<GsonSalesVisit>  getModifiedSalesVisit()
    {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonSalesVisit> cp= realm.where(GsonSalesVisit.class)
                .equalTo("IsModified",true).findAll();
        ArrayList<GsonSalesVisit> newlist=new ArrayList<>();
        for (GsonSalesVisit item:cp)
        {
            GsonSalesVisit newItem=realm.copyFromRealm(item);
            newlist.add(newItem);
        }
        return  newlist;
    }


    public ArrayList<GsonSalesVisit>  getPendingSalesVisit()
    {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonSalesVisit> cp= realm.where(GsonSalesVisit.class)
                .notEqualTo("VisitStatus","Closed").and().equalTo("AssignToId",CommonShare.getEmpId(context)).findAll().sort("ScheduleDateMillisecond");
        ArrayList<GsonSalesVisit> newlist=new ArrayList<>();
        for (GsonSalesVisit item:cp)
        {
            GsonSalesVisit newItem=realm.copyFromRealm(item);
            newlist.add(newItem);
        }
        return  newlist;
    }

    public void callScheduleVisit()
    {
        String url=CommonShare.url+"Service1.svc/GetSalesScheduleVisit?EmpId="+CommonShare.getEmpId(context);
        AsyncUtilities utilities=new AsyncUtilities(context,false,url,"",1,this);
        //utilities.hideProgressDialoge();
        utilities.execute();
    }
    public ArrayList<GsonSalesVisit> pendingVisitList=new ArrayList<>();
    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        ArrayList<GsonSalesVisit> modifiedList=getModifiedSalesVisit();
        pendingVisitList.clear();
        Realm realm=Realm.getDefaultInstance();
        if((requestCode==1 || requestCode==2)  && responseCode==200)
        {
             Gson gson=new Gson();
             try
             {
                 JSONArray array=new JSONArray(str);
                 for(int i=0;i<array.length();i++)
                 {
                     JSONObject jsonObject=array.getJSONObject(i);
                     JSONObject visitObj=    jsonObject.getJSONObject("SalesVisitMaster").getJSONObject("visit");
                     GsonSalesVisit vObj=gson.fromJson(visitObj.toString(),GsonSalesVisit.class);
                     int flag=0;
                     GsonSalesVisit newObj=null;
                     for (GsonSalesVisit obj:modifiedList )
                     {
                        if( obj.getVisitTokenId().equalsIgnoreCase(vObj.getVisitTokenId()))
                         {
                             newObj=obj;
                             flag=1;

                            // if(!obj.getVisitStatus().equalsIgnoreCase("Schedule"))
                             if( ( obj.getVisitStatus().equalsIgnoreCase("Started")||
                                     obj.getVisitStatus().equalsIgnoreCase("Closed")
                                  )  && vObj.getVisitStatus().equalsIgnoreCase("Schedule")
                              )
                             {
                                 //flag=0;
                             }
                         }

                     }

                    if( realm.where(GsonSalesVisit.class)
                             .equalTo("VisitTokenId",vObj.getVisitTokenId()).count() >0)
                     {
                         GsonSalesVisit obj=  realm.where(GsonSalesVisit.class)
                                 .equalTo("VisitTokenId",vObj.getVisitTokenId()).findFirst();
                         if( ( obj.getVisitStatus().equalsIgnoreCase("Started")||
                                 obj.getVisitStatus().equalsIgnoreCase("Closed")
                         )  && vObj.getVisitStatus().equalsIgnoreCase("Schedule")
                         )
                         {
                             flag=1;
                         }

                     }


                     if(flag==0)
                     {

                         realm.beginTransaction();
                         realm.copyToRealmOrUpdate(vObj);
                         realm.commitTransaction();
                         pendingVisitList.add((vObj));
                     }
                     else
                     {
                         pendingVisitList.add((newObj));
                     }

                     try
                     {
                         JSONArray quotaionArray = jsonObject.getJSONArray("Quotation");
                         for(int j=0;j<quotaionArray.length();j++)
                         {
                             GsonQuatationRequestModel obj=gson.fromJson(quotaionArray.getJSONObject(j).toString(),GsonQuatationRequestModel.class);
                             realm.beginTransaction();
                             realm.copyToRealmOrUpdate(obj);
                             realm.commitTransaction();
                         }
                     }
                     catch (Exception ex)
                     {}
                 }
             }
             catch (Exception ex)
             {}

             try {
               DownloadUtility ut= (DownloadUtility) context;
               ut.onComplete(str,requestCode,responseCode);
             }
             catch (Exception ex)
             {}

        }
        else
        {
            Toast.makeText(context,"Cant reach to server",Toast.LENGTH_LONG).show();
        }
    }


    public GsonSalesVisit getVisitObjectByToken(String visitToken) {
        Realm realm=Realm.getDefaultInstance();
        GsonSalesVisit cp= realm.where(GsonSalesVisit.class)
                .equalTo("VisitTokenId",visitToken).findFirst();

        return  cp;
    }

    public void callAllVisit() {
        String url=CommonShare.url+"Service1.svc/GetSalesAllVIsit?EmpId="+CommonShare.getEmpId(context);
        AsyncUtilities utilities=new AsyncUtilities(context,false,url,"",2,this);
        //utilities.hideProgressDialoge();
        utilities.execute();
    }
    public void callAllVisitFilter(String urlStr) {
        String url=urlStr;
        AsyncUtilities utilities=new AsyncUtilities(context,false,url,"",2,this);
        //utilities.hideProgressDialoge();
        utilities.execute();
    }

    public ArrayList<GsonSalesVisit> getAllVisitList()
    {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonSalesVisit> cp= realm.where(GsonSalesVisit.class)
                .equalTo("DeleteStatus",false).findAll();
        ArrayList<GsonSalesVisit> newlist=new ArrayList<>();
        for (GsonSalesVisit item:cp)
        {
            GsonSalesVisit newItem=realm.copyFromRealm(item);
            newlist.add(newItem);
        }
        return  newlist;
    }

    public GsonSalesVisit getStarteVisit() {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonSalesVisit> cp= realm.where(GsonSalesVisit.class)
                .equalTo("VisitStatus","Started").and().equalTo("AssignToId",CommonShare.getEmpId(context)).findAll();
        ArrayList<GsonSalesVisit> newlist=new ArrayList<>();
        for (GsonSalesVisit item:cp)
        {
            GsonSalesVisit newItem=realm.copyFromRealm(item);
            return   newItem;
        }
        return  null;
    }


    public boolean isPendingZeroVisit()
    {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonSalesVisit> cp= realm.where(GsonSalesVisit.class).equalTo("SalesVisitId",0).findAll();
        return cp.size()>0 ?true:false;
    }
}
