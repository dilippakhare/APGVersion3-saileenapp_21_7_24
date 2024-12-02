package com.apgautomation.model.ServerModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GsonSalesVisit extends RealmObject {

    public String getVisitTokenId() {
        return VisitTokenId;
    }

    public void setVisitTokenId(String visitTokenId) {
        VisitTokenId = visitTokenId;
    }

    public int getSalesVisitId() {
        return SalesVisitId;
    }

    public void setSalesVisitId(int salesVisitId) {
        SalesVisitId = salesVisitId;
    }

    public String getVisitStatus() {
        return VisitStatus;
    }

    public void setVisitStatus(String visitStatus) {
        VisitStatus = visitStatus;
    }

    public boolean isExistingGroup() {
        return IsExistingGroup;
    }

    public void setExistingGroup(boolean existingGroup) {
        IsExistingGroup = existingGroup;
    }

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }

    public int getVGroupId() {
        return VGroupId;
    }

    public void setVGroupId(int VGroupId) {
        this.VGroupId = VGroupId;
    }

    public String getVGroupToken() {
        return VGroupToken;
    }

    public void setVGroupToken(String VGroupToken) {
        this.VGroupToken = VGroupToken;
    }

    public String getVGroupName() {
        return VGroupName;
    }

    public void setVGroupName(String VGroupName) {
        this.VGroupName = VGroupName;
    }

    public int getVAreaId() {
        return VAreaId;
    }

    public void setVAreaId(int VAreaId) {
        this.VAreaId = VAreaId;
    }

    public boolean isExistingCustomer() {
        return IsExistingCustomer;
    }

    public void setExistingCustomer(boolean existingCustomer) {
        IsExistingCustomer = existingCustomer;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public int getVCustomerId() {
        return VCustomerId;
    }

    public void setVCustomerId(int VCustomerId) {
        this.VCustomerId = VCustomerId;
    }

    public String getVCustomerName() {
        return VCustomerName;
    }

    public void setVCustomerName(String VCustomerName) {
        this.VCustomerName = VCustomerName;
    }

    public String getVCustomerToken() {
        return VCustomerToken;
    }

    public void setVCustomerToken(String VCustomerToken) {
        this.VCustomerToken = VCustomerToken;
    }

    public int getVCustomerAreaId() {
        return VCustomerAreaId;
    }

    public void setVCustomerAreaId(int VCustomerAreaId) {
        this.VCustomerAreaId = VCustomerAreaId;
    }

    public long getScheduleDateMillisecond() {
        return ScheduleDateMillisecond;
    }

    public void setScheduleDateMillisecond(long scheduleDateMillisecond) {
        ScheduleDateMillisecond = scheduleDateMillisecond;
    }

    public String getScheduleDate() {
        return ScheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        ScheduleDate = scheduleDate;
    }

    public int getAssignToId() {
        return AssignToId;
    }

    public void setAssignToId(int assignToId) {
        AssignToId = assignToId;
    }

    public String getAssignToName() {
        return AssignToName;
    }

    public void setAssignToName(String assignToName) {
        AssignToName = assignToName;
    }

    public int getEnterById() {
        return EnterById;
    }

    public void setEnterById(int enterById) {
        EnterById = enterById;
    }

    public String getEnteredDate() {
        return EnteredDate;
    }

    public void setEnteredDate(String enteredDate) {
        EnteredDate = enteredDate;
    }
    public long getEnteredDateMillisecond() {
        return EnteredDateMillisecond;
    }

    public void setEnteredDateMillisecond(long enteredDateMillisecond) {
        EnteredDateMillisecond = enteredDateMillisecond;
    }

    public String getEnterByName() {
        return EnterByName;
    }

    public void setEnterByName(String enterByName) {
        EnterByName = enterByName;
    }

    public long getStartTimeMillisecond() {
        return StartTimeMillisecond;
    }

    public void setStartTimeMillisecond(long startTimeMillisecond) {
        StartTimeMillisecond = startTimeMillisecond;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public long getServerStartTime() {
        return ServerStartTime;
    }

    public void setServerStartTime(long serverStartTime) {
        ServerStartTime = serverStartTime;
    }

    public String getStartLocation() {
        return StartLocation;
    }

    public void setStartLocation(String startLocation) {
        StartLocation = startLocation;
    }

    public long getEndTimeMillisecond() {
        return EndTimeMillisecond;
    }

    public void setEndTimeMillisecond(long endTimeMillisecond) {
        EndTimeMillisecond = endTimeMillisecond;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public long getServerEndTime() {
        return ServerEndTime;
    }

    public void setServerEndTime(long serverEndTime) {
        ServerEndTime = serverEndTime;
    }

    public String getEndLocation() {
        return EndLocation;
    }

    public void setEndLocation(String endLocation) {
        EndLocation = endLocation;
    }

    public String getVisitType() {
        return VisitType;
    }

    public void setVisitType(String visitType) {
        VisitType = visitType;
    }

    public String getWorkDetails() {
        return WorkDetails;
    }

    public void setWorkDetails(String workDetails) {
        WorkDetails = workDetails;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getFollowupPendingPoint() {
        return FollowupPendingPoint;
    }

    public void setFollowupPendingPoint(String followupPendingPoint) {
        FollowupPendingPoint = followupPendingPoint;
    }

    public boolean isCallDone() {
        return IsCallDone;
    }

    public void setCallDone(boolean callDone) {
        IsCallDone = callDone;
    }

    public boolean isCallPending() {
        return IsCallPending;
    }

    public void setCallPending(boolean callPending) {
        IsCallPending = callPending;
    }

    public boolean isCallReSchedule() {
        return IsCallReSchedule;
    }

    public void setCallReSchedule(boolean callReSchedule) {
        IsCallReSchedule = callReSchedule;
    }

    public String getRescheduleDate() {
        return RescheduleDate;
    }

    public void setRescheduleDate(String rescheduleDate) {
        RescheduleDate = rescheduleDate;
    }

    public long getChangedDate() {
        return ChangedDate;
    }

    public void setChangedDate(long changedDate) {
        ChangedDate = changedDate;
    }

    public boolean isDeleteStatus() {
        return DeleteStatus;
    }

    public void setDeleteStatus(boolean deleteStatus) {
        DeleteStatus = deleteStatus;
    }

    public boolean isModified() {
        return IsModified;
    }

    public void setModified(boolean modified) {
        IsModified = modified;
    }

    public String getCallPriority() {
        return CallPriority;
    }

    public void setCallPriority(String callPriority) {
        CallPriority = callPriority;
    }

    public String getRefferenceBy() {
        return RefferenceBy;
    }

    public void setRefferenceBy(String refferenceBy) {
        RefferenceBy = refferenceBy;
    }

    public int getCallReffereceToken() {
        return CallReffereceToken;
    }

    public void setCallReffereceToken(int callReffereceToken) {
        CallReffereceToken = callReffereceToken;
    }

    @PrimaryKey
    public String VisitTokenId ;
    public int SalesVisitId ;

    public String VisitStatus ;
    public boolean IsExistingGroup ;
    public int GroupId ;
    public int VGroupId ;
    public String VGroupToken ;
    public String VGroupName ;
    public int VAreaId ;
    public boolean IsExistingCustomer ;
    public int CustomerId ;
    public int VCustomerId ;
    public String VCustomerName ;
    public String VCustomerToken ;
    public int VCustomerAreaId ;

    public long ScheduleDateMillisecond ;
    public String ScheduleDate ;
    public int AssignToId ;
    public String AssignToName ;
    public int EnterById ;
    public String EnteredDate ;


    public long EnteredDateMillisecond;
    public String EnterByName ;


    public long StartTimeMillisecond ;
    public String StartTime ;
    public long ServerStartTime ;
    public String StartLocation ;
    public long EndTimeMillisecond ;
    public String EndTime ;
    public long ServerEndTime ;
    public String EndLocation ;
    public String VisitType ;
    public String WorkDetails ;
    public String Remark ;
    public String FollowupPendingPoint ;
    public boolean IsCallDone ;
    public boolean IsCallPending ;
    public boolean IsCallReSchedule ;

    public long getRescheduleDateMilliSecond() {
        return RescheduleDateMilliSecond;
    }

    public void setRescheduleDateMilliSecond(long rescheduleDateMilliSecond) {
        RescheduleDateMilliSecond = rescheduleDateMilliSecond;
    }

    public long RescheduleDateMilliSecond ;
    public String RescheduleDate;
    public long ChangedDate ;
    public boolean DeleteStatus ;

    public  boolean IsModified;

    public String CallPriority;
    public String RefferenceBy;
    public int CallReffereceToken;
    public String ContactName1;

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

    public String getContactName2() {
        return ContactName2;
    }

    public void setContactName2(String contactName2) {
        ContactName2 = contactName2;
    }

    public String getContactNo2() {
        return ContactNo2;
    }

    public void setContactNo2(String contactNo2) {
        ContactNo2 = contactNo2;
    }

    public String getEmailId2() {
        return EmailId2;
    }

    public void setEmailId2(String emailId2) {
        EmailId2 = emailId2;
    }

    public String ContactNo1;
    public String EmailId1;
    public String ContactName2;
    public String ContactNo2;
    public String EmailId2;
    public String getComponyAddress() {
        return ComponyAddress;
    }

    public void setComponyAddress(String componyAddress) {
        ComponyAddress = componyAddress;
    }

    public String ComponyAddress;


    public String toString()
    {
        return  this.VCustomerName+"("+this.VGroupName+")";
    }
}
