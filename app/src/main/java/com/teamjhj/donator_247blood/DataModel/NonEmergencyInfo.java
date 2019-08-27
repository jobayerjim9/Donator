package com.teamjhj.donator_247blood.DataModel;

public class NonEmergencyInfo {
    private String location, selectedBloodGroup, reason, name, phone, key, uid, bags;
    private int date, month, year, hour, minute;
    private double lat, longt;
    private boolean liked;
    private double distamceFromUser;
    private boolean closed;
    public NonEmergencyInfo() {
    }

    public NonEmergencyInfo(String location, String selectedBloodGroup, String reason, double lat, double longt, String uid) {
        this.location = location;
        this.selectedBloodGroup = selectedBloodGroup;
        this.reason = reason;
        this.lat = lat;
        this.longt = longt;
        this.uid = uid;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public String getBags() {
        return bags;
    }

    public void setBags(String bags) {
        this.bags = bags;
    }

    public double getDistamceFromUser() {
        return distamceFromUser;
    }

    public void setDistamceFromUser(double distamceFromUser) {
        this.distamceFromUser = distamceFromUser;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongt() {
        return longt;
    }

    public void setLongt(double longt) {
        this.longt = longt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSelectedBloodGroup() {
        return selectedBloodGroup;
    }

    public void setSelectedBloodGroup(String selectedBloodGroup) {
        this.selectedBloodGroup = selectedBloodGroup;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

}
