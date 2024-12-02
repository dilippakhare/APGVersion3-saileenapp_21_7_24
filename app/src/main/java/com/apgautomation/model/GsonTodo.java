package com.apgautomation.model;

import androidx.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GsonTodo extends RealmObject
{
    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public int getToDoId() {
        return ToDoId;
    }

    public void setToDoId(int toDoId) {
        ToDoId = toDoId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public long getDueDateMillisecond() {
        return DueDateMillisecond;
    }

    public void setDueDateMillisecond(long dueDateMillisecond) {
        DueDateMillisecond = dueDateMillisecond;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public String getAttchment() {
        return Attchment;
    }

    public void setAttchment(String attchment) {
        Attchment = attchment;
    }

    public int getChangedById() {
        return ChangedById;
    }

    public void setChangedById(int changedById) {
        ChangedById = changedById;
    }

    public String getChangedBy() {
        return ChangedBy;
    }

    public void setChangedBy(String changedBy) {
        ChangedBy = changedBy;
    }

    public boolean isDeleteStatus() {
        return DeleteStatus;
    }

    public void setDeleteStatus(boolean deleteStatus) {
        DeleteStatus = deleteStatus;
    }

     @PrimaryKey
     public String Token;
     public  int ToDoId;
     public String Title;
     public String Description;

     public long EnterDateMillisecond;
     public String EnterDate;

     public  int EnterById;
     public  String EnterByName;

     public long DueDateMillisecond;
     public String DueDate;

    public long getReDueDateMillisecond() {
        return ReDueDateMillisecond;
    }

    public void setReDueDateMillisecond(long reDueDateMillisecond) {
        ReDueDateMillisecond = reDueDateMillisecond;
    }

     public String getReDueDate() {
        return ReDueDate;
    }

     public void setReDueDate(String reDueDate) {
        ReDueDate = reDueDate;
    }

     public String getPriority() {
        return Priority;
    }

     public void setPriority(String priority) {
        Priority = priority;
    }

     public boolean isRepeated() {
        return IsRepeated;
    }

     public void setRepeated(boolean repeated) {
        IsRepeated = repeated;
    }

     public long ReDueDateMillisecond;
     public String ReDueDate;
     public String Priority;
     public boolean IsRepeated;
     public String Attchment;

     public  int ChangedById;
     public  String ChangedBy;

     public boolean DeleteStatus;
    public  Boolean IsModified;

    public Boolean getModified() {
        return IsModified;
    }

    public void setModified(Boolean modified) {
        IsModified = modified;
    }


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatus1() {
        return Status1;
    }

    public void setStatus1(String status1) {
        Status1 = status1;
    }

    public  String Status;
    public  String Status1;


    public String getEmpids() {
        return empids;
    }

    public void setEmpids(String empids) {
        this.empids = empids;
    }

    public String empids;


    @NonNull
    @Override
    public String toString() {
        return getTitle()+"-"+getEmpids();
    }
}
