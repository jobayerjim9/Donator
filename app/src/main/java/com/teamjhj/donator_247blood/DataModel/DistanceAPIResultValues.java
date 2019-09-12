package com.teamjhj.donator_247blood.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DistanceAPIResultValues {
    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("value")
    @Expose
    private int value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
