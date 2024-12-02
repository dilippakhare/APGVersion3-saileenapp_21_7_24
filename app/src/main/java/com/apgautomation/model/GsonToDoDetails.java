package com.apgautomation.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GsonToDoDetails extends RealmObject
{
    @PrimaryKey
    public  String DetailToken;

    public String getDetailToken() {
        return DetailToken;
    }

    public void setDetailToken(String detailToken) {
        DetailToken = detailToken;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public int getDetailId() {
        return DetailId;
    }

    public void setDetailId(int detailId) {
        DetailId = detailId;
    }

    public int getToDoId() {
        return ToDoId;
    }

    public void setToDoId(int toDoId) {
        ToDoId = toDoId;
    }

    public int getEnterById() {
        return EnterById;
    }

    public void setEnterById(int enterById) {
        EnterById = enterById;
    }

    public String getEnterBy() {
        return EnterBy;
    }

    public void setEnterBy(String enterBy) {
        EnterBy = enterBy;
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

    public String getTaskDetails() {
        return TaskDetails;
    }

    public void setTaskDetails(String taskDetails) {
        TaskDetails = taskDetails;
    }

    public Boolean getComplete() {
        return IsComplete;
    }

    public void setComplete(Boolean complete) {
        IsComplete = complete;
    }


    public String Token;
    public int DetailId;
    public int ToDoId;
    public int EnterById;
    public String EnterBy;
    public String EnterDate;
    public long EnterDateMillisecond;


    public  String TaskDetails;
    public  Boolean IsComplete;
    public  Boolean IsWorkStarted;
    public  Boolean IsWorkInProgress;

    public Boolean getWorkStarted() {
        return IsWorkStarted;
    }

    public void setWorkStarted(Boolean workStarted) {
        IsWorkStarted = workStarted;
    }

    public Boolean getWorkInProgress() {
        return IsWorkInProgress;
    }

    public void setWorkInProgress(Boolean workInProgress) {
        IsWorkInProgress = workInProgress;
    }


    public Boolean getModified() {
        return IsModified;
    }

    public void setModified(Boolean modified) {
        IsModified = modified;
    }

    public  Boolean IsModified;
}
