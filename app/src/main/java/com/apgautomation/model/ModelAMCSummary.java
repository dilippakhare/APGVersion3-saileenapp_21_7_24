package com.apgautomation.model;

public class ModelAMCSummary
{
    private float EngineerId;
    private String EngineerName;
    private float AmcDone;
    private float AMCPending;
    private float Total;
    private float Pending;
    private float OtherTotal;
    private float OtherPending;


    // Getter Methods

    public float getEngineerId() {
        return EngineerId;
    }

    public String getEngineerName() {
        return EngineerName;
    }

    public float getAmcDone() {
        return AmcDone;
    }

    public float getAMCPending() {
        return AMCPending;
    }

    public float getTotal() {
        return Total;
    }

    public float getPending() {
        return Pending;
    }

    public float getOtherTotal() {
        return OtherTotal;
    }

    public float getOtherPending() {
        return OtherPending;
    }

    // Setter Methods

    public void setEngineerId(float EngineerId) {
        this.EngineerId = EngineerId;
    }

    public void setEngineerName(String EngineerName) {
        this.EngineerName = EngineerName;
    }

    public void setAmcDone(float AmcDone) {
        this.AmcDone = AmcDone;
    }

    public void setAMCPending(float AMCPending) {
        this.AMCPending = AMCPending;
    }

    public void setTotal(float Total) {
        this.Total = Total;
    }

    public void setPending(float Pending) {
        this.Pending = Pending;
    }

    public void setOtherTotal(float OtherTotal) {
        this.OtherTotal = OtherTotal;
    }

    public void setOtherPending(float OtherPending) {
        this.OtherPending = OtherPending;
    }
}