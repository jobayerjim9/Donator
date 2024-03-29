package com.teamjhj.donator_247blood.DataModel;

public class AcceptingData {
    private boolean accepted;
    private int radius;
    private boolean bloodRecieved;
    public AcceptingData() {
    }

    public AcceptingData(boolean accepted, int radius) {
        this.accepted = accepted;
        this.radius = radius;
    }

    public boolean isBloodRecieved() {
        return bloodRecieved;
    }

    public void setBloodRecieved(boolean bloodRecieved) {
        this.bloodRecieved = bloodRecieved;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
