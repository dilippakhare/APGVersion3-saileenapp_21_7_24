package com.apgautomation.model.ServerModel;

import com.apgautomation.model.LeaveModel;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;

public class ServerLeaveModel {
    public int LeaveId;
    public String Token ;
    public int EmpId ;
    public int ToId ;
    public String Reason ;
    public String LeaveType ;
    public int ResponsiblePersonId ;
    public String EnterTime ;
    public String SeeenTime;
    public String ApproveStatus ;
    public String ApproveRemark ;
    public String ApproveTime ;
    public boolean DeleteStatus ;
    public String DeleteTime ;
    public float TotalDays ;
    public String ServerTime ;
    public ArrayList<ServerLeaveDetails> leaveDetails=new ArrayList<>();
    public LeaveModel toLeaveModel()
    {
        LeaveModel model=new LeaveModel();
        model.LeaveId=LeaveId;
        model.Token=Token;
        model.EmpId=EmpId;
        model.ToId=ToId;
        model.Reason=Reason;
        model.LeaveType=LeaveType;
        model.ResponsiblePersonId=ResponsiblePersonId;
        model.EnterTime= CommonShare.parseDate( EnterTime);
        model.SeeenTime= CommonShare.parseDate( SeeenTime);
        model.ApproveStatus=ApproveStatus;
        model.ApproveRemark=ApproveRemark;
        model.ApproveTime=CommonShare.parseDate(ApproveTime);
        model.DeleteStatus=DeleteStatus;
        model.DeleteTime=CommonShare.parseDate(DeleteTime);
        model.TotalDays=TotalDays;
        model.ServerTime=CommonShare.parseDate(ServerTime);
        model.DeleteStatus=this.DeleteStatus;

        return  model;
    }
}
