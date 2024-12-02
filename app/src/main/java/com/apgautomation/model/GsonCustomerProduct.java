package com.apgautomation.model;

import com.apgautomation.utility.CommonShare;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GsonCustomerProduct extends RealmObject implements InterfaceCustomerProduct
{
    public  String toString()
    {
        return  CustomerName+" ("+GroupName+")";
    }
    @PrimaryKey
    public int RecId;
    public int CustomerId;
    public String CustomerName;
    public int GroupId;
    public String GroupName;
    public String Area;
    public float AreaId;
    public String AMCStatus;
    public String InstallationDate = null;
    public String AmcStartDate;
    public String AmcEndDate;


    public String getManufactureYear() {
        return ManufactureYear;
    }

    public void setManufactureYear(String manufactureYear) {
        ManufactureYear = manufactureYear;
    }

    public String ManufactureYear;


    public  long LastVisitMilliSecond;

    public void setLastVisitMilliSecond(long lastVisitMilliSecond) {
        this.LastVisitMilliSecond = lastVisitMilliSecond;
    }

    public Date AmcStartDateObj;
    public Date AmcEndDateObj;
    public  Date LastVisitDateObj;

    public String EquipmentName;
    public String EquipmentType;
    public String ModelName;
    public float ModelId;
    public String PortNo;
    public String SerialNumber;
    public float EngineerId;
    public String EngineerName;
    public String ContactPerson = null;
    public String PersonNumber = null;
    public String ContactPerson1 = null;
    public String PersonNumber1 = null;
    public String ComponyNumber = null;
    public String ComponyEmail = null;
    public String Address = null;
    public String LastVisitDate = null;
    public String EnterDate;
    public String EnterBy;
    public String ChangedBy = null;
    public String ChangedDate = null;
    public boolean DeleteStatus;


    // Getter Methods

    public int getRecId() {
        return RecId;
    }

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

    public String getAMCStatus() {
        return AMCStatus;
    }

    public String getInstallationDate() {
        return InstallationDate;
    }

    public String getAmcStartDate() {
        return AmcStartDate;
    }

    public String getAmcEndDate() {
        return AmcEndDate;
    }

    public String getEquipmentName() {
        return EquipmentName;
    }

    public String getEquipmentType() {
        return EquipmentType;
    }

    public String getModelName() {
        return ModelName;
    }

    public float getModelId() {
        return ModelId;
    }

    public String getPortNo() {
        return PortNo;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public float getEngineerId() {
        return EngineerId;
    }

    public String getEngineerName() {
        return EngineerName;
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

    public String getLastVisitDate() {
        return LastVisitDate;
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

    public void setRecId(int RecId) {
        this.RecId = RecId;
    }

    public void setCustomerId(int CustomerId) {
        this.CustomerId = CustomerId;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public void setGroupId(int GroupId) {
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

    public void setAMCStatus(String AMCStatus) {
        this.AMCStatus = AMCStatus;
    }

    public void setInstallationDate(String InstallationDate) {
        this.InstallationDate = InstallationDate;
    }

    public void setAmcStartDate(String AmcStartDate) {
        this.AmcStartDate = AmcStartDate;
    }

    public void setAmcEndDate(String AmcEndDate) {
        this.AmcEndDate = AmcEndDate;
    }

    public void setEquipmentName(String EquipmentName) {
        this.EquipmentName = EquipmentName;
    }

    public void setEquipmentType(String EquipmentType) {
        this.EquipmentType = EquipmentType;
    }

    public void setModelName(String ModelName) {
        this.ModelName = ModelName;
    }

    public void setModelId(float ModelId) {
        this.ModelId = ModelId;
    }

    public void setPortNo(String PortNo) {
        this.PortNo = PortNo;
    }

    public void setSerialNumber(String SerialNumber) {
        this.SerialNumber = SerialNumber;
    }

    public void setEngineerId(float EngineerId) {
        this.EngineerId = EngineerId;
    }

    public void setEngineerName(String EngineerName) {
        this.EngineerName = EngineerName;
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

    public void setLastVisitDate(String LastVisitDate) {
        this.LastVisitDate = LastVisitDate;
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

    public void convertDates()
    {
        try
        {
          CommonShare.getDateTime(CommonShare.parseDate(this.getAmcStartDate()));
          this.  AmcStartDateObj=  CommonShare.getJavaDate(CommonShare.parseDate(this.getAmcStartDate()));
          this.   AmcEndDateObj=  CommonShare.getJavaDate(CommonShare.parseDate(this.getAmcEndDate()));
           this.LastVisitDateObj=CommonShare.getJavaDate(CommonShare.parseDate(this.getLastVisitDate()));
         // if(this.getLastVisitMilliSecond()!=0)
            this.  setLastVisitMilliSecond(CommonShare.parseDate(this.getLastVisitDate()));

        }
        catch (Exception ex){}
    }

    public long getLastVisitMilliSecond() {
        return  this.LastVisitMilliSecond;
    }
}
