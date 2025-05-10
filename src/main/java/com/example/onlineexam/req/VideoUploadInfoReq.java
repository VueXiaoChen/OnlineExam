package com.example.onlineexam.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class VideoUploadInfoReq {
    private String hash;
    private String title;
    private Integer type;
    private Integer auth;
    private Double duration;
    private String mcId;
    private String scId;
    private String tags;
    private String descr;
    private String coverUrl;


    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getScId() {
        return scId;
    }

    public void setScId(String scId) {
        this.scId = scId;
    }

    public String getMcId() {
        return mcId;
    }

    public void setMcId(String mcId) {
        this.mcId = mcId;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Integer getAuth() {
        return auth;
    }

    public void setAuth(Integer auth) {
        this.auth = auth;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "VideoUploadInfoReq{" +
                "hash='" + hash + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", auth=" + auth +
                ", duration=" + duration +
                ", mcId='" + mcId + '\'' +
                ", scId='" + scId + '\'' +
                ", tags='" + tags + '\'' +
                ", descr='" + descr + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }
}
