package com.apgautomation.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GsonComplaintWork extends RealmObject {

    public String getWorkToken() {
        return WorkToken;
    }

    public void setWorkToken(String workToken) {
        WorkToken = workToken;
    }

    public String getComplaintTokenToken() {
        return ComplaintTokenToken;
    }

    public void setComplaintTokenToken(String complaintTokenToken) {
        ComplaintTokenToken = complaintTokenToken;
    }

    public String getWork() {
        return Work;
    }

    public void setWork(String work) {
        Work = work;
    }

    public String getEnterDate() {
        return EnterDate;
    }

    public void setEnterDate(String enterDate) {
        EnterDate = enterDate;
    }

    public long getEnterMillisecond() {
        return EnterMillisecond;
    }

    public void setEnterMillisecond(long enterMillisecond) {
        EnterMillisecond = enterMillisecond;
    }

    public int getEmpId() {
        return EmpId;
    }

    public void setEmpId(int empId) {
        EmpId = empId;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    @PrimaryKey
    public String WorkToken;

    public int getWorkId() {
        return WorkId;
    }

    public void setWorkId(int workId) {
        WorkId = workId;
    }

    public int WorkId;
    public String ComplaintTokenToken;
    public String Work;
    public String EnterDate;
    public long EnterMillisecond;
    public int EmpId;
    public String EmpName;

    public String getAttachment() {
        return Attachment;
    }

    public void setAttachment(String attachment) {
        Attachment = attachment;
    }

    public String Attachment;


    public boolean isModified() {
        return IsModified;
    }

    public void setModified(boolean modified) {
        IsModified = modified;
    }

    public boolean IsModified;
}
