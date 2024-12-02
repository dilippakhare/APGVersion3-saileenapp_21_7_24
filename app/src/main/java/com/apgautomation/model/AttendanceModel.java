package com.apgautomation.model;

public class AttendanceModel
{

   /* String attendanceTable="create table Attendance(Token integer primary key,AttendanceId integer," +
            "AttendanceDate integer,int EmpId,StatusId integer," +
            "StartTime intreger,StartLocation text,StartPhotoPath text,StartKm integer," +
            " EndTime integer,EndLocation text,EndPhotoPath text,EndKm integer,ServerStartTime integer,ServerEndTime integer," +
            "IsVerified text,VerifyTime integer,VerifiedById integer,VerifiedStatus text,VerifyRemark text," +
            "IsModified text,LocalStartPhotoPath text,LocalEndPhotoPath text,IsTaApplicable text)";
*/
    public long AttendanceId ;
    public String Token ;
    public long AttendanceDate ;
    public int EmpId ;
    public int StatusId ;
    public long StartTime ;
    public String StartLocation ;
    public String StartPhotoPath ;
    public int StartKm ;
    public long EndTime ;
    public String EndLocation ;
    public String EndPhotoPath ;
    public int EndKm ;
    public long ServerStartTime ;
    public long ServerEndTime ;
    public boolean IsVerified ;
    public long VerifyTime ;
    public int VerifiedById ;
    public String VerifiedStatus ;
    public String VerifyRemark ;

    //Local Database
    public boolean IsModified ;
    public String LocalStartPhotoPath ,LocalEndPhotoPath ;
    public boolean DeleteStatus,IsTaApplicable;

    public String CurrentStatus;
    public long CloseTime ;

}
