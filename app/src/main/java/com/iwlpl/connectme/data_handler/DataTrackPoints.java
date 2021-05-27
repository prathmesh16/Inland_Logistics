package com.iwlpl.connectme.data_handler;

public class DataTrackPoints {

    String status;
    String orogin;
    String date;

    public DataTrackPoints() {
    }

    public DataTrackPoints(String status, String orogin, String date) {
        this.status = status;
        this.orogin = orogin;
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrogin() {
        return orogin;
    }

    public void setOrogin(String orogin) {
        this.orogin = orogin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
