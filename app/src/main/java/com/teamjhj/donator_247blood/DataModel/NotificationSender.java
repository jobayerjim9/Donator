package com.teamjhj.donator_247blood.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationSender {
    @SerializedName("to")
    @Expose
    private String to;
//    @SerializedName("notification")
//    @Expose
//    private NotificationData notificationData;

    @SerializedName("data")
    @Expose
    private NotificationData data;
    public NotificationSender(String token, NotificationData notificationData) {
        this.to = token;
       // this.notificationData = notificationData;
        this.data=notificationData;
    }

    public NotificationData getData() {
        return data;
    }

    public void setData(NotificationData data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

//    public NotificationData getNotificationData() {
//        return notificationData;
//    }
//
//    public void setNotificationData(NotificationData notificationData) {
//        this.notificationData = notificationData;
//    }
}