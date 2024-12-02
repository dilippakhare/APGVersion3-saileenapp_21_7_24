package com.apgautomation.model;

public class AttendanceStatusBean
{
    public int StatusId,SequenceNo;
    public String StatusName;
    public boolean IsFullDay,IsFirstHalf,IsSecondHalf,IsWeeklyOff,IsHoliday,IsExtraWorkingDay,DeleteStatus;



    public  String toString()
    {
        return  StatusName;
    }
}
