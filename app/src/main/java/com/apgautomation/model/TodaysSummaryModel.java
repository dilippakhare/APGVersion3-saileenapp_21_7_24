package com.apgautomation.model;
public class TodaysSummaryModel {
    private float BookedComplaint;
    private float SolvedComplaint;
    private float NotSchedledComplaint;
    private float BreakdownComplaint;
    private float ServiceVisit;
    private float SalesVisit;
    private float FSRCnt;
    private float QuotationCnt;
    private float ServiceUnplanned;
    private float SalesUnplanned;
    private float TotalComplaint;
    private float TotalSolvedComplint;
    private float TotalPendingComplaint;
    private float TotlExceeded;
    private float TotalNotSchedledComplaint;
    private float PendingCheckup;


    public int AmcDone,AMCPending,AMCTotal ,UnderCCF,UnderObservation;

    // Getter Methods

    public float getBookedComplaint() {
        return BookedComplaint;
    }

    public float getSolvedComplaint() {
        return SolvedComplaint;
    }

    public float getNotSchedledComplaint() {
        return NotSchedledComplaint;
    }

    public float getBreakdownComplaint() {
        return BreakdownComplaint;
    }

    public float getServiceVisit() {
        return ServiceVisit;
    }

    public float getSalesVisit() {
        return SalesVisit;
    }

    public float getFSRCnt() {
        return FSRCnt;
    }

    public float getQuotationCnt() {
        return QuotationCnt;
    }

    public float getServiceUnplanned() {
        return ServiceUnplanned;
    }

    public float getSalesUnplanned() {
        return SalesUnplanned;
    }

    public float getTotalComplaint() {
        return TotalComplaint;
    }

    public float getTotalSolvedComplint() {
        return TotalSolvedComplint;
    }

    public float getTotalPendingComplaint() {
        return TotalPendingComplaint;
    }

    public float getTotlExceeded() {
        return TotlExceeded;
    }

    public float getTotalNotSchedledComplaint() {
        return TotalNotSchedledComplaint;
    }

    public float getPendingCheckup() {
        return PendingCheckup;
    }

    // Setter Methods

    public void setBookedComplaint(float BookedComplaint) {
        this.BookedComplaint = BookedComplaint;
    }

    public void setSolvedComplaint(float SolvedComplaint) {
        this.SolvedComplaint = SolvedComplaint;
    }

    public void setNotSchedledComplaint(float NotSchedledComplaint) {
        this.NotSchedledComplaint = NotSchedledComplaint;
    }

    public void setBreakdownComplaint(float BreakdownComplaint) {
        this.BreakdownComplaint = BreakdownComplaint;
    }

    public void setServiceVisit(float ServiceVisit) {
        this.ServiceVisit = ServiceVisit;
    }

    public void setSalesVisit(float SalesVisit) {
        this.SalesVisit = SalesVisit;
    }

    public void setFSRCnt(float FSRCnt) {
        this.FSRCnt = FSRCnt;
    }

    public void setQuotationCnt(float QuotationCnt) {
        this.QuotationCnt = QuotationCnt;
    }

    public void setServiceUnplanned(float ServiceUnplanned) {
        this.ServiceUnplanned = ServiceUnplanned;
    }

    public void setSalesUnplanned(float SalesUnplanned) {
        this.SalesUnplanned = SalesUnplanned;
    }

    public void setTotalComplaint(float TotalComplaint) {
        this.TotalComplaint = TotalComplaint;
    }

    public void setTotalSolvedComplint(float TotalSolvedComplint) {
        this.TotalSolvedComplint = TotalSolvedComplint;
    }

    public void setTotalPendingComplaint(float TotalPendingComplaint) {
        this.TotalPendingComplaint = TotalPendingComplaint;
    }

    public void setTotlExceeded(float TotlExceeded) {
        this.TotlExceeded = TotlExceeded;
    }

    public void setTotalNotSchedledComplaint(float TotalNotSchedledComplaint) {
        this.TotalNotSchedledComplaint = TotalNotSchedledComplaint;
    }

    public void setPendingCheckup(float PendingCheckup) {
        this.PendingCheckup = PendingCheckup;
    }
}