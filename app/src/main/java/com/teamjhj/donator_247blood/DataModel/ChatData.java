package com.teamjhj.donator_247blood.DataModel;

import java.util.Date;

public class ChatData implements Comparable<ChatData> {
    private String senderName, recieverName, message, uid, messegingKey, messageCreator;
    private boolean sent, recieved, viewed = false;
    private Date messageTime;
    private long diffSec;
    public ChatData() {
    }

    public ChatData(String senderName, String recieverName, String message, boolean sent, boolean recieved, String uid, Date messageTime, boolean viewed, String messageCreator) {
        this.senderName = senderName;
        this.recieverName = recieverName;
        this.message = message;
        this.sent = sent;
        this.recieved = recieved;
        this.uid = uid;
        this.messageTime = messageTime;
        this.viewed = viewed;
        this.messageCreator = messageCreator;
    }

    public String getMessageCreator() {
        return messageCreator;
    }

    public void setMessageCreator(String messageCreator) {
        this.messageCreator = messageCreator;
    }

    public String getMessegingKey() {
        return messegingKey;
    }

    public void setMessegingKey(String messegingKey) {
        this.messegingKey = messegingKey;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public long getDiffSec() {
        return diffSec;
    }

    public void setDiffSec(long diffSec) {
        this.diffSec = diffSec;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
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

    @Override
    public int compareTo(ChatData chatData) {
        return getMessageTime().compareTo(chatData.getMessageTime());
    }
}
