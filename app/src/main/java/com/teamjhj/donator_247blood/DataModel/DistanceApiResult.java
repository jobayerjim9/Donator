package com.teamjhj.donator_247blood.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DistanceApiResult {

    @SerializedName("distance")
    @Expose
    private DistanceAPIResultValues distance;

    @SerializedName("duration")
    @Expose
    private DistanceAPIResultValues duration;

    @SerializedName("status")
    @Expose
    private String status;


    public DistanceAPIResultValues getDistance() {
        return distance;
    }

    public void setDistance(DistanceAPIResultValues distance) {
        this.distance = distance;
    }

    public DistanceAPIResultValues getDuration() {
        return duration;
    }

    public void setDuration(DistanceAPIResultValues duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
