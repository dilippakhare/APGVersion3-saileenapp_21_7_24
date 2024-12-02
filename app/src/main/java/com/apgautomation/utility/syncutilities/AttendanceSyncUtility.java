package com.apgautomation.utility.syncutilities;

import android.content.Context;
import android.util.Log;

import com.apgautomation.controller.AttendanceController;
import com.apgautomation.model.AttendanceModel;
import com.apgautomation.model.ServerModel.ServerAttendanceModel;
import com.apgautomation.model.database.ItemDAOAttendanceModel;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.netrequest.FileUploadClass1;
import com.apgautomation.utility.serverutility.netrequest.HttpURLConnectionExample1;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AttendanceSyncUtility
{
    Context context;
    AttendanceController attendanceController;
    public  AttendanceSyncUtility( Context context)
    {
        this.context=context;
        attendanceController=new AttendanceController(this.context);

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
                        Thread.sleep(1000);
                        ArrayList<AttendanceModel>  list= attendanceController.getNotSavedPhoto();
                        uploadPhotos(list);
                        ArrayList<AttendanceModel> list1= attendanceController.getModifiedRecord();
                        saveToServer(list1);
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

    void uploadPhotos(ArrayList<AttendanceModel> list)
    {
        try
        {
            for(int i=0;i<list.size();i++) {
                FileUploadClass1 f=new FileUploadClass1();
                if( list.get(i).StartPhotoPath ==null  || list.get(i).StartPhotoPath.length()<10) {
                    if (list.get(i).LocalStartPhotoPath != null && list.get(i).LocalStartPhotoPath.length() > 5) {
                        try {
                            String res = f.checkinstream("", list.get(i).LocalStartPhotoPath, CommonShare.url + "UploadService.svc/UploadImage", 1, "attendance");
                            JSONObject j = new JSONObject(res);
                            list.get(i).StartPhotoPath = j.getString("serverpath");
                            ItemDAOAttendanceModel itemDAOAttendanceModel = new ItemDAOAttendanceModel(context);
                            itemDAOAttendanceModel.updatePhotoStartPath(list.get(i));
                        } catch (Exception ex) {
                            Log.e("APG", "Local Photo Error:-" + ex.toString());
                        }
                    }
                }
                if( list.get(i).EndPhotoPath ==null  || list.get(i).EndPhotoPath.length()<10) {
                    {
                        if (list.get(i).LocalEndPhotoPath != null && list.get(i).LocalEndPhotoPath.length() > 5) {
                            try {
                                String res = f.checkinstream("", list.get(i).LocalEndPhotoPath, CommonShare.url + "UploadService.svc/UploadImage", 1, "attendance");
                                JSONObject j = new JSONObject(res);
                                list.get(i).EndPhotoPath = j.getString("serverpath");
                                ItemDAOAttendanceModel itemDAOAttendanceModel = new ItemDAOAttendanceModel(context);
                                itemDAOAttendanceModel.updatePhotoEndPath(list.get(i));
                            } catch (Exception ex) {
                                Log.e("APG", "Local Photo Error:-" + ex.toString());
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("APG","Error:-"+ex.toString());
        }
    }
    void saveToServer(  ArrayList<AttendanceModel> list )
    {
        synchronized(CommonShare.attendanceSync) {
            Gson g = new Gson();
            for (int i = 0; i < list.size(); i++) {
                String jsonStr = g.toJson(list.get(i));
                HttpURLConnectionExample1 servercall = new HttpURLConnectionExample1();
                String url = CommonShare.url + "Service1.svc/SaveAttendance";
                try {
                    String res = servercall.performPostCallJson(url, jsonStr);
                    if (res.startsWith("[")) {
                        JSONArray array = new JSONArray(res);
                        ServerAttendanceModel model = g.fromJson(array.getJSONObject(0).toString(), ServerAttendanceModel.class);
                        AttendanceModel m= model.toAttendanceModel();
                        m.AttendanceDate=list.get(i).AttendanceDate;
                        m.StartTime=list.get(i).StartTime;
                        m.EndTime=list.get(i).EndTime;
                        m.CloseTime=list.get(i).CloseTime;

                        attendanceController.saveServerAttendanceModel(m);
                    } else {
                        ServerAttendanceModel model = g.fromJson(res, ServerAttendanceModel.class);
                        AttendanceModel m= model.toAttendanceModel();

                        m.AttendanceDate=list.get(i).AttendanceDate;
                        m.StartTime=list.get(i).StartTime;
                        m.EndTime=list.get(i).EndTime;
                        m.CloseTime=list.get(i).CloseTime;

                        attendanceController.saveServerAttendanceModel(m);

                    }
                }
                catch (Exception ex){}
            }
        }
    }
}
