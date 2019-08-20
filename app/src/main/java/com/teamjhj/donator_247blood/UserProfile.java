package com.teamjhj.donator_247blood;


public class UserProfile {
    private String name, uid, bloodGroup, mobileNumber, privacy;
    private int lastDonationDate, lastDonationMonth, lastDonationYear, DonationDays;
    private boolean available;
    private String token;

    public UserProfile() {
    }

    public UserProfile(String name, String uid, String bloodGroup, int lastDonationDate, int lastDonationMonth, int lastDonationYear, String mobileNumber, String privacy) {
        this.name = name;
        this.uid = uid;
        this.bloodGroup = bloodGroup;
        this.lastDonationDate = lastDonationDate;
        this.lastDonationMonth = lastDonationMonth;
        this.lastDonationYear = lastDonationYear;
        this.mobileNumber = mobileNumber;
        DonationDays = 0;
        available = true;
        this.privacy = privacy;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }


    public int getDonationDays() {
        return DonationDays;
    }

    public void setDonationDays(int donationDays) {
        DonationDays = donationDays;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public int getLastDonationDate() {
        return lastDonationDate;
    }

    public void setLastDonationDate(int lastDonationDate) {
        this.lastDonationDate = lastDonationDate;
    }

    public int getLastDonationMonth() {
        return lastDonationMonth;
    }

    public void setLastDonationMonth(int lastDonationMonth) {
        this.lastDonationMonth = lastDonationMonth;
    }

    public int getLastDonationYear() {
        return lastDonationYear;
    }

    public void setLastDonationYear(int lastDonationYear) {
        this.lastDonationYear = lastDonationYear;
    }
}
