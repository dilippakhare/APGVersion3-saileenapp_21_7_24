package com.apgautomation.model;

public class EmployeeModel
{
    public  int EmpId ,DeptId ,MangerId;
    public String EmpCode ,EmpName,EmpAdrress,EmpPhoto,RoleName,MobileNo,MobileNo1 ,WeeklyOfDay;
    public boolean DeleteStatus,IsChecked,IsOldCamera;
   public EmployeeModel()
   {}
    public String toString()
    {
        return  EmpName;
    }
    public EmployeeModel(int id ,String nm)
    {
        EmpId=id;EmpName=nm;
    }
}
