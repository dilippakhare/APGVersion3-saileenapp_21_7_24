package com.apgautomation.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.apgautomation.model.AttendanceModel;

import java.util.ArrayList;

public class ItemDAOAttendanceModel
{

    Context context;
    public  ItemDAOAttendanceModel(Context context)
    {
        this.context=context;
    }
    public long insertRecord(AttendanceModel bean)
    {

        SQLiteDatabase db=new DBHelpler(context).getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("AttendanceId",bean. AttendanceId);
        cv.put("Token", bean.Token);
        cv.put("AttendanceDate",bean. AttendanceDate);
        cv.put("EmpId", bean.EmpId);
        cv.put("StatusId",bean. StatusId);
        cv.put("StartTime", bean.StartTime);
        cv.put("StartPhotoPath",bean. StartPhotoPath);
        cv.put("StartKm", bean.StartKm);
        cv.put("StartLocation", bean.StartLocation);


        cv.put("EndTime",bean. EndTime);
        cv.put("EndLocation", bean.EndLocation);
        cv.put("EndPhotoPath",bean. EndPhotoPath);
        cv.put("EndKm", bean.EndKm);
      //  cv.put("StartPhotoPath",bean. StartPhotoPath);
        cv.put("ServerStartTime", bean.ServerStartTime);

        cv.put("EndLocation", bean.EndLocation);

        cv.put("ServerEndTime",bean. ServerEndTime);
        cv.put("IsVerified", bean.IsVerified+"");
        cv.put("VerifyTime",bean. VerifyTime);
        cv.put("VerifiedById", bean.VerifiedById);

        cv.put("VerifiedStatus", bean.VerifiedStatus);
        cv.put("VerifyTime",bean. VerifyTime);
        cv.put("VerifyRemark", bean.VerifyRemark);

        cv.put("DeleteStatus", bean.DeleteStatus);

        cv.put("IsModified", bean.IsModified+"");
        cv.put("LocalStartPhotoPath",bean. LocalStartPhotoPath);
        cv.put("LocalEndPhotoPath", bean.LocalEndPhotoPath);
        cv.put("IsTaApplicable",bean.IsTaApplicable+"");
        cv.put("CurrentStatus",bean.CurrentStatus);
        cv.put("CloseTime",bean.CloseTime);
        //cv.put("ADate",bean.date);
        long ln=db.insert("Attendance", null, cv);
        Log.d("APG","Record Inserted:-"+ln);
        db.close();
        return ln;
    }

