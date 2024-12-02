package com.apgautomation.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GsonAskQuery extends RealmObject {
    @PrimaryKey
    public String Token;

    public String getRefferenceToken() {
        return RefferenceToken;
    }

    public void setRefferenceToken(String refferenceToken) {
        RefferenceToken = refferenceToken;
    }

    public String RefferenceToken;

    public String MsgId;
    public String Msg;
    public String Attachment;

    public int EnterById;
    public String EnterByName;
    public long EnterDateMillisecond;
    public String EnterDate;

    public int ToId;
    public String ToName;

    public boolean IsModified;
    public boolean IsReed;
    public boolean IsParentMsg;

    public long ReedMillisecond;
    public String ReedDate;
    public boolean DeleteStatus;


    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String msgId) {
        MsgId = msgId;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getAttachment() {
        return Attachment;
    }

    public void setAttachment(String attachment) {
        Attachment = attachment;
    }

    public int getEnterById() {
        return EnterById;
    }

    public void setEnterById(int enterById) {
        EnterById = enterById;
    }

    public String getEnterByName() {
        return EnterByName;
    }

    public void setEnterByName(String enterByName) {
        EnterByName = enterByName;
    }

    public long getEnterDateMillisecond() {
        return EnterDateMillisecond;
    }

    public void setEnterDateMillisecond(long enterDateMillisecond) {
        EnterDateMillisecond = enterDateMillisecond;
    }

    public String getEnterDate() {
        return EnterDate;
    }

    public void setEnterDate(String enterDate) {
        EnterDate = enterDate;
    }

    public int getToId() {
        return ToId;
    }

    public void setToId(int toId) {
        ToId = toId;
    }

    public String getToName() {
        return ToName;
    }

    public void setToName(String toName) {
        ToName = toName;
    }

    public boolean isModified() {
        return IsModified;
    }

    public void setModified(boolean modified) {
        IsModified = modified;
    }

    public boolean isReed() {
        return IsReed;
    }

    public void setReed(boolean reed) {
        IsReed = reed;
    }

    public boolean isParentMsg() {
        return IsParentMsg;
    }

    public void setParentMsg(boolean parentMsg) {
        IsParentMsg = parentMsg;
    }

    public long getReedMillisecond() {
        return ReedMillisecond;
    }

    public void setReedMillisecond(long reedMillisecond) {
        ReedMillisecond = reedMillisecond;
    }

    public String getReedDate() {
        return ReedDate;
    }

    public void setReedDate(String reedDate) {
        ReedDate = reedDate;
    }

    public boolean isDeleteStatus() {
        return DeleteStatus;
    }

    public void setDeleteStatus(boolean deleteStatus) {
        DeleteStatus = deleteStatus;
    }




}
