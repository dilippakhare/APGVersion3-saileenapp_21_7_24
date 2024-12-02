package com.apgautomation.model;

import java.util.ArrayList;

public class LeaveModel {

    public int LeaveId;
    public String Token ;
    public int EmpId ;
    public int ToId ;
    public String Reason ;
    public String LeaveType ;
    public int ResponsiblePersonId ;
    public long EnterTime ;
    public long SeeenTime;
    public String ApproveStatus ;
    public String ApproveRemark ;
    public long ApproveTime ;
    public boolean DeleteStatus ;
    public long DeleteTime ;
    public float TotalDays ;
    public long ServerTime ;

    public ArrayList<LeaveDetails> leaveDetails=new ArrayList<>();
}
