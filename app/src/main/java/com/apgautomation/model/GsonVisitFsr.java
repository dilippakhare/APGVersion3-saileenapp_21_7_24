package com.apgautomation.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GsonVisitFsr extends RealmObject {
    public int getRecId() {
        return RecId;
    }

    public void setRecId(int recId) {
        RecId = recId;
    }

    public int getVisitId() {
        return VisitId;
    }

    public void setVisitId(int visitId) {
        VisitId = visitId;
    }

    public int getCustomerProductId() {
        return CustomerProductId;
    }

    public void setCustomerProductId(int customerProductId) {
        CustomerProductId = customerProductId;
    }

    public String getFSRToken() {
        return FSRToken;
    }

    public void setFSRToken(String FSRToken) {
        this.FSRToken = FSRToken;
    }

    public String getFSRNO() {
        return FSRNO;
    }

    public void setFSRNO(String FSRNO) {
        this.FSRNO = FSRNO;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getFSRAttachment() {
        return FSRAttachment;
    }

    public void setFSRAttachment(String FSRAttachment) {
        this.FSRAttachment = FSRAttachment;
    }

    public String getLocalPath() {
        return LocalPath;
    }

    public void setLocalPath(String localPath) {
        LocalPath = localPath;
    }

    public String getVisitTokenId() {
        return VisitTokenId;
    }

    public void setVisitTokenId(String VisitTokenId) {
       this. VisitTokenId = VisitTokenId;
    }
    public int RecId,VisitId,CustomerProductId;
    @PrimaryKey
    public String FSRToken;
    public String  VisitTokenId,FSRNO,Remark,FSRAttachment,LocalPath;

    public String getRunningHourd() {
        return RunningHourd;
    }

    public void setRunningHourd(String runningHourd) {
        RunningHourd = runningHourd;
    }

    public String getLoadHourd() {
        return LoadHourd;
    }

    public void setLoadHourd(String loadHourd) {
        LoadHourd = loadHourd;
    }

    public String getMototStart() {
        return MototStart;
    }

    public void setMototStart(String mototStart) {
        MototStart = mototStart;
    }

    public String getLoadVolve() {
        return LoadVolve;
    }

    public void setLoadVolve(String loadVolve) {
        LoadVolve = loadVolve;
    }

    public String getDutyCycle() {
        return DutyCycle;
    }

    public void setDutyCycle(String dutyCycle) {
        DutyCycle = dutyCycle;
    }

    public String  RunningHourd,LoadHourd,MototStart,LoadVolve,DutyCycle;

    public String getFSRType() {
        return FSRType;
    }

    public String FSRType;

    public boolean isModified() {
        return Modified;
    }

    public void setModified(boolean modified) {
        Modified = modified;
    }

    public boolean Modified;

    public void setFSRType(String toString)
    {
        this.FSRType=toString;
    }
}
