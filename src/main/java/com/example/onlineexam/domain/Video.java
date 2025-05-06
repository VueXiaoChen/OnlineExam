package com.example.onlineexam.domain;

import java.util.Date;

public class Video {
    private Integer vid;

    private Integer uid;

    private String title;

    private Byte type;

    private Byte auth;

    private Double duration;

    private String mcId;

    private String scId;

    private String tags;

    private String descr;

    private String coverUrl;

    private String videoUrl;

    private Byte status;

    private Date uploadDate;

    private Date deleteDate;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getAuth() {
        return auth;
    }

    public void setAuth(Byte auth) {
        this.auth = auth;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getMcId() {
        return mcId;
    }

    public void setMcId(String mcId) {
        this.mcId = mcId;
    }

    public String getScId() {
        return scId;
    }

    public void setScId(String scId) {
        this.scId = scId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", vid=").append(vid);
        sb.append(", uid=").append(uid);
        sb.append(", title=").append(title);
        sb.append(", type=").append(type);
        sb.append(", auth=").append(auth);
        sb.append(", duration=").append(duration);
        sb.append(", mcId=").append(mcId);
        sb.append(", scId=").append(scId);
        sb.append(", tags=").append(tags);
        sb.append(", descr=").append(descr);
        sb.append(", coverUrl=").append(coverUrl);
        sb.append(", videoUrl=").append(videoUrl);
        sb.append(", status=").append(status);
        sb.append(", uploadDate=").append(uploadDate);
        sb.append(", deleteDate=").append(deleteDate);
        sb.append("]");
        return sb.toString();
    }
}