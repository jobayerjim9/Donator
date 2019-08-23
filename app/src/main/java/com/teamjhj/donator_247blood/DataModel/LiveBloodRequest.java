package com.teamjhj.donator_247blood.DataModel;

import java.util.Date;

public class LiveBloodRequest {
    private Date date;
    private double lat, lon;
    private String reason;

    public LiveBloodRequest() {
    }

    public LiveBloodRequest(Date date, double lat, double lon, String reason) {
        this.date = date;
        this.lat = lat;
        this.lon = lon;
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
