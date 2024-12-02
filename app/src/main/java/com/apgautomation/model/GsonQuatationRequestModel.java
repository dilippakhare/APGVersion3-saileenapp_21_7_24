package com.apgautomation.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GsonQuatationRequestModel extends RealmObject {
    public int QuotationId ;
    @PrimaryKey
    public String QuatationToken ;
    public int GroupId;
    public int VisitId;

 /*   public int getQuotatationNo() {
        return QuotatationNo;
    }

    public void setQuotatationNo(int quotatationNo) {
        QuotatationNo = quotatationNo;
    }

    public int QuotatationNo;*/
    public String VisitToken ;
    public String ContactPerson ;
    public String ContactNumber ;
    public int EnterByEMpId ;

    public String getVisitType() {
        return VisitType;
    }

    public void setVisitType(String visitType) {
        VisitType = visitType;
    }

    public String VisitType;

    public int getAssignToEmpId() {
        return AssignToEmpId;
    }

    public void setAssignToEmpId(int assignToEmpId) {
        AssignToEmpId = assignToEmpId;
    }

    public String getResponseRemark() {
        return ResponseRemark;
    }

    public void setResponseRemark(String responseRemark) {
        ResponseRemark = responseRemark;
    }

    public String ResponseRemark;

    public int AssignToEmpId ;
    public String EnterDate ;
    public long EnterDateMillisecond ;
    public String Attachment ;
    public String QuationDetails ;
    public String ResponseDate ;
    public long ResponseDateMillisecond ;
    public int ResponseByEmpId ;
    public boolean isModified;
    public String Localpath;

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String Remark;
    public int getRequestTag() {
        return requestTag;
    }

    public void setRequestTag(int requestTag) {
        this.requestTag = requestTag;
    }

    public int requestTag;


    public String getLocalpath() {
        return Localpath;
    }

    public void setLocalpath(String localpath) {
        Localpath = localpath;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    public int getQuotationId() {
        return QuotationId;
    }

    public void setQuotationId(int quotationId) {
        QuotationId = quotationId;
    }

    public String getQuatationToken() {
        return QuatationToken;
    }

    public void setQuatationToken(String quatationToken) {
        QuatationToken = quatationToken;
    }

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }

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

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        ContactPerson = contactPerson;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public int getEnterByEMpId() {
        return EnterByEMpId;
    }

    public void setEnterByEMpId(int enterByEMpId) {
        EnterByEMpId = enterByEMpId;
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

    public String getAttachment() {
        return Attachment;
    }

    public void setAttachment(String attachment) {
        Attachment = attachment;
    }

    public String getQuationDetails() {
        return QuationDetails;
    }

    public void setQuationDetails(String quationDetails) {
        QuationDetails = quationDetails;
    }

    public String getResponseDate() {
        return ResponseDate;
    }

    public void setResponseDate(String responseDate) {
        ResponseDate = responseDate;
    }

    public long getResponseDateMillisecond() {
        return ResponseDateMillisecond;
    }

    public void setResponseDateMillisecond(long responseDateMillisecond) {
        ResponseDateMillisecond = responseDateMillisecond;
    }

    public int getResponseByEmpId() {
        return ResponseByEmpId;
    }

    public void setResponseByEmpId(int responseByEmpId) {
        ResponseByEmpId = responseByEmpId;
    }
}
