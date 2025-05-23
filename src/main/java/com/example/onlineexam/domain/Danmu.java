package com.example.onlineexam.domain;

import java.util.Date;

public class Danmu {
    private Integer id;

    private Integer vid;

    private Integer uid;

    private String content;

    private Integer fontsize;

    private Integer mode;

    private String color;

    private Double timePoint;

    private Integer state;

    private Date createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVid() {
        return vid;
    }

    public void setVid(Integer vid) {
        this.vid = vid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getFontsize() {
        return fontsize;
    }

    public void setFontsize(Integer fontsize) {
        this.fontsize = fontsize;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(Double timePoint) {
        this.timePoint = timePoint;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", vid=").append(vid);
        sb.append(", uid=").append(uid);
        sb.append(", content=").append(content);
        sb.append(", fontsize=").append(fontsize);
        sb.append(", mode=").append(mode);
        sb.append(", color=").append(color);
        sb.append(", timePoint=").append(timePoint);
        sb.append(", state=").append(state);
        sb.append(", createDate=").append(createDate);
        sb.append("]");
        return sb.toString();
    }
}