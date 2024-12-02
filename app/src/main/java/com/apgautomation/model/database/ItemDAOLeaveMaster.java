package com.apgautomation.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.apgautomation.model.LeaveDetails;
import com.apgautomation.model.LeaveModel;

import java.util.ArrayList;

public class ItemDAOLeaveMaster {
    Context context;

    public ItemDAOLeaveMaster(Context context) {
        this.context = context;
    }

    public long insertRecord(LeaveModel bean) {

        SQLiteDatabase db = new DBHelpler(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ApproveRemark", bean.ApproveRemark);
        cv.put("ApproveStatus", bean.ApproveStatus);
        cv.put("ApproveTime", bean.ApproveTime);
        cv.put("DeleteStatus", bean.DeleteStatus);
        cv.put("DeleteTime", bean.DeleteTime);
        cv.put("LeaveId", bean.LeaveId);
        cv.put("Token", bean.Token);
        cv.put("EmpId", bean.EmpId);
        cv.put("ToId", bean.ToId);


        cv.put("Reason", bean.Reason);
        cv.put("LeaveType", bean.LeaveType);
        cv.put("ResponsiblePersonId", bean.ResponsiblePersonId);
        cv.put("EnterTime", bean.EnterTime);
        //  cv.put("StartPhotoPath",bean. StartPhotoPath);
        cv.put("SeeenTime", bean.SeeenTime);

        cv.put("TotalDays", bean.TotalDays);

        cv.put("ServerTime", bean.ServerTime);

        //cv.put("ADate",bean.date);
        long ln = db.insert("Leave", null, cv);
        Log.d("APG", "Record Inserted:-" + ln);
        db.close();
        return ln;
    }

    public ArrayList<LeaveModel> getRecentLeave()
    {
        ArrayList<LeaveModel> list = new ArrayList<>();
        SQLiteDatabase db = new DBHelpler(context).getWritableDatabase();


        Cursor c = db.rawQuery("SELECT * FROM Leave ", null);
        if (c != null) {
            if (c.moveToFirst()) {

                int i = 0;
                while (i < c.getCount()) {
                    LeaveModel model = new LeaveModel();
                    model.ApproveRemark = c.getString(c.getColumnIndex("ApproveRemark"));
                    model.Token = c.getString(c.getColumnIndex("Token"));


                    model.ApproveStatus = c.getString(c.getColumnIndex("ApproveStatus"));
                    model.EmpId = c.getInt(c.getColumnIndex("EmpId"));
                    model.ApproveTime = c.getInt(c.getColumnIndex("ApproveTime"));
                    model.DeleteTime = c.getLong(c.getColumnIndex("DeleteTime"));
                    model.LeaveId = c.getInt(c.getColumnIndex("LeaveId"));
                    model.EmpId = c.getInt(c.getColumnIndex("EmpId"));

                    model.ToId = c.getInt(c.getColumnIndex("ToId"));
                    model.Reason = c.getString(c.getColumnIndex("Reason"));
                    model.LeaveType = c.getString(c.getColumnIndex("LeaveType"));
                    model.ResponsiblePersonId = c.getInt(c.getColumnIndex("ResponsiblePersonId"));
                    model.EnterTime = c.getLong(c.getColumnIndex("EnterTime"));
                    model.SeeenTime = c.getLong(c.getColumnIndex("SeeenTime"));

                    model.TotalDays = c.getFloat(c.getColumnIndex("TotalDays"));
                    model.ServerTime = c.getLong(c.getColumnIndex("ServerTime"));
                    String myDeleteDStatus=c.getString(c.getColumnIndex("DeleteStatus"));
                    model.DeleteStatus=c.getString(c.getColumnIndex("DeleteStatus")).equalsIgnoreCase("true")?true :false;
                    if(!model.DeleteStatus  && myDeleteDStatus.equalsIgnoreCase("0"))
                      list.add(model);
                    c.moveToNext();
                    i++;
                }
            }
        }
        return list;
    }


    public long insertLeaveDetailsRecord(LeaveDetails bean) {

        SQLiteDatabase db = new DBHelpler(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Ldate", bean.Ldate);
        cv.put("LeaveType", bean.LeaveType);
        cv.put("TokenId", bean.TokenId);


        long ln = db.insert("LeaveDetails", null, cv);
        Log.d("APG", "Record Inserted:-" + ln);
        db.close();
        return ln;
    }


    public ArrayList<LeaveDetails> getLeaveDetails(String Token) {
        ArrayList<LeaveDetails> list = new ArrayList<>();
        SQLiteDatabase db = new DBHelpler(context).getWritableDatabase();


        Cursor c = db.rawQuery("SELECT * FROM LeaveDetails where TokenId='"+Token+"'", null);
        if (c != null) {
            if (c.moveToFirst()) {

                int i = 0;
                while (i < c.getCount()) {
                    LeaveDetails model = new LeaveDetails();
                    model.Ldate = c.getLong(c.getColumnIndex("Ldate"));
                    model.TokenId = c.getString(c.getColumnIndex("TokenId"));


                    model.LeaveType = c.getString(c.getColumnIndex("LeaveType"));

                    list.add(model);
                    c.moveToNext();
                    i++;
                }
            }

        }
        return list;
    }

    public void deleteRecord(LeaveModel model) {

        SQLiteDatabase db=new DBHelpler(context).getWritableDatabase();
        int rows=db.delete("Leave", "Token='"+model.Token+"'", null);
        Log.d("Attendance removed:", rows+"");
        db.close();

        db=new DBHelpler(context).getWritableDatabase();
        int rows1=db.delete("LeaveDetails", "TokenId='"+model.Token+"'", null);
        Log.d("Attendance removed:", rows+"");
        db.close();
    }

    public void deleteRecordAllRecords()
    {
        try {
            SQLiteDatabase db = new DBHelpler(context).getWritableDatabase();
            int rows = db.delete("Leave", null, null);
            Log.d("Atte Remove Rows:", rows + "");
            db.close();
        }
        catch (Exception ex)
        {}
        try {
            SQLiteDatabase db = new DBHelpler(context).getWritableDatabase();
            int rows = db.delete("LeaveDetails", null, null);
            Log.d("Atte Remove Rows:", rows + "");
            db.close();
        }
        catch (Exception ex)
        {}
    }
}

