package com.teamjhj.donator_247blood.DataModel;

import java.util.Date;

public class CommentsData {
    private String name, message, uid;
    private Date commentsDate;

    public CommentsData() {
    }


    public CommentsData(String name, String message, String uid, Date commentsDate) {
        this.name = name;
        this.message = message;
        this.uid = uid;
        this.commentsDate = commentsDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCommentsDate() {
        return commentsDate;
    }

    public void setCommentsDate(Date commentsDate) {
        this.commentsDate = commentsDate;
    }
}
