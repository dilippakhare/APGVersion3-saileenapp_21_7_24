package com.apgautomation.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GSONCloseComplaint extends RealmObject
{


    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getSolution() {
        return Solution;
    }

    public void setSolution(String solution) {
        Solution = solution;
    }

    public int getClosedBy() {
        return ClosedBy;
    }

    public void setClosedBy(int closedBy) {
        ClosedBy = closedBy;
    }

    public boolean isModified() {
        return IsModified;
    }

    public void setModified(boolean modified) {
        IsModified = modified;
    }



    public long getMatchTimeMillisecond() {
        return MatchTimeMillisecond;
    }

    public void setMatchTimeMillisecond(long matchTimeMillisecond) {
        MatchTimeMillisecond = matchTimeMillisecond;
    }
    @PrimaryKey
    public String Token;
    public long MatchTimeMillisecond;
    public boolean  IsModified;
    public String Solution;
    public int ClosedBy;

}
