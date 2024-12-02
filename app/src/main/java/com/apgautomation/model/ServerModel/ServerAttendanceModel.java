package com.apgautomation.model.ServerModel;


import com.apgautomation.model.AttendanceModel;
import com.apgautomation.utility.CommonShare;

public class ServerAttendanceModel {
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
    public String CloseTime ;


    //--- Other Details---
    public String  StatusName,EmpName,EmpPhoto,MobileNo,MobileNo1,DeptId,DeptName;
    public AttendanceModel toAttendanceModel()
    {
        AttendanceModel model=new AttendanceModel();
        model.AttendanceId=this.AttendanceId ;
        model.Token=this.Token ;
        model.AttendanceDate= CommonShare.parseDate(this.AttendanceDate);
        model.EmpId=this.EmpId ;
        model.StatusId=this.StatusId ;
        model. StartTime= CommonShare.parseDate(this.StartTime) ;
        model.StartLocation=this.StartLocation ;
        model. StartPhotoPath=this.StartPhotoPath ;
        model.StartKm=this.StartKm ;
        model. EndTime=CommonShare.parseDate(this.EndTime);
        model.EndLocation=this.EndLocation ;
        model.EndPhotoPath=this.EndPhotoPath ;
        model. EndKm=this.EndKm ;
        model. ServerStartTime=CommonShare.parseDate(this.ServerStartTime) ;
        model.ServerEndTime=CommonShare.parseDate(this.ServerEndTime) ;
        model.IsVerified=this.IsVerified ;
        model.VerifyTime= CommonShare.parseDate( this.VerifyTime ) ;
        model.VerifiedById=this.VerifiedById ;
        model.VerifiedStatus=this.VerifiedStatus ;
        model.VerifyRemark=this.VerifyRemark ;
        model.DeleteStatus=this.DeleteStatus;
        model.IsTaApplicable=this.IsTaApplicable;
        model.CurrentStatus=this.CurrentStatus;
        model.CloseTime=CommonShare.parseDate(this.CloseTime) ;
        model.LocalStartPhotoPath=this.LocalStartPhotoPath;
        model.LocalEndPhotoPath=this.LocalEndPhotoPath;

        return  model;
    }

}
