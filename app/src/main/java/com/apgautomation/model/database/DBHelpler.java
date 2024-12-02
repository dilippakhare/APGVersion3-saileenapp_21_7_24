package com.apgautomation.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;


public class DBHelpler extends SQLiteOpenHelper
{

    String attendanceTable="create table Attendance(Token text primary key,AttendanceId integer," +
            "AttendanceDate integer,EmpId integer,StatusId integer," +
            "StartTime intreger,StartLocation text,StartPhotoPath text,StartKm integer," +
            " EndTime integer,EndLocation text,EndPhotoPath text,EndKm integer,ServerStartTime integer,ServerEndTime integer," +
            "IsVerified text,VerifyTime integer,VerifiedById integer,VerifiedStatus text,VerifyRemark text," +
            "IsModified text,LocalStartPhotoPath text,LocalEndPhotoPath text,DeleteStatus text,IsTaApplicable text,CurrentStatus text,CloseTime integer)";

    String leaveTable="create table Leave(Token text primary key,LeaveId integer," +
            "EmpId integer,ToId integer,Reason text," +
            "LeaveType text,ResponsiblePersonId integer,EnterTime integer,SeeenTime integer," +
            " ApproveStatus text,ApproveRemark text,ApproveTime integer,DeleteStatus text,DeleteTime integer,TotalDays REAL," +
            "ServerTime integer)";
       String leaveDetailsTable="create table LeaveDetails(Ldate integer,LeaveType text,TokenId text)";

   // String complaint="create table Complaint(Token text primary key,ComplaintId integer,CustomerId integer,SerialNumber text,EquipmentName text,EquipmentType text,ModelName text,ModelId text,ComplaintType text,ComplaintDetails text,EnterDate integer,EnterBy text,IsBookedByClient text )";


    public DBHelpler(@Nullable Context context) {
        super(context,"apgversion1", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(attendanceTable);
        sqLiteDatabase.execSQL(leaveTable);
        sqLiteDatabase.execSQL(leaveDetailsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}
