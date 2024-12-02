package com.apgautomation.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GsonVisitMaster extends RealmObject {

    public int getVisitId() {
        return VisitId;
    }

    public void setVisitId(int visitId) {
        VisitId = visitId;
    }

    public String getVisitToken() {
        return VisitToken;
    }

    public void setVisitToken(String visitToken) {
        VisitToken = visitToken;
    }

    public String getVisitStatus() {
        return VisitStatus;
    }

    public void setVisitStatus(String visitStatus) {
        VisitStatus = visitStatus;
    }

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }

    public int getAssigntoEmpId() {
        return AssigntoEmpId;
    }

    public void setAssigntoEmpId(int assigntoEmpId) {
        AssigntoEmpId = assigntoEmpId;
    }

    public int getBookByEmpId() {
        return BookByEmpId;
    }

    public void setBookByEmpId(int bookByEmpId) {
        BookByEmpId = bookByEmpId;
    }

    public String getBookByName() {
        return BookByName;
    }

    public void setBookByName(String bookByName) {
        BookByName = bookByName;
    }

    public String getScheduleDate() {
        return ScheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        ScheduleDate = scheduleDate;
    }

    public long getScheduleDateMillisecond() {
        return ScheduleDateMillisecond;
    }

    public void setScheduleDateMillisecond(long scheduleDateMillisecond) {
        ScheduleDateMillisecond = scheduleDateMillisecond;
    }

    public String getVisitType() {
        return VisitType;
    }

    public void setVisitType(String visitType) {
        VisitType = visitType;
    }

    public String getReasonForVisit() {
        return ReasonForVisit;
    }

    public void setReasonForVisit(String reasonForVisit) {
        ReasonForVisit = reasonForVisit;
    }

    public int getComplaintNos() {
        return ComplaintNos;
    }

    public void setComplaintNos(int complaintNos) {
        ComplaintNos = complaintNos;
    }

    public String getVisitDate() {
        return VisitDate;
    }

    public void setVisitDate(String visitDate) {
        VisitDate = visitDate;
    }

    public long getVisitDateMillisecond() {
        return VisitDateMillisecond;
    }

    public void setVisitDateMillisecond(long visitDateMillisecond) {
        VisitDateMillisecond = visitDateMillisecond;
    }

    public boolean isDeleteStatus() {
        return DeleteStatus;
    }

    public void setDeleteStatus(boolean deleteStatus) {
        DeleteStatus = deleteStatus;
    }

    public String getEnterDate() {
        return EnterDate;
    }

    public void setEnterDate(String enterDate) {
        EnterDate = enterDate;
    }

    public long getEnterDateMillisecond() {
        return EnterDateMillisecond;
    }

    public void setEnterDateMillisecond(long enterDateMillisecond) {
        EnterDateMillisecond = enterDateMillisecond;
    }

    public String getChangedBy() {
        return ChangedBy;
    }

    public void setChangedBy(String changedBy) {
        ChangedBy = changedBy;
    }

    public String getChangedDate() {
        return ChangedDate;
    }

    public void setChangedDate(String changedDate) {
        ChangedDate = changedDate;
    }

    public long getChangedDateMillisecond() {
        return ChangedDateMillisecond;
    }

    public void setChangedDateMillisecond(long changedDateMillisecond) {
        ChangedDateMillisecond = changedDateMillisecond;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public long getStartTimeMillisecond() {
        return StartTimeMillisecond;
    }

    public void setStartTimeMillisecond(long startTimeMillisecond) {
        StartTimeMillisecond = startTimeMillisecond;
    }

    public String getStartLocation() {
        return StartLocation;
    }

    public void setStartLocation(String startLocation) {
        StartLocation = startLocation;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public long getEndTimeMillisecond() {
        return EndTimeMillisecond;
    }

    public void setEndTimeMillisecond(long endTimeMillisecond) {
        EndTimeMillisecond = endTimeMillisecond;
    }

    public String getServerEndTime() {
        return ServerEndTime;
    }

    public void setServerEndTime(String serverEndTime) {
        ServerEndTime = serverEndTime;
    }

    public String getEndLocation() {
        return EndLocation;
    }

    public void setEndLocation(String endLocation) {
        EndLocation = endLocation;
    }

    public String getVisitRemark() {
        return VisitRemark;
    }

    public void setVisitRemark(String visitRemark) {
        VisitRemark = visitRemark;
    }

    public int getRefferenceVisitId() {
        return RefferenceVisitId;
    }

    public void setRefferenceVisitId(int refferenceVisitId) {
        RefferenceVisitId = refferenceVisitId;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getEtc1() {
        return etc1;
    }

    public void setEtc1(String etc1) {
        this.etc1 = etc1;
    }

    public String getEtc2() {
        return etc2;
    }

    public void setEtc2(String etc2) {
        this.etc2 = etc2;
    }

    public String getEtc3() {
        return etc3;
    }

    public void setEtc3(String etc3) {
        this.etc3 = etc3;
    }

    public String getEtc4() {
        return etc4;
    }

    public void setEtc4(String etc4) {
        this.etc4 = etc4;
    }

    public String getEtc5() {
        return etc5;
    }

    public void setEtc5(String etc5) {
        this.etc5 = etc5;
    }

    public int VisitId ;
    @PrimaryKey
    public String VisitToken ;
    public String VisitStatus ;
    public int GroupId ;
    public int AssigntoEmpId ;
    public int BookByEmpId ;
    public String BookByName ;
    public String ScheduleDate;
    public long ScheduleDateMillisecond;
    public String VisitType ;
    public String ReasonForVisit ;
    public int ComplaintNos ;
    public String VisitDate ;
    public long VisitDateMillisecond ;
    public boolean DeleteStatus ;
    public String EnterDate ;
    public long EnterDateMillisecond;
    public String ChangedBy ;
    public String ChangedDate ;
    public long ChangedDateMillisecond ;

    public String  StartTime ;
    public long  StartTimeMillisecond ;
   // public String  ServerStartTime;
   // public long  ServerStartTimeMillisecond;
    public String StartLocation ;
    public String EndTime;
    public long EndTimeMillisecond;

   // public long ServerEndTimeMillisecond ;
    public String ServerEndTime ;
    public String EndLocation;
    public String VisitRemark ;
    public int RefferenceVisitId ;

    public boolean isModified;

    public String EmpName;
    public String MobileNo;
    public String GroupName;

    public String etc1 ;
    public String etc2 ;
    public String etc3 ;
    public String etc4 ;
    public String etc5 ;

    public String getContactName1() {
        return ContactName1;
    }

    public void setContactName1(String contactName1) {
        ContactName1 = contactName1;
    }

    public String getContactNo1() {
        return ContactNo1;
    }

    public void setContactNo1(String contactNo1) {
        ContactNo1 = contactNo1;
    }

    public String getEmailId1() {
        return EmailId1;
    }

    public void setEmailId1(String emailId1) {
        EmailId1 = emailId1;
    }

    public String ContactName1;
    public String ContactNo1;
    public String EmailId1;

    public String getComponyAddress() {
        return ComponyAddress;
    }

    public void setComponyAddress(String componyAddress) {
        ComponyAddress = componyAddress;
    }

    public String ComponyAddress;


    public void setIsModified(boolean b) {
        this.isModified=true;
    }
    public boolean getIsModified() {
       return this.isModified;
    }
}
