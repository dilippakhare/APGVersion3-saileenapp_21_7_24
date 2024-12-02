package com.apgautomation.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.apgautomation.model.ComplaintModel;
import com.apgautomation.model.LeaveModel;

import java.util.ArrayList;

public class ItemDAOComplaintModel
{
  Context context;

  public ItemDAOComplaintModel(Context context) {
    this.context = context;
  }

  public long insertRecord(ComplaintModel bean) {

    SQLiteDatabase db = new DB1Helpler(context).getWritableDatabase();
    ContentValues cv = new ContentValues();
    cv.put("ComplaintDetails", bean.ComplaintDetails);
    cv.put("SelectedEquipment", bean.SelectedEquipment);
    cv.put("CustomerId", bean.CustomerId);
    cv.put("SerialNumber", bean.SerialNumber);
    cv.put("EquipmentName", bean.EquipmentName);
    cv.put("EquipmentType", bean.EquipmentType);
    cv.put("Token", bean.Token);
    cv.put("ModelId", bean.ModelId);
    cv.put("ModelName", bean.ModelName);
    cv.put("RecId", bean.RecId);
    cv.put("PortNo", bean.PortNo);

    cv.put("IsBookedByClient", bean.IsBookedByClient);
    cv.put("EnterBy", bean.EnterBy);
    cv.put("EnterDate", bean.EnterDate);
    cv.put("ComplaintId", bean.ComplaintId);
    cv.put("ContactNumber", bean.ContactNumber);
    cv.put("ContactPerson", bean.ContactPerson);
    cv.put("Localpath", bean.Localpath);
    cv.put("ComplaintType", bean.ComplaintType);
    //cv.put("EnterTime", bean.EnterTime);


    long ln = db.insert("Complaint", null, cv);
    Log.d("APG", "Record Inserted:-" + ln);
    db.close();
    return ln;
  }

  public ArrayList<ComplaintModel> getComplaints()
  {
    ArrayList<ComplaintModel> list = new ArrayList<>();
    SQLiteDatabase db = new DB1Helpler(context).getWritableDatabase();


    Cursor c = db.rawQuery("SELECT * FROM Complaint where ComplaintId=0", null);
    if (c != null) {
      if (c.moveToFirst()) {

        int i = 0;
        while (i < c.getCount())
        {
          ComplaintModel model = new ComplaintModel();
          model.ComplaintDetails = c.getString(c.getColumnIndex("ComplaintDetails"));
          model.Token = c.getString(c.getColumnIndex("Token"));


          model.SerialNumber = c.getString(c.getColumnIndex("SerialNumber"));
          model.EquipmentName = c.getString(c.getColumnIndex("EquipmentName"));
          model.EquipmentType = c.getString(c.getColumnIndex("EquipmentType"));
          model.ModelId = c.getInt(c.getColumnIndex("ModelId"));
          model.ComplaintDetails = c.getString(c.getColumnIndex("ComplaintDetails"));
          model.ModelName = c.getString(c.getColumnIndex("ModelName"));
          model.IsBookedByClient = c.getString(c.getColumnIndex("IsBookedByClient"));
          model.EnterBy = c.getString(c.getColumnIndex("EnterBy"));
          model.ComplaintId = c.getInt(c.getColumnIndex("ComplaintId"));
          model.CustomerId = c.getInt(c.getColumnIndex("CustomerId"));
          model.EnterDate = c.getLong(c.getColumnIndex("EnterDate"));
          model.PortNo = c.getString(c.getColumnIndex("PortNo"));
          model.RecId = c.getInt(c.getColumnIndex("RecId"));
          model.ContactNumber = c.getString(c.getColumnIndex("ContactNumber"));
          model.ContactPerson = c.getString(c.getColumnIndex("ContactPerson"));

          model.SelectedEquipment = c.getString(c.getColumnIndex("SelectedEquipment"));

          model.Attachment1 = c.getString(c.getColumnIndex("Attachment1"));
          model.Localpath = c.getString(c.getColumnIndex("Localpath"));
          model.ComplaintType = c.getString(c.getColumnIndex("ComplaintType"));


          list.add(model);
          c.moveToNext();
          i++;
        }
      }
    }
    return list;
  }


  public void deleteRecord(LeaveModel model)
  {
    SQLiteDatabase db = new DB1Helpler(context).getWritableDatabase();
    int rows = db.delete("Complaint", "Token='" + model.Token + "'", null);
    Log.d("Complaint removed:", rows + "");
    db.close();
  }

  public int updateComplaintNo(int ComplaintId,String Token)
  {
    SQLiteDatabase db = new DB1Helpler(context).getWritableDatabase();

    ContentValues cv=new ContentValues();
    cv.put("ComplaintId", ComplaintId);

    int returnValue= db.update("Complaint", cv,"Token='"+Token+"'",null);
    db.close();
    Log.d("APG","StartPhotoUpdate");
    return returnValue ;
  }
  public int updateAttechment(String Token,String path)
  {
    SQLiteDatabase db = new DB1Helpler(context).getWritableDatabase();

    ContentValues cv=new ContentValues();
    cv.put("Attachment1", path);

    int returnValue= db.update("Complaint", cv,"Token='"+Token+"'",null);
    db.close();
    Log.d("APG","StartPhotoUpdate");
    return returnValue ;
  }

}