    public ArrayList<AttendanceModel> getRecentAttendance()
    {
        ArrayList<AttendanceModel> list=new ArrayList<>();
        SQLiteDatabase db=new DBHelpler(context).getWritableDatabase();


        Cursor c=   db.rawQuery("SELECT * FROM Attendance", null);
        if(c!=null)
        {
            if(c.moveToFirst()) {

                int i = 0;
                while (i < c.getCount()) {
                    AttendanceModel model=new AttendanceModel();
                    model.AttendanceId= c.getInt(c.getColumnIndex("AttendanceId"));
                    model.Token= c.getString(c.getColumnIndex("Token"));


                    model.AttendanceDate= c.getLong(c.getColumnIndex("AttendanceDate"));
                    model.EmpId= c.getInt(c.getColumnIndex("EmpId"));
                    model.StatusId= c.getInt(c.getColumnIndex("StatusId"));
                    model.StartTime= c.getLong(c.getColumnIndex("StartTime"));
                    model.StartPhotoPath= c.getString(c.getColumnIndex("StartPhotoPath"));
                    model.StartKm= c.getInt(c.getColumnIndex("StartKm"));

                    model.EndTime= c.getLong(c.getColumnIndex("EndTime"));
                    model.EndLocation= c.getString(c.getColumnIndex("EndLocation"));
                    model.EndPhotoPath= c.getString(c.getColumnIndex("EndPhotoPath"));
                    model.EndKm= c.getInt(c.getColumnIndex("EndKm"));
                    model.ServerStartTime= c.getLong(c.getColumnIndex("ServerStartTime"));
                    model.ServerEndTime= c.getLong(c.getColumnIndex("StartKm"));
                    try {
                        model.IsVerified = c.getString(c.getColumnIndex("IsVerified")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}
                    model.VerifiedById= c.getInt(c.getColumnIndex("VerifiedById"));
                    model.VerifyTime= c.getLong(c.getColumnIndex("VerifyTime"));

                    try {
                        model.IsModified = c.getString(c.getColumnIndex("IsModified")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}
                    model.LocalStartPhotoPath= c.getString(c.getColumnIndex("LocalStartPhotoPath"));
                    model.LocalEndPhotoPath= c.getString(c.getColumnIndex("LocalEndPhotoPath"));

                    try {
                        model.DeleteStatus = c.getString(c.getColumnIndex("DeleteStatus")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}
                    try {
                        model.IsTaApplicable = c.getString(c.getColumnIndex("IsTaApplicable")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}

                    model.CurrentStatus= c.getString(c.getColumnIndex("CurrentStatus"));

                    model.CloseTime= c.getLong(c.getColumnIndex("CloseTime"));


                    list.add(model);
                    c.moveToNext();
                    i++;
                }
            }
        }
        return list;
    }
    public AttendanceModel  getAttendanceByToken(String Token)
    {

        SQLiteDatabase db=new DBHelpler(context).getWritableDatabase();

        Cursor c=   db.rawQuery("SELECT * FROM Attendance where Token='"+Token+"'", null);
        if(c!=null)
        {
            if(c.moveToFirst()) {

                int i = 0;
                while (i < c.getCount()) {
                    AttendanceModel model=new AttendanceModel();
                    model.AttendanceId= c.getInt(c.getColumnIndex("AttendanceId"));
                    model.Token= c.getString(c.getColumnIndex("Token"));


                    model.AttendanceDate= c.getLong(c.getColumnIndex("AttendanceDate"));
                    model.EmpId= c.getInt(c.getColumnIndex("EmpId"));
                    model.StatusId= c.getInt(c.getColumnIndex("StatusId"));
                    model.StartTime= c.getLong(c.getColumnIndex("StartTime"));
                    model.StartLocation= c.getString(c.getColumnIndex("StartLocation"));
                    model.StartPhotoPath= c.getString(c.getColumnIndex("StartPhotoPath"));
                    model.StartKm= c.getInt(c.getColumnIndex("StartKm"));

                    model.EndTime= c.getLong(c.getColumnIndex("EndTime"));
                    model.EndLocation= c.getString(c.getColumnIndex("EndLocation"));
                    model.EndPhotoPath= c.getString(c.getColumnIndex("EndPhotoPath"));
                    model.EndKm= c.getInt(c.getColumnIndex("EndKm"));
                    model.ServerStartTime= c.getLong(c.getColumnIndex("ServerStartTime"));
                    model.ServerEndTime= c.getLong(c.getColumnIndex("ServerEndTime"));
                    try {
                        model.IsVerified = c.getString(c.getColumnIndex("IsVerified")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}
                    model.VerifiedById= c.getInt(c.getColumnIndex("VerifiedById"));
                    model.VerifyTime= c.getLong(c.getColumnIndex("VerifyTime"));

                    try {
                        model.IsModified = c.getString(c.getColumnIndex("IsModified")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}
                    model.LocalStartPhotoPath= c.getString(c.getColumnIndex("LocalStartPhotoPath"));
                    model.LocalEndPhotoPath= c.getString(c.getColumnIndex("LocalEndPhotoPath"));

                    try {
                        model.DeleteStatus = c.getString(c.getColumnIndex("DeleteStatus")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}
                    try {
                        model.IsTaApplicable = c.getString(c.getColumnIndex("IsTaApplicable")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}

                    model.CurrentStatus= c.getString(c.getColumnIndex("CurrentStatus"));
                    model.CloseTime= c.getLong(c.getColumnIndex("CloseTime"));

                    return  model;
                }
            }

        }
        return null;
    }

    public AttendanceModel getTodayAttendance(long dayStartTime,long dayEndTime, int userId)
    {
        SQLiteDatabase db = new DBHelpler(context).getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM Attendance where AttendanceDate>=" + dayStartTime + " and AttendanceDate<="+dayEndTime, null);
        if (c != null) {
            if (c.moveToFirst()) {
                int i = 0;
                while (i < c.getCount()) {
                    AttendanceModel model=new AttendanceModel();
                    model.AttendanceId= c.getInt(c.getColumnIndex("AttendanceId"));
                    model.Token= c.getString(c.getColumnIndex("Token"));


                    model.AttendanceDate= c.getLong(c.getColumnIndex("AttendanceDate"));
                    model.EmpId= c.getInt(c.getColumnIndex("EmpId"));
                    model.StatusId= c.getInt(c.getColumnIndex("StatusId"));
                    model.StartTime= c.getLong(c.getColumnIndex("StartTime"));
                    model.StartLocation= c.getString(c.getColumnIndex("StartLocation"));
                    model.StartPhotoPath= c.getString(c.getColumnIndex("StartPhotoPath"));
                    model.StartKm= c.getInt(c.getColumnIndex("StartKm"));

                    model.EndTime= c.getLong(c.getColumnIndex("EndTime"));
                    model.EndLocation= c.getString(c.getColumnIndex("EndLocation"));
                    model.EndPhotoPath= c.getString(c.getColumnIndex("EndPhotoPath"));
                    model.EndKm= c.getInt(c.getColumnIndex("EndKm"));
                    model.ServerStartTime= c.getLong(c.getColumnIndex("ServerStartTime"));
                    model.ServerEndTime= c.getLong(c.getColumnIndex("ServerEndTime"));
                    try {
                        model.IsVerified = c.getString(c.getColumnIndex("IsVerified")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}
                    model.VerifiedById= c.getInt(c.getColumnIndex("VerifiedById"));
                    model.VerifyTime= c.getLong(c.getColumnIndex("VerifyTime"));

                    try {
                        model.IsModified = c.getString(c.getColumnIndex("IsModified")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}
                    model.LocalStartPhotoPath= c.getString(c.getColumnIndex("LocalStartPhotoPath"));
                    model.LocalEndPhotoPath= c.getString(c.getColumnIndex("LocalEndPhotoPath"));

                    try {
                        model.DeleteStatus = c.getString(c.getColumnIndex("DeleteStatus")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}
                    try {
                        model.IsTaApplicable = c.getString(c.getColumnIndex("IsTaApplicable")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}

                    model.CurrentStatus= c.getString(c.getColumnIndex("CurrentStatus"));
                    model.CloseTime= c.getLong(c.getColumnIndex("CloseTime"));

                    return  model;
                }

            }
        }
        return null;
    }

    public void deleteRecord(AttendanceModel model)
    {
        SQLiteDatabase db=new DBHelpler(context).getWritableDatabase();
        int rows=db.delete("Attendance", "Token='"+model.Token+"'", null);
        Log.d("Attendance removed:", rows+"");
        db.close();

    }
    public void deleteAllRecord()
    {
        SQLiteDatabase db=new DBHelpler(context).getWritableDatabase();
        int rows=db.delete("Attendance", null, null);
        Log.d("Atte Remove Rows:", rows+"");
        db.close();

    }

    public ArrayList<AttendanceModel> getNotSavedPhotoList()
    {
        ArrayList<AttendanceModel> list=new ArrayList<>();
        SQLiteDatabase db = new DBHelpler(context).getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM Attendance    where   length( ifnull(StartPhotoPath,''))<10"+
                       " OR length( ifnull( EndPhotoPath,''))<10  "
              , null);
        if (c != null) {
            if (c.moveToFirst()) {
                int i = 0;
                while (i < c.getCount()) {
                    AttendanceModel model=new AttendanceModel();
                    model.AttendanceId= c.getInt(c.getColumnIndex("AttendanceId"));
                    model.Token= c.getString(c.getColumnIndex("Token"));
                    model.AttendanceDate= c.getLong(c.getColumnIndex("AttendanceDate"));
                    model.EmpId= c.getInt(c.getColumnIndex("EmpId"));
                    model.StatusId= c.getInt(c.getColumnIndex("StatusId"));
                    model.StartTime= c.getLong(c.getColumnIndex("StartTime"));
                    model.StartLocation= c.getString(c.getColumnIndex("StartLocation"));
                    model.StartPhotoPath= c.getString(c.getColumnIndex("StartPhotoPath"));
                    model.StartKm= c.getInt(c.getColumnIndex("StartKm"));
                    model.EndTime= c.getLong(c.getColumnIndex("EndTime"));
                    model.EndLocation= c.getString(c.getColumnIndex("EndLocation"));
                    model.EndPhotoPath= c.getString(c.getColumnIndex("EndPhotoPath"));
                    model.EndKm= c.getInt(c.getColumnIndex("EndKm"));
                    model.ServerStartTime= c.getLong(c.getColumnIndex("ServerStartTime"));
                    model.ServerEndTime= c.getLong(c.getColumnIndex("ServerEndTime"));
                    try {
                        model.IsVerified = c.getString(c.getColumnIndex("IsVerified")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}
                    model.VerifiedById= c.getInt(c.getColumnIndex("VerifiedById"));
                    model.VerifyTime= c.getLong(c.getColumnIndex("VerifyTime"));

                    try {
                        model.IsModified = c.getString(c.getColumnIndex("IsModified")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}
                    model.LocalStartPhotoPath= c.getString(c.getColumnIndex("LocalStartPhotoPath"));
                    model.LocalEndPhotoPath= c.getString(c.getColumnIndex("LocalEndPhotoPath"));

                    try {
                        model.DeleteStatus = c.getString(c.getColumnIndex("DeleteStatus")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}
                    try {
                        model.IsTaApplicable = c.getString(c.getColumnIndex("IsTaApplicable")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}

                    model.CurrentStatus= c.getString(c.getColumnIndex("CurrentStatus"));
                    model.CloseTime= c.getLong(c.getColumnIndex("CloseTime"));
                    list.add(  model);
                    c.moveToNext();
                    i++;
                }

            }
        }
        return list;
    }

    public int updatePhotoStartPath(AttendanceModel attendanceModel)
    {
        SQLiteDatabase db = new DBHelpler(context).getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put("StartPhotoPath", attendanceModel.StartPhotoPath);

        int returnValue= db.update("Attendance", cv,"Token='"+attendanceModel.Token+"'",null);
        db.close();
        Log.d("APG","StartPhotoUpdate");
        return returnValue ;
    }
    public int updatePhotoEndPath(AttendanceModel attendanceModel)
    {
        SQLiteDatabase db = new DBHelpler(context).getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put("EndPhotoPath", attendanceModel.EndPhotoPath);

        int returnValue= db.update("Attendance", cv,"Token='"+attendanceModel.Token+"'",null);
        db.close();
        Log.d("APG","EndPhotoUpdate");
        return returnValue ;
    }

    public ArrayList<AttendanceModel> getModiefiedRecords()
    {
        ArrayList<AttendanceModel> list=new ArrayList<>();
        SQLiteDatabase db = new DBHelpler(context).getWritableDatabase();
//where IsModified='true'
        Cursor c = db.rawQuery("SELECT * FROM Attendance  where IsModified='true'", null);
        if (c != null) {
            if (c.moveToFirst()) {
                int i = 0;
                while (i < c.getCount()) {
                    AttendanceModel model=new AttendanceModel();
                    model.AttendanceId= c.getInt(c.getColumnIndex("AttendanceId"));
                    model.Token= c.getString(c.getColumnIndex("Token"));
                    model.AttendanceDate= c.getLong(c.getColumnIndex("AttendanceDate"));
                    model.EmpId= c.getInt(c.getColumnIndex("EmpId"));
                    model.StatusId= c.getInt(c.getColumnIndex("StatusId"));
                    model.StartTime= c.getLong(c.getColumnIndex("StartTime"));
                    model.StartLocation= c.getString(c.getColumnIndex("StartLocation"));
                    model.StartPhotoPath= c.getString(c.getColumnIndex("StartPhotoPath"));
                    model.StartKm= c.getInt(c.getColumnIndex("StartKm"));
                    model.EndTime= c.getLong(c.getColumnIndex("EndTime"));
                    model.EndLocation= c.getString(c.getColumnIndex("EndLocation"));
                    model.EndPhotoPath= c.getString(c.getColumnIndex("EndPhotoPath"));
                    model.EndKm= c.getInt(c.getColumnIndex("EndKm"));
                    model.ServerStartTime= c.getLong(c.getColumnIndex("ServerStartTime"));
                    model.ServerEndTime= c.getLong(c.getColumnIndex("ServerEndTime"));
                    try {
                        model.IsVerified = c.getString(c.getColumnIndex("IsVerified")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}
                    model.VerifiedById= c.getInt(c.getColumnIndex("VerifiedById"));
                    model.VerifyTime= c.getLong(c.getColumnIndex("VerifyTime"));

                    try {
                        model.IsModified = c.getString(c.getColumnIndex("IsModified")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}
                    model.LocalStartPhotoPath= c.getString(c.getColumnIndex("LocalStartPhotoPath"));
                    model.LocalEndPhotoPath= c.getString(c.getColumnIndex("LocalEndPhotoPath"));

                    try {
                        model.DeleteStatus = c.getString(c.getColumnIndex("DeleteStatus")).equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}
                    try {
                        String strTA=c.getString(c.getColumnIndex("IsTaApplicable"));
                        model.IsTaApplicable = strTA.equalsIgnoreCase("true")?true :false;
                    }catch (Exception ex){}

                    model.CurrentStatus= c.getString(c.getColumnIndex("CurrentStatus"));
                    model.CloseTime= c.getLong(c.getColumnIndex("CloseTime"));
                    list.add(  model);
                    c.moveToNext();
                    i++;
                }

            }
        }
        return list;
    }
}
