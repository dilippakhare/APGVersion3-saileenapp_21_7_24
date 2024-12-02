package com.apgautomation.utility.syncutilities;

import android.content.Context;
import android.util.Log;

import com.apgautomation.controller.SaleVisitController;
import com.apgautomation.controller.VisitModuleController;
import com.apgautomation.model.GSONCloseComplaint;
import com.apgautomation.model.GsonAskQuery;
import com.apgautomation.model.GsonComplaintWork;
import com.apgautomation.model.GsonCustomerProduct;
import com.apgautomation.model.GsonQuatationRequestModel;
import com.apgautomation.model.GsonToDoDetails;
import com.apgautomation.model.GsonTodo;
import com.apgautomation.model.GsonVisitFsr;
import com.apgautomation.model.GsonVisitMaster;
import com.apgautomation.model.GsonVisitPhoto;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.netrequest.FileUploadClass1;
import com.apgautomation.utility.serverutility.netrequest.HttpURLConnectionExample1;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class VisitSyncUtility {
  Context context;
  VisitModuleController controller;

  public VisitSyncUtility(Context context) {
    this.context = context;
    // attendanceController=new AttendanceController(this.context);
    controller=new VisitModuleController(context);

  }

  /*  Visit Save*/
  public void saveVisit()
  {
    try
    {
      sendQUotationAndFSR();
    }
    catch (Exception ex)
    {}
    Log.d("com.apgautomation","VisitThread");
    try {
      ArrayList<GsonVisitMaster> modifiedList = controller.getModifiedRecord();
      Log.d("com.apgautomation", "Visit modied count " + modifiedList.size());
      sendToServer(modifiedList);
    }
    catch (Exception ex)
    {  Log.d("com.apgautomation","visit Exception :"+ex.toString());}

  }
  public boolean isAllVisitSaved()
  {
    ArrayList<GsonVisitMaster> modifiedList = controller.getModifiedRecord();
    return  modifiedList.size() == 0 ;
  }
  public void sync()
  {

      //----   Quotation And  ServiceVisit Modified//
    Thread t= new Thread(new Runnable() {
      @Override
      public void run() {

         saveVisit();
      }
    });
    t.start();

//----   To Do List Modified
    new Thread(new Runnable() {
      @Override
      public void run() {

        try {
          Gson gson=new Gson();
          Realm realm=Realm.getDefaultInstance() ;
          RealmResults<GsonTodo> mlist= realm.where(GsonTodo.class).equalTo("IsModified",true).findAll();

          ArrayList<GsonTodo>  list=new ArrayList<>();

          for (GsonTodo obj:mlist)
          {
            try {
              GsonTodo item = realm.copyFromRealm(obj);
              String str = gson.toJson(item);
              HttpURLConnectionExample1 servercall = new HttpURLConnectionExample1();
              String url= CommonShare.url+ "Service1.svc/SaveTodoTask";
              Log.d("Vk",url);
              Log.d("Vk",str);
              String res = servercall.performPostCallJson(url, str);
              Log.d("Vk",res);
              GsonTodo mobj= gson.fromJson(res,GsonTodo.class);
              if(mobj.getToDoId()>0)
              {
                         realm.beginTransaction();
                         realm.copyToRealmOrUpdate(mobj);
                         realm.commitTransaction();
              }
            }
            catch (Exception ex)
            {}

          }


        }
        catch (Exception ex)
        {
          Log.d("com.apgautomation",ex.toString());
        }

      }
    }).start();


    //---   ToDO Details Modifed
    new Thread(new Runnable() {
      @Override
      public void run() {

        try {
          Gson gson=new Gson();
          Realm realm=Realm.getDefaultInstance() ;
          RealmResults<GsonToDoDetails> mlist= realm.where(GsonToDoDetails.class).equalTo("IsModified",true).findAll();

          ArrayList<GsonTodo>  list=new ArrayList<>();
          synchronized(CommonShare.todoSync) {
            for (GsonToDoDetails obj : mlist) {
              try {
                GsonToDoDetails item = realm.copyFromRealm(obj);
                String str = gson.toJson(item);
                HttpURLConnectionExample1 servercall = new HttpURLConnectionExample1();
                String url = CommonShare.url + "Service1.svc/SaveTodoTaskDetails";
                String res = servercall.performPostCallJson(url, str);
                JSONObject jj=new JSONObject(res);
                GsonToDoDetails mobj = gson.fromJson(res, GsonToDoDetails.class);

                if(mobj.DetailId>0) {
                  realm.beginTransaction();
                  realm.copyToRealmOrUpdate(mobj);
                  realm.commitTransaction();
                }
              } catch (Exception ex) {
              }

            }
          }


        }
        catch (Exception ex)
        {
          Log.d("com.apgautomation",ex.toString());
        }

      }
    }).start();



  //----------  Gson Ask Query Modied
    new Thread(new Runnable() {
      @Override
      public void run() {

        try {
          Gson gson=new Gson();
          Realm realm=Realm.getDefaultInstance() ;
          RealmResults<GsonAskQuery> mlist= realm.where(GsonAskQuery.class).equalTo("IsModified",true).findAll();

          ArrayList<GsonTodo>  list=new ArrayList<>();

          for (GsonAskQuery obj:mlist)
          {
            try
            {
              GsonAskQuery item = realm.copyFromRealm(obj);
              String str = gson.toJson(item);
              HttpURLConnectionExample1 servercall = new HttpURLConnectionExample1();
              String url= CommonShare.url+ "Service1.svc/SaveAskQuery";
              String res = servercall.performPostCallJson(url, str);
              GsonAskQuery mobj= gson.fromJson(res,GsonAskQuery.class);

              realm.beginTransaction();
              realm.copyToRealmOrUpdate(mobj);
              realm.commitTransaction();
            }
            catch (Exception ex)
            {}

          }


        }
        catch (Exception ex)
        {
          Log.d("com.apgautomation",ex.toString());
        }

      }
    }).start();


    //-- Complaint Work Modofied
    new Thread(new Runnable() {
      @Override
      public void run() {
        //
        try {
          Gson gson=new Gson();
          Realm realm=Realm.getDefaultInstance() ;
          RealmResults<GsonComplaintWork> mlist= realm.where(GsonComplaintWork.class).equalTo("IsModified",true).findAll();

          ArrayList<GsonTodo>  list=new ArrayList<>();

          for (GsonComplaintWork obj:mlist)
          {
            try
            {
              GsonComplaintWork item = realm.copyFromRealm(obj);
              String str = gson.toJson(item);
              HttpURLConnectionExample1 servercall = new HttpURLConnectionExample1();
              String url= CommonShare.url+ "Service1.svc/UpdateComplaintWOrk";
              String res = servercall.performPostCallJson(url, str);
              GsonComplaintWork mobj= gson.fromJson(res,GsonComplaintWork.class);

              realm.beginTransaction();
              realm.copyToRealmOrUpdate(mobj);
              realm.commitTransaction();
            }
            catch (Exception ex)
            {}

          }


        }
        catch (Exception ex)
        {
          Log.d("com.apgautomation",ex.toString());
        }

      }
    }).start();


// --- Complaint Solution Modiifed
      new Thread(new Runnable() {
          @Override
          public void run() {
              //
              try {
                  Gson gson=new Gson();
                  Realm realm=Realm.getDefaultInstance() ;
                  RealmResults<GSONCloseComplaint> mlist= realm.where(GSONCloseComplaint.class).equalTo("IsModified",true).findAll();

                  ArrayList<GsonTodo>  list=new ArrayList<>();

                  for (GSONCloseComplaint obj:mlist)
                  {
                      try
                      {
                        GSONCloseComplaint item = realm.copyFromRealm(obj);
                          String str = gson.toJson(item);
                          HttpURLConnectionExample1 servercall = new HttpURLConnectionExample1();
                          String url= CommonShare.url+ "Service1.svc/CloseComplaintPost";
                          String res = servercall.performPostCallJson(url, str);
                          GSONCloseComplaint mobj= gson.fromJson(res,GSONCloseComplaint.class);

                          realm.beginTransaction();
                          realm.copyToRealmOrUpdate(mobj);
                          realm.commitTransaction();
                      }
                      catch (Exception ex)
                      {}

                  }


              }
              catch (Exception ex)
              {
                  Log.d("com.apgautomation",ex.toString());
              }

          }
      }).start();
  }

  private void sendToServer(ArrayList<GsonVisitMaster> modifiedList)
  {
    Realm realm=Realm.getDefaultInstance();
    Gson gson=new Gson();
    synchronized (CommonShare.visitSync) {
      String modifiedGroupIds="";
      for (GsonVisitMaster model : modifiedList)
      {
        model=realm.copyFromRealm(model);
        String  json= gson.toJson(model);
        HttpURLConnectionExample1 servercall = new HttpURLConnectionExample1();
        String url = CommonShare.url + "Service1.svc/scheduleEngVisit";
        try {
          String res = servercall.performPostCallJson(url, json);

          GsonVisitMaster m=gson.fromJson(res,GsonVisitMaster.class);
          m.isModified=false;
          controller.saveVisitSchedule(m);
        }
        catch (Exception ex)
        {}
        if(modifiedGroupIds.equalsIgnoreCase(""))
        {
          modifiedGroupIds=model.GroupId+"";
        }
        else if( ! (","+modifiedGroupIds+",").contains(","+model.GroupId+","))
        {
          modifiedGroupIds=modifiedGroupIds+","+model.GroupId+"";
        }
      }

      try
      {
        String [] arr=modifiedGroupIds.split(",");
        for(int i=0;i<arr.length;i++)
        {
          HttpURLConnectionExample1 servercall = new HttpURLConnectionExample1();
          String url = CommonShare.url + "Service1.svc/SyncCustomerProductByGroupId?GroupId="+arr[i];
          try {
            String res = servercall.sendGet(url);
            JSONArray  jsonArray=new JSONArray(res);
            // Realm realm=Realm.getDefaultInstance();
            for (int j = 0; j < jsonArray.length(); j++) {
              GsonCustomerProduct gsonCustomerProduct = gson.fromJson(jsonArray.getJSONObject(j).toString(), GsonCustomerProduct.class);
              gsonCustomerProduct.convertDates();
              realm.beginTransaction();
              realm.copyToRealmOrUpdate(gsonCustomerProduct);
              realm.commitTransaction();
            }


          } catch (Exception ex) {
          }
        }
      }
      catch (Exception ex)
      {

      }
    }
  }

  void sendQUotationAndFSR()
  {
    synchronized (CommonShare.visitSync)
    {


      ArrayList<GsonVisitFsr> fsrList1 = controller.getGsonFSRModified();
      ArrayList<GsonVisitFsr> fsrList =new ArrayList<>();
      Gson gson=new Gson();
      try
      {
        Realm realm=Realm.getDefaultInstance();

        for(int i=0;i<fsrList1.size();i++) {
          fsrList.add(realm.copyFromRealm(fsrList1.get(i)));
        }

        for(int i=0;i<fsrList.size();i++)
        {


          String  fsrServerPath=fsrList.get(i).getFSRAttachment();

          if(fsrServerPath==null || fsrServerPath.length()<5)
          {
            try {
              FileUploadClass1 f = new FileUploadClass1();
              String res = f.checkinstream("", fsrList.get(i).getLocalPath(), CommonShare.url + "UploadService.svc/UploadImage", 1, "fsr");
              JSONObject j = new JSONObject(res);
              fsrList.get(i).setFSRAttachment(j.getString("serverpath"));
              controller.saveFSR(fsrList.get(i));
            }
            catch (Exception ex){}
          }
          if((fsrList.get(i).getFSRAttachment()!=null && fsrList.get(i).getFSRAttachment().length()>5) || CommonShare.isFsrEnabled(context) )
          try {

            String jsonStr = gson.toJson(fsrList.get(i));
            HttpURLConnectionExample1 servercall = new HttpURLConnectionExample1();
            String url = CommonShare.url + "Service1.svc/SaveFSR";
            try {
              String res = servercall.performPostCallJson(url, jsonStr);
              JSONObject j=new JSONObject(res);

              GsonVisitFsr item=    gson.fromJson(res,GsonVisitFsr.class);
              realm.beginTransaction();
              realm.copyToRealmOrUpdate(item);
              realm.commitTransaction();

              GsonCustomerProduct customerProduct = realm.where(GsonCustomerProduct.class).equalTo("RecId",item.CustomerProductId).findFirst();
              customerProduct.setLastVisitDate("/Date("+System.currentTimeMillis()+")/");
              customerProduct.setLastVisitMilliSecond(System.currentTimeMillis());
              customerProduct.convertDates();
              realm.beginTransaction();
              realm.copyToRealmOrUpdate(customerProduct);
              realm.commitTransaction();

            } catch (Exception ex) {
            }
          }
          catch (Exception ex)
          {}
        }
      }
      catch (Exception ex)
      {}


      //Sales Sync Visit utility
      try {

        saveSaleseVisitToServer();
      }catch (Exception ex)
      {}
      try
      {
        saveVisitPhoto();
      }
      catch (Exception ex)
      {}

    }

    try
    {
         sendQuotation();
    }
    catch (Exception ex){}
  }

  void sendQuotation()
  {
    ArrayList<GsonQuatationRequestModel> quotationList1 = controller.getGsonQuoationModified();
    ArrayList<GsonQuatationRequestModel> quotationList=new ArrayList<>();
    try
    {
      Realm realm=Realm.getDefaultInstance();

      for(int i=0;i<quotationList1.size();i++) {
        quotationList.add(realm.copyFromRealm(quotationList1.get(i)));
      }
      Gson gson =new Gson() ;
      for(int i=0;i<quotationList.size();i++) {
        String  quotationPath=quotationList.get(i).getAttachment();

        if(quotationPath==null || quotationPath.length()<5)
        {
          try {
            FileUploadClass1 f = new FileUploadClass1();
            String res = f.checkinstream("", quotationList.get(i).getLocalpath(), CommonShare.url + "UploadService.svc/UploadImage", 1, "quotation");
            JSONObject j = new JSONObject(res);
            quotationList.get(i).setAttachment(j.getString("serverpath"));
            controller.saveQuatationRequest(quotationList.get(i));
          }
          catch (Exception ex){}
        }

        try {
          String jsonStr = gson.toJson(quotationList.get(i));
          HttpURLConnectionExample1 servercall = new HttpURLConnectionExample1();
          String url = CommonShare.url + "Service1.svc/SaveQuotation";
          try {
            String res = servercall.performPostCallJson(url, jsonStr);
            JSONObject j=new JSONObject(res);
            GsonQuatationRequestModel item=    gson.fromJson(res,GsonQuatationRequestModel.class);
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(item);
            realm.commitTransaction();
          } catch (Exception ex) {
          }
        }
        catch (Exception ex)
        {}
      }
    }
    catch (Exception ex)
    {}
  }

  void saveSaleseVisitToServer()
  {
    try
    {
      SaleVisitController controller=new SaleVisitController(context);
      controller.saveScheduleVisitToServer();
      controller.saveSalesVisitModelTOServer();
    }
    catch (Exception ex)
    {}
  }


  void saveVisitPhoto()
  {

    try
    {
      VisitModuleController controller=new VisitModuleController(context);
      ArrayList<GsonVisitPhoto> photoList=  controller.getNotSavedPhoto();
      Gson gson=new Gson();
      for (GsonVisitPhoto photo:photoList)
      {
        FileUploadClass1 f = new FileUploadClass1();
        String res = f.checkinstream("", photo.getLocalPath(), CommonShare.url + "UploadService.svc/UploadImage", 1, "visitphoto");
        JSONObject j = new JSONObject(res);

        Realm realm=Realm.getDefaultInstance();
        try
        {
          GsonVisitPhoto simpleObj=realm.copyFromRealm(photo);
          simpleObj.setPhoto(j.getString("serverpath"));
          String jsonStr = gson.toJson(simpleObj);
          HttpURLConnectionExample1 servercall = new HttpURLConnectionExample1();
          String url = CommonShare.url + "Service1.svc/SaveVisitPhoto";
          try
          {
            String resPhoto = servercall.performPostCallJson(url, jsonStr);
            JSONObject jj=new JSONObject(resPhoto);
            if(jj.getInt("RecId")>0)
            {

              GsonVisitPhoto item = gson.fromJson(resPhoto, GsonVisitPhoto.class);
              realm.beginTransaction();
              realm.copyToRealmOrUpdate(item);
              realm.commitTransaction();


            }
          } catch (Exception ex) {
          }
        }
        catch (Exception ex)
        {}

      }

    }
    catch (Exception ex){
      Log.d("APP", ex.toString());
    }
  }



  public boolean isPeningZeoVisit()
  {
    boolean isZero= this.controller.isPendingZeroVisit();
    if(isZero)
      return  true;

    SaleVisitController c=new SaleVisitController(context);
    isZero= c.isPendingZeroVisit();

     if(isZero)
       return isZero;

    return  false;
  }


}
