package com.teamjhj.donator_247blood.DataModel;

import java.util.Date;

public class LiveBloodRequest {
    private Date date;
    private double lat, lon;
    private String reason;
    private String bloodGroup;
    private String key;
    public LiveBloodRequest() {
    }

    public LiveBloodRequest(Date date, double lat, double lon, String reason, String bloodGroup) {
        this.date = date;
        this.lat = lat;
        this.lon = lon;
        this.reason = reason;
        this.bloodGroup = bloodGroup;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
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
