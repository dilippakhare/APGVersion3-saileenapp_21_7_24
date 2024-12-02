package com.apgautomation.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GsonGroup  extends RealmObject {
    @PrimaryKey
    private int GroupId;
    private String GroupName;

    public boolean isISVeryfied() {
        return ISVeryfied;
    }

    public void setISVeryfied(boolean ISVeryfied) {
        this.ISVeryfied = ISVeryfied;
    }

    private boolean ISVeryfied;


    // Getter Methods

    public int getGroupId() {
        return GroupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    // Setter Methods

    public void setGroupId(int GroupId) {
        this.GroupId = GroupId;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    public String toString()
    {
        return  GroupName;
    }



}