package com.example.onlineexam.req;

public class WebMessageReq {
    private String type;

    private String comment;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    private String userid;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "WebMessageReq{" +
                "type='" + type + '\'' +
                ", comment='" + comment + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }
}
