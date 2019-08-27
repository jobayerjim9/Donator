package com.teamjhj.donator_247blood.DataModel;

import java.util.Date;

public class PendingDonationData {
    private Date date;
    private String name;

    public PendingDonationData() {
    }

    public PendingDonationData(Date date, String name) {
        this.date = date;
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
