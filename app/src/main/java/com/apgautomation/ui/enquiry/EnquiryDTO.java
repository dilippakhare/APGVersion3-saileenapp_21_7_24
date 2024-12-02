package com.apgautomation.ui.enquiry;

public class EnquiryDTO {
    private int RecId;
    private int EmpId;
    private String ProductName;
    private int SentTo;
    private int CustomerId;
    private int GroupId;
    private String ContactPerson;
    private String ContactNo;
    private String EnquiryDetails;
    private String EnterDate = null;
    private String EnterBy = null;
    private String EnquiryVerification;
    private String OrderStatus = null;
    private String OrderValue = null;
    private String VeryfiedBy = null;
    private String ChangedDate = null;
    private String ChangedBy = null;
    private String DeleteStatus = null;
    private String DeleteDate = null;

    private String EmpName = null;
    private String GroupName = null;
    private String CustomerName = null;
    public String SentToName;
    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getEnterByName() {
        return EnterByName;
    }

    public void setEnterByName(String enterByName) {
        EnterByName = enterByName;
    }

    public String getSentToName() {
        return SentToName;
    }

    public void setSentToName(String sentToName) {
        SentToName = sentToName;
    }

    private String EnterByName = null;

    // Getter Methods

    public int getRecId() {
        return RecId;
    }

    public int getEmpId() {
        return EmpId;
    }

    public String getProductName() {
        return ProductName;
    }

    public int getSentTo() {
        return SentTo;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public int getGroupId() {
        return GroupId;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public String getEnquiryDetails() {
        return EnquiryDetails;
    }

    public String getEnterDate() {
        return EnterDate;
    }

    public String getEnterBy() {
        return EnterBy;
    }

    public String getEnquiryVerification() {
        return EnquiryVerification;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public String getOrderValue() {
        return OrderValue;
    }

    public String getVeryfiedBy() {
        return VeryfiedBy;
    }

    public String getChangedDate() {
        return ChangedDate;
    }

    public String getChangedBy() {
        return ChangedBy;
    }

    public String getDeleteStatus() {
        return DeleteStatus;
    }

    public String getDeleteDate() {
        return DeleteDate;
    }

    // Setter Methods

    public void setRecId(int RecId) {
        this.RecId = RecId;
    }

    public void setEmpId(int EmpId) {
        this.EmpId = EmpId;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public void setSentTo(int SentTo) {
        this.SentTo = SentTo;
    }

    public void setCustomerId(int CustomerId) {
        this.CustomerId = CustomerId;
    }

    public void setGroupId(int GroupId) {
        this.GroupId = GroupId;
    }

    public void setContactPerson(String ContactPerson) {
        this.ContactPerson = ContactPerson;
    }

    public void setContactNo(String ContactNo) {
        this.ContactNo = ContactNo;
    }

    public void setEnquiryDetails(String EnquiryDetails) {
        this.EnquiryDetails = EnquiryDetails;
    }

    public void setEnterDate(String EnterDate) {
        this.EnterDate = EnterDate;
    }

    public void setEnterBy(String EnterBy) {
        this.EnterBy = EnterBy;
    }

    public void setEnquiryVerification(String EnquiryVerification) {
        this.EnquiryVerification = EnquiryVerification;
    }

    public void setOrderStatus(String OrderStatus) {
        this.OrderStatus = OrderStatus;
    }

    public void setOrderValue(String OrderValue) {
        this.OrderValue = OrderValue;
    }

    public void setVeryfiedBy(String VeryfiedBy) {
        this.VeryfiedBy = VeryfiedBy;
    }

    public void setChangedDate(String ChangedDate) {
        this.ChangedDate = ChangedDate;
    }

    public void setChangedBy(String ChangedBy) {
        this.ChangedBy = ChangedBy;
    }

    public void setDeleteStatus(String DeleteStatus) {
        this.DeleteStatus = DeleteStatus;
    }

    public void setDeleteDate(String DeleteDate) {
        this.DeleteDate = DeleteDate;
    }

    public String toString()
    {
        return CustomerId+"-"+ProductName+"-"+EmpId;
    }
}