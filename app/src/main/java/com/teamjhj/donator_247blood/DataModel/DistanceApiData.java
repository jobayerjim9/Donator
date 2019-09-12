package com.teamjhj.donator_247blood.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DistanceApiData {
    @SerializedName("rows")
    @Expose
    private List<DistanceApiResponse> rows=new ArrayList<>();

    @SerializedName("status")
    @Expose
    private String status;

    public List<DistanceApiResponse> getRows() {
        return rows;
    }

    public void setRows(List<DistanceApiResponse> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
