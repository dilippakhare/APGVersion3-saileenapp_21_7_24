package com.apgautomation.utility.syncutilities;

import android.content.Context;
import android.util.Log;

import com.apgautomation.controller.AttendanceController;
import com.apgautomation.model.LeaveModel;
import com.apgautomation.model.ServerModel.ServerLeaveDetails;
import com.apgautomation.model.ServerModel.ServerLeaveModel;
import com.apgautomation.model.database.ItemDAOLeaveMaster;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.netrequest.HttpURLConnectionExample1;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LeaveSyncUtility {
    Context context;
    AttendanceController attendanceController;
    public  LeaveSyncUtility( Context context)
    {
        this.context=context;
       // attendanceController=new AttendanceController(this.context);

    }
    public void sync()
    {
        try
        {

            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    try {
                        ItemDAOLeaveMaster itemDAOLeaveMaster=new ItemDAOLeaveMaster(context);
                        ArrayList<LeaveModel> list= itemDAOLeaveMaster.getRecentLeave();
                        for(int i=0;i<list.size();i++)
                        {
                            list.get(i).leaveDetails=itemDAOLeaveMaster.getLeaveDetails(list.get(i).Token);
                        }
                        saveToServer(list);
                    }
                    catch (Exception ex){
                        Log.e("APG",ex.toString());
                    }

                }
            }).start();


        }
        catch (Exception ex)
        {}
    }

    void saveToServer(  ArrayList<LeaveModel> list )
    {
        synchronized(CommonShare.attendanceSync) {
            Gson g = new Gson();
            for (int i = 0; i < list.size(); i++)
            {
                if(list.get(i).LeaveId==0)
                {
                    try {
                        String jsonStr = g.toJson(list.get(i));
                        HttpURLConnectionExample1 servercall = new HttpURLConnectionExample1();
                        String url = CommonShare.url + "Service1.svc/SaveLeave";
                        String res = servercall.performPostCallJson(url, jsonStr);
                        JSONObject jsonObject=new JSONObject(res);
                        ServerLeaveModel model = g.fromJson(jsonObject.getJSONObject("leaveApplication").toString(), ServerLeaveModel.class);
                        try
                        {
                            model.leaveDetails=new ArrayList<>();
                            JSONArray arr=jsonObject.getJSONArray("leaveDetails");
                            for(int j=0;j<arr.length();j++)
                            {
                                ServerLeaveDetails mOb = g.fromJson(jsonObject.getJSONObject("leaveApplication").toString(), ServerLeaveDetails.class);
                                model.leaveDetails.add(mOb);
                            }
                        }
                        catch (Exception ex){}
                       ItemDAOLeaveMaster itemDAOLeaveMaster = new ItemDAOLeaveMaster(context);
                        itemDAOLeaveMaster.deleteRecord(model.toLeaveModel());
                        itemDAOLeaveMaster.insertRecord(model.toLeaveModel());
                        if (model.leaveDetails != null) {
                            for (int index = 0; index < model.leaveDetails.size(); index++) {
                                itemDAOLeaveMaster.insertLeaveDetailsRecord(model.leaveDetails.get(index).toLeaveDetils());
                            }
                        }
                    }
                    catch (Exception ex)
                    {
                        Log.d("APG",ex.toString());
                    }
                }
               // ServerAttendanceModel model = g.fromJson(res, ServerAttendanceModel.class);
              //  attendanceController.saveServerAttendanceModel(model.toAttendanceModel());
            }
        }
    }
}
