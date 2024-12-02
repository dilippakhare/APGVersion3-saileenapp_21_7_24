package com.apgautomation.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;


public class DB1Helpler extends SQLiteOpenHelper
{
  String complaint="create table Complaint(Token text primary key,ComplaintId integer,CustomerId integer,SerialNumber text,EquipmentName text,EquipmentType text,ModelName text,ModelId text,ComplaintType text,ComplaintDetails text,EnterDate integer,EnterBy text,IsBookedByClient text,SelectedEquipment text,PortNo text,RecId integer ,ContactPerson text,ContactNumber text,Attachment1 text,Localpath text,Satisfacton text,ClientRemark text,Solution text,RemarkBy text," +
          "ClosedBy integer,ComplaintStatus text,CurrentStatus text,AssignToId integer,AssignTo text,ScheduleBy text)";


  public DB1Helpler(@Nullable Context context) {
    super(context,"ComplaintBookingDb5", null, 3);

  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase)
  {
    sqLiteDatabase.execSQL(complaint);

  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
  {

  }
}
