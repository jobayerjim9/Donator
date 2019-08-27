package com.teamjhj.donator_247blood.DataModel;

public class DonationHistory {
    String hospitalName, key;
    int day, month, year;
    int dayCount;
    public DonationHistory() {
    }

    public DonationHistory(String hospitalName, int day, int month, int year) {
        this.hospitalName = hospitalName;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
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
}
