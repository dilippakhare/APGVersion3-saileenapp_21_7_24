package com.apgautomation.model.ServerModel;

import com.apgautomation.model.LeaveDetails;
import com.apgautomation.utility.CommonShare;

public class ServerLeaveDetails
{
    public String Ldate;
    public String LeaveType ;
    public String TokenId ;
    public LeaveDetails toLeaveDetils()
    {
        LeaveDetails model=new LeaveDetails();
        model.Ldate= CommonShare.parseDate(Ldate);
        model.LeaveType=LeaveType;
        model.TokenId=TokenId;
        return  model;
    }
}
