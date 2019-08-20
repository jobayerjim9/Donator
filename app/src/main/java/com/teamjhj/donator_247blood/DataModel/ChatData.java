package com.teamjhj.donator_247blood.DataModel;

public class ChatData {
    private String senderName, recieverName, message, uid;
    private boolean sent, recieved;

    public ChatData() {
    }

    public ChatData(String senderName, String recieverName, String message, boolean sent, boolean recieved, String uid) {
        this.senderName = senderName;
        this.recieverName = recieverName;
        this.message = message;
        this.sent = sent;
        this.recieved = recieved;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public boolean isRecieved() {
        return recieved;
    }

    public void setRecieved(boolean recieved) {
        this.recieved = recieved;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRecieverName() {
        return recieverName;
    }

    public void setRecieverName(String recieverName) {
        this.recieverName = recieverName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
