package com.apgautomation.controller;

import android.content.Context;
import android.util.Log;

import com.apgautomation.model.GsonGroup;
import com.apgautomation.model.GsonQuatationRequestModel;
import com.apgautomation.model.GsonVisitFsr;
import com.apgautomation.model.GsonVisitMaster;
import com.apgautomation.model.GsonVisitPhoto;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class VisitModuleController implements DownloadUtility
{
    Context context;
    public   VisitModuleController(Context context)
    {
        this.context=context;
    }
    public  void saveVisitSchedule(GsonVisitMaster model)
    {
        Realm realm=Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(model);
        realm.commitTransaction();
        String s=model.getVisitId()+"   ,  "+ model.getVisitStatus()+"  ,  " + model.getAssigntoEmpId();
        Log.d("Update Value",s);
    }
    public ArrayList<GsonVisitMaster> getModifiedRecord()
    {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonVisitMaster> cp= realm.where(GsonVisitMaster.class).equalTo("isModified",true).findAll();

        ArrayList<GsonVisitMaster> list=new ArrayList<>(cp );
        return  list;
    }



    public  void setInitial()
    {
        scheduleList=  getScheduleList();
    }
    public ArrayList<GsonVisitMaster> getScheduleList()
    {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonVisitMaster> cp= realm.where(GsonVisitMaster.class).notEqualTo("VisitStatus","Complete").findAll();
        ArrayList<GsonVisitMaster> list=new ArrayList<>( );
        for (GsonVisitMaster o:cp
        ) {
            GsonVisitMaster m=  realm.copyFromRealm(o);
            if(m.getGroupName() == null || m.getGroupName() .equalsIgnoreCase("")){
                try {
                    GsonGroup g = realm.where(GsonGroup.class).equalTo("GroupId", m.getGroupId()).findFirst();
                    m.setGroupName(g.getGroupName());
                }
                catch (Exception e) {

                }
            }
            if(m.getEmpName()== null ||m.getEmpName().equalsIgnoreCase("")) {
                try {
                      m.setEmpName(CommonShare.empMap.get(m.getAssigntoEmpId()).EmpName);
                }
                catch (Exception ex){

                }
            }
            list.add(m );
        }
        return  list;
    }
    public void loadPendingVisits()
    {
        String url=CommonShare.url+"Service1.svc/GetEngVisits?EmpId="+CommonShare.getEmpId(context);
        AsyncUtilities utilities=new AsyncUtilities(context,false,url,"",1,this);
        //utilities.hideProgressDialoge();
        utilities.execute();
    }

    public ArrayList<GsonVisitMaster> scheduleList=new ArrayList<>();
    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {

        //CommonShare.alert(context,str);
        try
        {
            Realm realm=Realm.getDefaultInstance();
            RealmResults<GsonVisitMaster> cp= realm.where(GsonVisitMaster.class).equalTo("isModified",true).findAll();

            Gson gson=new Gson();
            JSONArray array=new JSONArray(str);
            for (int i=0;i<array.length();i++)
            {

                GsonVisitMaster model=gson.fromJson(array.getJSONObject(i).toString(),GsonVisitMaster.class);
              //  scheduleList.add(model);
                int flag=0;
                for (GsonVisitMaster modifiedObject:cp)
                {
                    if(model.getVisitToken().equalsIgnoreCase(modifiedObject.getVisitToken()))
                    {
                        if(modifiedObject.getIsModified())
                            flag=1;
                    }
                }
                //---     Exted Check
                try
                {
                    GsonVisitMaster dbModel= realm.where(GsonVisitMaster.class).equalTo("VisitToken",model.getVisitToken()).findFirst();
                    if(dbModel.getEndTimeMillisecond()>0 &&  model.getEndTimeMillisecond()==0    )
                    {
                        flag=1;
                    }

                    if( (   dbModel.getVisitRemark()!=null || dbModel.getVisitRemark().length()>0  ) &&
                            (   model.getVisitRemark()==null || model.getVisitRemark().length()==0  || model.getVisitRemark().equalsIgnoreCase("null") )   )
                    {
                        flag=1;
                    }
                }
                catch (Exception ex){}

                if(flag==0)
                {
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(model);
                    realm.commitTransaction();
                }
            }

        }
        catch (Exception ex)
        {}

        scheduleList.clear();
        scheduleList.addAll(  getScheduleList());

        ((DownloadUtility) context).onComplete(str,requestCode,responseCode);
    }

    public boolean isAnyVisitStarted()
    {
       Realm realm=Realm.getDefaultInstance();
       if( realm.where(GsonVisitMaster.class).equalTo("VisitStatus","Started").count()>0 ||
               realm.where(GsonVisitMaster.class).equalTo("VisitStatus","Inprogress-Fsr-Pending").count()>0

       )
               //.and().equalTo("AssigntoEmpId",CommonShare.getEmpId(context)).count()>0)
           return true;
       else
           return false;

    }
    public GsonVisitMaster getStaretedVisit()
    {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonVisitMaster> cp=  realm.where(GsonVisitMaster.class).equalTo("VisitStatus","Started").and().equalTo("AssigntoEmpId",CommonShare.getEmpId(context)).findAll();
        if(!cp.isEmpty())
            return  cp.first();
        return  null;

    }

    public GsonVisitMaster getVisitmasterByToken(String visitToken)
    {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonVisitMaster> cp=  realm.where(GsonVisitMaster.class).equalTo("VisitToken",visitToken).and().equalTo("AssigntoEmpId",CommonShare.getEmpId(context)).findAll();
        if(!cp.isEmpty())
            return   realm.copyFromRealm( cp.first());
        return  null;
    }

    public void saveFSR(ArrayList<GsonVisitFsr> fsrList) {
        Realm realm=Realm.getDefaultInstance();
        for (GsonVisitFsr obj :fsrList
             ) {
            obj.setModified(true);
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(obj);
            realm.commitTransaction();
        }
    }
    public void saveFSR(GsonVisitFsr obj) {
            Realm realm=Realm.getDefaultInstance();
            obj.setModified(true);
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(obj);
            realm.commitTransaction();
    }

    public void saveQuatationRequest(ArrayList<GsonQuatationRequestModel> requestList) {
        Realm realm=Realm.getDefaultInstance();
        for (GsonQuatationRequestModel obj :requestList )
        {
            obj.setModified(true);
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(obj);
            realm.commitTransaction();
        }
    }

    public void saveQuatationRequest(GsonQuatationRequestModel obj) {
            Realm realm=Realm.getDefaultInstance();
            obj.setModified(true);
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(obj);
            realm.commitTransaction();
    }

    public ArrayList<GsonVisitFsr> getGsonFSRModified()
    {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonVisitFsr> cp= realm.where(GsonVisitFsr.class).isEmpty("FSRAttachment").or().equalTo("RecId",0).findAll();
        ArrayList<GsonVisitFsr> list=new ArrayList<>(cp );

        return  list;
    }
    public ArrayList<GsonVisitPhoto> getNotSavedPhoto()
    {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonVisitPhoto> cp= realm.where(GsonVisitPhoto.class).equalTo("RecId",0).findAll();
        ArrayList<GsonVisitPhoto> list=new ArrayList<>(cp );
        return  list;
    }
    public ArrayList<GsonQuatationRequestModel> getGsonQuoationModified()
    {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonQuatationRequestModel> cp= realm.where(GsonQuatationRequestModel.class).isEmpty("Attachment").or().equalTo("QuotationId",0).findAll();
        ArrayList<GsonQuatationRequestModel> list=new ArrayList<>(cp );
        return  list;
    }
    public ArrayList<GsonQuatationRequestModel> getGsonQuoationByVisitToken(String Token)
    {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonQuatationRequestModel> cp= realm.where(GsonQuatationRequestModel.class).equalTo("VisitToken",Token).findAll();
        ArrayList<GsonQuatationRequestModel> list=new ArrayList<>(cp );
        return  list;
    }


    public void savePhoto(GsonVisitPhoto photoModel) {
        Realm realm=Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(photoModel);
        realm.commitTransaction();
    }

    public ArrayList<GsonVisitPhoto> getVisitPhoto(String Token)
    {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonVisitPhoto> cp= realm.where(GsonVisitPhoto.class).equalTo("VisitToken",Token).findAll();
        ArrayList<GsonVisitPhoto> list=new ArrayList<>(cp );
        return  list;
    }



    public boolean isPendingZeroVisit()
    {
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonVisitMaster> cp= realm.where(GsonVisitMaster.class).equalTo("VisitId",0).findAll();
        return cp.size()>0 ?true:false;
    }
}
