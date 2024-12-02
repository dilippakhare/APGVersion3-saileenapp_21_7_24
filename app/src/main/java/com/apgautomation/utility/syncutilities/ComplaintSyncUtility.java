package com.apgautomation.utility.syncutilities;

import android.content.Context;

import com.apgautomation.model.ComplaintModel;
import com.apgautomation.model.database.ItemDAOComplaintModel;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.netrequest.FileUploadClass1;
import com.apgautomation.utility.serverutility.netrequest.HttpURLConnectionExample1;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

public class ComplaintSyncUtility
{
  Context context;
  // AttendanceController attendanceController;
  public  ComplaintSyncUtility( Context context)
  {
    this.context=context;
    // attendanceController=new AttendanceController(this.context);
    new Thread(new Runnable() {
      @Override
      public void run() {
        sendComplaints();
      }
    }).start();
  }
  void sendComplaints() {
    try
    {
      Gson g = new Gson();
      ItemDAOComplaintModel itemDAOComplaintModel=new ItemDAOComplaintModel(context);
      ArrayList<ComplaintModel> list= itemDAOComplaintModel.getComplaints();
      for (ComplaintModel model:list)
      {

        try {
          if (model.Localpath != null && model.Localpath.length()>5)
          {
            String serverpath=  uploadPhoto(model.Localpath);
            if(serverpath!=null && serverpath.length()>5)
            {
              itemDAOComplaintModel.updateAttechment(model.Token,serverpath);
              model.Attachment1=serverpath;
            }
          }
        }
        catch (Exception ex)
        {
          return;
        }

        HttpURLConnectionExample1 servercall = new HttpURLConnectionExample1();
        String url = CommonShare.url + "Service1.svc/BookComplaint";
        String jsonStr=g.toJson(model);
        try {
          String res = servercall.performPostCallJson(url, jsonStr);
          JSONObject obj=new JSONObject(res);
          int cid=  obj.getInt("ComplaintId");
          itemDAOComplaintModel.updateComplaintNo(cid,model.Token);
        }
        catch (Exception ex)
        {}
      }

    }
    catch (Exception ex)
    {}
  }
  public String uploadPhoto(String localPath)
  {
    String serverpath="";
    try {
      FileUploadClass1 f = new FileUploadClass1();
      String res = f.checkinstream("",localPath, CommonShare.url + "UploadService.svc/UploadImage", 1, "complaint");
      JSONObject j = new JSONObject(res);
      serverpath= j.getString("serverpath");
    }
    catch (Exception ex){}

    return serverpath;
  }
}
