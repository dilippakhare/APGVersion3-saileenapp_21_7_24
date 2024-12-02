package com.apgautomation.model.ServerModel;

public class EmployeeAttendanceListModel {

    public long AttendanceId ;
    public String Token ;
    public String AttendanceDate ;
    public int EmpId ;
    public int StatusId ;
    public String StartTime ;
    public String StartLocation ;
    public String StartPhotoPath ;
    public int StartKm ;
    public String EndTime ;
    public String EndLocation ;
    public String EndPhotoPath ;
    public int EndKm ;
    public String ServerStartTime ;
    public String ServerEndTime ;
    public boolean IsVerified ;
    public String VerifyTime ;
    public int VerifiedById ;
    public String VerifiedStatus ;
    public String VerifyRemark ;

    //Local Database
    public boolean IsModified ;
    public boolean IsTaApplicable;
    public String LocalStartPhotoPath ,LocalEndPhotoPath ;
    public  boolean DeleteStatus;
    public String CurrentStatus;
    public String CloseTime   ,StatusName  ,EmpName ,EmpPhoto ,MobileNo ,DeptId ,DeptName ;


}
