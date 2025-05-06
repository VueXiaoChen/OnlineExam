package com.example.onlineexam.req;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VideoReq extends PageReq{

    /**
     * 视频ID
     */

    private Integer vid;

    /**
     * 投稿用户ID
     */

    private Integer uid;

    /**
     * 标题
     */

    private String title;

    /**
     * 类型 1自制 2转载
     */

    private Integer type;

    /**
     * 作者声明 0不声明 1未经允许禁止转载
     */

    private Integer auth;

    /**
     * 播放总时长 单位秒
     */

    private Double duration;

    /**
     * 主分区ID
     */

    private String mcId;

    /**
     * 子分区ID
     */

    private String scId;

    /**
     * 标签 回车分隔
     */
    private String tags;

    /**
     * 简介
     */
    private String descr;

    /**
     * 封面url
     */

    private String coverUrl;

    /**
     * 视频url
     */

    private String videoUrl;

    /**
     * 状态 0审核中 1已过审 2未通过 3已删除
     */

    private Integer status;

    /**
     * 上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")

    private Date uploadDate;

    /**
     * 删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAuth() {
        return auth;
    }

    public void setAuth(Integer auth) {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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
