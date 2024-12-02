package com.apgautomation.model;


public class GSONCustomerMasterBeanExtends
{
    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelect;


    public  String toString()
    {
        return  CustomerName+" ("+GroupName+")";
    }

    public int CustomerId;
    public String CustomerName;
    public float GroupId;
    public String GroupName;
    public String Area;
    public float AreaId;
    public String ContactPerson = null;
    public String PersonNumber = null;
    public String ContactPerson1 = null;
    public String PersonNumber1 = null;
    public String ComponyNumber = null;
    public String ComponyEmail = null;
    public String Address = null;
    public String EnterDate;
    public String EnterBy;
    public String ChangedBy = null;
    public String ChangedDate = null;
    public boolean DeleteStatus;


    // Getter Methods

    public int getCustomerId() {
        return CustomerId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public float getGroupId() {
        return GroupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public String getArea() {
        return Area;
    }

    public float getAreaId() {
        return AreaId;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public String getPersonNumber() {
        return PersonNumber;
    }

    public String getContactPerson1() {
        return ContactPerson1;
    }

    public String getPersonNumber1() {
        return PersonNumber1;
    }

    public String getComponyNumber() {
        return ComponyNumber;
    }

    public String getComponyEmail() {
        return ComponyEmail;
    }

    public String getAddress() {
        return Address;
    }

    public String getEnterDate() {
        return EnterDate;
    }

    public String getEnterBy() {
        return EnterBy;
    }

    public String getChangedBy() {
        return ChangedBy;
    }

    public String getChangedDate() {
        return ChangedDate;
    }

    public boolean getDeleteStatus() {
        return DeleteStatus;
    }

    // Setter Methods

    public void setCustomerId(int CustomerId) {
        this.CustomerId = CustomerId;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public void setGroupId(float GroupId) {
        this.GroupId = GroupId;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    public void setArea(String Area) {
        this.Area = Area;
    }

    public void setAreaId(float AreaId) {
        this.AreaId = AreaId;
    }

    public void setContactPerson(String ContactPerson) {
        this.ContactPerson = ContactPerson;
    }

    public void setPersonNumber(String PersonNumber) {
        this.PersonNumber = PersonNumber;
    }

    public void setContactPerson1(String ContactPerson1) {
        this.ContactPerson1 = ContactPerson1;
    }

    public void setPersonNumber1(String PersonNumber1) {
        this.PersonNumber1 = PersonNumber1;
    }

    public void setComponyNumber(String ComponyNumber) {
        this.ComponyNumber = ComponyNumber;
    }

    public void setComponyEmail(String ComponyEmail) {
        this.ComponyEmail = ComponyEmail;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public void setEnterDate(String EnterDate) {
        this.EnterDate = EnterDate;
    }

    public void setEnterBy(String EnterBy) {
        this.EnterBy = EnterBy;
    }

    public void setChangedBy(String ChangedBy) {
        this.ChangedBy = ChangedBy;
    }

    public void setChangedDate(String ChangedDate) {
        this.ChangedDate = ChangedDate;
    }

    public void setDeleteStatus(boolean DeleteStatus) {
        this.DeleteStatus = DeleteStatus;
    }
}