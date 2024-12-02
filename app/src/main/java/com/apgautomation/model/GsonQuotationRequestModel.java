package com.apgautomation.model;

import com.apgautomation.utility.CommonShare;

public class GsonQuotationRequestModel {
    private float QuotationId;
    private String QuatationToken;
    private float GroupId;
    private float VisitId;
    private String VisitToken;
    private String ContactPerson = null;
    private String ContactNumber = null;
    private float EnterByEMpId;
    private String EnterDate;
    private String Attachment;
    private String QuationDetails;
    private String ResponseDate = null;
    private String ResponseByEmpId = null;
    private String Localpath;
    private String GroupName;
    public String ResponseRemark;
    public String VisitType;
    public String getVisitType() {
        return VisitType;
    }

    public void setVisitType(String visitType) {
        VisitType = visitType;
    }


    public String getResponseRemark() {
        return ResponseRemark;
    }

    public void setResponseRemark(String responseRemark) {
        ResponseRemark = responseRemark;
    }



    public String getAssignedToName() {
        return AssignedToName;
    }

    public void setAssignedToName(String assignedToName) {
        AssignedToName = assignedToName;
    }

    public String AssignedToName;

    public int getAssignToEmpId() {
        return AssignToEmpId;
    }

    public void setAssignToEmpId(int assignToEmpId) {
        AssignToEmpId = assignToEmpId;
    }

    public int AssignToEmpId ;


    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String Remark;

    // Getter Methods

    public float getQuotationId() {
        return QuotationId;
    }

    public String getQuatationToken() {
        return QuatationToken;
    }

    public float getGroupId() {
        return GroupId;
    }

    public float getVisitId() {
        return VisitId;
    }

    public String getVisitToken() {
        return VisitToken;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public float getEnterByEMpId() {
        return EnterByEMpId;
    }

    public String getEnterDate() {
        return EnterDate;
    }

    public String getAttachment() {
        return Attachment;
    }

    public String getQuationDetails() {
        return QuationDetails;
    }

    public String getResponseDate() {
        return ResponseDate;
    }

    public String getResponseByEmpId() {
        return ResponseByEmpId;
    }

    public String getLocalpath() {
        return Localpath;
    }

    public String getGroupName() {
        return GroupName;
    }

    // Setter Methods

    public void setQuotationId(float QuotationId) {
        this.QuotationId = QuotationId;
    }

    public void setQuatationToken(String QuatationToken) {
        this.QuatationToken = QuatationToken;
    }

    public void setGroupId(float GroupId) {
        this.GroupId = GroupId;
    }

    public void setVisitId(float VisitId) {
        this.VisitId = VisitId;
    }

    public void setVisitToken(String VisitToken) {
        this.VisitToken = VisitToken;
    }

    public void setContactPerson(String ContactPerson) {
        this.ContactPerson = ContactPerson;
    }

    public void setContactNumber(String ContactNumber) {
        this.ContactNumber = ContactNumber;
    }

    public void setEnterByEMpId(float EnterByEMpId) {
        this.EnterByEMpId = EnterByEMpId;
    }

    public void setEnterDate(String EnterDate) {
        this.EnterDate = EnterDate;
    }

    public void setAttachment(String Attachment) {
        this.Attachment = Attachment;
    }

    public void setQuationDetails(String QuationDetails) {
        this.QuationDetails = QuationDetails;
    }

    public void setResponseDate(String ResponseDate) {
        this.ResponseDate = ResponseDate;
    }

    public void setResponseByEmpId(String ResponseByEmpId) {
        this.ResponseByEmpId = ResponseByEmpId;
    }

    public void setLocalpath(String Localpath) {
        this.Localpath = Localpath;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }


    public String toString()
    {
        String str="Quotation Request For "+this.GroupName+"";
        try {
            str=str+" on "+CommonShare.getDateTime(CommonShare.parseDate(this.getEnterDate()));
        }
        catch (Exception ex)
        {}
        return str;
    }
}