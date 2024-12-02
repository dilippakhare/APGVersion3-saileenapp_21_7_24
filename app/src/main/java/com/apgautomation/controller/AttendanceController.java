package com.apgautomation.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.apgautomation.model.AttendanceModel;
import com.apgautomation.model.database.ItemDAOAttendanceModel;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.DownloadUtility;

import java.util.ArrayList;

public class AttendanceController implements DownloadUtility
{
    Context context;
    public AttendanceController(Context context)
    {
        this.context=context;
    }
    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

    }


    public AttendanceModel getTodaysAttendanceToken()
    {
        ItemDAOAttendanceModel itemDAOAttendanceModel=new ItemDAOAttendanceModel(context);
        String strDate= CommonShare.convertToDate(System.currentTimeMillis());
        long startDateTime=  CommonShare.ddMMYYDateToLong(strDate.replace("/","-"));
        long endDatetime=startDateTime+86364000;
        AttendanceModel model= itemDAOAttendanceModel.getTodayAttendance(startDateTime,endDatetime, CommonShare.getUserId(context));
        return model;
    }

    public AttendanceModel generateTodaysAttendanceModel()
    {
        String token=  CommonShare.generateToken(context);
        AttendanceModel model=new AttendanceModel();
        model.Token=token;

        return model;
    }

    public void saveLocalAttendanceModel(AttendanceModel model)
    {
            ItemDAOAttendanceModel itemDAOAttendanceModel = new ItemDAOAttendanceModel(context);
            itemDAOAttendanceModel.deleteRecord(model);
            model.IsModified = true;
            itemDAOAttendanceModel.insertRecord(model);
            try {
                Intent i = new Intent();
                i.setAction("RefreshAttendance");
                ((Activity) context).sendBroadcast(i);
            }
            catch (Exception ex)
            {
            }
        try {
            Intent i = new Intent();
            i.setAction("SendAttendance");
            ((Activity) context).sendBroadcast(i);
        }
        catch (Exception ex)
        {
        }

    }
    public void saveServerAttendanceModel(AttendanceModel model)
    {
        ItemDAOAttendanceModel itemDAOAttendanceModel=new ItemDAOAttendanceModel(context);
        itemDAOAttendanceModel.deleteRecord(model);
        model.IsModified=false;
        itemDAOAttendanceModel.insertRecord(model);

    }
    public ArrayList<AttendanceModel> getNotSavedPhoto()
    {
        ItemDAOAttendanceModel itemDAOAttendanceModel=new ItemDAOAttendanceModel(context);
        ArrayList<AttendanceModel> list=  itemDAOAttendanceModel.getNotSavedPhotoList();
        return  list;
    }

    public ArrayList<AttendanceModel> getModifiedRecord()
    {
        ItemDAOAttendanceModel itemDAOAttendanceModel=new ItemDAOAttendanceModel(context);
        ArrayList<AttendanceModel> list=  itemDAOAttendanceModel.getModiefiedRecords();
        return  list;
    }

    public AttendanceModel getLastDayAttendanceToken()
    {
        ItemDAOAttendanceModel itemDAOAttendanceModel=new ItemDAOAttendanceModel(context);
        String strDate= CommonShare.convertToDate( new CommonShare().getForwordDay( System.currentTimeMillis(),-1));
        long startDateTime=  CommonShare.ddMMYYDateToLong(strDate.replace("/","-"));
        long endDatetime=startDateTime+86364000;
        AttendanceModel model= itemDAOAttendanceModel.getTodayAttendance(startDateTime,endDatetime, CommonShare.getUserId(context));
        return model;
    }
}
