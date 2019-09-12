package com.teamjhj.donator_247blood.DataModel;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeometryData {
    @SerializedName("location")
    @Expose
    private LocationData location;

    public LocationData getLocation() {
        return location;
    }

    public void setLocation(LocationData location) {
        this.location = location;
    }
}
