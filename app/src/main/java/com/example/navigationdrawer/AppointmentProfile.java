package com.example.navigationdrawer;

public class AppointmentProfile {
    public String aptNum;
    public String aptDate;
    public Long aptSlot;
    public String aptRemark;
    public String aptLocation;
    public String status;
    public String services;
    public String worker;
    public String userUid;
    public String bookingDateTime;

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public AppointmentProfile(String aptNum, String aptDate, Long aptSlot, String aptRemark, String aptLocation, String status, String services, String worker, String userUid, String bookingDateTime) {
        this.aptNum = aptNum;
        this.aptDate = aptDate;
        this.aptSlot = aptSlot;
        this.aptRemark = aptRemark;
        this.aptLocation = aptLocation;
        this.status = status;
        this.services = services;
        this.worker = worker;
        this.userUid = userUid;
        this.bookingDateTime = bookingDateTime;
    }

    public AppointmentProfile(){

    }

    public String getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(String bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public String getAptRemark() {
        return aptRemark;
    }

    public void setAptRemark(String aptRemark) {
        this.aptRemark = aptRemark;
    }

    public String getAptLocation() {
        return aptLocation;
    }

    public void setAptLocation(String aptLocation) {
        this.aptLocation = aptLocation;
    }

    public String getAptNum() {
        return aptNum;
    }

    public void setAptNum(String aptNum) {
        this.aptNum = aptNum;
    }

    public String getAptDate() {
        return aptDate;
    }

    public void setAptDate(String aptDate) {
        this.aptDate = aptDate;
    }

    public Long getAptSlot() {
        return aptSlot;
    }

    public void setAptSlot(Long aptSlot) {
        this.aptSlot = aptSlot;
    }
}
