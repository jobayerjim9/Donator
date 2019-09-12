package com.teamjhj.donator_247blood.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DistanceApiResponse {

    @SerializedName("elements")
    @Expose
    private List<DistanceApiResult> elements;

    public List<DistanceApiResult> getElements() {
        return elements;
    }

    public void setElements(List<DistanceApiResult> elements) {
        this.elements = elements;
    }
}
