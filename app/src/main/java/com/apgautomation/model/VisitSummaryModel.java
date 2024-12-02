package com.apgautomation.model;

public class VisitSummaryModel {
    public int EmpId,DeptId,PlannedCnt,CompletedCnt,FsrCnt;
    public  String EmpName;
    public  String toString()
    {
        return  EmpName+"("+PlannedCnt+"-"+CompletedCnt+")";
    }
}
